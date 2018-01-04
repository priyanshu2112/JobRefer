package com.example.daivansh.jobreferer.activities;

import android.support.annotation.DrawableRes;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daivansh.jobreferer.R;
import com.example.daivansh.jobreferer.helper.SPHelper;
import com.example.daivansh.jobreferer.connections.JobApplyConnection;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JobDescriptionActivity extends AppCompatActivity {
    Toolbar toolbar;
    TextView txttitle,txtdesc,txtsal,txtexp,txtloc,txtdate;
    Button btnapplynow;
    String jobid,userid,dateofapply;
    String intentFrom;
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                overridePendingTransition(R.anim.from, R.anim.to);
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_description);
        overridePendingTransition(R.anim.from, R.anim.to);
        toolbar=(Toolbar)findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setTitle("APPLY FOR JOB HERE");
        btnapplynow=(Button)findViewById(R.id.buttonApplyNow);
        txttitle=(TextView)findViewById(R.id.textViewjobtitle);
        txtdesc=(TextView)findViewById(R.id.textViewjobdesc);
        txtsal=(TextView)findViewById(R.id.textViewjobsalary);
        txtexp=(TextView)findViewById(R.id.textViewexp);
        txtloc=(TextView)findViewById(R.id.textViewloc);
        txtdate=(TextView)findViewById(R.id.textViewdate);
        jobid=getIntent().getStringExtra("job_id");
        txttitle.setText(getIntent().getStringExtra("job_title"));
        txtdesc.setText(getIntent().getStringExtra("job_description"));
        txtsal.setText("₹ "+getIntent().getStringExtra("job_salary"));
        txtexp.setText(getIntent().getStringExtra("job_experience"));
        txtloc.setText(getIntent().getStringExtra("job_location"));
        txtdate.setText(getIntent().getStringExtra("job_date"));
        intentFrom = getIntent().getStringExtra("intent_from");
        actionBar.setTitle(getIntent().getStringExtra("job_title").toUpperCase());
        actionBar.setSubtitle(getIntent().getStringExtra("job_date"));
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_cross);


        if(intentFrom.equals("recent_applications"))
        {
            btnapplynow.setEnabled(false);
            btnapplynow.setVisibility(View.INVISIBLE);

        }
        else if(intentFrom.equals("home"))
        {
            btnapplynow.setEnabled(true);
            btnapplynow.setVisibility(View.VISIBLE);
        }

        btnapplynow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateofapply=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                new SPHelper(JobDescriptionActivity.this,getString(R.string.defaultPrefer));
                userid= SPHelper.getData("uid");
                if(!(dateofapply.equals(""))&&!(jobid.equals(""))&&!(userid.equals("")))
                {
                    new JobApplyConnection(JobDescriptionActivity.this).execute(jobid,userid,dateofapply);
                }
                else
                {
                    Toast.makeText(JobDescriptionActivity.this,"Empty Fields",Toast.LENGTH_LONG).show();
                }
            }
        });
    }




}
