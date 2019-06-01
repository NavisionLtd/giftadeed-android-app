package giftadeed.kshantechsoft.com.giftadeed.Animation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import giftadeed.kshantechsoft.com.giftadeed.Collaboration.CreateCollabFragment;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsFrag;

public class FadeInActivity extends AppCompatActivity implements Animation.AnimationListener {
    private TextView txtcreditpoints, txttotalpoints, txtneedname;
    private ImageView imageView, imageViewClose;
    private Button btnShare;
    private Animation animFadein;
    private String needtype = "", credits_points = "", total_points = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reward_dialog);
        imageView = (ImageView) findViewById(R.id.image_reward);
        imageViewClose = (ImageView) findViewById(R.id.ic_close);
        txtcreditpoints = (TextView) findViewById(R.id.text_deed_points);
        txttotalpoints = (TextView) findViewById(R.id.text_deed_total_points);
        txtneedname = (TextView) findViewById(R.id.text_deed_type);
        btnShare = (Button) findViewById(R.id.btn_tag_reward_share);

        needtype = getIntent().getStringExtra("need_name");
        credits_points = getIntent().getStringExtra("credit_points");
        total_points = getIntent().getStringExtra("total_points");

        // load the animation
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.zoom_out);
        // set animation listener
        animFadein.setAnimationListener(this);
        imageView.setVisibility(View.VISIBLE);
        // start the animation
        imageView.startAnimation(animFadein);

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
}
