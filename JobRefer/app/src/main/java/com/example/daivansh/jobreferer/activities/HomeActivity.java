package com.example.daivansh.jobreferer.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daivansh.jobreferer.R;
import com.example.daivansh.jobreferer.helper.SPHelper;
import com.example.daivansh.jobreferer.fragments.SearchinterestFragment;
import com.example.daivansh.jobreferer.fragments.SearchrecentFragment;
import com.example.daivansh.jobreferer.connections.UpdateSkillsConnection;

import java.util.ArrayList;



public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {




    FragmentManager fragmentManager;
    String usernm,email,uid;
    TextView tv_username,tv_email;
    int row_count;
    //RecentPosts_CONNECTION recentPosts_connection;
    String temp,selectedposition = "";
    String[] jid,jtitle,jsal,jdate,jlocation;
    String userskills,userid;
    Context context=HomeActivity.this;

    String interests[];
    //= { "Mobile App Developement", "UI Designing", "Network Admin", "Security", "Web Development", "Database" };
    protected ArrayList<CharSequence> selectedinterest = new ArrayList<CharSequence>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //new GetSkillsConnection(getApplicationContext()).execute();
        //TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/Montserrat-Regular.ttf");
try {
    new SPHelper(context, getString(R.string.defaultPrefer));
    userid = SPHelper.getData(getString(R.string.keyUID));
    //recentPosts_connection = new RecentPosts_CONNECTION(this);

    //recentPosts_connection.execute(userid);
    fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction().replace(R.id.content_home, new SearchrecentFragment()).commit();
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar222);
    setSupportActionBar(toolbar);
    getSupportActionBar().setTitle("Recent");

    final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

    fab.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            /*
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                        */
            Intent intent = new Intent(HomeActivity.this, AddPostActivity.class);
            startActivity(intent);
        }
    });

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
    drawer.setDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
    navigationView.setNavigationItemSelectedListener(this);
    View hView = navigationView.getHeaderView(0);
    new SPHelper(getApplicationContext(), getString(R.string.defaultPrefer));
    usernm = SPHelper.getData(getString(R.string.keyFIRSTNAME));
    usernm += " ";
    usernm += SPHelper.getData(getString(R.string.keyLASTNAME));
    email = SPHelper.getData(getString(R.string.keyEMAIL));
    userskills = SPHelper.getData(getString(R.string.keyINTRESTS));
    uid = SPHelper.getData(getString(R.string.keyUID));
    tv_username = (TextView) hView.findViewById(R.id.app_drawer_username);
    tv_email = (TextView) hView.findViewById(R.id.app_drawer_email);
    tv_username.setText(usernm);
    tv_email.setText(email);
    retrieveSkills();
    if (userskills.equals(""))
        showSelectInterestsDialog();
    overridePendingTransition(R.anim.from, R.anim.to);
}
catch (Exception ex)
{
    Toast.makeText(HomeActivity.this, "Could Not Connected To Server...!!", Toast.LENGTH_LONG).show();
  //  Toast.makeText(HomeActivity.this, ""+ex.getMessage(), Toast.LENGTH_LONG).show();
}

    }
    private void retrieveSkills()
    {
        new SPHelper(getApplicationContext(),getString(R.string.skillsPrefer));
        row_count = Integer.parseInt(SPHelper.getData("row_count"));
        //Toast.makeText(getApplicationContext(),"Row count(HOME) : "+row_count,Toast.LENGTH_SHORT).show();
        interests = new String[row_count];
        for(int cnt = 0;cnt < row_count;cnt++)
        {
            interests[cnt] = SPHelper.getData(cnt+"");
        }
    }
    protected void showSelectInterestsDialog() {

        boolean[] checkedInterest = new boolean[interests.length];

        int count = interests.length;

        for(int i = 0; i < count; i++)

            checkedInterest[i] = selectedinterest.contains(interests[i]);

        DialogInterface.OnMultiChoiceClickListener interestDialogListener = new DialogInterface.OnMultiChoiceClickListener() {

            @Override

            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                if(isChecked)
                    selectedinterest.add(interests[which]);
                else
                    selectedinterest.remove(interests[which]);


                    if (!(onChangeSelectedInterests().equals(""))) {
                        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE)
                                .setEnabled(true);
                    } else {
                        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE)
                                .setEnabled(false);
                    }

                //onChangeSelectedInterests();


            }

        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Interests");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String selectedskills = onChangeSelectedInterests();
                SPHelper.putData(getString(R.string.keyINTRESTS),selectedskills);
                    new UpdateSkillsConnection(getBaseContext()).execute(uid, selectedskills);
            }
        });

        builder.setMultiChoiceItems(interests, checkedInterest, interestDialogListener);
        AlertDialog dialog = builder.create();

        dialog.show();
        ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_POSITIVE)
                .setEnabled(false);

    }
    protected String onChangeSelectedInterests() {
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        for(CharSequence interest : selectedinterest)
            if(stringBuilder.toString()=="") {
                stringBuilder.append( interest);
            }else
            {
                stringBuilder.append("," + interest);
            }
        //tvinterestdisplay.setText(stringBuilder.toString());
        selectedposition="";
        while (i<selectedinterest.size()) {
            temp = (String) selectedinterest.get(i);
            for (int j = 0; j < interests.length; j++) {
                if (interests[j] == temp) {
                    if (selectedposition.equals(""))
                        selectedposition = selectedposition +(j);
                    else
                        selectedposition =selectedposition+ "," + (j);
                }
            }
            i++;
        }
        //Toast.makeText(getBaseContext(), selectedposition, Toast.LENGTH_LONG).show();
        return selectedposition;

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            moveTaskToBack(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.home,menu);
        return true;
    }

  /*  @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the HomeActivity/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.app_bar_search) {
            Intent intent=new Intent(HomeActivity.this,LoginActivity.class);
            startActivity(intent);
            //return true;
        }

        return super.onOptionsItemSelected(item);
    }       */

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.searchinterest) {
            fragmentManager.beginTransaction().replace(R.id.content_home,new SearchinterestFragment()).commit();
            getSupportActionBar().setTitle("Intrests");
        } else if (id == R.id.searchrecent) {
            fragmentManager.beginTransaction().replace(R.id.content_home,new SearchrecentFragment()).commit();
            getSupportActionBar().setTitle("Recent");
        } else if (id == R.id.profile) {
            Intent i = new Intent(getApplicationContext(),ViewProfileActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.from, R.anim.to);

        } else if (id == R.id.post) {

            //Toast.makeText(context,jid[0],Toast.LENGTH_LONG).show();
            Intent intent=new Intent(HomeActivity.this,RecentPostsActivity.class);
            startActivity(intent);

        } else if (id == R.id.application) {
            Intent intent=new Intent(HomeActivity.this,RecentApplicationsActivity.class);
            startActivity(intent);

        } else if (id == R.id.logout) {
            logoutDialog();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void logoutDialog()
    {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Confirm logout...");
        alertDialog.setMessage(usernm+" are you sure you want to logout?");
        alertDialog.setIcon(R.drawable.logo);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SPHelper.deletePrefernce(getString(R.string.defaultPrefer));
                Intent intent = new Intent(getApplicationContext(),SplashActivity.class);
                startActivity(intent);
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }


   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        SPHelper.deletePrefernce(getString(R.string.skillsPrefer));
    }
*/

}
