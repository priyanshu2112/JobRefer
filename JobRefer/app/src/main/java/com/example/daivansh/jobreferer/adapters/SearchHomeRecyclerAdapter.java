package com.example.daivansh.jobreferer.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daivansh.jobreferer.R;
import com.example.daivansh.jobreferer.activities.JobDescriptionActivity;
import com.example.daivansh.jobreferer.helper.TypefaceUtil;
import com.example.daivansh.jobreferer.pojos.MySearchHomeData;

import java.util.ArrayList;

/**
 * Created by daivansh on 09-06-2017.
 */

public class SearchHomeRecyclerAdapter extends RecyclerView.Adapter<SearchHomeRecyclerAdapter.SearchHomeViewHolder> {
    ArrayList<MySearchHomeData> arrayList = new ArrayList<MySearchHomeData>();
   // String description[],experience[],jobid[];
    Context context;
    public SearchHomeRecyclerAdapter(ArrayList<MySearchHomeData> arrayList,Context con)
    {
        this.arrayList = arrayList;
    //    description=desc;
      //  experience=exp;
       context=con;
        //TypefaceUtil.overrideFont(context, "SANS-SERIF", "fonts/Montserrat-Regular.ttf");
      //  jobid=jid;
    }
    @Override
    public SearchHomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recentapplicationlistitem,parent,false);
        SearchHomeViewHolder myViewHolder = new SearchHomeViewHolder(view,context,arrayList);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(SearchHomeViewHolder holder, int position) {
        MySearchHomeData mySearchHomeData =  arrayList.get(position);
        holder.jobtitle.setText(mySearchHomeData.getJobtitle());
        holder.salary.setText("₹ "+mySearchHomeData.getSalary());
        holder.date.setText(mySearchHomeData.getDate());
        holder.location.setText(mySearchHomeData.getLocation());
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }
    public static class SearchHomeViewHolder extends RecyclerView.ViewHolder
    {
        TextView jobtitle,salary,date,location;
        ArrayList<MySearchHomeData> list = new ArrayList<MySearchHomeData>();
        Context cobject;
        CardView cardView;
        RelativeLayout relativeLayout;

        public SearchHomeViewHolder(final View itemView, final Context cob, ArrayList<MySearchHomeData> lob) {
            super(itemView);
            cobject=cob;
            list=lob;
            jobtitle = (TextView) itemView.findViewById(R.id.textViewapplicationjobtitle);
            salary = (TextView) itemView.findViewById(R.id.textViewapplicationjobsalary);
            date = (TextView) itemView.findViewById(R.id.textViewapplicationjobdate);
            location = (TextView) itemView.findViewById(R.id.textViewapplicationjoblocation);
            cardView = (CardView) itemView.findViewById(R.id.card_job);
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relative_job);
            Animation animation = AnimationUtils.loadAnimation(cob,R.anim.animation_slide);
            cardView.setAnimation(animation);
            //Animation animation1 = AnimationUtils.loadAnimation(cob,R.anim.animation);
            //relativeLayout.setAnimation(animation1);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    MySearchHomeData data = list.get(position);


                    Intent intent = new Intent(cobject,JobDescriptionActivity.class);
                    intent.putExtra("job_title",data.getJobtitle());
                    intent.putExtra("job_salary",data.getSalary());
                    intent.putExtra("job_date",data.getDate());
                    intent.putExtra("job_location",data.getLocation());
                    intent.putExtra("job_description",data.getJdescription());
                    intent.putExtra("job_experience",data.getExperience());
                    intent.putExtra("job_id",data.getJobid());
                    intent.putExtra("intent_from","home");

                    cobject.startActivity(intent);
                    //cobject.overridePendingTransition(R.anim.from, R.anim.to);
                }
            });
        }
    }
}
