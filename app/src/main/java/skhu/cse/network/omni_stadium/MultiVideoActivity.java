package skhu.cse.network.omni_stadium;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.VideoView;

public class MultiVideoActivity extends AppCompatActivity {

    private ImageView imgView_warning1, imgView_warning2;
    private VideoView videoview1st, videoview2nd;
    private ProgressBar spinnerView1, spinnerView2;
    private boolean isClosed1stServer=false;
    private boolean isClosed2ndServer=false;

    private String VideoURL1st = "rtsp://192.168.63.109:8554/test";
    private String VideoURL2nd = "rtsp://mpv.cdn3.bigCDN.com:554/bigCDN/definst/mp4:bigbuckbunnyiphone_400.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_video);

        imgView_warning1=(ImageView)findViewById(R.id.imgView_warning1);
        imgView_warning2=(ImageView)findViewById(R.id.imgView_warning2);

        videoview1st = (VideoView) findViewById(R.id.VideoView1st);
        videoview2nd = (VideoView) findViewById(R.id.VideoView2nd);

        videoview1st.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.v("videoview1st Test", "onError Called");
                if(what==MediaPlayer.MEDIA_ERROR_SERVER_DIED)
                    Log.v("videoview1st Test", "Media Error, Server Died " + extra);
                else if(what==MediaPlayer.MEDIA_ERROR_UNKNOWN)
                    Log.v("videoview1st Test", "Media Error, Error Unknown " + extra);
                isClosed1stServer=true;
                return false;
            }
        });
        videoview2nd.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.v("videoview2nd Test", "onError Called");
                if(what==MediaPlayer.MEDIA_ERROR_SERVER_DIED)
                    Log.v("videoview2nd Test", "Media Error, Server Died " + extra);
                else if(what==MediaPlayer.MEDIA_ERROR_UNKNOWN)
                    Log.v("videoview2nd Test", "Media Error, Error Unknown " + extra);
                isClosed2ndServer=true;
                return false;
            }
        });

        videoview1st.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent=new Intent(getApplicationContext(), FullVideoActivity.class);
                intent.putExtra("VideoURL", VideoURL1st);
                startActivity(intent);
                return false;
            }
        });

        videoview2nd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent=new Intent(getApplicationContext(), FullVideoActivity.class);
                intent.putExtra("VideoURL", VideoURL2nd);
                startActivity(intent);
                return false;
            }
        });

        spinnerView1 = (ProgressBar) findViewById(R.id.spinnerView1);
        spinnerView2 = (ProgressBar) findViewById(R.id.spinnerView2);

        spinnerView1.setVisibility(View.VISIBLE);
        spinnerView2.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        try
        {
            if(!isClosed1stServer)
            {
                Uri videoUri1st = Uri.parse(VideoURL1st);
                imgView_warning1.setVisibility(View.GONE);
                videoview1st.setVideoURI(videoUri1st);
            }
            else
            {
                spinnerView1.setVisibility(View.GONE);
                imgView_warning1.setVisibility(View.VISIBLE);
                videoview1st.setOnTouchListener(null);
            }

            if(!isClosed2ndServer)
            {
                Uri videoUri2nd = Uri.parse(VideoURL2nd);
                imgView_warning2.setVisibility(View.GONE);
                videoview2nd.setVideoURI(videoUri2nd);
            }
            else
            {
                spinnerView2.setVisibility(View.GONE);
                imgView_warning2.setVisibility(View.VISIBLE);
                videoview2nd.setOnTouchListener(null);
            }

        }
        catch (Exception e)
        {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        videoview1st.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                Log.v("Loaded", " video1");
                spinnerView1.setVisibility(View.GONE);
                videoview1st.setAlpha(1.0f);
                videoview1st.start();
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        });
        videoview2nd.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                Log.v("Loaded", " video2");
                spinnerView2.setVisibility(View.GONE);
                videoview2nd.setAlpha(1.0f);
                videoview2nd.start();
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
