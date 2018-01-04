package com.example.daivansh.jobreferer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daivansh.jobreferer.R;
import com.example.daivansh.jobreferer.adapters.RecentPostsRecyclerAdapter;
import com.example.daivansh.jobreferer.helper.SPHelper;
import com.example.daivansh.jobreferer.pojos.MySearchHomeData;

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

public class RecentPostsActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    String userid;
    View view;

    Context context = RecentPostsActivity.this;
    ArrayList<MySearchHomeData> arrayList = new ArrayList<MySearchHomeData>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent_posts);
        overridePendingTransition(R.anim.from, R.anim.to);
        view = findViewById(R.id.activity_recent_posts);
        toolbar = (Toolbar) findViewById(R.id.toolbar4);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("YOUR RECENT POSTS");
        new SPHelper(context, getString(R.string.defaultPrefer));
        userid = SPHelper.getData(getString(R.string.keyUID));
        new RecentPosts_CONN().execute(userid);
    }


        private class RecentPosts_CONN extends AsyncTask<String,Void,String> {

            String userid,server;
            String[] jobid,jobtitle,jobsal,jobdate,joblocation;
           // String[][] all;




            @Override
            protected String doInBackground(String... params) {
                userid=params[0];
                server = context.getString(R.string.server_url);

                String data, link, result;
                BufferedReader bufferedReader;
                try
                {
                 /*   data = "?userid="+ URLEncoder.encode(userid,"UTF-8");
                    link = server+"/recentposts.php"+data;

                    URL url = new URL(link);
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    result = bufferedReader.readLine();
                    return result;      */

                    data = URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");

                    link = server+"/recentposts.php";

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
            @Override
            protected void onPostExecute(String res) {
                String jSonres=res;
                Snackbar snackbar = Snackbar.make(view,"No job posts.",Snackbar.LENGTH_INDEFINITE);
                View snackbarView = snackbar.getView();
                TextView snackbartv = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
                snackbartv.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
                snackbartv.setAllCaps(true);
                snackbartv.setTextSize(18);
                snackbartv.setWidth(snackbarView.getWidth());
                snackbartv.setGravity(Gravity.CENTER);


                JSONObject jsonObject;


                if (jSonres != null) {
                    try {
                        // JSONObject jsonObjecttemp = new JSONObject(jSonres);
                        // String queryresult=jsonObjecttemp.getString("query_result");
                        jsonObject = new JSONObject(jSonres);
                        JSONArray jsonArray = (JSONArray) jsonObject.get("data");
                        if (!(jsonArray.getJSONObject(0).getString("query_result").equalsIgnoreCase("NO_POST_PRESENT")))
                        {
                            jobid = new String[jsonArray.length()];
                            jobtitle = new String[jsonArray.length()];
                            jobsal = new String[jsonArray.length()];
                            jobdate = new String[jsonArray.length()];
                            joblocation = new String[jsonArray.length()];
                            for (int i = 0; i < jsonArray.length(); i++)
                            {
                                jobid[i] = jsonArray.getJSONObject(i).getString("jobid");
                                jobtitle[i] = jsonArray.getJSONObject(i).getString("jobtitle");
                                jobsal[i] = jsonArray.getJSONObject(i).getString("jobsalary");
                                jobdate[i] = jsonArray.getJSONObject(i).getString("jobdate");
                                joblocation[i] = jsonArray.getJSONObject(i).getString("joblocation");
                            }
                            //all = integrateAll();
                            //DataRecentPosts.data=all;
                            recyclerView = (RecyclerView) findViewById(R.id.recentpostlist);
                            //DataRecentPosts dataRecentPosts = new DataRecentPosts();
                            //jobid = DataRecentPosts.data[0];
                            //jobtitle = DataRecentPosts.data[1];
                            //jobsal = DataRecentPosts.data[2];
                            //jobdate = DataRecentPosts.data[3];
                            //joblocation = DataRecentPosts.data[4];
                           // if (!(jobtitle[0].equals("")) && !(jobsal[0].equals("")) && !(jobdate[0].equals("")) && !(joblocation[0].equals("")))
                          //  {
                                if (jobid != null && jobtitle != null && jobsal != null && jobdate != null && joblocation != null) {
                                    int i = 0;
                                    for (String data : jobtitle) {
                                        MySearchHomeData mySearchHomeData = new MySearchHomeData(data, jobsal[i], jobdate[i], joblocation[i],jobsal[i], jobdate[i], joblocation[i]);
                                        arrayList.add(mySearchHomeData);
                                        i++;
                                    }
                                    adapter = new RecentPostsRecyclerAdapter(context, jobid, arrayList);
                                    recyclerView.setHasFixedSize(true);
                                    layoutManager = new LinearLayoutManager(context);
                                    recyclerView.setLayoutManager(layoutManager);
                                    recyclerView.setAdapter(adapter);
                                }
                        //    }
                            else
                            {
                                Toast toast = new Toast(getBaseContext());
                                toast.setText("NO DATA FOUND");
                                toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
                                toast.setDuration(Toast.LENGTH_LONG);
                                toast.show();
                            }



                        }
                        else
                        {
                            //Toast.makeText(context, "No Jobs posted by you yet...!!", Toast.LENGTH_LONG).show();
                            snackbar.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                       // Toast.makeText(context, ":::" + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                   // Toast.makeText(context,"Could not get JSON data",Toast.LENGTH_LONG).show();
                    Toast.makeText(RecentPostsActivity.this, "Could Not Connected To Server...!!", Toast.LENGTH_LONG).show();
                }

            }
         /*   public String[][] integrateAll()
            {
                all[0] = jobid;
                all[1] = jobtitle;
                all[2] = jobsal;
                all[3] = jobdate;
                all[4] = joblocation;
                return all;
            }   */



        }





//        Toast.makeText(context, jobid[0], Toast.LENGTH_LONG).show();


/*    public static void maprecentpostsdata(String id[], String title[], String salary[], String date[], String location[]) {
        jobid = id;
        jobtitle = title;
        jobsal = salary;
        jobdate = date;
        joblocation = location;
    }*/

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getBaseContext(),HomeActivity.class);
        startActivity(intent);
    }
}

