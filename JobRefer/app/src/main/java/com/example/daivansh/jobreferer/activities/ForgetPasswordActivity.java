package com.example.daivansh.jobreferer.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daivansh.jobreferer.R;
import com.example.daivansh.jobreferer.connections.ValidateOTPConnection;

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

public class ForgetPasswordActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
TextView tv_open,tv_close,textTimer;
    EditText et_otp, editText_email;
    Button btn_validateOTP;
    String email,uid,user_otp;
    int time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        editText_email = (EditText) findViewById(R.id.et_fp_email);

        Button button = (Button) findViewById(R.id.button_SEND_OTP);
        tv_open = (TextView) findViewById(R.id.tv_open);
        tv_close = (TextView) findViewById(R.id.tv_close);
        textTimer = (TextView)findViewById(R.id.tv_timer);
        time = 180;
        btn_validateOTP = (Button)findViewById(R.id.button_VALIDATE_OTP);
        et_otp = (EditText) findViewById(R.id.et_fp_OTP);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = editText_email.getText().toString();
                if(!email.equals(""))
                new Sending_OTP().execute(email);
            }
        });

        btn_validateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Toast.makeText(ForgetPasswordActivity.this,"v'll send it buk!", Toast.LENGTH_LONG).show();
                user_otp = et_otp.getText().toString();

                if(!user_otp.equals(""))
                new ValidateOTPConnection(ForgetPasswordActivity.this).execute(uid,email,user_otp);

            }
        });
    }




    public class Sending_OTP extends AsyncTask<String, Void, String>
    {

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(ForgetPasswordActivity.this);
            progressDialog.setMessage("Requesting for OTP");
            progressDialog.setIndeterminate(true);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);

            progressDialog.show();
        }



        @Override
        protected String doInBackground(String... params) {
            String server = getString(R.string.server_url);
            String mail = params[0];
            String data, link, result;
            BufferedReader bufferedReader;
            try {
                data = URLEncoder.encode("user_mail", "UTF-8") + "=" + URLEncoder.encode(mail, "UTF-8");
                link = server+"/requestotp.php";

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
            progressDialog.dismiss();

            //Toast.makeText(ForgetPasswordActivity.this,res, Toast.LENGTH_LONG).show();
            if (jsonStr != null)
            {
                try {
                    final JSONObject jsonObject = new JSONObject(jsonStr);
                    String otp_sent = jsonObject.getString("otp_sent");

                    if(otp_sent.equals("YES"))
                    {

                        new CountDownTimer(180000, 1000) {

                            public void onTick(long millisUntilFinished) {
                                textTimer.setText(checkDigit(time));
                                time--;
                            }

                            public void onFinish() {
                                textTimer.setText("OTP has been expired!");
                            }

                        }.start();





                        tv_open.setEnabled(true);
                        tv_close.setEnabled(true);
                        et_otp.setEnabled(true);
                        btn_validateOTP.setEnabled(true);

                        try {
                            uid = jsonObject.getString("user_id");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                    else if(otp_sent.equals("NO"))
                    {


                        Toast.makeText(ForgetPasswordActivity.this,"Email not registered", Toast.LENGTH_LONG).show();
                        //Toast.makeText(ForgetPasswordActivity.this,res, Toast.LENGTH_LONG).show();
//                        progressDialog.dismiss();
                    }
                    else if(otp_sent.equals("EMAIL_INCORRECT"))
                    {


                        Toast.makeText(ForgetPasswordActivity.this,"Your email is not genuine", Toast.LENGTH_LONG).show();
                        //Toast.makeText(ForgetPasswordActivity.this,res, Toast.LENGTH_LONG).show();
//                        progressDialog.dismiss();
                    }
                    else
                    {
                       // progressDialog.dismiss();

                        Toast.makeText(ForgetPasswordActivity.this,"Something is not good! Please contact developer", Toast.LENGTH_LONG).show();
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }

        }
        public String checkDigit(int number) {
            return number <= 9 ? "0" + number : String.valueOf(number);
        }

    }

}
