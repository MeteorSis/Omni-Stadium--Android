package skhu.cse.network.omni_stadium.Reservation;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import skhu.cse.network.omni_stadium.Etc.LoadingDialog;
import skhu.cse.network.omni_stadium.OmniApplication;
import skhu.cse.network.omni_stadium.R;


public class DetailReserveActivity extends AppCompatActivity {
    private String value;
    private int price;
    private boolean isCheckedInArr = false;
    private ToggleButton tempTB;
    private EditText seatInfo;
    private char charRow;
    private int row;
    private CharSequence chSq_seat_no;
    private int seat_no;
    private ToggleButton btArr[];
    private String mem_id;
    private LoadingDialog lDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserv_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button SeatOk = (Button) findViewById(R.id.btSeatOk);
        seatInfo = (EditText) findViewById(R.id.etSeatInfo);
        seatInfo.setFocusableInTouchMode(false); // EditText를 읽기전용으로 만듦
        Intent intent = getIntent();
        value = intent.getStringExtra("Sector");
        price=intent.getIntExtra("Price", 0);

        setTitle("지정석 : " + value);
        mem_id= ((OmniApplication)getApplicationContext()).getMem_id();
        btArr = new ToggleButton[50];
        for (int i = 0; i < btArr.length; ++i) {
            int resource = getResources().getIdentifier("tbG" + (i + 1), "id", "skhu.cse.network.omni_stadium");
            btArr[i] = (ToggleButton) findViewById(resource);
        }
        
        SeatOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCheckedInArr) {
                    if(((OmniApplication)getApplicationContext()).getTicket_no()==null)
                    {
                        AlertDialog.Builder dlg = new AlertDialog.Builder(DetailReserveActivity.this);
                        dlg.setTitle("예매 정보");
                        dlg.setMessage("해당 좌석을 결제 하시겠습니까?\n   가격 : " + price + "원");

                        dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                row = Character.getNumericValue(charRow);
                                seat_no= Integer.parseInt((chSq_seat_no).toString());
                                lDialog = new LoadingDialog(DetailReserveActivity.this, "결제중...");
                                lDialog.show();
                                new TicketBuyingTask().execute(mem_id, value, String.valueOf(row), String.valueOf(seat_no));
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
                        dlg.setCancelable(false); // 백버튼 비활성화
                    }
                    else
                    {
                        Toast.makeText(DetailReserveActivity.this, "구입한 티켓이 이미 있습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "선택한 좌석이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        new ReserveTask().execute(value);
    }

    public void onToggleClicked(View v) {
        ToggleButton tB = (ToggleButton) v;
        boolean on = tB.isChecked();

        if (on) {
            if (isCheckedInArr) {
                tempTB.toggle();
                tempTB.setTextColor(Color.parseColor("#5FBEAA"));
                tempTB = tB;
            } else {
                isCheckedInArr = true;
                tempTB = tB;
            }
            tB.setTextColor(Color.parseColor("#FFFFFF"));
            chSq_seat_no = tB.getText();
            charRow = chSq_seat_no.charAt(0);
            if (chSq_seat_no.charAt(1) != '0')
                charRow++;
            seatInfo.setText(charRow + "열 " + chSq_seat_no + "석\n   가격 : " + price + "원");
        } else {
            tB.setTextColor(Color.parseColor("#5FBEAA"));
            isCheckedInArr = false;
            tempTB = null;
            seatInfo.setText("");
        }
    }

    private class ReserveTask extends AsyncTask<String, Void, JSONArray> {                           //지정석 좌석 현황 DB연동

        @Override
        protected JSONArray doInBackground(String... params) {
            URL url = null;
            HttpURLConnection httpCon = null;
            JSONArray getJSONArr = null;

            try {
                url = new URL("http://192.168.63.25:51223/AndroidClientTicketingRequestPost/OccupiedSeats");
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
            try {
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); ++i) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        int seat_no = jsonObject.getInt("seat_no")-1;
                        // 이미 점유된 좌석의 상태 변경
                        btArr[seat_no].setEnabled(false);
                        btArr[seat_no].setTextColor(Color.parseColor("#afaeae"));
                    }
                }
            } catch (Exception e) {

            }
        }
    }

    private class TicketBuyingTask extends AsyncTask<String, Void, JSONObject> {        //앱에서 지정석 구매 DB연동

        private String zone;
        private int seat_row, seat_no;

        @Override
        protected JSONObject doInBackground(String... params) {
            URL url = null;
            HttpURLConnection httpCon = null;
            JSONObject getJSON = null;

            try {
                url = new URL("http://192.168.63.25:51223/AndroidClientTicketingRequestPost/Buying");
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
                outJson.put("아이디", params[0]);
                zone=params[1];
                outJson.put("구역정보", params[1]);
                seat_row=Integer.valueOf(params[2]);
                seat_no=Integer.valueOf(params[3]);
                outJson.put("좌석정보", seat_no);
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
                    OmniApplication omniApplication=(OmniApplication)getApplicationContext();
                    omniApplication.setTicket_no(jsonObject.getInt("티켓"));
                    omniApplication.setSeat_zone(zone);
                    omniApplication.setSeat_row(seat_row);
                    omniApplication.setSeat_no(seat_no);
                    Log.d("app Test", omniApplication.getMem_id()+", "+omniApplication.getMem_name()+", "+omniApplication.getTicket_no()+", "+omniApplication.getSeat_zone()+", "+omniApplication.getSeat_row()+", "+omniApplication.getSeat_no());
                    // 예매가 완료된 좌석의 상태 변경
                    btArr[seat_no-1].setEnabled(false);
                    btArr[seat_no-1].setTextColor(Color.parseColor("#afaeae"));
                    Toast.makeText(DetailReserveActivity.this,msg, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(DetailReserveActivity.this, msg , Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            } catch (Exception e) {
            }
        }
    }
}