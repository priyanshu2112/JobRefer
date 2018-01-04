package com.example.daivansh.jobreferer.connections;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

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
 * Created by Priyanshu on 05-06-2017.
 */

public class UserRegistrationConnection extends AsyncTask<String,Void,String> {
    String fnm,lnm,mail,num,pswd,gender,server;
    Context context;

    ProgressDialog progDailog;

    public UserRegistrationConnection(Context context)
    {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progDailog = new ProgressDialog(context);
        progDailog.setMessage("Registering");
        progDailog.setIndeterminate(true);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(false);
        progDailog.show();
    }



    @Override
    protected String doInBackground(String... args) {
        fnm = args[0];
        lnm = args[1];
        mail = args[2];
        num = args[3];
        pswd = args[4];
        gender = args[5];


        server = context.getString(R.string.server_url);

        String data, link, result;
        BufferedReader bufferedReader;
        try
        {
         /*   data = "?firstname="+ URLEncoder.encode(fnm,"UTF-8");
            data += "&lastname="+ URLEncoder.encode(lnm,"UTF-8");
            data += "&email="+ URLEncoder.encode(mail,"UTF-8");
            data += "&contactno="+ URLEncoder.encode(num,"UTF-8");
            data += "&gender="+ URLEncoder.encode(gender,"UTF-8");
            data += "&password="+ URLEncoder.encode(pswd,"UTF-8");


            link = server+"/signup.php"+data;

            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            result = bufferedReader.readLine();
            return result;      */

            data = URLEncoder.encode("firstname", "UTF-8") + "=" + URLEncoder.encode(fnm, "UTF-8");
            data += "&" + URLEncoder.encode("lastname", "UTF-8") + "=" + URLEncoder.encode(lnm, "UTF-8");
            data += "&" + URLEncoder.encode("email", "UTF-8") + "=" + URLEncoder.encode(mail, "UTF-8");
            data += "&" + URLEncoder.encode("contactno", "UTF-8") + "=" + URLEncoder.encode(num, "UTF-8");
            data += "&" + URLEncoder.encode("gender", "UTF-8") + "=" + URLEncoder.encode(gender, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(pswd, "UTF-8");

            link = server+"/signup.php";

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
          //  Toast.makeText(context,"Data_not_found",Toast.LENGTH_LONG).show();
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
                    new UserLoginConnection(context).execute(mail,pswd);}
                else if(query_result.equals("FAILURE"))
                {Toast.makeText(context,"Registration Failed! Please try after some time",Toast.LENGTH_LONG).show();}
                else if(query_result.equals("EMAIL_EXIST"))
                {Toast.makeText(context,"Email already exist",Toast.LENGTH_LONG).show();}
                else if(query_result.equals("NUMBER_EXIST"))
                {Toast.makeText(context,"Contact already exist",Toast.LENGTH_LONG).show();}
                else
                {Toast.makeText(context,"Could Not Connect To Server...!!",Toast.LENGTH_LONG).show();}
            }
            catch (JSONException e)
            {
                e.printStackTrace();

//                Toast.makeText(context,"Error parsing JSON data.",Toast.LENGTH_LONG).show();
//                Toast.makeText(context,e.getMessage()+" <",Toast.LENGTH_LONG).show();
                Toast.makeText(context,"Could Not Connect To Server...!!",Toast.LENGTH_LONG).show();
            }
        }
        else
        {
//            Toast.makeText(context,"Could not get JSON data",Toast.LENGTH_LONG).show();
            Toast.makeText(context,"Could Not Connect To Server...!!",Toast.LENGTH_LONG).show();
        }
    }
}
