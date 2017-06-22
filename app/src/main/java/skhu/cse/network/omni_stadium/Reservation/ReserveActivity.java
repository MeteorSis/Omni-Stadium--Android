package skhu.cse.network.omni_stadium.Reservation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import java.util.HashMap;

import pl.polidea.view.ZoomView;
import skhu.cse.network.omni_stadium.Etc.LoadingDialog;
import skhu.cse.network.omni_stadium.OmniApplication;
import skhu.cse.network.omni_stadium.R;

public class ReserveActivity extends AppCompatActivity {

    private Bitmap bitmap_ZoomView;
    private CustomZoomView zoomView;
    private LoadingDialog lDialog;
    static final int REQ_CODE = 1;
    private HashMap<String, Integer> zonePrices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserv_main);
        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.reserv_zoom, null, false);

        new GetPriceTask().execute("가격정보");

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        zoomView = new CustomZoomView(this);
        zoomView.addView(v);
        zoomView.setLayoutParams(layoutParams);
        zoomView.setMiniMapEnabled(false); // 좌측 상단 검은색 미니맵 설정안함
        zoomView.setMaxZoom(4f); // 줌 Max 배율 설정  1f 로 설정하면 줌 안됩니다.
        ConstraintLayout container = (ConstraintLayout) findViewById(R.id.container);
        container.addView(zoomView);
        Glide.with(ReserveActivity.this).load(R.drawable.noun_1018844_cc).into((ImageView) findViewById(R.id.ivzoom_info));
        Glide.with(ReserveActivity.this).load(R.drawable.seatimageview).into((ImageView) findViewById(R.id.ivseat));

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
                        Intent intent = new Intent(getApplicationContext(), DetailReserveActivity.class);
                        intent.putExtra("Sector", "중앙 블루석A");
                        intent.putExtra("Price", zonePrices.get("중앙 블루석A"));
                        startActivityForResult(intent, REQ_CODE);
                        Log.v("Event Test", "가운데 연파랑 영역");
                    } else if (redPixel == 221 && greenPixel == 0 && bluePixel == 42) {
                        //아래 왼쪽 빨강 영역
                        Intent intent = new Intent(getApplicationContext(), DetailReserveActivity.class);
                        intent.putExtra("Sector", "3루 레드석");
                        intent.putExtra("Price", zonePrices.get("3루 레드석"));
                        startActivityForResult(intent, REQ_CODE);
                        Log.v("Event Test", "아래 왼쪽 빨강 영역");
                    } else if (redPixel == 221 && greenPixel == 1 && bluePixel == 42) {
                        //아래 오른쪽 빨강 영역
                        Intent intent = new Intent(getApplicationContext(), DetailReserveActivity.class);
                        intent.putExtra("Sector", "1루 레드석");
                        intent.putExtra("Price", zonePrices.get("1루 레드석"));
                        startActivityForResult(intent, REQ_CODE);
                        Log.v("Event Test", "아래 오른쪽 빨강 영역");
                    } else if (redPixel == 36 && greenPixel == 41 && bluePixel == 172) {
                        //가운데 진파랑 영역
                        Intent intent = new Intent(getApplicationContext(), DetailReserveActivity.class);
                        intent.putExtra("Sector", "중앙 블루석B");
                        intent.putExtra("Price", zonePrices.get("중앙 블루석B"));
                        startActivityForResult(intent, REQ_CODE);
                        Log.v("Event Test", "가운데 진파랑 영역");
                    } else if (redPixel == 36 && greenPixel == 40 && bluePixel == 83) {
                        //아래 왼쪽 남색 영역
                        Intent intent = new Intent(getApplicationContext(), DetailReserveActivity.class);
                        intent.putExtra("Sector", "3루 네이비석");
                        intent.putExtra("Price", zonePrices.get("3루 네이비석"));
                        startActivityForResult(intent, REQ_CODE);
                        Log.v("Event Test", "아래 왼쪽 남색 영역");
                    } else if (redPixel == 36 && greenPixel == 41 && bluePixel == 83) {
                        //아래 오른쪽 남색 영역
                        Intent intent = new Intent(getApplicationContext(), DetailReserveActivity.class);
                        intent.putExtra("Sector", "1루 네이비석");
                        intent.putExtra("Price", zonePrices.get("1루 네이비석"));
                        startActivityForResult(intent, REQ_CODE);
                        Log.v("Event Test", "아래 오른쪽 남색 영역");
                    } else if (redPixel == 52 && greenPixel == 150 && bluePixel == 0) {
                        //왼쪽 위 그린 영역
                        Log.v("Event Test", "왼쪽 위 그린 영역");
                        if(((OmniApplication)getApplicationContext()).getTicket_no()==null)
                        {
                            AlertDialog.Builder dlg = new AlertDialog.Builder(ReserveActivity.this);
                            dlg.setTitle("");
                            dlg.setMessage("3루 외야그린석을 결제하시겠습니까?\n   가격 : " + zonePrices.get("3루 외야그린석") + "원");
                            dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    lDialog = new LoadingDialog(ReserveActivity.this, "결제중...");
                                    lDialog.show();
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
                        }
                        else
                        {
                            Toast.makeText(ReserveActivity.this, "구입한 티켓이 이미 있습니다.", Toast.LENGTH_SHORT).show();
                        }
                    } else if (redPixel == 52 && greenPixel == 150 && bluePixel == 1) {
                        //오른쪽 위 그린 영역
                        Log.v("Event Test", "오른쪽 위 그린 영역");
                        if(((OmniApplication)getApplicationContext()).getTicket_no()==null)
                        {
                            AlertDialog.Builder dlg = new AlertDialog.Builder(ReserveActivity.this);
                            dlg.setTitle("");
                            dlg.setMessage("1루 외야그린석을 결제 하시겠습니까?\n  가격 : "+zonePrices.get("1루 외야그린석")+"원");
                            dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    lDialog = new LoadingDialog(ReserveActivity.this, "결제중...");
                                    lDialog.show();
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
                        else
                        {
                            Toast.makeText(ReserveActivity.this, "구입한 티켓이 이미 있습니다.", Toast.LENGTH_SHORT).show();
                        }
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
        //NavUtils.navigateUpFromSameTask(this);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetPriceTask extends AsyncTask<String, Void, JSONObject>
    {
        @Override
        protected JSONObject doInBackground(String... params) {
            URL url = null;
            HttpURLConnection httpCon = null;
            JSONObject getJSON = null;

            try {
                url = new URL("http://192.168.63.25:51223/AndroidClientTicketingRequestPost/GetPrice");
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
                e.printStackTrace();
            } finally {
                httpCon.disconnect();
            }
            return getJSON;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                if(jsonObject!=null)
                {
                    Log.v("price test", jsonObject.toString());
                    zonePrices = new HashMap<>();
                    zonePrices.put("1루 네이비석", jsonObject.getInt("1루 네이비석"));
                    zonePrices.put("1루 레드석", jsonObject.getInt("1루 레드석"));
                    zonePrices.put("1루 외야그린석", jsonObject.getInt("1루 외야그린석"));
                    zonePrices.put("3루 네이비석", jsonObject.getInt("3루 네이비석"));
                    zonePrices.put("3루 레드석", jsonObject.getInt("3루 레드석"));
                    zonePrices.put("3루 외야그린석", jsonObject.getInt("3루 외야그린석"));
                    zonePrices.put("중앙 블루석A", jsonObject.getInt("중앙 블루석A"));
                    zonePrices.put("중앙 블루석B", jsonObject.getInt("중앙 블루석B"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return getJSON;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try {
                lDialog.dismiss();

                int result = jsonObject.getInt("결과"); // 예매 성공: 0 예매 실패: else
                String msg = jsonObject.getString("메시지");
                if (result == 0) {
                    Toast.makeText(ReserveActivity.this, msg, Toast.LENGTH_SHORT).show();
                    OmniApplication omniApplication=(OmniApplication)getApplicationContext();
                    omniApplication.setTicket_no(jsonObject.getInt("티켓"));
                    omniApplication.setSeat_zone(zone);
                    Log.d("app Test", omniApplication.getMem_id()+", "+omniApplication.getMem_name()+", "+omniApplication.getTicket_no()+", "+omniApplication.getSeat_zone()+", "+omniApplication.getSeat_row()+", "+omniApplication.getSeat_no());
                    finish();
                } else {
                    Toast.makeText(ReserveActivity.this, msg, Toast.LENGTH_SHORT).show();
                    finish();
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
