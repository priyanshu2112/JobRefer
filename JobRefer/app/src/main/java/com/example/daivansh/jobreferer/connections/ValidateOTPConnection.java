package com.example.daivansh.jobreferer.connections;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.daivansh.jobreferer.R;
import com.example.daivansh.jobreferer.activities.ForgetPasswordActivity;
import com.example.daivansh.jobreferer.activities.LoginActivity;
import com.example.daivansh.jobreferer.activities.ResetPasswordActivity;
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
 * Created by Priyanshu on 15-07-2017.
 */

public class ValidateOTPConnection extends AsyncTask<String, Void, String> {
    String uid,email,user_otp;
    Context context;
    public ValidateOTPConnection(Context context)
    {
        this.context = context;
    }
    //ProgressDialog progressDialog;
    @Override
    protected void onPreExecute() {
//        progressDialog = new ProgressDialog(context);
//        progressDialog.setMessage("Validating...");
//        progressDialog.setIndeterminate(true);
//        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//        progressDialog.setCancelable(false);
//        progressDialog.show();

    }

    @Override
    protected String doInBackground(String... params) {
        String server = context.getString(R.string.server_url);
        uid = params[0];
        email = params[1];
        user_otp = params[2];


        String data, link, result;
        BufferedReader bufferedReader;
        try {
            data = URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(uid, "UTF-8");
            data += "&" + URLEncoder.encode("user_otp", "UTF-8") + "=" + URLEncoder.encode(user_otp, "UTF-8");
            link = server+"/validateotp.php";

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
        } catch (Exception e) {
            e.printStackTrace();
            return new String("Exception : "+e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String res) {
        String jsonStr = res;
        //Toast.makeText(context,jsonStr+"",Toast.LENGTH_LONG).show();

        if (jsonStr != null)
        {
            try
            {
                JSONObject jsonObject = new JSONObject(jsonStr);
                String query_result = jsonObject.getString("validate");
                if (query_result.equals("YES"))
                {
                    //Toast.makeText(context,"Correct OTP",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(context,ResetPasswordActivity.class);
                    intent.putExtra("uid",uid);
                    intent.putExtra("email",email);
                    context.startActivity(intent);
                }
                else if(query_result.equals("NO"))
                {
                    Toast.makeText(context,"Sorry, Invalid OTP",Toast.LENGTH_LONG).show();
                }
                else if(query_result.equals("EXPIRED"))
                {
                    Toast.makeText(context,"This OTP is expired. Please request for another one.",Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(context,"Could Not Connect To Server",Toast.LENGTH_LONG).show();
                }
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
