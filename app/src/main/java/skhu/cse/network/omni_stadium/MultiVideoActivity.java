package skhu.cse.network.omni_stadium;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.VideoView;

public class MultiVideoActivity extends AppCompatActivity {
    ProgressDialog pDialog;

    VideoView videoview1st;
    VideoView videoview2nd;
    Boolean video1stLoaded = false;
    Boolean video2ndLoaded = false;

    String VideoURL1st = "rtsp://192.168.63.25:8554/test";
    String VideoURL2nd = "rtsp://192.168.63.25:8554/test";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_video);

        videoview1st = (VideoView) findViewById(R.id.VideoView1st);
        videoview2nd = (VideoView) findViewById(R.id.VideoView2nd);

        // Create a progressbar
        pDialog = new ProgressDialog(MultiVideoActivity.this);
        // Set progressbar title
        pDialog.setTitle("Camera Streaming");
        // Set progressbar message
        pDialog.setMessage("Buffering...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        // Show progressbar
        pDialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        try
        {
            // Get the URL from String VideoURL
            Uri videoUri1st = Uri.parse(VideoURL1st);
            Uri videoUri2nd = Uri.parse(VideoURL2nd);
            videoview1st.setVideoURI(videoUri1st);
            videoview2nd.setVideoURI(videoUri2nd);
        }
        catch (Exception e)
        {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        videoview1st.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                video1stLoaded=true;
                Log.v("Loaded", " video1");
                if(video2ndLoaded)
                {
                    pDialog.dismiss();
                    videoview1st.start();
                    videoview2nd.start();
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            }
        });
        videoview2nd.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                video2ndLoaded=true;
                Log.v("Loaded", " video2");
                if(video1stLoaded)
                {
                    pDialog.dismiss();
                    videoview1st.start();
                    videoview2nd.start();
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            }
        });
        videoview1st.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent=new Intent(getApplicationContext(), FullVideoActivity.class);
                intent.putExtra("VideoURL", VideoURL1st);
                intent.putExtra("camNum", 1);
                startActivity(intent);
                return false;
            }
        });

        videoview2nd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent=new Intent(getApplicationContext(), FullVideoActivity.class);
                intent.putExtra("VideoURL", VideoURL2nd);
                intent.putExtra("camNum", 2);
                startActivity(intent);
                return false;
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
