package com.example.daivansh.jobreferer.connections;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

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
 * Created by Priyanshu on 08-06-2017.
 */

public class UpdateSkillsConnection extends AsyncTask<String,Void,String> {
    Context context;
    String server,skills;

    public UpdateSkillsConnection(Context context)
    {

        this.context = context;
    }
    @Override
    protected String doInBackground(String... params) {

        server = context.getString(R.string.server_url);

        String data, link, result;
        BufferedReader bufferedReader;
        try
        {

           /* data = "?uid="+ URLEncoder.encode(params[0],"UTF-8");
            skills = params[1];
            data += "&uskills="+ URLEncoder.encode(skills,"UTF-8");
            link = server+"/UpdateSkills.php"+data;

            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3000);
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            result = bufferedReader.readLine();
            return result;      */

            data = URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(params[0], "UTF-8");
            data += "&" + URLEncoder.encode("uskills", "UTF-8") + "=" + URLEncoder.encode(params[1], "UTF-8");

            link = server+"/UpdateSkills.php";

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
           // Toast.makeText(context,"Contact developer:"+ex.getMessage(),Toast.LENGTH_LONG).show();
            return new String("Exception : "+ex.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        String jsonStr = result;
        if (jsonStr != null)
        {
            try
            {
                JSONObject jsonObject = new JSONObject(jsonStr);
                String query_result = jsonObject.getString("query_result");
                if (query_result.equals("SUCCESS"))
                {
                    new SPHelper(context,context.getString(R.string.skillsPrefer));
                    SPHelper.putData(context.getString(R.string.keyINTRESTS),skills);
                }
                else if(query_result.equals("FAILURE"))
                {
                    Toast.makeText(context,"Interests Not Saved...!",Toast.LENGTH_LONG).show();
                }
                else
                {Toast.makeText(context,"Could Not Connect To Server...!!",Toast.LENGTH_LONG).show();}
            }
            catch (JSONException e)
            {
                e.printStackTrace();
              //  Toast.makeText(context,"Error parsing JSON data.",Toast.LENGTH_LONG).show();
                Toast.makeText(context,"Could Not Connect To Server...!!",Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            //Toast.makeText(context,"Could not get JSON data",Toast.LENGTH_LONG).show();
            Toast.makeText(context,"Could Not Connect To Server...!!",Toast.LENGTH_LONG).show();
        }


    }
}
