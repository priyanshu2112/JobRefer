package com.example.daivansh.jobreferer.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.example.daivansh.jobreferer.R;
import com.example.daivansh.jobreferer.activities.SeeApplicantsActivity;
import com.example.daivansh.jobreferer.connections.DeletePostConnection;
import com.example.daivansh.jobreferer.pojos.MySearchHomeData;

import java.util.ArrayList;

/**
 * Created by daivansh on 09-06-2017.
 */

public class RecentPostsRecyclerAdapter extends RecyclerView.Adapter<RecentPostsRecyclerAdapter.RecentPostsViewHolder> {
    ArrayList<MySearchHomeData> arrayList = new ArrayList<MySearchHomeData>();
    static String[] jobtitle,jobsal,jobdate,joblocation;
    String jobid[];
    Context context;
    public RecentPostsRecyclerAdapter(Context con,String jid[],ArrayList<MySearchHomeData> list)
    {
        context=con;
        jobid=jid;
        arrayList=list;
    }
    @Override
    public RecentPostsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recentpostlistitem,parent,false);
        RecentPostsViewHolder myViewHolder = new RecentPostsViewHolder(view,context,arrayList,jobid);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(RecentPostsViewHolder holder, int position) {
        MySearchHomeData mySearchHomeData =  arrayList.get(position);
        holder.jobtitle.setText(mySearchHomeData.getJobtitle());
        holder.salary.setText(mySearchHomeData.getSalary());
        holder.date.setText(mySearchHomeData.getDate());
        holder.location.setText(mySearchHomeData.getLocation());
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public static class RecentPostsViewHolder extends RecyclerView.ViewHolder
    {
        String jobid[];
        TextView jobtitle,salary,date,location;
        Button btndeletepost,btnseeapplicants;
        CardView cardView;
        ArrayList<MySearchHomeData> list = new ArrayList<MySearchHomeData>();
        Context cobject;
        public RecentPostsViewHolder(View itemView, final Context cob, ArrayList<MySearchHomeData> lob, String jid[]) {
            super(itemView);
            cobject=cob;
            list=lob;
            jobid=jid;
            jobtitle = (TextView) itemView.findViewById(R.id.textViewapplicationjobtitle);
            salary = (TextView) itemView.findViewById(R.id.textViewapplicationjobsalary);
            date = (TextView) itemView.findViewById(R.id.textViewrecentpostdate);
            location = (TextView) itemView.findViewById(R.id.textViewapplicationjoblocation);
            cardView = (CardView) itemView.findViewById(R.id.card_post);
            Animation animation = AnimationUtils.loadAnimation(cob,R.anim.animation_slide);
            cardView.setAnimation(animation);
            btndeletepost=(Button)itemView.findViewById(R.id.buttondeletepost);
            btnseeapplicants=(Button)itemView.findViewById(R.id.buttonseeapplicants);
            btndeletepost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logoutDialog();
                }
            });
            btnseeapplicants.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(cobject,SeeApplicantsActivity.class);
                    intent.putExtra("jobid",jobid[getAdapterPosition()]);
                    cobject.startActivity(intent);

                }
            });


         /*   itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    MySearchHomeData data = list.get(position);
                    Intent intent = new Intent(cobject,JobDescriptionActivity.class);
                    intent.putExtra("job_title",data.getJobtitle());
                    intent.putExtra("job_salary",data.getSalary());
                    intent.putExtra("job_date",data.getDate());
                    intent.putExtra("job_location",data.getLocation());
                    intent.putExtra("job_id",jobid[position]);
                    cobject.startActivity(intent);
                }
            });     */
        }
        public void logoutDialog()
        {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(cobject);
            alertDialog.setTitle("Confirm Delete Post...");
            alertDialog.setMessage("Are you sure you want to delete job post?");
            alertDialog.setIcon(R.drawable.logo);
            alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                   // String jobreferenceid=jobid[getAdapterPosition()];
                  //deleteconnection class is called here by sending jobid to delete job post
                    new DeletePostConnection(cobject).execute(jobid[getAdapterPosition()]);
                }
            });
            alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
        }
    }
}
