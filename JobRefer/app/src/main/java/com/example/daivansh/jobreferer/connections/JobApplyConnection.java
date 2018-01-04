package com.example.daivansh.jobreferer.connections;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.example.daivansh.jobreferer.activities.HomeActivity;
import com.example.daivansh.jobreferer.R;

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

public class JobApplyConnection extends AsyncTask<String,View,String> {
    String jobid,userid,dateofapply,server;
    Context context;
    ProgressDialog progDailog;

    public JobApplyConnection(Context context)
    {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progDailog = new ProgressDialog(context);
        progDailog.setMessage("Applying");
        progDailog.setIndeterminate(true);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(false);
        progDailog.show();
    }

    @Override
    protected String doInBackground(String... args) {
        jobid = args[0];
        userid = args[1];
        dateofapply = args[2];
        server = context.getString(R.string.server_url);

        String data, link, result;
        BufferedReader bufferedReader;

        try
        {
           /* data = "?jobid="+ URLEncoder.encode(jobid,"UTF-8");
            data += "&userid="+ URLEncoder.encode(userid,"UTF-8");
            data += "&dateofapply="+ URLEncoder.encode(dateofapply,"UTF-8");

            link = server+"/jobapply.php"+data;

            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            result = bufferedReader.readLine();
            return result;      */

            data = URLEncoder.encode("jobid", "UTF-8") + "=" + URLEncoder.encode(jobid, "UTF-8");
            data += "&" + URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
            data += "&" + URLEncoder.encode("dateofapply", "UTF-8") + "=" + URLEncoder.encode(dateofapply, "UTF-8");

            link = server+"/jobapply.php";

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
            //Toast.makeText(context,"Data_not_found",Toast.LENGTH_LONG).show();
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
                String query_result = jsonObject.getString("query_result");
                if (query_result.equals("SUCCESS"))
                {
                    Toast.makeText(context,"Application For Job Submitted Successfully...!!",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(context,HomeActivity.class);
                    context.startActivity(intent);
                }
                else if(query_result.equals("FAILURE"))
                {Toast.makeText(context,"Error While Applying For Job...!!",Toast.LENGTH_LONG).show();}
                else
                {Toast.makeText(context,"Could Not Connect To Server...!!",Toast.LENGTH_LONG).show();}
            }
            catch (JSONException e)
            {
                e.printStackTrace();
                Toast.makeText(context,"Could Not Connect To Server...!!",Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(context,"Could Not Connect To Server...!!",Toast.LENGTH_LONG).show();
        }

    }

}
