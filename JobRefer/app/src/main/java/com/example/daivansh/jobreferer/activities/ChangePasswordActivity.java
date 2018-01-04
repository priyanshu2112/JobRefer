package com.example.daivansh.jobreferer.activities;

import android.content.DialogInterface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.daivansh.jobreferer.R;
import com.example.daivansh.jobreferer.connections.ChangePasswordConnection;
import com.example.daivansh.jobreferer.helper.SPHelper;

public class ChangePasswordActivity extends AppCompatActivity {
    Toolbar toolbar;
    EditText oldpass,newpass,confpass;
    Button btnchangepasswd;
    String stroldpwd,strnewpwd,strconfpwd,uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        toolbar=(Toolbar)findViewById(R.id.toolbar8);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Change Password");
        oldpass=(EditText)findViewById(R.id.editTextoldpasswd);
        newpass=(EditText)findViewById(R.id.editTextnewpasswd);
        confpass=(EditText)findViewById(R.id.editTextconfirmpwd);
        btnchangepasswd=(Button)findViewById(R.id.buttonchangepasswd);

        btnchangepasswd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stroldpwd=oldpass.getText().toString();
                strnewpwd=newpass.getText().toString();
                strconfpwd=confpass.getText().toString();
                if(!(stroldpwd.equals("")) && !(strnewpwd.equals("")) && !(strconfpwd.equals("")))
                {
                    if(strnewpwd.equals(strconfpwd))
                    {
                       /* new SPHelper(ChangePasswordActivity.this,getString(R.string.defaultPrefer));
                        uid = SPHelper.getData(getString(R.string.keyUID));
                        new ChangePasswordConnection(ChangePasswordActivity.this).execute(stroldpwd,strnewpwd,uid);    */
                        changePasswordDialog();
                    }
                    else
                    {
                        Toast.makeText(ChangePasswordActivity.this, "New Password Do Not Match...!!", Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(ChangePasswordActivity.this, "Please Fill Out All Fields...!!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
    public void changePasswordDialog()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ChangePasswordActivity.this);
        alertDialog.setTitle("Confirm Password Change...");
        alertDialog.setMessage("Are you sure you want to change your password?");
        alertDialog.setIcon(R.drawable.logo);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                new SPHelper(ChangePasswordActivity.this,getString(R.string.defaultPrefer));
                uid = SPHelper.getData(getString(R.string.keyUID));
                new ChangePasswordConnection(ChangePasswordActivity.this).execute(stroldpwd,strnewpwd,uid);
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }
 /*
    @Override
    public void onBackPressed() {
        //Intent startMain = new Intent(Intent.ACTION_MAIN);
        //startMain.addCategory(Intent.CATEGORY_HOME);
        //startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(startMain);
    }*/


}
