package skhu.cse.network.omni_stadium.Reservation;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

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

import skhu.cse.network.omni_stadium.MainActivity;
import skhu.cse.network.omni_stadium.MenuActivity;
import skhu.cse.network.omni_stadium.OmniApplication;
import skhu.cse.network.omni_stadium.R;


public class DetailReservActivity extends AppCompatActivity {
    private String value;
    private boolean isCheckedInArr = false;
    private ToggleButton tempTB;
    private EditText seatInfo;
    private char charRow;
    private int row;
    private CharSequence chSq_seat_no;
    private int seat_no;
    private ToggleButton btArr[];
    private String mem_id;
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
        setTitle("지정석 : " + value);
        mem_id= ((OmniApplication)getApplicationContext()).getMem_id();
        btArr = new ToggleButton[50];
        for (int i = 0; i < btArr.length; ++i) {
            int resource = getResources().getIdentifier("tbG" + (i + 1), "id", "skhu.cse.network.omni_stadium");
            btArr[i] = (ToggleButton) findViewById(resource);
        }
        /*ToggleButton[][] btArray =new ToggleButton[5][10];
        int[] tbGIDArr={ R.id.tbG1, R.id.tbG2, R.id.tbG3, R.id.tbG4, R.id.tbG5, R.id.tbG6,  R.id.tbG7, R.id.tbG8, R.id.tbG9, R.id.tbG10,
                R.id.tbG11, R.id.tbG12, R.id.tbG13, R.id.tbG14, R.id.tbG15, R.id.tbG16,  R.id.tbG17, R.id.tbG18, R.id.tbG19, R.id.tbG20,
                R.id.tbG21, R.id.tbG22, R.id.tbG23, R.id.tbG24, R.id.tbG25, R.id.tbG26,  R.id.tbG27, R.id.tbG28, R.id.tbG29, R.id.tbG30,
                R.id.tbG31, R.id.tbG32, R.id.tbG33, R.id.tbG34, R.id.tbG35, R.id.tbG36,  R.id.tbG37, R.id.tbG38, R.id.tbG39, R.id.tbG40,
                R.id.tbG41, R.id.tbG42, R.id.tbG43, R.id.tbG44, R.id.tbG45, R.id.tbG46,  R.id.tbG47, R.id.tbG48, R.id.tbG49, R.id.tbG50 };
        int tbGIDArrCnt=0;
        for(int row= 0; row <btArray.length; row++)
        {
            for(int column= 0; column<btArray[row].length; column++)
            {
                btArray[row][column] = (ToggleButton)findViewById(tbGIDArr[tbGIDArrCnt++]);
                btArray[row][column].setOnClickListener(new ToggleButtonClickedListener());
            }
        }*/

        SeatOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCheckedInArr) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(DetailReservActivity.this);
                    dlg.setTitle("예매 정보");
                    dlg.setMessage("해당 좌석을 결제 하시겠습니까?");

                    dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            row = Character.getNumericValue(charRow);
                            seat_no = Integer.parseInt(chSq_seat_no.toString());
                            new TicketBuyingTask().execute(value, String.valueOf(seat_no),mem_id);
                          /*  Toast.makeText(getApplicationContext(), "결제가 완료 되었습니다.\n" + row + "열 " + seat_no + "석", Toast.LENGTH_SHORT).show();*/
                            finish();
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
                } else {
                    Toast.makeText(getApplicationContext(), "선택한 좌석이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        new ReservTask().execute(value);
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
            seatInfo.setText(charRow + "열 " + chSq_seat_no + "석");
        } else {
            tB.setTextColor(Color.parseColor("#5FBEAA"));
            isCheckedInArr = false;
            tempTB = null;
            seatInfo.setText("");
        }
    }

    private class ReservTask extends AsyncTask<String, Void, JSONArray> {

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
                        int seat_no = jsonObject.getInt("seat_no") - 1;
                        // 이미 점유된 좌석의 상태 변경
                        btArr[seat_no].setEnabled(false);
                        btArr[seat_no].setTextColor(Color.parseColor("#afaeae"));
                    }
                }
            } catch (Exception e) {

            }
        }
    }

    private class TicketBuyingTask extends AsyncTask<String, Void, JSONObject> {

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
                outJson.put("구역정보", params[0]);
                outJson.put("좌석정보", Integer.valueOf(params[1]));
                outJson.put("아이디", params[2]);
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
                    // 예매가 완료된 좌석의 상태 변경
                    btArr[seat_no].setEnabled(false);
                    btArr[seat_no].setTextColor(Color.parseColor("#afaeae"));
                    Toast.makeText(DetailReservActivity.this,"예매 되었습니다.", Toast.LENGTH_SHORT).show();
                } else {
                Toast.makeText(DetailReservActivity.this,"예매에 실패했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
            }
        }
    }
}