package com.example.daivansh.jobreferer.connections;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.daivansh.jobreferer.R;
import com.example.daivansh.jobreferer.activities.HomeActivity;
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
 * Created by Priyanshu on 09-06-2017.
 */

public class UpdateProfileConnection extends AsyncTask<String,Void,String> {
    String uid,fnm,lnm,mail,cnm,pswd,gender,intr,expr,cloc,server,ftl;
    Context context;

    public UpdateProfileConnection(Context context)
    {this.context = context;}
    /*
        ProgressDialog progDailog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progDailog = new ProgressDialog(context);
            progDailog.setMessage("Authenticating");
            progDailog.setIndeterminate(false);
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(false);
            progDailog.show();
        }
    */
    @Override
    protected String doInBackground(String... args) {
        server = context.getString(R.string.server_url);

        uid = args[0];
        fnm = args[1];
        lnm = args[2];
        intr = args[3];
        cnm = args[4];
        cloc = args[5];
        expr = args[6];

        String data, link, result;
        BufferedReader bufferedReader;
        try
        {
           /* data = "?uid="+ URLEncoder.encode(uid,"UTF-8");
            data += "&fnm="+ URLEncoder.encode(fnm,"UTF-8");
            data += "&lnm="+ URLEncoder.encode(lnm,"UTF-8");
            data += "&intr="+ URLEncoder.encode(intr,"UTF-8");
            data += "&cnm="+ URLEncoder.encode(cnm,"UTF-8");
            data += "&cloc="+ URLEncoder.encode(cloc,"UTF-8");
            data += "&expr="+ URLEncoder.encode(expr,"UTF-8");

            link = server+"/UpdateProfile.php"+data;

            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            result = bufferedReader.readLine();
            return result;      */
            data = URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(uid, "UTF-8");
            data += "&" + URLEncoder.encode("fnm", "UTF-8") + "=" + URLEncoder.encode(fnm, "UTF-8");
            data += "&" + URLEncoder.encode("lnm", "UTF-8") + "=" + URLEncoder.encode(lnm, "UTF-8");
            data += "&" + URLEncoder.encode("cnm", "UTF-8") + "=" + URLEncoder.encode(cnm, "UTF-8");
            data += "&" + URLEncoder.encode("intr", "UTF-8") + "=" + URLEncoder.encode(intr, "UTF-8");
            data += "&" + URLEncoder.encode("cloc", "UTF-8") + "=" + URLEncoder.encode(cloc, "UTF-8");
            data += "&" + URLEncoder.encode("expr", "UTF-8") + "=" + URLEncoder.encode(expr, "UTF-8");

            link = server+"/UpdateProfile.php";

            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
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
        if (jsonStr != null)
        {
            try
            {
                JSONObject jsonObject = new JSONObject(jsonStr);
                String query_result = jsonObject.getString("query_result");
                if (query_result.equals("DONE"))
                {
                    new SPHelper(context,context.getString(R.string.defaultPrefer));
                    gender = SPHelper.getData(context.getString(R.string.keyGENDER));
                    mail = SPHelper.getData(context.getString(R.string.keyEMAIL));



                    SPHelper.putAllData(uid,fnm,lnm,cnm,mail,gender,intr,expr,cloc);


                    Toast.makeText(context,"Changes saved!",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(context,HomeActivity.class);
                    context.startActivity(i);




                }
                else if(query_result.equals("NOPE"))
                {
                    Toast.makeText(context,"Wrong Username or Password",Toast.LENGTH_LONG).show();}

                else
                {Toast.makeText(context,"Could not connect to server",Toast.LENGTH_LONG).show();}
            }
            catch (JSONException e)
            {
                e.printStackTrace();

                Toast.makeText(context,"Error parsing JSON data.",Toast.LENGTH_LONG).show();
                Toast.makeText(context,e.getMessage()+" <",Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(context,"Could not get JSON data",Toast.LENGTH_LONG).show();
        }
        //progDailog.dismiss();
    }
}
