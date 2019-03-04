package giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TagaNeed.GPSTracker;

public class SOSOptionActivity extends AppCompatActivity {
    LinearLayout layoutCall, layoutSMS, layoutShareLocation;
    String Latitude, Longitude, message1, message2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sos_options_layout);

        layoutCall = (LinearLayout) findViewById(R.id.layout_call);
        layoutSMS = (LinearLayout) findViewById(R.id.layout_sms);
        layoutShareLocation = (LinearLayout) findViewById(R.id.layout_share_location);
        Latitude = String.valueOf(new GPSTracker(SOSOptionActivity.this).getLatitude());
        Longitude = String.valueOf(new GPSTracker(SOSOptionActivity.this).getLongitude());
        message1 = "Your friend is in emergency situation \n";
        message2 = "http://maps.google.com/maps?saddr=" + Latitude + "," + Longitude;

        layoutCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dial = "tel:7038676940";
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse(dial));
                startActivity(intent);
            }
        });

        layoutSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("smsto:7038676940");
                Intent it = new Intent(Intent.ACTION_SENDTO, uri);
                it.putExtra("sms_body", message1 + message2);
                startActivity(it);
            }
        });

        layoutShareLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SOSOptionActivity.this, EmergencyStageTwo.class);
                intent.putExtra("latitude", Latitude);
                intent.putExtra("longitude", Longitude);
                startActivity(intent);
            }
        });
    }
}
