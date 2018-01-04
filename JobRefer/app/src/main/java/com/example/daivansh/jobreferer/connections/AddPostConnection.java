package com.example.daivansh.jobreferer.connections;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.View;
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
 * Created by daivansh on 05-06-2017.
 */

public class AddPostConnection extends AsyncTask<String,View,String> {
    Context context;
    String title,desc,loc,totalsalary,exp,intrst,curdate,expirydate,server,userid;
    public AddPostConnection(Context context)
    {
        this.context = context;
    }

    ProgressDialog progDailog;
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progDailog = new ProgressDialog(context);
        progDailog.setMessage("Posting");
        progDailog.setIndeterminate(true);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(false);
        progDailog.show();
    }

    @Override
    protected String doInBackground(String... args) {
        title = args[0];
        desc = args[1];
        loc = args[2];
        totalsalary = args[3];
        exp = args[4];
        intrst = args[5];
        curdate=args[6];
        expirydate=args[7];
        userid=args[8];
        server = context.getString(R.string.server_url);

        String data, link, result;
        BufferedReader bufferedReader;
        try
        {
           /* data = "?title="+ URLEncoder.encode(title,"UTF-8");
            data += "&description="+ URLEncoder.encode(desc,"UTF-8");
            data += "&location="+ URLEncoder.encode(loc,"UTF-8");
            data += "&salary="+ URLEncoder.encode(totalsalary,"UTF-8");
            data += "&experience="+ URLEncoder.encode(exp,"UTF-8");
            data += "&interests="+ URLEncoder.encode(intrst,"UTF-8");
            data += "&currentdate="+ URLEncoder.encode(curdate,"UTF-8");
            data += "&expirydate="+ URLEncoder.encode(expirydate,"UTF-8");
            data += "&userid="+ URLEncoder.encode(userid,"UTF-8");

            link = server+"/addpost.php"+data;

            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            result = bufferedReader.readLine();
            return result;      */


            data = URLEncoder.encode("title", "UTF-8") + "=" + URLEncoder.encode(title, "UTF-8");
            data += "&" + URLEncoder.encode("description", "UTF-8") + "=" + URLEncoder.encode(desc, "UTF-8");
            data += "&" + URLEncoder.encode("location", "UTF-8") + "=" + URLEncoder.encode(loc, "UTF-8");
            data += "&" + URLEncoder.encode("salary", "UTF-8") + "=" + URLEncoder.encode(totalsalary, "UTF-8");
            data += "&" + URLEncoder.encode("experience", "UTF-8") + "=" + URLEncoder.encode(exp, "UTF-8");
            data += "&" + URLEncoder.encode("interests", "UTF-8") + "=" + URLEncoder.encode(intrst, "UTF-8");
            data += "&" + URLEncoder.encode("currentdate", "UTF-8") + "=" + URLEncoder.encode(curdate, "UTF-8");
            data += "&" + URLEncoder.encode("expirydate", "UTF-8") + "=" + URLEncoder.encode(expirydate, "UTF-8");
            data += "&" + URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");

            link = server+"/addpost.php";

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
                {Toast.makeText(context,"Job posted Successfully...!!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context, RecentPostsActivity.class);
                    context.startActivity(intent);
                }
                else if(query_result.equals("FAILURE"))
                {Toast.makeText(context,"Post Not Added...!!",Toast.LENGTH_LONG).show();}
                else
                {Toast.makeText(context,"Could Not Connect To Server",Toast.LENGTH_LONG).show();}
            }
            catch (JSONException e)
            {
                e.printStackTrace();
             //   Toast.makeText(context,"Error parsing JSON data.",Toast.LENGTH_LONG).show();
                Toast.makeText(context,"Could Not Connect To Server",Toast.LENGTH_LONG).show();
            }
        }
        else
        {
          //  Toast.makeText(context,"Could Not Get Data",Toast.LENGTH_LONG).show();
            Toast.makeText(context,"Could Not Connect To Server...!!",Toast.LENGTH_LONG).show();
        }
    }
}
