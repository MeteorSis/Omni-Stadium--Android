package skhu.cse.network.omni_stadium.Streaming;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import skhu.cse.network.omni_stadium.MenuActivity;
import skhu.cse.network.omni_stadium.R;

public class MultiVideoActivity extends AppCompatActivity {

    private ImageView imgView_warning1, imgView_warning2;
    private VideoView videoview1st, videoview2nd;
    private ProgressBar spinnerView1, spinnerView2;

    /*************************************From DB****************************************/
    /*private boolean isClosed1stServer=true;
    private boolean isClosed2ndServer=true;

    private String server1IP="192.168.63.109";
    private String server2IP="192.168.63.109";
    private int server1Port=8554;
    private int server2Port=8554;
    private String server1Path="test";
    private String server2Path="test";

    private String VideoURL1st = "rtsp://"+server1IP+":"+server1Port+"/"+server1Path;
    private String VideoURL2nd = "rtsp://"+server2IP+":"+server2Port+"/"+server2Path;*/
    /***********************************************************************************/
    private boolean isClosed1stServer;
    private boolean isClosed2ndServer;

    private String server1IP;
    private String server2IP;
    private int server1Port;
    private int server2Port;
    private String server1Path;
    private String server2Path;

    private String VideoURL1st;
    private String VideoURL2nd;

    private TextView tvCam1, tvCam2;

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
                Toast.makeText(MultiVideoActivity.this, "서버가 불안정합니다. 스트리밍 기능을 종료합니다.", Toast.LENGTH_SHORT).show();
                MultiVideoActivity.this.finish();
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
                Toast.makeText(MultiVideoActivity.this, "서버가 불안정합니다. 스트리밍 기능을 종료합니다.", Toast.LENGTH_SHORT).show();
                MultiVideoActivity.this.finish();
                return false;
            }
        });

        spinnerView1 = (ProgressBar) findViewById(R.id.spinnerView1);
        spinnerView2 = (ProgressBar) findViewById(R.id.spinnerView2);

        tvCam1=(TextView)findViewById(R.id.tvCam1);
        tvCam2=(TextView)findViewById(R.id.tvCam2);

        spinnerView1.setVisibility(View.VISIBLE);
        spinnerView2.setVisibility(View.VISIBLE);

        //AsyncTask Start
        new GetStreamingStatusTask().execute("영상요청");
        Glide.with(MultiVideoActivity.this).load(R.drawable.videoframe).into((ImageView)findViewById(R.id.videoFrame));
    }

    @Override
    protected void onResume() {
        super.onResume();
        spinnerView1.setVisibility(View.VISIBLE);
        spinnerView2.setVisibility(View.VISIBLE);

        tvCam1.setVisibility(View.INVISIBLE);
        tvCam2.setVisibility(View.INVISIBLE);

        videoview1st.setAlpha(0);
        videoview2nd.setAlpha(0);
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoview1st.stopPlayback();
        videoview2nd.stopPlayback();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    private class GetStreamingStatusTask extends AsyncTask<String, Void, JSONArray>
    {
        @Override
        protected JSONArray doInBackground(String... params) {
            URL url = null;
            HttpURLConnection httpCon = null;
            JSONArray getJSONArr = null;

            try {
                url = new URL("http://192.168.63.25:51223/AndroidClientStreamingRequestPost");
                httpCon = (HttpURLConnection) url.openConnection();

                httpCon.setRequestMethod("POST");
                httpCon.setDoInput(true);
                httpCon.setDoOutput(true);
                httpCon.setConnectTimeout(2000);
                httpCon.setReadTimeout(2000);

                httpCon.setRequestProperty("Cache-Control", "no-cache");
                //서버에 요청할 Response Data Type
                httpCon.setRequestProperty("Accept", "application/json");
                //서버에 전송할 Data Type
                //httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpCon.setRequestProperty("Content-Type", "application/json");

                JSONObject outJson = new JSONObject();
                outJson.put(params[0], params[0]);

                OutputStream out = new BufferedOutputStream(httpCon.getOutputStream());
                out.write(outJson.toString().getBytes("UTF-8"));
                out.flush();

                int responseCode = httpCon.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream=httpCon.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    StringBuilder result = new StringBuilder();
                    while((line = bufferedReader.readLine()) != null)
                        result.append(line);
                    inputStream.close();
                    getJSONArr = new JSONArray(result.toString());
                }
            } catch (Exception e) {
            } finally {
                httpCon.disconnect();
            }
            return getJSONArr;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            if(jsonArray!=null)
            {
                try {
                    isClosed1stServer = jsonArray.getJSONObject(0).getBoolean("상태");
                    if (!isClosed1stServer) {
                        server1IP = jsonArray.getJSONObject(0).getString("서버");
                        server1Port = jsonArray.getJSONObject(0).getInt("포트");
                        server1Path = jsonArray.getJSONObject(0).getString("경로");
                        VideoURL1st = "rtsp://" + server1IP + ":" + server1Port + "/" + server1Path;
                    }

                    isClosed2ndServer = jsonArray.getJSONObject(1).getBoolean("상태");
                    if (!isClosed2ndServer) {
                        server2IP = jsonArray.getJSONObject(1).getString("서버");
                        server2Port = jsonArray.getJSONObject(1).getInt("포트");
                        server2Path = jsonArray.getJSONObject(1).getString("경로");
                        VideoURL2nd = "rtsp://" + server2IP + ":" + server2Port + "/" + server2Path;
                    }
                    Log.v("Test1", isClosed1stServer + ", " + server1IP + ", " + server1Port + ", " + server1Path + ", " + VideoURL1st);
                    Log.v("Test2", isClosed2ndServer + ", " + server2IP + ", " + server2Port + ", " + server2Path + ", " + VideoURL2nd);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                isClosed1stServer=true;
                isClosed2ndServer=true;
            }

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
                            return true;
                        }
                    });
                    videoview1st.setVideoURI(videoUri1st);
                }
                else
                {
                    spinnerView1.setVisibility(View.INVISIBLE);
                    Glide.with(MultiVideoActivity.this).load(R.drawable.warning).into(imgView_warning1);
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
                            return true;
                        }
                    });
                    videoview2nd.setVideoURI(videoUri2nd);
                }
                else
                {
                    spinnerView2.setVisibility(View.INVISIBLE);
                    Glide.with(MultiVideoActivity.this).load(R.drawable.warning).into(imgView_warning2);
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
                    tvCam1.setVisibility(View.VISIBLE);
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
                    tvCam2.setVisibility(View.VISIBLE);
                    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }
}
