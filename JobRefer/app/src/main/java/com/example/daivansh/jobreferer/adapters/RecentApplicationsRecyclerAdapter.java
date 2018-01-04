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
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.daivansh.jobreferer.R;
import com.example.daivansh.jobreferer.activities.JobDescriptionActivity;
import com.example.daivansh.jobreferer.activities.RecentApplicationDescriptionActivity;
import com.example.daivansh.jobreferer.pojos.MyRecentApplicationsData;

import java.util.ArrayList;

/**
 * Created by daivansh on 09-06-2017.
 */

public class RecentApplicationsRecyclerAdapter extends RecyclerView.Adapter<RecentApplicationsRecyclerAdapter.RecentApplicationsViewHolder> {
    ArrayList<MyRecentApplicationsData> arrayList = new ArrayList<MyRecentApplicationsData>();
    String description[],experience[];
    Context context;
    public RecentApplicationsRecyclerAdapter(ArrayList<MyRecentApplicationsData> arrayList, String desc[], String exp[], Context con)
    {
        this.arrayList = arrayList;
        description=desc;
        experience=exp;
        context=con;
    }
    @Override
    public RecentApplicationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recentapplicationlistitem,parent,false);
        RecentApplicationsViewHolder myViewHolder = new RecentApplicationsViewHolder(view,context,arrayList);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecentApplicationsViewHolder holder, int position) {
        MyRecentApplicationsData myRecentApplicationsData =  arrayList.get(position);
        holder.jobtitle.setText(myRecentApplicationsData.getJobtitle());
        holder.salary.setText(myRecentApplicationsData.getSalary());
        holder.date.setText(myRecentApplicationsData.getDate());
        holder.location.setText(myRecentApplicationsData.getLocation());
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class RecentApplicationsViewHolder extends RecyclerView.ViewHolder
    {
        TextView jobtitle,salary,date,location;
        CardView cardView;
        //Button btn;
        ArrayList<MyRecentApplicationsData> list = new ArrayList<MyRecentApplicationsData>();
        Context cobject;
        public RecentApplicationsViewHolder(View itemView, Context cob, ArrayList<MyRecentApplicationsData> lob) {
            super(itemView);
            cobject=cob;
            list=lob;

            jobtitle = (TextView) itemView.findViewById(R.id.textViewapplicationjobtitle);
            salary = (TextView) itemView.findViewById(R.id.textViewapplicationjobsalary);
            date = (TextView) itemView.findViewById(R.id.textViewapplicationjobdate);
            location = (TextView) itemView.findViewById(R.id.textViewapplicationjoblocation);
            cardView = (CardView) itemView.findViewById(R.id.card_job);
            Animation animation = AnimationUtils.loadAnimation(cob,R.anim.animation_slide);
            cardView.setAnimation(animation);
            //btn = (Button) itemView.findViewById(R.id.buttonApplyNow);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    MyRecentApplicationsData data = list.get(position);
                    Intent intent = new Intent(cobject, JobDescriptionActivity.class);
                    intent.putExtra("job_title",data.getJobtitle());
                    intent.putExtra("job_salary",data.getSalary());
                    intent.putExtra("job_date",data.getDate());
                    intent.putExtra("job_location",data.getLocation());
                    intent.putExtra("job_description",data.getJdescription());
                    intent.putExtra("job_experience",data.getExperience());
                    intent.putExtra("intent_from","recent_applications");

                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    //btn.setEnabled(false);
                    cobject.startActivity(intent);
                }
            });
        }
    }
}
