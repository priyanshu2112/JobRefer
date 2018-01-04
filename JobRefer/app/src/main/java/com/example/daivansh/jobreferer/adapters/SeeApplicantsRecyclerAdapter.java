package com.example.daivansh.jobreferer.adapters;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.daivansh.jobreferer.R;
import com.example.daivansh.jobreferer.helper.SPHelper;
import com.example.daivansh.jobreferer.pojos.MySeeApplicantsData;

import java.util.ArrayList;

/**
 * Created by daivansh on 09-06-2017.
 */

public class SeeApplicantsRecyclerAdapter extends RecyclerView.Adapter<SeeApplicantsRecyclerAdapter.SeeApplicantsViewHolder> {
    ArrayList<MySeeApplicantsData> arrayList = new ArrayList<MySeeApplicantsData>();
    Context context;

    public SeeApplicantsRecyclerAdapter(ArrayList<MySeeApplicantsData> arrayList, Context con) {
        this.arrayList = arrayList;
        context = con;
    }

    @Override
    public SeeApplicantsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seeapplicantslistitem, parent, false);
        SeeApplicantsViewHolder myViewHolder = new SeeApplicantsViewHolder(view, context, arrayList);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(SeeApplicantsViewHolder holder, int position) {
        MySeeApplicantsData mySeeApplicantsData = arrayList.get(position);
        holder.txtname.setText(mySeeApplicantsData.getApplicantname());
        holder.txtmail.setText(mySeeApplicantsData.getApplicantemailid());
        holder.txtcontact.setText(mySeeApplicantsData.getApplicantcontactno());
        Toast.makeText(context,"mail:"+mySeeApplicantsData.getApplicantemailid(),Toast.LENGTH_LONG).show();
        Toast.makeText(context,"number:"+mySeeApplicantsData.getApplicantcontactno(),Toast.LENGTH_LONG).show();
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public static class SeeApplicantsViewHolder extends RecyclerView.ViewHolder {
        TextView txtname, txtmail, txtcontact;
        CardView cardView;
        //ImageView call, mail;

        ArrayList<MySeeApplicantsData> list = new ArrayList<MySeeApplicantsData>();
        Context cobject;

        public SeeApplicantsViewHolder(View itemView, Context cob, ArrayList<MySeeApplicantsData> lob) {
            super(itemView);
            cobject = cob;
            list = lob;
            txtname = (TextView) itemView.findViewById(R.id.tv_applicantname);
            txtmail = (TextView) itemView.findViewById(R.id.tv_applicantmail);
            txtcontact = (TextView) itemView.findViewById(R.id.tv_applicantcontact);
            cardView = (CardView) itemView.findViewById(R.id.card_applicant);
            Animation animation = AnimationUtils.loadAnimation(cob,R.anim.animation_slide);
            cardView.setAnimation(animation);
            LinearLayout call = (LinearLayout) itemView.findViewById(R.id.view_call);
            LinearLayout email = (LinearLayout) itemView.findViewById(R.id.view_email);
//            call=(ImageView)itemView.findViewById(R.id.imagevcall);
//            mail=(ImageView)itemView.findViewById(R.id.imagevmail);
            call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + list.get(getAdapterPosition()).getApplicantcontactno().trim()));
                    if (ActivityCompat.checkSelfPermission(cobject, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    cobject.startActivity(intent);
                }
            });
            email.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new SPHelper(cobject,cobject.getString(R.string.skillsPrefer));
                    String useremail=SPHelper.getData(cobject.getString(R.string.keyEMAIL));
                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.setType("plain/text");
                    sendIntent.setData(Uri.parse(useremail));
                    sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                    sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { list.get(getAdapterPosition()).getApplicantemailid().trim() });
                     sendIntent.putExtra(Intent.EXTRA_SUBJECT, "test");
                      sendIntent.putExtra(Intent.EXTRA_TEXT, "hello. this is a message sent from my demo app :-)");

                    
                    cobject.startActivity(sendIntent);

                }
            });
        }
    }
}
