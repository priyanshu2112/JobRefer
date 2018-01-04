package com.example.daivansh.jobreferer.activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.example.daivansh.jobreferer.R;

public class RecentApplicationDescriptionActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txttitle,txtdesc,txtsal,txtexp,txtloc,txtdate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_application_description);
        toolbar=(Toolbar)findViewById(R.id.toolbar9);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("JOB DESCRIPTION");

        txttitle=(TextView)findViewById(R.id.textViewdescjobtitle);
        txtdesc=(TextView)findViewById(R.id.textViewdescjobdesc);
        txtsal=(TextView)findViewById(R.id.textViewdescjobsalary);
        txtexp=(TextView)findViewById(R.id.textViewdescjobexp);
        txtloc=(TextView)findViewById(R.id.textViewdescjoblocation);
        txtdate=(TextView)findViewById(R.id.textViewdate);
        txttitle.setText(getIntent().getStringExtra("job_title"));
        txtdesc.setText(getIntent().getStringExtra("job_description"));
        txtsal.setText(getIntent().getStringExtra("job_salary"));
        txtexp.setText(getIntent().getStringExtra("job_experience"));
        txtloc.setText(getIntent().getStringExtra("job_location"));
        txtdate.setText(getIntent().getStringExtra("job_date"));
    }
}
