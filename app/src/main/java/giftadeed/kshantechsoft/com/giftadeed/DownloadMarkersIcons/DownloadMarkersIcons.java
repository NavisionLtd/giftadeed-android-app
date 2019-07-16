package giftadeed.kshantechsoft.com.giftadeed.DownloadMarkersIcons;

////////////////////////////////////////////////////////
// Copy Rights : Navision Ltd.
// Last Modified On : 06-Dec-18 11:11 AM
// Description : This class is used to download all markers and icons from server and store in local storage
////////////////////////////////////////////////////////

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Random;

import giftadeed.kshantechsoft.com.giftadeed.R;

public class DownloadMarkersIcons extends AppCompatActivity {
    Bitmap mIcon;
    Button btnDisp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.download_marker_icons);
        btnDisp = (Button) findViewById(R.id.btn_disp);
        new MyDownloadTask().execute();

        btnDisp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DisplayImage();
            }
        });
    }

    class MyDownloadTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                URL url = new URL("https://kshandemo.co.in/gad3/api/17.png");
                HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
                urlcon.setDoInput(true);
                String responseMsg = urlcon.getResponseMessage();
                int response = urlcon.getResponseCode();
                InputStream in = urlcon.getInputStream();
                mIcon = BitmapFactory.decodeStream(in);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            SaveImage(mIcon);
        }
    }

    public Bitmap getbmpfromURL(String surl) {
        try {
            URL url = new URL(surl);
            HttpURLConnection urlcon = (HttpURLConnection) url.openConnection();
            urlcon.setDoInput(true);
            urlcon.connect();
            InputStream in = urlcon.getInputStream();
            Bitmap mIcon = BitmapFactory.decodeStream(in);
            SaveImage(mIcon);
            return mIcon;
        } catch (Exception e) {
            Log.e("DownloadError", "" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private void SaveImage(Bitmap finalBitmap) {
//        String root = Environment.getExternalStorageDirectory().toString();
//        File myDir = new File(root + "/saved_images");
        File myDir = new File("/data/data/giftadeed.kshantechsoft.com.giftadeed/saved_images");
        if (!myDir.exists()) {
            myDir.mkdirs();
        }
        String fname = "Image.png";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Toast.makeText(this, "Image downloaded", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void DisplayImage() {
        File imgFile = new File("/data/data/giftadeed.kshantechsoft.com.giftadeed/saved_images/Image.png");
        if (imgFile.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ImageView myImage = (ImageView) findViewById(R.id.imageview);
            myImage.setImageBitmap(myBitmap);
        }
    }
}
