package com.example.daivansh.jobreferer.connections;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daivansh.jobreferer.R;
import com.example.daivansh.jobreferer.activities.HomeActivity;
import com.example.daivansh.jobreferer.activities.LoginActivity;
import com.example.daivansh.jobreferer.helper.SPHelper;
import com.example.daivansh.jobreferer.helper.TypefaceUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Priyanshu on 07-06-2017.
 */

public class GetSkillsConnection extends AsyncTask<String,Void,String>
{Context context;
    String server;
    RelativeLayout relativeLayout;
    View view;
    Snackbar snackbar;
    View snackbarView;
    TextView snackbartv;

    public GetSkillsConnection(Context context, View view)
    {
        this.context = context;
        this.view = view;
        new SPHelper(context,context.getString(R.string.skillsPrefer));
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        snackbar = Snackbar.make(view,"Connecting...",Snackbar.LENGTH_INDEFINITE);
        snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorAccent_low));
        snackbartv = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        snackbartv.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        //snackbartv.setAllCaps(true);
        snackbartv.setTextSize(16);
        TypefaceUtil.overrideFont(context, "SANS-SERIF", "fonts/Montserrat-Regular.ttf");
        snackbartv.setTypeface(Typeface.SANS_SERIF);
        snackbartv.setWidth(snackbarView.getWidth());
        snackbartv.setGravity(Gravity.CENTER);
        snackbar.show();
    }

    @Override
    protected String doInBackground(String... params) {
        server = context.getString(R.string.server_url);

        String data, link, result;
        BufferedReader bufferedReader;
        try
        {
            link = server+"/RetrieveSkills.php";

            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(5000);
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            result = bufferedReader.readLine();
            return result;
        }
        catch (Exception ex)
        {
            //Toast.makeText(context,"Contact developer:"+ex.getMessage(),Toast.LENGTH_LONG).show();
            return new String("Exception : "+ex.getMessage());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onPostExecute(String res) {
        String skill,uid;


//        FrameLayout.LayoutParams params =(FrameLayout.LayoutParams)snackbarView.getLayoutParams();
//        params.gravity = Gravity.TOP;
//        snackbarView.setLayoutParams(params);

        if (res != null)
        {
            try
            {
                new SPHelper(context,context.getString(R.string.skillsPrefer));
                JSONObject jsonObject = new JSONObject(res);
                int row_count = Integer.parseInt(jsonObject.getString("row_count"));
                SPHelper.putData("row_count",row_count+"");
                //Toast.makeText(context,"row_count : "+row_count,Toast.LENGTH_SHORT).show();
                for(int cnt = 0;cnt < row_count;cnt++)
                {
                    skill = jsonObject.getString(cnt+"");
                    SPHelper.putData(cnt+"",skill);
                }

                new SPHelper(context,context.getString(R.string.defaultPrefer));
                uid = SPHelper.getData(context.getString(R.string.keyUID));
                if(uid!="NOT_PRESENT") {
                    snackbartv.setText("Connected, Redirecting to home");
                    snackbar.show();
                    Thread background1 = new Thread() {
                        public void run() {
                            try
                            {
                                sleep(1200);
                                Intent i = new Intent(context, HomeActivity.class);
                                context.startActivity(i);
                            }
                            catch (Exception e) {
                            }
                        }
                    };
                    background1.start();

                    //context.overridePendingTransition(R.anim.from, R.anim.to);
                }
                else
                {
                    snackbartv.setText("Connected, Redirecting to login");
                    snackbar.show();
                    Thread background2 = new Thread() {
                        public void run() {
                            try
                            {
                                sleep(1200);
                                Intent i = new Intent(context, LoginActivity.class);
                                context.startActivity(i);
                            }
                            catch (Exception e) {
                            }
                        }
                    };
                    background2.start();
                }
            }
            catch (JSONException ex)
            {
                ex.printStackTrace();
                //Toast.makeText(context,"Could Not Connect To Server",Toast.LENGTH_LONG).show();
                //Snackbar.make(view,"Could Not Connect To Server",Snackbar.LENGTH_INDEFINITE).show();
                snackbartv.setText("Could not connect to server.");
                snackbar.show();
            }
        }
        else
        {
            //Toast.makeText(context,"Could Not Connect To Server",Toast.LENGTH_LONG).show();
            //Snackbar.make(view,"Could Not Connect To Server",Snackbar.LENGTH_INDEFINITE).show();
            //snackbartv.setAllCaps(true);
            snackbartv.setText("Could not connect to server.");
            snackbar.show();
        }
    }
}
