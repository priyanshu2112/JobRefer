package com.example.daivansh.jobreferer.connections;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.daivansh.jobreferer.R;
import com.example.daivansh.jobreferer.activities.RecentPostsActivity;

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
 * Created by Priyanshu on 05-06-2017.
 */

public class DeletePostConnection extends AsyncTask<String,Void,String> {
    String jobid,server;
    Context context;

    public DeletePostConnection(Context context)
    {
        this.context = context;
    }
    @Override
    protected String doInBackground(String... args) {
       jobid = args[0];


        server = context.getString(R.string.server_url);

        String data, link, result;
        BufferedReader bufferedReader;
        try
        {
            /*    data = "?jobid="+ URLEncoder.encode(jobid,"UTF-8");


            link = server+"/deletepost.php"+data;

            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            result = bufferedReader.readLine();
            return result;      */

            data = URLEncoder.encode("jobid", "UTF-8") + "=" + URLEncoder.encode(jobid, "UTF-8");

            link = server+"/deletepost.php";

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
            Toast.makeText(context,"Data_not_found",Toast.LENGTH_LONG).show();
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
                if (query_result.equals("SUCCESS"))
                {Toast.makeText(context,"Job Post Deleted Successfully...!!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context,RecentPostsActivity.class);
                    context.startActivity(intent);
                }
                else if(query_result.equals("FAILURE"))
                {Toast.makeText(context,"Job Post Not Deleted...!!",Toast.LENGTH_LONG).show();}
                else
                {Toast.makeText(context,"Could Not Connect To Server",Toast.LENGTH_LONG).show();}
            }
            catch (JSONException e)
            {
                e.printStackTrace();

                Toast.makeText(context,"Could Not Connect To Server",Toast.LENGTH_LONG).show();
               // Toast.makeText(context,e.getMessage()+" <",Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(context,"Could Not Connect To Server",Toast.LENGTH_LONG).show();
        }
    }
}
