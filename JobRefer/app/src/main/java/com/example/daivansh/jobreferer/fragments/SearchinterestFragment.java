package com.example.daivansh.jobreferer.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.daivansh.jobreferer.R;
import com.example.daivansh.jobreferer.adapters.SearchHomeRecyclerAdapter;
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
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by daivansh on 04-06-2017.
 */

public class SearchinterestFragment extends android.support.v4.app.Fragment implements MenuItem.OnActionExpandListener, SearchView.OnQueryTextListener{
    View myview;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    Context context;
    SearchView searchView;
    String custsearchoptions[]=new String[]{"Job Title","Location","Experience"};
    SwipeRefreshLayout swipeRefreshLayout;
    String[] jobid,jobtitle,jobdesc,jobsal,jobdate,joblocation,jobexperience;
    Boolean titlecheck=false,locationcheck=false,expcheck=false;
    ArrayList<MySearchHomeData> arrayList = new ArrayList<MySearchHomeData>();
    ArrayList<MySearchHomeData> sortedarrayList = new ArrayList<MySearchHomeData>();
    ArrayList<MySearchHomeData> temparrayList = new ArrayList<MySearchHomeData>();
    protected ArrayList<CharSequence> selectedsearchoptions = new ArrayList<CharSequence>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context=container.getContext();
        new SPHelper(context,getString(R.string.defaultPrefer));
        new SearchInterest_CONN().execute(SPHelper.getData(getString(R.string.keyUID)), SPHelper.getData(getString(R.string.keyINTRESTS)));
        myview=inflater.inflate(R.layout.searchinterestlayout,container,false);
        swipeRefreshLayout= (SwipeRefreshLayout) myview.findViewById(R.id.swipeRefreshLayoutinterest);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                refreshItems();
            }
        });
        swipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return myview;
    }
    void refreshItems() {
        new SearchInterest_CONN().execute(SPHelper.getData(getString(R.string.keyUID)), SPHelper.getData(getString(R.string.keyINTRESTS)));
        swipeRefreshLayout.setRefreshing(false);
    }
    protected void showSelectInterestsDialog() {

        boolean[] checkedInterest = new boolean[custsearchoptions.length];
        int count = custsearchoptions.length;


        for(int i = 0; i < count; i++)
            checkedInterest[i] = selectedsearchoptions.contains(custsearchoptions[i]);


        DialogInterface.OnMultiChoiceClickListener interestDialogListener = new DialogInterface.OnMultiChoiceClickListener() {
            @Override

            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked)

                    selectedsearchoptions.add(custsearchoptions[which]);

                else

                    selectedsearchoptions.remove(custsearchoptions[which]);


            }

        };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("SEARCH BY");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(selectedsearchoptions.contains(custsearchoptions[0]))
                {
                    titlecheck=true;
                }
                else {
                    titlecheck = false;
                }
                if(selectedsearchoptions.contains(custsearchoptions[1]))
                {
                    locationcheck=true;
                }
                else {
                    locationcheck=false;
                }
                if(selectedsearchoptions.contains(custsearchoptions[2]))
                {
                    expcheck=true;
                }
                else {
                    expcheck = false;
                }



                if(titlecheck==true || locationcheck==true || expcheck==true)
                {

                    searchView.setVisibility(View.VISIBLE);
                }
                else
                {
                    searchView.setQuery("",true);
                    searchView.setVisibility(View.INVISIBLE);
                }


                if(titlecheck==false && locationcheck==false && expcheck==true)
                {
                    searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                else
                {
                    searchView.setInputType(InputType.TYPE_CLASS_TEXT);
                }

            }
        });
        builder.setMultiChoiceItems(custsearchoptions, checkedInterest, interestDialogListener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void filter(String text) {
        temparrayList.clear();
        if(titlecheck==true && locationcheck==true && expcheck==true){
            if (text.isEmpty()) {
                temparrayList.addAll(sortedarrayList);
            } else {
                text = text.toLowerCase();
                for (MySearchHomeData item : sortedarrayList) {
                    if (item.getJobtitle().toLowerCase().contains(text) || item.getLocation().toLowerCase().contains(text)
                            || item.getExperience().toLowerCase().equals(text)) {
                        temparrayList.add(item);
                    }
                }
            }
        }
        else if(titlecheck==true && locationcheck==true)
        {
            if (text.isEmpty()) {
                temparrayList.addAll(sortedarrayList);
            } else {
                text = text.toLowerCase();
                for (MySearchHomeData item : sortedarrayList) {
                    if (item.getJobtitle().toLowerCase().contains(text) || item.getLocation().toLowerCase().contains(text)) {
                        temparrayList.add(item);
                    }
                }
            }

        }
        else if (titlecheck==true && expcheck==true)
        {
            if (text.isEmpty()) {
                temparrayList.addAll(sortedarrayList);
            } else {
                text = text.toLowerCase();
                for (MySearchHomeData item :sortedarrayList) {
                    if (item.getJobtitle().toLowerCase().contains(text) || item.getExperience().toLowerCase().equals(text)) {
                        temparrayList.add(item);
                    }
                }
            }

        }
        else if(locationcheck==true && expcheck==true)
        {
            if (text.isEmpty()) {
                temparrayList.addAll(sortedarrayList);
            } else {
                text = text.toLowerCase();
                for (MySearchHomeData item : sortedarrayList) {
                    if (item.getLocation().toLowerCase().contains(text) || item.getExperience().toLowerCase().equals(text)) {
                        temparrayList.add(item);
                    }
                }
            }

        }
        else if(titlecheck==true)
        {
            if (text.isEmpty()) {
                temparrayList.addAll(sortedarrayList);
            } else {
                text = text.toLowerCase();
                for (MySearchHomeData item : sortedarrayList) {
                    if (item.getJobtitle().toLowerCase().contains(text)) {
                        temparrayList.add(item);
                    }
                }
            }

        }
        else if(locationcheck==true)
        {
            if (text.isEmpty()) {
                temparrayList.addAll(sortedarrayList);
            } else {
                text = text.toLowerCase();
                for (MySearchHomeData item : sortedarrayList) {
                    if (item.getLocation().toLowerCase().contains(text)) {
                        temparrayList.add(item);
                    }
                }
            }

        }
        else if(expcheck==true)
        {
            if (text.isEmpty()) {
                temparrayList.addAll(sortedarrayList);
            } else {
                text = text.toLowerCase();
                for (MySearchHomeData item : sortedarrayList) {
                    if (item.getExperience().toLowerCase().contains(text)) {
                        temparrayList.add(item);
                    }
                }
            }

        }
        else
        {
            temparrayList.addAll(sortedarrayList);
        }
        createlist(temparrayList);
    }
    /*  public void filter(String text) {
          temparrayList.clear();
          if(text.isEmpty()){
              temparrayList.addAll(arrayList);
          } else{
              text = text.toLowerCase();
              for(MySearchHomeData item: arrayList){
                  if(item.getJobtitle().toLowerCase().contains(text)){
                      temparrayList.add(item);
                  }
              }
              adapter = new SearchHomeRecyclerAdapter(temparrayList, context);
              recyclerView.setHasFixedSize(true);
              layoutManager = new LinearLayoutManager(context);
              recyclerView.setLayoutManager(layoutManager);
              recyclerView.setAdapter(adapter);
          }
          adapter.notifyDataSetChanged();
      }*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.searchbar, menu);
        SearchManager searchManager =
                (SearchManager)context.getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint("Keyword To Search");
        searchView.setVisibility(View.INVISIBLE);
        super.onCreateOptionsMenu(menu,inflater);
    }

    public void createlist(ArrayList<MySearchHomeData> arr)
    {
        adapter = new SearchHomeRecyclerAdapter(arr, context);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.filtersearch:
                showSelectInterestsDialog();
                return true;
            case R.id.sortlowtohigh:
                item.setChecked(true);
                if( jobid!=null && jobdesc!=null && jobtitle!=null && jobsal!=null && jobdate!=null  && joblocation!=null && jobexperience!=null) {
                    searchView.setQuery("", true);
                    Collections.sort(sortedarrayList, bySalary());
                    createlist(sortedarrayList);
                }
                else
                {
                    Snackbar.make(getView(), "Sorry, no jobs present currently for you.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                return  true;
            case R.id.sorthightolow:
                item.setChecked(true);
                if( jobid!=null && jobdesc!=null && jobtitle!=null && jobsal!=null && jobdate!=null  && joblocation!=null && jobexperience!=null) {
                    searchView.setQuery("", true);
                    Collections.sort(sortedarrayList, Collections.reverseOrder(bySalary()));
                    createlist(sortedarrayList);
                }
                else
                {
                    Snackbar.make(getView(), "Sorry, no jobs present currently for you.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                return  true;
            case R.id.sortbyrecent:
                item.setChecked(true);
                if( jobid!=null && jobdesc!=null && jobtitle!=null && jobsal!=null && jobdate!=null  && joblocation!=null && jobexperience!=null) {
                    searchView.setQuery("", true);
                    sortedarrayList = (ArrayList<MySearchHomeData>) arrayList.clone();
                    createlist(sortedarrayList);
                }
                else
                {
                    Snackbar.make(getView(), "Sorry, no jobs present currently for you.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }
                return  true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static Comparator<MySearchHomeData> bySalary()
    {
        return new Comparator<MySearchHomeData>()
        {
            @Override
            public int compare(MySearchHomeData o1, MySearchHomeData o2)
            {
                return Integer.valueOf(o1.getSalary().substring(o1.getSalary().indexOf("-") + 2))
                        - Integer.valueOf(o2.getSalary().substring(o2.getSalary().indexOf("-") + 2));
            }
        };
    }


    @Override
    public boolean onMenuItemActionExpand(MenuItem item) {
        return false;
    }

    @Override
    public boolean onMenuItemActionCollapse(MenuItem item) {
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        if( jobid!=null && jobdesc!=null && jobtitle!=null && jobsal!=null && jobdate!=null  && joblocation!=null && jobexperience!=null) {
            filter(query.trim());
            return true;
        }
        else
        {
            Snackbar.make(getView(), "Sorry, no jobs present currently for you.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return false;
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if( jobid!=null && jobdesc!=null && jobtitle!=null && jobsal!=null && jobdate!=null  && joblocation!=null && jobexperience!=null) {
            filter(newText.trim());
            return true;
        }
        else
        {
            Snackbar.make(getView(), "Sorry, no jobs present currently for you.", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            return false;
        }
    }


    public class SearchInterest_CONN extends AsyncTask<String,Void,String> {

        String userid,userIntrstStr,server;

        @Override
        protected String doInBackground(String... params) {
            userid=params[0];
            userIntrstStr=params[1];
            server = context.getString(R.string.server_url);
            String data, link, result;
            BufferedReader bufferedReader;
            try
            {
              /*  data = "?userid="+ URLEncoder.encode(userid,"UTF-8");
                data += "&userinterests="+ URLEncoder.encode(userIntrstStr,"UTF-8");
                link = server+"/searchinterest.php"+data;

                URL url = new URL(link);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                result = bufferedReader.readLine();
                return result;      */

                data = URLEncoder.encode("userid", "UTF-8") + "=" + URLEncoder.encode(userid, "UTF-8");
                data += "&" + URLEncoder.encode("userinterests", "UTF-8") + "=" + URLEncoder.encode(userIntrstStr, "UTF-8");
                link = server+"/searchinterest.php";

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
            String jSonres = res;
            JSONObject jsonObject;
            if (jSonres != null) {
                try {
                    jsonObject = new JSONObject(jSonres);
                    JSONArray jsonArray = (JSONArray) jsonObject.get("data");
                    //  JSONObject jsonObjecttemp=new JSONObject(jSonres);
                    //  String queryresult=jsonObjecttemp.getString("query_result");
                    if(!(jsonArray.getJSONObject(0).getString("query_result").equalsIgnoreCase("NO_JOBS_PRESENT")))
                    {
                        jobid = new String[jsonArray.length()];
                        jobtitle = new String[jsonArray.length()];
                        jobsal = new String[jsonArray.length()];
                        jobdate = new String[jsonArray.length()];
                        joblocation = new String[jsonArray.length()];
                        jobdesc = new String[jsonArray.length()];
                        jobexperience = new String[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            jobid[i] = jsonArray.getJSONObject(i).getString("jobid");
                            jobtitle[i] = jsonArray.getJSONObject(i).getString("jobtitle");
                            jobsal[i] = jsonArray.getJSONObject(i).getString("jobsalary");
                            jobdate[i] = jsonArray.getJSONObject(i).getString("jobdate");
                            joblocation[i] = jsonArray.getJSONObject(i).getString("joblocation");
                            jobdesc[i] = jsonArray.getJSONObject(i).getString("jobdescription");
                            jobexperience[i] = jsonArray.getJSONObject(i).getString("jobexperience");
                        }
                        arrayList.clear();
                        recyclerView=(RecyclerView)myview.findViewById(R.id.searchinterestlist);
                        if( jobid!=null && jobdesc!=null && jobtitle!=null && jobsal!=null && jobdate!=null  && joblocation!=null && jobexperience!=null)
                        {
                            int i = 0;
                            for (String data : jobtitle) {
                                MySearchHomeData mySearchHomeData = new MySearchHomeData(data, jobsal[i], jobdate[i], joblocation[i],jobexperience[i],jobid[i],jobdesc[i]);
                                arrayList.add(mySearchHomeData);
                                i++;
                            }
                            adapter = new SearchHomeRecyclerAdapter(arrayList, context);
                            recyclerView.setHasFixedSize(true);
                            layoutManager = new LinearLayoutManager(context);
                            recyclerView.setLayoutManager(layoutManager);
                            recyclerView.setAdapter(adapter);
                            sortedarrayList= (ArrayList<MySearchHomeData>) arrayList.clone();
                        }
                    }
                    else
                    {
                        // Toast.makeText(context,"No Jobs Present Currently For You...!!",Toast.LENGTH_LONG).show();
                        Snackbar.make(getView(), "Sorry, No Jobs Matching To Your Interests...!!", Snackbar.LENGTH_LONG).setActionTextColor(Color.rgb(255,193,7)).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    //   Toast.makeText(context, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                    Toast.makeText(context,"Could Not Connect To Server...!!",Toast.LENGTH_LONG).show();
                }
            } else {
                // Toast.makeText(context,"Could not get JSON data",Toast.LENGTH_LONG).show();
                Toast.makeText(context,"Could Not Connect To Server...!!",Toast.LENGTH_LONG).show();
            }
        }
    }
}
