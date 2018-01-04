package com.example.daivansh.jobreferer.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class ResetPasswordActivity extends AppCompatActivity {
    String pass1,pass2,uid,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        TextView tv_email = (TextView) findViewById(R.id.user_email);
        final EditText et_pswd = (EditText) findViewById(R.id.editTextnewpasswd_reset);
        final EditText et_pswd2 = (EditText) findViewById(R.id.editTextconfirmpwd_reset);
        Button btn_changePswd = (Button) findViewById(R.id.buttonchangepasswd_reset);
        Intent intent = getIntent();
        uid = intent.getStringExtra("uid");
        email = intent.getStringExtra("email");
        tv_email.setText(email);
        btn_changePswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass1 = et_pswd.getText().toString();
                pass2 = et_pswd2.getText().toString();
                if(!pass1.equals("") && !pass2.equals(""))
                {
                    if(pass1.equals(pass2))
                    {
                        new NewPassword().execute(uid,pass1);
                    }
                    else
                    {
                        Toast.makeText(ResetPasswordActivity.this, "Plese enter both passwords equal", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(ResetPasswordActivity.this, "Please enter both passwords", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private class NewPassword extends AsyncTask<String, Void, String>
    {
        //ProgressDialog progDailog;
        Context context = ResetPasswordActivity.this;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progDailog = new ProgressDialog(context);
//            progDailog.setMessage("Changing your password");
//            progDailog.setIndeterminate(true);
//            progDailog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
//            progDailog.setCancelable(false);
//            progDailog.show();
        }



        @Override
        protected String doInBackground(String... params) {
            String server = getString(R.string.server_url);
            String uid = params[0];
            String pswd = params[1];
            String data, link, result;
            BufferedReader bufferedReader;
            try {
                data = URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(uid, "UTF-8");
                data += "&" + URLEncoder.encode("pswd", "UTF-8") + "=" + URLEncoder.encode(pswd, "UTF-8");
                link = server+"/resetpassword.php";

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

            if (jsonStr != null)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    String query_result = jsonObject.getString("query_result");
                    if (query_result.equals("SUCCESS"))
                    {
                        Toast.makeText(context,"Password Changed",Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(context,LoginActivity.class);
                        context.startActivity(intent);
                    }
                    else if(query_result.equals("FAIL"))
                    {
                        Toast.makeText(context,"Password not changed",Toast.LENGTH_LONG).show();
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

}
