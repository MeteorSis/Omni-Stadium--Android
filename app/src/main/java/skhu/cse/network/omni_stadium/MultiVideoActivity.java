package skhu.cse.network.omni_stadium;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;

public class MultiVideoActivity extends AppCompatActivity {

    private ImageView imgView_warning1, imgView_warning2;
    private VideoView videoview1st, videoview2nd;
    private ProgressBar spinnerView1, spinnerView2;

    /*************************************From DB****************************************/
    private boolean isClosed1stServer=true;
    private boolean isClosed2ndServer=true;

    private String server1IP="192.168.63.109";
    private String server2IP="192.168.63.109";
    private int server1Port=8554;
    private int server2Port=8554;
    private String server1Path="test";
    private String server2Path="test";

    private String VideoURL1st = "rtsp://"+server1IP+":"+server1Port+"/"+server1Path;
    private String VideoURL2nd = "rtsp://"+server2IP+":"+server2Port+"/"+server2Path;
    /***********************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_video);

        imgView_warning1=(ImageView)findViewById(R.id.imgView_warning1);
        Glide.with(this).load(R.drawable.warning).into(imgView_warning1);
        imgView_warning2=(ImageView)findViewById(R.id.imgView_warning2);
        Glide.with(this).load(R.drawable.warning).into(imgView_warning2);

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
                return false;
            }
        });

        spinnerView1 = (ProgressBar) findViewById(R.id.spinnerView1);
        spinnerView2 = (ProgressBar) findViewById(R.id.spinnerView2);

        spinnerView1.setVisibility(View.VISIBLE);
        spinnerView2.setVisibility(View.VISIBLE);

        /**********웹에 요청**********/
        //new GetStreamingStatusTask().execute("StreamingStatus");
        /****************************/
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
                videoview1st.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction()==MotionEvent.ACTION_UP)
                        {
                            Intent intent=new Intent(getApplicationContext(), FullVideoActivity.class);
                            intent.putExtra("VideoURL", VideoURL1st);
                            startActivity(intent);
                        }
                        return false;
                    }
                });
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
                videoview2nd.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if(event.getAction()==MotionEvent.ACTION_UP)
                        {
                            Intent intent = new Intent(getApplicationContext(), FullVideoActivity.class);
                            intent.putExtra("VideoURL", VideoURL2nd);
                            startActivity(intent);
                        }
                        return false;
                    }
                });
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
    protected void onPause() {
        super.onPause();
        videoview1st.stopPlayback();
        videoview2nd.stopPlayback();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /*private class GetStreamingStatusTask extends AsyncTask<String, Void, JSONObject>
    {
        @Override
        protected JSONObject doInBackground(String... params) {
            JSONObject jsonObject;

            URL url=new URL("http://192.168.63.25:23280/app/parkinginfo.jsp")
            HttpURLConnection httpCon=(HttpURLConnection)url.openConnection();

            jsonObject=new JSONObject();
            jsonObject.put("")
            return returnValue;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try
            {
                isClosed1stServer=jsonObject.getBoolean("isClosed1stServer");
                if(!isClosed1stServer)
                {
                    server1IP=jsonObject.getString("server1IP");
                    server1Port=jsonObject.getInt("server1Port");
                    server1Path=jsonObject.getString("server1Path");
                    VideoURL1st = "rtsp://"+server1IP+":"+server1Port+"/"+server1Path;
                }

                isClosed2ndServer=jsonObject.getBoolean("isClosed2ndServer");
                if(!isClosed2ndServer)
                {
                    server2IP=jsonObject.getString("server2IP");
                    server2Port=jsonObject.getInt("server2Port");
                    server2Path=jsonObject.getString("server2Path");
                    VideoURL2nd = "rtsp://"+server2IP+":"+server2Port+"/"+server2Path;
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }*/
}
