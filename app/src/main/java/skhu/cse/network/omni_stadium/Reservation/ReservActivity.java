package skhu.cse.network.omni_stadium.Reservation;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import pl.polidea.view.ZoomView;
import skhu.cse.network.omni_stadium.OmniApplication;
import skhu.cse.network.omni_stadium.R;

public class ReservActivity extends AppCompatActivity {

    private Bitmap bitmap_ZoomView;
    private CustomZoomView zoomView;
    private Handler mHandler;
    private ProgressDialog mProgressDialog;
    static final int REQ_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserv_main);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.reserv_zoom, null, false);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        zoomView = new CustomZoomView(this);
        zoomView.addView(v);
        zoomView.setLayoutParams(layoutParams);
        zoomView.setMiniMapEnabled(false); // 좌측 상단 검은색 미니맵 설정안함
        zoomView.setMaxZoom(4f); // 줌 Max 배율 설정  1f 로 설정하면 줌 안됩니다.
        ConstraintLayout container = (ConstraintLayout) findViewById(R.id.container);
        container.addView(zoomView);
        Glide.with(ReservActivity.this).load(R.drawable.noun_1018844_cc).into((ImageView) findViewById(R.id.ivzoom_info));
        Glide.with(ReservActivity.this).load(R.drawable.seatimageview).into((ImageView) findViewById(R.id.ivseat));

        zoomView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int pixel = bitmap_ZoomView.getPixel((int) event.getX(), (int) event.getY());
                    int redPixel = Color.red(pixel);
                    int greenPixel = Color.green(pixel);
                    int bluePixel = Color.blue(pixel);

                    Log.v("Pixel", redPixel + ", " + greenPixel + ", " + bluePixel);
                    if (redPixel == 0 && greenPixel == 94 && bluePixel == 221) {
                        //가운데 연파랑 영역
                        Intent intent = new Intent(getApplicationContext(), DetailReservActivity.class);
                        intent.putExtra("Sector", "중앙 블루석A");
                        startActivityForResult(intent, REQ_CODE);
                        Log.v("Event Test", "가운데 연파랑 영역");
                    } else if (redPixel == 221 && greenPixel == 0 && bluePixel == 42) {
                        //아래 왼쪽 빨강 영역
                        Intent intent = new Intent(getApplicationContext(), DetailReservActivity.class);
                        intent.putExtra("Sector", "3루 레드석");
                        startActivity(intent);
                        Log.v("Event Test", "아래 왼쪽 빨강 영역");
                    } else if (redPixel == 221 && greenPixel == 1 && bluePixel == 42) {
                        //아래 오른쪽 빨강 영역
                        Intent intent = new Intent(getApplicationContext(), DetailReservActivity.class);
                        intent.putExtra("Sector", "1루 레드석");
                        startActivityForResult(intent, REQ_CODE);
                        Log.v("Event Test", "아래 오른쪽 빨강 영역");
                    } else if (redPixel == 36 && greenPixel == 41 && bluePixel == 172) {
                        //가운데 진파랑 영역
                        Intent intent = new Intent(getApplicationContext(), DetailReservActivity.class);
                        intent.putExtra("Sector", "중앙 블루석B");
                        startActivityForResult(intent, REQ_CODE);
                        Log.v("Event Test", "가운데 진파랑 영역");
                    } else if (redPixel == 36 && greenPixel == 40 && bluePixel == 83) {
                        //아래 왼쪽 남색 영역
                        Intent intent = new Intent(getApplicationContext(), DetailReservActivity.class);
                        intent.putExtra("Sector", "3루 네이비석");
                        startActivityForResult(intent, REQ_CODE);
                        Log.v("Event Test", "아래 왼쪽 남색 영역");
                    } else if (redPixel == 36 && greenPixel == 41 && bluePixel == 83) {
                        //아래 오른쪽 남색 영역
                        Intent intent = new Intent(getApplicationContext(), DetailReservActivity.class);
                        intent.putExtra("Sector", "1루 네이비석");
                        startActivityForResult(intent, REQ_CODE);
                        Log.v("Event Test", "아래 오른쪽 남색 영역");
                    } else if (redPixel == 52 && greenPixel == 150 && bluePixel == 0) {
                        //왼쪽 위 그린 영역
                        Log.v("Event Test", "왼쪽 위 그린 영역");
                        /*((OmniApplication)getApplicationContext()).setSeat_zone("3루 외야그린석");*/
                        mHandler = new Handler(); //프로그레스 다이얼로그를 위한 핸들러 생성
                        AlertDialog.Builder dlg = new AlertDialog.Builder(ReservActivity.this);
                        dlg.setTitle("");
                        dlg.setMessage("3루 외야그린석을 예매 하시겠습니까?");
                        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mProgressDialog = ProgressDialog.show(ReservActivity.this, "",
                                        "결제중입니다.", true);
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                                mProgressDialog.dismiss();
                                                Toast.makeText(ReservActivity.this, "결제 완료", Toast.LENGTH_SHORT).show();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, 1000);
                                new UnreservedSeatTask().execute("3루 외야그린석");
                            }
                        });
                        dlg.setCancelable(false);
                        dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "결제가 취소 되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dlg.show();
                    } else if (redPixel == 52 && greenPixel == 150 && bluePixel == 1) {
                        //오른쪽 위 그린 영역
                        Log.v("Event Test", "오른쪽 위 그린 영역");
                        mHandler = new Handler(); //프로그레스 다이얼로그를 위한 핸들러 생성
                        AlertDialog.Builder dlg = new AlertDialog.Builder(ReservActivity.this);
                        dlg.setTitle("");
                        dlg.setMessage("1루 외야그린석을 예매 하시겠습니까?");
                        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                              /*  mProgressDialog = ProgressDialog.show(ReservActivity.this, "",
                                        "결제중입니다.", true);
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                                                mProgressDialog.dismiss();
                                                Toast.makeText(ReservActivity.this, "결제 완료", Toast.LENGTH_SHORT).show();
                                                new UnreservedSeatTask().execute(Sector);
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }, 1000); */
                                new UnreservedSeatTask().execute("1루 외야그린석");
                            }
                        });
                        dlg.setCancelable(false);
                        dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "결제가 취소 되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        });
                        dlg.show();
                    }
                }
                return false;
            }
        });
    }

    private class CustomZoomView extends ZoomView {
        public CustomZoomView(Context context) {
            super(context);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            if (bitmap_ZoomView == null) {
                bitmap_ZoomView = Bitmap.createBitmap(zoomView.getWidth(), zoomView.getHeight(), Bitmap.Config.ARGB_8888);
                Log.v("Test", zoomView.getWidth() + ", " + zoomView.getHeight());
                Canvas myCanvas = new Canvas(bitmap_ZoomView);
                zoomView.draw(myCanvas);
            }
            return super.dispatchTouchEvent(ev);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }

    private class UnreservedSeatTask extends AsyncTask<String, Void, JSONObject> {

        private String zone;

        @Override
        protected JSONObject doInBackground(String... params) {
            URL url = null;
            HttpURLConnection httpCon = null;
            JSONObject getJSON = null;

            try {
                url = new URL("http://192.168.63.25:51223/AndroidClientTicketingRequestPost/FreeSeat");
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
                outJson.put("아이디", ((OmniApplication)getApplicationContext()).getMem_id());
                zone=params[0];
                outJson.put("구역정보", params[0]);
                OutputStream out = new BufferedOutputStream(httpCon.getOutputStream());
                out.write(outJson.toString().getBytes("UTF-8"));
                out.flush();

                int responseCode = httpCon.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpCon.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    StringBuilder result = new StringBuilder();
                    while ((line = bufferedReader.readLine()) != null)
                        result.append(line);
                    inputStream.close();
                    getJSON = new JSONObject(result.toString());
                }
            } catch (Exception e) {
            } finally {
                httpCon.disconnect();
            }
            return getJSON;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                int result = jsonObject.getInt("결과"); // 예매 성공: 0 예매 실패: else
                String msg = jsonObject.getString("메시지");
                if (result == 0) {
                    Toast.makeText(ReservActivity.this, msg, Toast.LENGTH_SHORT).show();
                    OmniApplication omniApplication=(OmniApplication)getApplicationContext();
                    omniApplication.setTicket_no(jsonObject.getInt("티켓"));
                    omniApplication.setSeat_zone(zone);
                    Log.d("app Test", omniApplication.getMem_id()+", "+omniApplication.getMem_name()+", "+omniApplication.getTicket_no()+", "+omniApplication.getSeat_zone()+", "+omniApplication.getSeat_row()+", "+omniApplication.getSeat_no());
                    finish();
                } else {
                    Toast.makeText(ReservActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_CODE){
            if(resultCode == RESULT_OK){
                finish();
            }
        }
    }
}
