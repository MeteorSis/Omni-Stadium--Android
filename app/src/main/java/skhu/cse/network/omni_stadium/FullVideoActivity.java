package skhu.cse.network.omni_stadium;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.VideoView;

public class FullVideoActivity extends AppCompatActivity {

    ProgressBar spinnerView;
    VideoView videoview;
    String VideoURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_video);

        VideoURL=getIntent().getStringExtra("VideoURL");

        videoview = (VideoView) findViewById(R.id.FullVideoView);

        videoview.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.v("videoview Test", "onError Called");
                if(what==MediaPlayer.MEDIA_ERROR_SERVER_DIED)
                    Log.v("videoview Test", "Media Error, Server Died " + extra);
                else if(what==MediaPlayer.MEDIA_ERROR_UNKNOWN)
                    Log.v("videoview Test", "Media Error, Error Unknown " + extra);
                return false;
            }
        });

        spinnerView = (ProgressBar) findViewById(R.id.spinnerView);
        spinnerView.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        try
        {
            // Get the URL from String VideoURL
            Uri videoUri = Uri.parse(VideoURL);
            videoview.setVideoURI(videoUri);
        }
        catch (Exception e)
        {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        videoview.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                Log.v("Loaded", " Full Video");
                spinnerView.setVisibility(View.GONE);
                videoview.setAlpha(1.0f);
                videoview.start();
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
