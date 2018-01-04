package com.example.daivansh.jobreferer.activities;


import android.support.v7.app.ActionBar;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.daivansh.jobreferer.R;
import com.example.daivansh.jobreferer.connections.UserRegistrationConnection;

public class RegistrationActivity extends AppCompatActivity {
    EditText fname,lname,email,contact,pwd,confpwd;
    RadioButton rbmale,rbfemale;
    Button register;
    Toolbar toolbar;
    String fnm,lnm,mail,num,pswd,cpswd,gender;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        overridePendingTransition(R.anim.from, R.anim.to);
        fname=(EditText)findViewById(R.id.editTextfname);
        lname=(EditText)findViewById(R.id.editTextlname);
        email=(EditText)findViewById(R.id.editTextemail);
        contact=(EditText)findViewById(R.id.editTextcontact);
        pwd=(EditText)findViewById(R.id.editTextpwd);
        confpwd=(EditText)findViewById(R.id.editTextconfpwd);
        register=(Button)findViewById(R.id.buttonregister);
        rbmale=(RadioButton)findViewById(R.id.radioButtonmale);
        rbfemale=(RadioButton)findViewById(R.id.radioButtonfemale);
//        toolbar = (Toolbar)findViewById(R.id.toolbar222);
//        setSupportActionBar(toolbar);
//      ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }
    public void login(View v)
    {
        Intent intent=new Intent(RegistrationActivity.this,LoginActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.from, R.anim.to);
    }
    public void register()
    {
        fnm = fname.getText().toString();
        lnm = lname.getText().toString();
        mail = email.getText().toString();
        num = contact.getText().toString();
        pswd = pwd.getText().toString();
        cpswd = confpwd.getText().toString();

        if(rbmale.isChecked())
        {
            gender = "M";
        }
        else if(rbfemale.isChecked())
        {
            gender = "F";
        }

        if(!(fnm.equals(""))&&!(lnm.equals(""))&&!(mail.equals(""))&&!(num.equals(""))&&!(pswd.equals(""))&&!(cpswd.equals("")))
        {
            if(pswd.equals(cpswd))
            {
                new UserRegistrationConnection(this).execute(fnm,lnm,mail,num,pswd,gender);
            }
        }
        else
        {
            Toast.makeText(this,"Fill All Feilds...!!",Toast.LENGTH_LONG).show();
        }
    }


}
