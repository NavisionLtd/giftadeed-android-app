package giftadeed.kshantechsoft.com.giftadeed.TaggedNeeds.list_Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.ui.IconGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;

import giftadeed.kshantechsoft.com.giftadeed.R;

/**
 * Created by Darshan on 07/03/2017.
 */

public class JobRenderer extends DefaultClusterRenderer<MyItem> {
    private final IconGenerator iconGenerator;
    private final IconGenerator clusterIconGenerator;
    private final ImageView imageView;
    private final ImageView clusterImageView;
    private final TextView txt_count;
    private final String TAG = "ClusterRenderer";
    private DisplayImageOptions options;
    Bitmap bitmapImage;
    Context ctx;
    private final int mDimension;
    URL url;

    //ImageLoader imgloader;
    public JobRenderer(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager) {
        super(context, map, clusterManager);
        ctx = context;
        // initialize cluster icon generator
        mDimension = (int) ctx.getResources().getDimension(R.dimen.custom_profile_image);
        clusterIconGenerator = new IconGenerator(context.getApplicationContext());
        View clusterView = LayoutInflater.from(context).inflate(R.layout.view_custom_marker, null);
        clusterIconGenerator.setContentView(clusterView);
        clusterImageView = (ImageView) clusterView.findViewById(R.id.profile_image);
        txt_count = (TextView) clusterView.findViewById(R.id.text_count);
        // initialize cluster item icon generator
        iconGenerator = new IconGenerator(context.getApplicationContext());
        imageView = new ImageView(context.getApplicationContext());
        // markerWidth = (int) context.getResources().getDimension(R.dimen.profile_image);
        // markerHeight = (int) context.getResources().getDimension(R.dimen.custom_profile_image);
        imageView.setLayoutParams(new ViewGroup.LayoutParams(60, 60));
        //int padding = (int) context.getResources().getDimension(R.dimen.custom_profile_padding);
        //int padding = 2;
        //imageView.setPadding(padding, padding, padding, padding);
        iconGenerator.setContentView(imageView);
        // Picasso.with(ctx).load(clusterManager.getItems().iterator().getimg_path()).into(imageView);
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.dem)
                .showImageForEmptyUri(R.drawable.dem)
                .showImageOnFail(R.drawable.dem)
                .cacheInMemory(false)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    @Override
    protected void onBeforeClusterItemRendered(MyItem job, MarkerOptions markerOptions) {
        Picasso.with(ctx).load(job.getimg_path()).into(imageView);
        try {
            url = new URL(job.getimg_path());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            bitmapImage = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap icon = iconGenerator.makeIcon();
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmapImage)).title(job.getTitle());

//        loadAsync(imageView, job.getimg_path());
//        try {
//            Bitmap icon = iconGenerator.makeIcon();
//            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(job.getTitle());
//        } catch (Exception e) {
//            // FIXME workaround : called after Bitmap recycled
//            Log.e(TAG, e.getMessage(), e);
//        }
    }

    @Override
    protected void onBeforeClusterRendered(Cluster<MyItem> cluster, MarkerOptions markerOptions) {
        Iterator<MyItem> iterator = cluster.getItems().iterator();
        Picasso.with(ctx).load(iterator.next().getimg_path()).into(clusterImageView);
        try {
            url = new URL(iterator.next().getimg_path());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            bitmapImage = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Bitmap icon = clusterIconGenerator.makeIcon(iterator.next().getimg_path());
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmapImage, 70, 70, false)));

//        loadAsync(clusterImageView, iterator.next().getimg_path());
//        try {
//            Bitmap icon = clusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
//            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
//        } catch (Exception e) {
//            // FIXME workaround : called after Bitmap recycled
//            Log.e(TAG, e.getMessage(), e);
//        }
    }

    @Override
    protected boolean shouldRenderAsCluster(Cluster cluster) {
        return cluster.getSize() > 1;
    }

    private void loadAsync(final ImageView imageView, final String url) {
            // if there are no cached entry or already recycled, start async load
            Picasso.with(ctx).load(url).into(new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    imageView.setImageBitmap(bitmap);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    imageView.setImageResource(R.drawable.ic_launcher);
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {
                }
            });
    }
}