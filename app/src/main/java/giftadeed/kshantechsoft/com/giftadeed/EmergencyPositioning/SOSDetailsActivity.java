package giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.okhttp.OkHttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;
import giftadeed.kshantechsoft.com.giftadeed.Utils.Validation;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class SOSDetailsActivity extends AppCompatActivity {
    ImageView sosImage;
    TextView txtsos_creater, txtaddress, txtemergency, txtDate;
    String str_sosid;
    private List<UploadSOS> soslist;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    LinearLayout creatorlayout, emergencyLayout;
    SimpleArcDialog mDialog;
    String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sos_details);

        sosImage = (ImageView) findViewById(R.id.sos_image_1);
        txtsos_creater = (TextView) findViewById(R.id.tv_sos_1);
        txtemergency = (TextView) findViewById(R.id.tv_emergency_1);
        emergencyLayout = (LinearLayout) findViewById(R.id.sos_emergency_layout_1);
        creatorlayout = (LinearLayout) findViewById(R.id.sos_creator_layout_1);
        txtaddress = (TextView) findViewById(R.id.txt_sos_address_1);
        txtDate = (TextView) findViewById(R.id.tv_sos_created_1);

        mDialog = new SimpleArcDialog(this);
        str_sosid = getIntent().getStringExtra("sos_id");
        if (!(Validation.isOnline(SOSDetailsActivity.this))) {
            ToastPopUp.show(SOSDetailsActivity.this, getString(R.string.network_validation));
        } else {
            getSOS_Details();
            mFirebaseInstance = FirebaseDatabase.getInstance();
            mFirebaseDatabase = mFirebaseInstance.getReference(WebServices.DATABASE_SOS_UPLOADS);
            soslist = new ArrayList<>();
            DatabaseReference reference = mFirebaseDatabase;
            //adding an event listener to fetch values
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    //iterating through all the values in database
                    for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                        UploadSOS uploadSOS = postSnapshot.getValue(UploadSOS.class);
                        soslist.add(uploadSOS);
                    }
                    Log.d("sos_list", "" + soslist.size());
                    if (soslist.size() > 0) {
                        for (UploadSOS sos : soslist) {
                            if (sos.getSosid().equals(str_sosid)) {
                                path = sos.getSosurl();
                            }
                        }
                        if (path.length() > 0) {
                            sosImage.setVisibility(View.VISIBLE);
                            Glide.with(SOSDetailsActivity.this).load(path).into(sosImage);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    public void getSOS_Details() {
        mDialog.setConfiguration(new ArcConfiguration(this));
        mDialog.show();
        mDialog.setCancelable(false);
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder().baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        SOSDetailsInterface service = retrofit.create(SOSDetailsInterface.class);
        Call<List<EmergencyInfoPOJO>> call = service.fetchData(str_sosid);
        call.enqueue(new Callback<List<EmergencyInfoPOJO>>() {
            @Override
            public void onResponse(Response<List<EmergencyInfoPOJO>> response, Retrofit retrofit) {
                Log.d("response_sosdetails", "" + response.body());
                try {
                    List<EmergencyInfoPOJO> emergencyInfoPOJOS = response.body();
                    mDialog.dismiss();
                    if (emergencyInfoPOJOS.get(0).getUsername() != null) {
                        creatorlayout.setVisibility(View.VISIBLE);
                        txtsos_creater.setText(emergencyInfoPOJOS.get(0).getUsername());
                    } else {
                        creatorlayout.setVisibility(View.GONE);
                    }
                    if (emergencyInfoPOJOS.get(0).getSostype() != null) {
                        if (emergencyInfoPOJOS.get(0).getSostype().length() > 0) {
                            emergencyLayout.setVisibility(View.VISIBLE);
                            txtemergency.setText(emergencyInfoPOJOS.get(0).getSostype());
                        } else {
                            emergencyLayout.setVisibility(View.GONE);
                        }
                    }
                    txtaddress.setText(emergencyInfoPOJOS.get(0).getAddress());
                    txtDate.setText(emergencyInfoPOJOS.get(0).getCdate());
                } catch (Exception e) {
                    Log.d("response_sosdetails", "" + e.getMessage());
                    mDialog.dismiss();
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("response_sosdetails", "" + t.getMessage());
                mDialog.dismiss();
                ToastPopUp.show(SOSDetailsActivity.this, getString(R.string.server_response_error));
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
