package com.example.daivansh.jobreferer.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daivansh.jobreferer.R;
import com.example.daivansh.jobreferer.helper.SPHelper;
import com.example.daivansh.jobreferer.connections.UpdateProfileConnection;

import java.util.ArrayList;

public class ViewProfileActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener{
    Toolbar toolbar;
    Button changepwd;
    //Button btnsavechanges;
    String temp,selectedposition = "";
    String userIntrstStr;

    int num_of_excisting_skills;
    int userExistingInterestsIntAry[];
    String uid,fnm,lnm,mail,num,gender,expr,cloc;
    int totalinterests;
    EditText etfname,etlname,etlocation,etcontactno;
    ImageView interestimgbutton;
    TextView tvinterestdisplay,tvexperience;
    //protected String[] Allinterests = { "Mobile App Developement", "UI Designing", "Network Admin", "Security", "Web Development", "Database" };
    protected  String[] Allinterests;
    protected ArrayList<CharSequence> selectedinterest = new ArrayList<CharSequence>();

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viewprofile_menu,menu);
        return true;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_view_profile);


            /*
            Getting data from Shared Preferences
             */


            new SPHelper(getBaseContext(),getString(R.string.defaultPrefer));

            uid = SPHelper.getData(getString(R.string.keyUID));
            fnm = SPHelper.getData(getString(R.string.keyFIRSTNAME));
            lnm = SPHelper.getData(getString(R.string.keyLASTNAME));
            mail = SPHelper.getData(getString(R.string.keyEMAIL));
            num = SPHelper.getData(getString(R.string.keyPHONENO));
            gender = SPHelper.getData(getString(R.string.keyGENDER));
            expr = SPHelper.getData(getString(R.string.keyEXPERIENCE));if(expr.equals("")){expr = "0";}
            cloc = SPHelper.getData(getString(R.string.keyLOCATION));
            userIntrstStr = SPHelper.getData(getString(R.string.keyINTRESTS));

            /*
            Setting title bar
             */

            toolbar = (Toolbar) findViewById(R.id.toolbar7);
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Your Profile");


            interestimgbutton = (ImageView) findViewById(R.id.imageViewinterest);

            tvinterestdisplay = (TextView) findViewById(R.id.textViewinterestdisplay);
            changepwd = (Button) findViewById(R.id.buttonchangepwd);
            //btnsavechanges = (Button) findViewById(R.id.buttonsavechanges);
            tvexperience = (TextView) findViewById(R.id.textViewexperience);
            etfname = (EditText) findViewById(R.id.editTextfnm);
            etlname = (EditText) findViewById(R.id.editTextlnm);
            etlocation = (EditText) findViewById(R.id.editTextloc);
            etcontactno = (EditText) findViewById(R.id.editTextcntct);

            /*
            To retrieve existing interests into the integer readable
            */

            new SPHelper(this,getString(R.string.skillsPrefer));
            totalinterests= Integer.valueOf(SPHelper.getData("row_count"));
            //  Allinterests[0]="Select Interests";


                    /*
                        retrieving all skills(Values) into a String array
                    */


            Allinterests =new String[totalinterests];
            for(int i=0;i<totalinterests;i++)
            {
                Allinterests[i]= SPHelper.getData(i+"");
            }

                    /*
                        counting userSkillsStr length and then counting number of commas into that string
                        and then saving them as an individual element in Integer array

                        >>  Integer array  :  userExistingInterestsIntAry

                    */



            int usrInrStrSize = userIntrstStr.length();
            num_of_excisting_skills=0; //comma counts
            for(int i=0;i<usrInrStrSize;i++)
            {
                if(userIntrstStr.charAt(i) == ',')
                {
                    num_of_excisting_skills++;
                }
            }


            num_of_excisting_skills += 1; //user skills or interests count
          //  Toast.makeText(this,num_of_excisting_skills+"",Toast.LENGTH_SHORT).show();
          //  Toast.makeText(this,"user:"+userIntrstStr,Toast.LENGTH_SHORT).show();
            selectedposition =userIntrstStr;
            int m=0;
            userExistingInterestsIntAry = new int[num_of_excisting_skills];
            String tempIntereset = new String();
            for(int i=0;i<usrInrStrSize;i++)
            {
                tempIntereset="";
                while( (i < usrInrStrSize) && !(userIntrstStr.charAt(i) == ','))
                {
                    tempIntereset += userIntrstStr.charAt(i);
                    ++i;
                }
                userExistingInterestsIntAry[m]=Integer.valueOf(tempIntereset);
                ++m;
            }


            int count = Allinterests.length;
            for(int i=0;i<count;i++)
            {
                for(int j=0;j<num_of_excisting_skills;j++)
                {
                    if(i == userExistingInterestsIntAry[j])
                        selectedinterest.add(Allinterests[i]);
                }
                        /*if(Arrays.asList(userExistingInterestsIntAry).contains(i))
                            Toast.makeText(this,userExistingInterestsIntAry[i]+"",Toast.LENGTH_SHORT).show();
                           */

            }
            StringBuilder stringBuilder = new StringBuilder();
            for(CharSequence interest : selectedinterest)
                if(stringBuilder.toString()=="") {
                    stringBuilder.append(interest);
                }else
                {
                    stringBuilder.append(", " + interest);
                }







            /*
            Setting data into the fields
             */

            etfname.setText(fnm);
            etlname.setText(lnm);
            etcontactno.setText(num);
            tvinterestdisplay.setText(stringBuilder.toString());
            etlocation.setText(cloc);




            // Event listeners
            interestimgbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showSelectInterestsDialog();
                }
            });
            changepwd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(),ChangePasswordActivity.class);
                    startActivity(intent);

                }
            });
            tvexperience.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    shownumberpicker();
                }
            });


//            btnsavechanges.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {}
//            });

        }
        catch (Exception ex)
        {
           // Toast.makeText(ViewProfileActivity.this,"error : "+ex.getMessage(),Toast.LENGTH_LONG).show();
            Toast.makeText(ViewProfileActivity.this, "Could Not Connected To Server...!!", Toast.LENGTH_LONG).show();
        }
    }

    public void shownumberpicker()
    {

        final Dialog d = new Dialog(ViewProfileActivity.this);
        d.setTitle("Experience");
        d.setContentView(R.layout.numberpickerdialog);
        ImageView b1 = (ImageView) d.findViewById(R.id.Imgbutton1);
        Button b2 = (Button) d.findViewById(R.id.button1);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(100);
        np.setValue(Integer.valueOf(expr));
        np.setMinValue(0);
        np.setValue(Integer.parseInt(tvexperience.getText().toString()));
        np.setWrapSelectorWheel(false);
        np.setOnValueChangedListener(this);
        b1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                tvexperience.setText(String.valueOf(np.getValue()));
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }
    protected void showSelectInterestsDialog() {

        boolean[] checkedInterest = new boolean[Allinterests.length];
        int count = Allinterests.length;


        for(int i = 0; i < count; i++)
            checkedInterest[i] = selectedinterest.contains(Allinterests[i]);


        DialogInterface.OnMultiChoiceClickListener interestDialogListener = new DialogInterface.OnMultiChoiceClickListener() {
            @Override

            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                if(isChecked)

                    selectedinterest.add(Allinterests[which]);

                else

                    selectedinterest.remove(Allinterests[which]);

                onChangeSelectedInterests();

            }

        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select Interests");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onChangeSelectedInterests();
            }
        });
        builder.setMultiChoiceItems(Allinterests, checkedInterest, interestDialogListener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    protected void onChangeSelectedInterests() {
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        for(CharSequence interest : selectedinterest)
            if(stringBuilder.toString()=="") {
                stringBuilder.append( interest);
            }else
            {
                stringBuilder.append(", " + interest);
            }
        tvinterestdisplay.setText(stringBuilder.toString());
        selectedposition="";
        while (i<selectedinterest.size()) {
            temp = (String) selectedinterest.get(i);
            for (int j = 0; j < Allinterests.length; j++) {
                if (Allinterests[j] == temp) {
                    if (selectedposition.equals(""))
                        selectedposition = selectedposition +(j + 1);
                    else
                        selectedposition =selectedposition+ "," + (j + 1);
                }
            }
            i++;
        }
        //Toast.makeText(ViewProfileActivity.this, selectedposition, Toast.LENGTH_LONG).show();

    }

    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.save_changes)
        {


            View view= (View) findViewById(R.id.save_changes);

            fnm = etfname.getText().toString();
            lnm = etlname.getText().toString();
            num = etcontactno.getText().toString();
            cloc = etlocation.getText().toString();
            expr = tvexperience.getText().toString();





//            if(!(fnm.equals(SPHelper.getData(getString(R.string.keyFIRSTNAME))))
//                    && !(lnm.equalsIgnoreCase(SPHelper.getData(getString(R.string.keyLASTNAME))))
//                    && !(num.equalsIgnoreCase(SPHelper.getData(getString(R.string.keyPHONENO))))
//                    && !(expr.equalsIgnoreCase(SPHelper.getData(getString(R.string.keyEXPERIENCE))))
//                    && !(selectedposition.equalsIgnoreCase(SPHelper.getData(getString(R.string.keyINTRESTS))))
//                    && !(cloc.equalsIgnoreCase(SPHelper.getData(getString(R.string.keyLOCATION))))       )
//            {
                if (!(fnm.equals("")) && !(lnm.equals("")) && !(num.equals(""))  && !(selectedposition.equals("")))
                {
                    if (num.length() == 10)
                    {
                        item.setIcon(getResources().getDrawable(R.drawable.loading));
                        new UpdateProfileConnection(ViewProfileActivity.this).execute(uid, fnm, lnm, selectedposition, num, cloc, expr);
                    }
                    else
                    {
                        Toast.makeText(getBaseContext(), "Please Enter Correct Phone Number", Toast.LENGTH_LONG).show();

                    }
                }
                else
                {
                    if(selectedposition.equals(""))
                    {
                        Toast.makeText(getBaseContext(), "You Have To Select At Least One Interest ", Toast.LENGTH_LONG).show();
                    }
                    else
                        Toast.makeText(getBaseContext(), "Please Fill Out All Basic Things..!", Toast.LENGTH_LONG).show();
                }
//           }
//            else
//            {
//                Toast.makeText(getBaseContext(), "there is no change", Toast.LENGTH_LONG).show();
//            }


        }
        return super.onOptionsItemSelected(item);
    }
}

