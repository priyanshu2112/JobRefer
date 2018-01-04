package com.example.daivansh.jobreferer.connections;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.daivansh.jobreferer.activities.HomeActivity;
import com.example.daivansh.jobreferer.R;
import com.example.daivansh.jobreferer.helper.SPHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Priyanshu on 06-06-2017.
 */

public class UserLoginConnection extends AsyncTask<String,Void,String> {
    String uid,fnm,lnm,mail,cnm,pswd,gender,intr,expr,cloc,server,ftl;
    Context context;


    public UserLoginConnection(Context context)
    {
        this.context = context;
    }
    ProgressDialog progDailog;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progDailog = new ProgressDialog(context);
        progDailog.setMessage("Authenticating");
        progDailog.setIndeterminate(true);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(false);
        progDailog.show();
    }

    @Override
    protected String doInBackground(String... args) {
        server = context.getString(R.string.server_url);
        mail = args[0];
        pswd = args[1];
        String data, link, result;
        BufferedReader bufferedReader;
        try
        {
           /* data = "?u="+ URLEncoder.encode(mail,"UTF-8");
            data += "&p="+ URLEncoder.encode(pswd,"UTF-8");

            link = server+"/login.php"+data;

            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            result = bufferedReader.readLine();
            return result;      */

            data = URLEncoder.encode("u", "UTF-8") + "=" + URLEncoder.encode(mail, "UTF-8");
            data += "&" + URLEncoder.encode("p", "UTF-8") + "=" + URLEncoder.encode(pswd, "UTF-8");

            link = server+"/login.php";

            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
         //   OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
            OutputStream os=connection.getOutputStream();
            BufferedWriter wr=new BufferedWriter(new OutputStreamWriter(os,"UTF-8"));
            wr.write( data );
            wr.flush();
            wr.close();
            os.close();

            //     HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            result = bufferedReader.readLine();
            bufferedReader.close();
            return result;
        }
        catch (Exception ex)
        {
            //Toast.makeText(context,"Contact developer",Toast.LENGTH_LONG).show();
            return new String("Exception : "+ex.getMessage());
        }

    }

    @Override
    protected void onPostExecute(String res) {
        String jsonStr = res;
        progDailog.dismiss();
        if (jsonStr != null)
        {
            try
            {
                JSONObject jsonObject = new JSONObject(jsonStr);
                String query_result = jsonObject.getString("validate");
                if (query_result.equals("YES"))
                {
                    uid = jsonObject.getString("uid");
                    fnm = jsonObject.getString("fnm");
                    lnm = jsonObject.getString("lnm");
                    cnm = jsonObject.getString("cnm");
                    gender = jsonObject.getString("x");
                    intr = jsonObject.getString("intr");
                    expr = jsonObject.getString("expr");
                    cloc = jsonObject.getString("cloc");
                    ftl = jsonObject.getString("ftl");
new SPHelper(context,context.getString(R.string.defaultPrefer));
                    SPHelper.putAllData(uid,fnm,lnm,cnm,mail,gender,intr,expr,cloc,pswd);

                        //Toast.makeText(context,"NOT FIRST LOGIN",Toast.LENGTH_LONG).show();

                        Intent i = new Intent(context,HomeActivity.class);
                        context.startActivity(i);




                }
                else if(query_result.equals("NO"))
                {Toast.makeText(context,"Wrong Username or Password",Toast.LENGTH_LONG).show();}

                else
                {Toast.makeText(context,"Could Not Connect To Server...!!",Toast.LENGTH_LONG).show();}
            }
            catch (JSONException e)
            {
                e.printStackTrace();

            //    Toast.makeText(context,"Error parsing JSON data.",Toast.LENGTH_LONG).show();
              //  Toast.makeText(context,e.getMessage()+" <",Toast.LENGTH_LONG).show();
                Toast.makeText(context,"Could Not Connect To Server...!!",Toast.LENGTH_LONG).show();
            }
        }
        else
        {
         //   Toast.makeText(context,"Could not get JSON data",Toast.LENGTH_LONG).show();
            Toast.makeText(context,"Could Not Connect To Server...!!",Toast.LENGTH_LONG).show();
        }
        //progDailog.dismiss();
    }


}
