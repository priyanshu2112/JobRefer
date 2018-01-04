package com.example.daivansh.jobreferer.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.daivansh.jobreferer.R;
import com.example.daivansh.jobreferer.connections.UserLoginConnection;
import com.google.firebase.messaging.RemoteMessage;

public class LoginActivity extends AppCompatActivity {
    Button signin;
    EditText etmail,etpass;
    String mail,pswd;
    public static Context myContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        overridePendingTransition(R.anim.from, R.anim.to);
        signin=(Button)findViewById(R.id.buttonsignin);
        etmail=(EditText)findViewById(R.id.editTextemail);
        etpass=(EditText)findViewById(R.id.editTextpassword);
        myContext = getApplicationContext();

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }
public void signup(View v)
{
    Intent intent=new Intent(LoginActivity.this,RegistrationActivity.class);
    startActivity(intent);
}
    public void forgetPassword(View v)
    {
        Intent intent=new Intent(LoginActivity.this,ForgetPasswordActivity.class);
        startActivity(intent);
    }

    public void login()
    {
        mail = etmail.getText().toString();
        pswd = etpass.getText().toString();
        if(!(mail.equals(""))&&!(pswd.equals("")))
        {
            new UserLoginConnection(this).execute(mail,pswd);
        }
        else
        {
            Toast.makeText(this,"fill all feilds",Toast.LENGTH_LONG).show();
        }
    }
    //public static Context getLoginActivityContext() {return myContext;}

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            //Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Toast.makeText(getBaseContext(), "" + remoteMessage.getNotification().getBody(), Toast.LENGTH_LONG).show();

        }
    }
}
