package com.example.daivansh.jobreferer.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.daivansh.jobreferer.R;
import com.example.daivansh.jobreferer.helper.SPHelper;
import com.example.daivansh.jobreferer.connections.GetSkillsConnection;
import com.example.daivansh.jobreferer.helper.TypefaceUtil;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    String uid;
    RelativeLayout relativeLayout;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        TypefaceUtil.overrideFont(this, "SANS-SERIF", "fonts/Montserrat-Regular.ttf");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        relativeLayout = (RelativeLayout) findViewById(R.id.activity_splash);
        linearLayout = (LinearLayout) findViewById(R.id.Splash_Title);
        final View view = relativeLayout;
        overridePendingTransition(R.anim.from, R.anim.to);

        new SPHelper(getBaseContext(),getString(R.string.defaultPrefer));
        uid = SPHelper.getData(getString(R.string.keyUID));
        ImageView iv = (ImageView)findViewById(R.id.iv_logo);
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation);
        Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.animation2);
        iv.setAnimation(animation);
        linearLayout.setAnimation(animation1);


        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                Thread background = new Thread() {
                    public void run() {
                        try
                        {
                            sleep(800);
                            new GetSkillsConnection(SplashActivity.this,view).execute();
                        }
                        catch (Exception e) {
                        }
                    }
                };
                background.start();

            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            //Log.d(TAG, "Message data payload: " + remoteMessage.getData());
            Toast.makeText(getBaseContext(),""+remoteMessage.getNotification().getBody(),Toast.LENGTH_LONG).show();

        }

    }
}










