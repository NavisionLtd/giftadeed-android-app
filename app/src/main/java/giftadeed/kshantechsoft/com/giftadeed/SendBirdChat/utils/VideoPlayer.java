package giftadeed.kshantechsoft.com.giftadeed.SendBirdChat.utils;

import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import giftadeed.kshantechsoft.com.giftadeed.R;

public class VideoPlayer extends AppCompatActivity {
    private VideoView myVideoView;
    private int position = 0;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;
    private String mUrl;
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        //set the media controller buttons
        if (mediaControls == null) {
            mediaControls = new MediaController(VideoPlayer.this);
        }
        Bundle extras = getIntent().getExtras();
        mUrl = extras.getString("url");
        mName = extras.getString("name");
        //initialize the VideoView
        myVideoView = (VideoView) findViewById(R.id.video_view);
// video download from url server save into exrternal storage and play by videoView

        //getting the details from external stoargae whtaher the file exists or not
        String SDCardRoot = Environment.getExternalStorageDirectory().toString();
        String audioFilePath = SDCardRoot + "/BillinYogiChat/" + mName;

        File extStore = Environment.getExternalStorageDirectory();
        File myFile = new File(extStore.getAbsolutePath() + "/BillinYogiChat/" + mName);

        if(myFile.exists()) {
            existingVideo(audioFilePath);
        }
        else
        {
            startDownload(mUrl);
        }


        //we also set an setOnPreparedListener in order to know when the video file is ready for playback
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

            public void onPrepared(MediaPlayer mediaPlayer) {
                // close the progress bar and play the video
              //  progressDialog.dismiss();
                //if we have a position on savedInstanceState, the video playback should start from here
                myVideoView.seekTo(position);
                if (position == 0) {
                    myVideoView.start();
                } else {
                    //if we come from a resumed activity, video playback will be paused
                    myVideoView.pause();
                }
            }
        });

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        //we use onSaveInstanceState in order to store the video playback position for orientation change
        savedInstanceState.putInt("Position", myVideoView.getCurrentPosition());
        myVideoView.pause();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //we use onRestoreInstanceState in order to play the video playback from the stored position
        position = savedInstanceState.getInt("Position");
        myVideoView.seekTo(position);
    }


    //===============================================================================================

    private void startDownload(String mUrl) {
        String url = mUrl;
        //String UrlName=fileName;
        new VideoPlayer.DownloadFileAsync().execute(url);
    }


    //downloading file from server based url
    class DownloadFileAsync extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(VideoPlayer.this);
            // set a title for the progress bar
          //  progressDialog.setTitle("Video is playing");
            // set a message for the progress bar
            progressDialog.setMessage("Loading...");
            //set the progress bar not cancelable on users' touch
            progressDialog.setCancelable(false);
            // show the progress bar
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... aurl) {
            int count;
            try {
                URL url = new URL(aurl[0]);
                URLConnection conexion = url.openConnection();
                conexion.connect();
                int lenghtOfFile = conexion.getContentLength();
                Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);
                InputStream input = new BufferedInputStream(url.openStream());

                File folder = new File(Environment.getExternalStorageDirectory().toString() + "/BillinYogiChat/");
                folder.mkdirs();

                //Save the path as a string value
                String extStorageDirectory = folder.toString();
                File file = new File(folder, mName);
                OutputStream output = new FileOutputStream(file);
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));
                    output.write(data, 0, count);
                }

                output.flush();
                output.close();
                input.close();
            } catch (Exception e) {
            }
            return null;
        }

        protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);


        }

        @Override
        protected void onPostExecute(String unused) {
            progressDialog.dismiss();
            try {

                String SDCardRoot = Environment.getExternalStorageDirectory()
                        .toString();
                String audioFilePath = SDCardRoot + "/BillinYogiChat/" + mName;


                //set the media controller in the VideoView
                myVideoView.setMediaController(mediaControls);

                //set the uri of the video to be played
                //   myVideoView.setVideoURI(Uri.parse(mUrl));
                //set the path fro to play from external storage
                myVideoView.setVideoPath(audioFilePath);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            myVideoView.requestFocus();


        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
    public void existingVideo(String strAudioFilepath)
    {
        //set the media controller in the VideoView
        myVideoView.setMediaController(mediaControls);

        //set the uri of the video to be played
        //   myVideoView.setVideoURI(Uri.parse(mUrl));
        //set the path fro to play from external storage
        myVideoView.setVideoPath(strAudioFilepath);
    }
}
