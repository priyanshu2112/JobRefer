package com.example.daivansh.jobreferer.activities;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daivansh.jobreferer.R;
import com.example.daivansh.jobreferer.adapters.SeeApplicantsRecyclerAdapter;
import com.example.daivansh.jobreferer.helper.TypefaceUtil;
import com.example.daivansh.jobreferer.pojos.MySeeApplicantsData;

import org.json.JSONArray;
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
import java.util.ArrayList;

import static com.example.daivansh.jobreferer.R.id.view;

public class SeeApplicantsActivity extends AppCompatActivity {
    Toolbar toolbar;
    String jobid;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    Context context=SeeApplicantsActivity.this;
    RelativeLayout rl;
    SwipeRefreshLayout swipeRefreshLayout;
    ArrayList<MySeeApplicantsData> arrayList = new ArrayList<MySeeApplicantsData>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_applicants);
        overridePendingTransition(R.anim.from, R.anim.to);
        rl = (RelativeLayout) findViewById(R.id.activity_see_applicants);
        toolbar=(Toolbar)findViewById(R.id.toolbar6);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("APPLICANTS FOR YOUR POST");
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayoutapplicants);
        jobid=getIntent().getStringExtra("jobid");
        //call here GetApplicantsData_CONNECTION to get list of applicants
          new ApplicantsData_CONN().execute(jobid);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_orange_light);

    }
    void refreshItems() {
        new ApplicantsData_CONN().execute(jobid);
        swipeRefreshLayout.setRefreshing(false);
    }


    private class ApplicantsData_CONN extends AsyncTask<String,Void,String> {
        String jobid,server;
        String[] applicantname,applicantemail,applicantcontact;

        View view=rl;
        Snackbar snackbar;
        View snackbarView;
        TextView snackbartv;

        @Override
        protected String doInBackground(String... params) {
            jobid=params[0];
            server = context.getString(R.string.server_url);

            String data, link, result;
            BufferedReader bufferedReader;
            try
            {
            /*    data = "?jobid="+ URLEncoder.encode(jobid,"UTF-8");
                link = server+"/applicantsdata.php"+data;

                URL url = new URL(link);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                result = bufferedReader.readLine();
                return result;      */

                data = URLEncoder.encode("jobid", "UTF-8") + "=" + URLEncoder.encode(jobid, "UTF-8");

                link = server+"/applicantsdata.php";

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
                //Toast.makeText(context,"Contact developer:"+ex.getMessage(),Toast.LENGTH_LONG).show();
                return new String("Exception : "+ex.getMessage());
            }
        }
        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onPostExecute(String res) {
            String jSonres=res;
            JSONObject jsonObject;

            snackbar = Snackbar.make(view,"No applicants applied yet.",Snackbar.LENGTH_INDEFINITE);
            snackbarView = snackbar.getView();
            //snackbarView.setBackgroundColor(ContextCompat.getColor(context, R.color.colorWhite));
            snackbarView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.colorAccent)));
            snackbarView.setBackgroundTintMode(PorterDuff.Mode.OVERLAY);
            snackbartv = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
            snackbartv.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
            snackbartv.setTextSize(16);
            TypefaceUtil.overrideFont(context, "SANS-SERIF", "fonts/Montserrat-Regular.ttf");
            snackbartv.setTypeface(Typeface.SANS_SERIF);

            if (jSonres != null) {
                try {
                   // JSONObject jsonObjecttemp = new JSONObject(jSonres);
                   // String queryresult=jsonObjecttemp.getString("query_result");
                    jsonObject = new JSONObject(jSonres);
                    JSONArray jsonArray = (JSONArray) jsonObject.get("data");
                    if (!(jsonArray.getJSONObject(0).getString("query_result").equalsIgnoreCase("NO_APPLICANT_PRESENT"))) {
                        applicantname = new String[jsonArray.length()];
                        applicantemail = new String[jsonArray.length()];
                        applicantcontact = new String[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            applicantname[i] = jsonArray.getJSONObject(i).getString("applicantname");
                            applicantemail[i] = jsonArray.getJSONObject(i).getString("applicantemail");
                            applicantcontact[i] = jsonArray.getJSONObject(i).getString("applicantcontact");
                        }
                        recyclerView=(RecyclerView)findViewById(R.id.seeapplicantslist);
                            arrayList.clear();
                        //   if(!(applicantname[0].equals(""))&&!(applicantmail[0].equals(""))&&!(applicantcontact[0].equals(""))) {
                        if( applicantname!=null && applicantemail!=null && applicantcontact!=null){
                            int i=0;
                            for (String data : applicantname) {
                                MySeeApplicantsData mySeeApplicantsData = new MySeeApplicantsData(data,  applicantcontact[i], applicantemail[i]);
                                arrayList.add(mySeeApplicantsData);
                                i++;
                            }
                            adapter = new SeeApplicantsRecyclerAdapter(arrayList, context);
                            recyclerView.setHasFixedSize(true);
                            layoutManager = new LinearLayoutManager(context);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);
                        }
                        else
                        {

                            snackbar.show();
                        }
                    } else {

                        snackbar.show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            else
            {
               // Toast.makeText(context,"Could not get JSON data",Toast.LENGTH_LONG).show();
                Toast.makeText(SeeApplicantsActivity.this, "Could Not Connected To Server...!!", Toast.LENGTH_LONG).show();
            }
        }
    }

}
