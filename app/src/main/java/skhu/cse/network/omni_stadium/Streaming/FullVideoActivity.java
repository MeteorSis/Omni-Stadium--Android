package skhu.cse.network.omni_stadium.Streaming;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import skhu.cse.network.omni_stadium.R;

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
                Toast.makeText(FullVideoActivity.this, "서버가 불안정합니다. 스트리밍 기능을 종료합니다.", Toast.LENGTH_SHORT).show();
                FullVideoActivity.this.finish();
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
                spinnerView.setVisibility(View.INVISIBLE);
                videoview.setAlpha(1.0f);
                videoview.start();
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        spinnerView.setVisibility(View.VISIBLE);
        videoview.setAlpha(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoview.stopPlayback();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }
}
