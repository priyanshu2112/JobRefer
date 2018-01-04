package com.example.daivansh.jobreferer.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daivansh.jobreferer.R;
import com.example.daivansh.jobreferer.helper.SPHelper;
import com.example.daivansh.jobreferer.connections.AddPostConnection;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddPostActivity extends AppCompatActivity implements NumberPicker.OnValueChangeListener{
    Toolbar toolbar;
    Spinner interestspinner;
    EditText ettitle,etdesc,etlocation,etinitsal,etmaxsal;
    TextView tvexperience,tvintrst,etdate;
    ImageView imgdate;
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog datePickerDialog;
    String strtitle,strusrid,strdesc,strloc,strinitsalary,strmaxsalary,strtotalsal,strexp,strintrst,strexpirydt,strcurdate;
   //String interesttypes[]={ "Mobile App Developement", "UI Designing", "Network Admin", "Security", "Web Development", "Database" };
   String interesttypes[];
    int totalinterests;
    private int day,month,year;
//    Button btnaddpost;
    protected ArrayList<CharSequence> selectedinterest = new ArrayList<CharSequence>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        overridePendingTransition(R.anim.animation_add_post, R.anim.to);
        toolbar=(Toolbar)findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("ADD POST");
        actionBar.setHomeAsUpIndicator(R.drawable.ic_action_cross);
        new SPHelper(this,getString(R.string.skillsPrefer));

        totalinterests= Integer.valueOf(SPHelper.getData("row_count"));
        interesttypes=new String[totalinterests+1];
        interesttypes[0]="Select Interests";
        for(int i=1;i<=totalinterests;i++)
        {
            interesttypes[i]= SPHelper.getData(""+(i-1));
        }
        dateFormatter=new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        interestspinner=(Spinner)findViewById(R.id.spinnerinterest);
        ArrayAdapter<CharSequence> adapter=new  ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, interesttypes);
        interestspinner.setAdapter(adapter);
        imgdate=(ImageView)findViewById(R.id.imageViewdate);
        ettitle=(EditText)findViewById(R.id.editTexttitle);
        etdesc=(EditText)findViewById(R.id.editTextdesc);
        etlocation=(EditText)findViewById(R.id.editTextlocation);
        tvexperience=(TextView) findViewById(R.id.textViewexp);
        etdate=(TextView) findViewById(R.id.textViewdate);
        etinitsal=(EditText)findViewById(R.id.editTextinitsal);
        etmaxsal=(EditText)findViewById(R.id.editTextmaxsal);
        tvexperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shownumberpicker();
            }
        });
//        btnaddpost=(Button)findViewById(R.id.buttonaddpost);
//        btnaddpost.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v){
//                addpost();
//            }
//        });
        imgdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datepick();
            }
        });
        interestspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position==0)
                {
                    strintrst="";
                }
                else
                {
                    strintrst=(position-1)+"";
                }
             //   Toast.makeText(AddPostActivity.this,strintrst,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viewprofile_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save_changes) {
            addpost();
        }
        return super.onOptionsItemSelected(item);
    }
    public void shownumberpicker()
    {

        final Dialog d = new Dialog(AddPostActivity.this);
        d.setTitle("Experience");
        d.setContentView(R.layout.numberpickerdialog);
        ImageView b1 = (ImageView) d.findViewById(R.id.Imgbutton1);
        Button b2 = (Button) d.findViewById(R.id.button1);
        final NumberPicker np = (NumberPicker) d.findViewById(R.id.numberPicker1);
        np.setMaxValue(100);
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
    @Override
    public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

    }
    public void datepick()
    {

        Calendar calendar= Calendar.getInstance();
        datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
              /*  if(month>=0 && month<=8)
                    etdate.setText(dayOfMonth+"/0"+(month+1)+"/"+year);
                else
                    etdate.setText(dayOfMonth+"/"+(month+1)+"/"+year);          */
                Calendar newDate=Calendar.getInstance();
                newDate.set(year,month,dayOfMonth);
                etdate.setText(dateFormatter.format(newDate.getTime()));

            }
        }  , calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    public  void addpost()  {
        new SPHelper(this,getString(R.string.defaultPrefer));
        strusrid= SPHelper.getData(getString(R.string.keyUID));
        strcurdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        strexpirydt=etdate.getText().toString();
        Date chkexpdate=new Date();
        try {
            chkexpdate = dateFormatter.parse(strexpirydt);
        }catch (ParseException p){}
        strtitle=ettitle.getText().toString();
        strdesc=etdesc.getText().toString();
        strloc=etlocation.getText().toString();
        strinitsalary=etinitsal.getText().toString();
        strmaxsalary=etmaxsal.getText().toString();
        strexp=tvexperience.getText().toString();
        strtotalsal=strinitsalary+","+strmaxsalary;
        if(!(strtitle.equals(""))&&!(strdesc.equals(""))&&!(strloc.equals(""))&&!(strinitsalary.equals(""))&&!(strmaxsalary.equals(""))
                &&!(strexp.equals(""))&&!(strintrst.equals(""))&&!(strcurdate.equals(""))&&!(strexpirydt.equals(""))&&!(strusrid.equals("")))
        {
            if(chkexpdate.after(new Date()))
            {
                if (Double.valueOf(strinitsalary) < Double.valueOf(strmaxsalary)) {
                    new AddPostConnection(AddPostActivity.this).execute(strtitle, strdesc, strloc, strtotalsal, strexp, strintrst, strcurdate, strexpirydt, strusrid);
                } else {
                    Toast.makeText(this, "Initial Salary Cannot Be Greater Than Max Salary...!!", Toast.LENGTH_LONG).show();
                }
            }
            else
            {
             Toast.makeText(this, "Expiry Date Must Be After Current Date...!!", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(this,"Please Fill All Feilds",Toast.LENGTH_LONG).show();
        }
    }
}
