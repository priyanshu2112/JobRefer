package com.example.daivansh.jobreferer.activities;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.daivansh.jobreferer.R;
import com.example.daivansh.jobreferer.adapters.RecentApplicationsRecyclerAdapter;
import com.example.daivansh.jobreferer.helper.SPHelper;
import com.example.daivansh.jobreferer.pojos.MyRecentApplicationsData;

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

public class RecentApplicationsActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    Context context = RecentApplicationsActivity.this;
    String userid;
    ArrayList<MyRecentApplicationsData> arrayList = new ArrayList<MyRecentApplicationsData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_applications);
        overridePendingTransition(R.anim.from, R.anim.to);
        toolbar = (Toolbar) findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("YOUR RECENT APPLICATIONS");

        new SPHelper(context, getString(R.string.defaultPrefer));
        userid = SPHelper.getData(getString(R.string.keyUID));
        new RecentApplications_CONN().execute(userid);

    }


    private class RecentApplications_CONN extends AsyncTask<String, Void, String> {
        String userid, server;
        String[] jobtitle, jobdesc, jobsal, jobdate, joblocation, jobexperience;

        @Override
        protected String doInBackground(String... params) {
            userid = params[0];
            server = context.getString(R.string.server_url);

            String data, link, result;
            BufferedReader bufferedReader;
            try {
               /*    data = "?userid=" + URLEncoder.encode(userid, "UTF-8");
                link = server + "/recentapplications.php" + data;

                URL url = new URL(link);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                result = bufferedReader.readLine();
                return result;  */

                data = URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");

                link = server+"/recentapplications.php";

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
            } catch (Exception ex) {
                //Toast.makeText(context,"Contact developer:"+ex.getMessage(),Toast.LENGTH_LONG).show();
                return new String("Exception : " + ex.getMessage());
            }
        }

        @Override
        protected void onPostExecute(String res) {
            String jSonres = res;
            JSONObject jsonObject;
            if (jSonres != null) {
                try {
                    jsonObject = new JSONObject(jSonres);
                    JSONArray jsonArray = (JSONArray) jsonObject.get("data");
                    //JSONObject jsonObjecttemp = new JSONObject(jSonres);
                    //String queryresult = jsonObjecttemp.getString("query_result");
                    if (!(jsonArray.getJSONObject(0).getString("query_result").equalsIgnoreCase("NO_APPLICATION_PRESENT"))) {
                        jobtitle = new String[jsonArray.length()];
                        jobsal = new String[jsonArray.length()];
                        jobdate = new String[jsonArray.length()];
                        joblocation = new String[jsonArray.length()];
                        jobdesc = new String[jsonArray.length()];
                        jobexperience = new String[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jobtitle[i] = jsonArray.getJSONObject(i).getString("jobtitle");
                            jobsal[i] = jsonArray.getJSONObject(i).getString("jobsalary");
                            jobdate[i] = jsonArray.getJSONObject(i).getString("jobdate");
                            joblocation[i] = jsonArray.getJSONObject(i).getString("joblocation");
                            jobdesc[i] = jsonArray.getJSONObject(i).getString("jobdescription");
                            jobexperience[i] = jsonArray.getJSONObject(i).getString("jobexperience");
                        }
                        recyclerView=(RecyclerView)findViewById(R.id.recentapplicationlist);
                        //  if (!(jobtitle[0].equals("")) && !(jobsal[0].equals("")) && !(jobdate[0].equals("")) && !(joblocation[0].equals(""))
                        //        && !(jobdesc[0].equals("")) && !(jobexperience[0].equals(""))) {
                        if( jobdesc!=null && jobtitle!=null && jobsal!=null && jobdate!=null  && joblocation!=null && jobexperience!=null){
                            int i = 0;
                            for (String data : jobtitle) {
                                MyRecentApplicationsData myRecentApplicationsData= new MyRecentApplicationsData(data, jobsal[i], jobdate[i], joblocation[i],jobexperience[i],jobdesc[i]);
                                arrayList.add(myRecentApplicationsData);
                                i++;
                            }
                            adapter = new RecentApplicationsRecyclerAdapter(arrayList, jobdesc, jobexperience, context);
                            recyclerView.setHasFixedSize(true);
                            layoutManager = new LinearLayoutManager(context);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);
                        }
                    } else {
                        Toast.makeText(context, "You Haven't Applied For Any Job Yet...!!", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                  //  Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            } else {
               // Toast.makeText(context, "Could not get JSON data", Toast.LENGTH_LONG).show();
                Toast.makeText(RecentApplicationsActivity.this, "Could Not Connected To Server...!!", Toast.LENGTH_LONG).show();
            }
        }
    }
}
