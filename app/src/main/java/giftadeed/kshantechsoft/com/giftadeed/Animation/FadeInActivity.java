package giftadeed.kshantechsoft.com.giftadeed.Animation;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import giftadeed.kshantechsoft.com.giftadeed.R;

public class FadeInActivity extends AppCompatActivity implements Animation.AnimationListener {
    ImageView imageView, imageViewClose;
    Animation animFadein;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reward_dialog);
        imageView = (ImageView) findViewById(R.id.image_reward);
        imageViewClose = (ImageView) findViewById(R.id.ic_close);

        // load the animation
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.zoom_out);
        // set animation listener
        animFadein.setAnimationListener(this);
        imageView.setVisibility(View.VISIBLE);
        // start the animation
        imageView.startAnimation(animFadein);

        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FadeInActivity.this.finish();
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
