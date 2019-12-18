/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.Animation;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;

import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;

public class FadeInActivity extends AppCompatActivity implements Animation.AnimationListener {
    private TextView txt2, txtcreditpoints, txttotalpoints, txtneedname;
    private ImageView gifSmiley, gifImage, imageView, imageViewClose;
    private LottieAnimationView lottieAnimationView;
    private Button btnShare;
    private Animation animFadein;
    private String reason = "", needtype = "", credits_points = "", total_points = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reward_dialog);
        gifSmiley = findViewById(R.id.gif_smiley_face);
        gifImage = (ImageView) findViewById(R.id.gif_image);
        imageView = (ImageView) findViewById(R.id.image_reward);
        lottieAnimationView = (LottieAnimationView) findViewById(R.id.image_reward1);
        imageViewClose = (ImageView) findViewById(R.id.ic_close);
        txt2 = (TextView) findViewById(R.id.text2);
        txtcreditpoints = (TextView) findViewById(R.id.text_deed_points);
        txttotalpoints = (TextView) findViewById(R.id.text_deed_total_points);
        txtneedname = (TextView) findViewById(R.id.text_deed_type);
        btnShare = (Button) findViewById(R.id.btn_tag_reward_share);

        reason = getIntent().getStringExtra("reason");
        needtype = getIntent().getStringExtra("need_name");
        credits_points = getIntent().getStringExtra("credit_points");
        total_points = getIntent().getStringExtra("total_points");

        // load the animation
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.zoom_out);
        // set animation listener
        animFadein.setAnimationListener(this);

        if (reason.equals("by tagging")) {
            /*imageView.setVisibility(View.VISIBLE);
            lottieAnimationView.setVisibility(View.GONE);
            Glide.with(this)
                    .load(R.drawable.tag_reward)
                    .into(imageView);
            // start the animation
            imageView.startAnimation(animFadein);*/

            // play sound from RAW folder
            MediaPlayer mp = MediaPlayer.create(this, R.raw.endorsed_sound);
            mp.start();
            //load thumb gif image from assets
            gifSmiley.setVisibility(View.GONE);
            gifImage.setVisibility(View.VISIBLE);
            Glide.with(this)
                    .load(R.drawable.thumb_gif)
                    .into(gifImage);
        } else {
            /*Glide.with(this)
                    .load(R.drawable.gift_reward)
                    .into(imageView);*/
//            lottieAnimationView.setVisibility(View.VISIBLE);
//            imageView.setVisibility(View.GONE);

            // play clapping sound from RAW folder
            MediaPlayer mp = MediaPlayer.create(this, R.raw.claps_sound);
            mp.start();
            //load clapping gif image from assets
            gifSmiley.setVisibility(View.VISIBLE);
            gifImage.setVisibility(View.GONE);
            Glide.with(this)
                    .load(R.drawable.smiley_face)
                    .into(gifSmiley);
        }

        txt2.setText(reason);
        txtneedname.setText(needtype);
        txtcreditpoints.setText(credits_points);
        txttotalpoints.setText(total_points);
        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String android_shortlink = "http://tiny.cc/kwb33y";
                String ios_shortlink = "http://tiny.cc/h4533y";
                String website = "https://www.giftadeed.com/";
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, "Hey! My latest points are " + total_points + " in the GiftADeed App.\n" +
                        "You can earn your points by downloading the app from\n\n" +
                        "Android : " + android_shortlink + "\n" +
                        "iOS : " + ios_shortlink + "\n\n" +
                        "Also, check the website at " + website);
                startActivity(Intent.createChooser(share, "Share your points on:"));
            }
        });

        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getBaseContext(), TaggedneedsActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        // Take any action after completing the animation
        // check for fade in animation
        if (animation == animFadein) {

        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onAnimationStart(Animation animation) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onBackPressed() {

    }
}
