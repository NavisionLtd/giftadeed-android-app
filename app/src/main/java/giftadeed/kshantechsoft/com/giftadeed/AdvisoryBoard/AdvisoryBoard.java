package giftadeed.kshantechsoft.com.giftadeed.AdvisoryBoard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.leo.simplearcloader.ArcConfiguration;
import com.leo.simplearcloader.SimpleArcDialog;
import com.squareup.okhttp.OkHttpClient;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import giftadeed.kshantechsoft.com.giftadeed.Bug.Bugreport;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsActivity;
import giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.TaggedneedsFrag;
import giftadeed.kshantechsoft.com.giftadeed.Utils.ToastPopUp;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by roshan on 19/3/18.
 */

/////////////////////////////////////////////////////////////////
//                                                             //
//       Shows advisory board details                          //
/////////////////////////////////////////////////////////////////

public class AdvisoryBoard extends Fragment {

    private View view;
    private FragmentManager fragmgr;
    private RecyclerView advisoryRecyclerView;
    private AdvisoryAdapter advisoryAdapter;
    private List<AdvisoryResponse.Advisory> advisories = new ArrayList<>();
    SimpleArcDialog mDialog;

    public static AdvisoryBoard newInstance(int sectionNumber) {
        AdvisoryBoard fragment = new AdvisoryBoard();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_advisory_board, container, false);

        TaggedneedsActivity.updateTitle("Advisory Board");
        TaggedneedsActivity.imgappbarcamera.setVisibility(View.GONE);
        TaggedneedsActivity.imgappbarsetting.setVisibility(View.GONE);
        TaggedneedsActivity.imgfilter.setVisibility(View.GONE);
        TaggedneedsActivity.editprofile.setVisibility(View.GONE);
        TaggedneedsActivity.saveprofile.setVisibility(View.GONE);
        TaggedneedsActivity.toggle.setDrawerIndicatorEnabled(true);
        TaggedneedsActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        TaggedneedsActivity.fragname = AdvisoryBoard.newInstance(0);
        mDialog = new SimpleArcDialog(getContext());
        fragmgr = getFragmentManager();


        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        advisoryRecyclerView = (RecyclerView) view.findViewById(R.id.advisoryRecyclerView);
        advisoryAdapter = new AdvisoryAdapter(getContext(), advisories);
        mDialog.setConfiguration(new ArcConfiguration(getContext()));
        mDialog.show();
        mDialog.setCancelable(false);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        advisoryRecyclerView.setLayoutManager(layoutManager);
        advisoryRecyclerView.setAdapter(advisoryAdapter);

        //-----------------------------getting data from server and setting it to adapter
        OkHttpClient client = new OkHttpClient();
        client.setConnectTimeout(1, TimeUnit.HOURS);
        client.setReadTimeout(1, TimeUnit.HOURS);
        client.setWriteTimeout(1, TimeUnit.HOURS);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(WebServices.MANI_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        AdvisoryDataInterface service = retrofit.create(AdvisoryDataInterface.class);

        Call<AdvisoryResponse> call = service.sendData("");
        call.enqueue(new Callback<AdvisoryResponse>() {
            @Override
            public void onResponse(Response<AdvisoryResponse> response, Retrofit retrofit) {
                try {
                    mDialog.dismiss();
                    advisories.clear();
                    advisories.addAll(response.body().getAdvisory());
                    advisoryAdapter.notifyDataSetChanged();
                }catch(Exception e){
                    mDialog.dismiss();
//                    StringWriter writer = new StringWriter();
//                    e.printStackTrace(new PrintWriter(writer));
//                    Bugreport bg = new Bugreport();
//                    bg.sendbug(writer.toString());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                ToastPopUp.show(getContext(), getString(R.string.server_response_error));
                mDialog.dismiss();
            }
        });
    }


    @Override
    public void onResume() {

        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                    Bundle bundle = new Bundle();
                    int i = 3;
                    bundle.putString("tab", "tab1");
                    TaggedneedsFrag mainHomeFragment = new TaggedneedsFrag();
                    mainHomeFragment.setArguments(bundle);
                    android.support.v4.app.FragmentTransaction fragmentTransaction =
                            getActivity().getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, mainHomeFragment);
                    fragmentTransaction.commit();

                    return true;
                }
                return false;
            }
        });
    }
}
