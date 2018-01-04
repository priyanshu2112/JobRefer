package com.example.daivansh.jobreferer.connections;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
import android.widget.Toast;

import com.example.daivansh.jobreferer.R;
import com.example.daivansh.jobreferer.activities.ViewProfileActivity;

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
 * Created by daivansh on 09-06-2017.
 */

public class ChangePasswordConnection extends AsyncTask<String,View,String> {
    String oldpasswd,newpasswd,confpassswd,userid,server;
    Context context;
    public ChangePasswordConnection(Context context)
    {
        this.context = context;
    }
    @Override
    protected String doInBackground(String... args) {
        oldpasswd = args[0];
        newpasswd = args[1];
        userid = args[2];
        server = context.getString(R.string.server_url);

        String data, link, result;
        BufferedReader bufferedReader;

        try
        {
           /* data = "?oldpassword="+ URLEncoder.encode(oldpasswd,"UTF-8");
            data += "&newpassword="+ URLEncoder.encode(newpasswd,"UTF-8");
            data += "&userid="+ URLEncoder.encode(userid,"UTF-8");

            link = server+"/changepassword.php"+data;

            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            result = bufferedReader.readLine();
            return result;  */

            data = URLEncoder.encode("oldpassword", "UTF-8") + "=" + URLEncoder.encode(oldpasswd, "UTF-8");
            data += "&" + URLEncoder.encode("newpassword", "UTF-8") + "=" + URLEncoder.encode(newpasswd, "UTF-8");
            data += "&" + URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");

            link = server+"/changepassword.php";

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
           // Toast.makeText(context,"Data_not_found",Toast.LENGTH_LONG).show();
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
                {
                    Toast.makeText(context,"Password Changed Successfully...!!",Toast.LENGTH_LONG).show();
                    Intent intent=new Intent(context,ViewProfileActivity.class);
                    context.startActivity(intent);
                }
                else if(query_result.equals("NO_CHANGE"))
                {Toast.makeText(context,"Already Same Password Is Present...!!  No Need To Change",Toast.LENGTH_LONG).show();}
                else if(query_result.equals("FAILURE"))
                {Toast.makeText(context,"Wrong OLD Password...!!",Toast.LENGTH_LONG).show();}
                else
                {Toast.makeText(context,"Could Not Connect To Server",Toast.LENGTH_LONG).show();}
            }
            catch (JSONException e)
            {
                e.printStackTrace();
             //  Toast.makeText(context,"Error parsing JSON data.",Toast.LENGTH_LONG).show();
                Toast.makeText(context,"Could Not Connect To Server",Toast.LENGTH_LONG).show();
            }
        }
        else
        {
           // Toast.makeText(context,"Could not get JSON data",Toast.LENGTH_LONG).show();
            Toast.makeText(context,"Could Not Connect To Server",Toast.LENGTH_LONG).show();
        }

    }

}
