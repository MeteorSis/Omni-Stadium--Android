package skhu.cse.network.omni_stadium.RegistrationUnreservedSeat;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
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

import skhu.cse.network.omni_stadium.OmniApplication;
import skhu.cse.network.omni_stadium.R;

public class NFCActivity extends AppCompatActivity {

    private static final String TAG = "NFCActivity";
    private boolean mResumed = false;
    private NfcAdapter mNfcAdapter;//실제 NFC 하드웨어와의 다리 역할을 한다.
    private EditText mNote;

    private PendingIntent mNfcPendingIntent;
    private IntentFilter[] mNdefExchangeFilters;

    private String body = null;//NFC 태그에서 읽어 들인 string을 저장하는 변수
    private JSONObject jsonBody = null;
    private boolean isNewMode = false;//자유석 좌석 신규등록 모드

    //좌석 현황 갱신에 필요한 변수
    private String zone;
    private ToggleButton btArr[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc_main);

        zone = ((OmniApplication)getApplicationContext()).getSeat_zone();

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mNote = ((EditText) findViewById(R.id.note));
        mNote.addTextChangedListener(mTextWatcher);

        TextView tvZone = (TextView)findViewById(R.id.tvNFCMain);
        tvZone.setText(zone);

        //이 액티비티에서 수신된 모든 NFC 인텐트를 처리
        mNfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        //태그로부터 텍스트를 읽거나 P2P를 통하여 교환할 때 필요한 인텐트 필터
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefDetected.addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {

        }
        mNdefExchangeFilters = new IntentFilter[]{ndefDetected};

        //좌석 갱신
        btArr = new ToggleButton[50];
        for (int i = 0; i < btArr.length; ++i) {
            int resource = getResources().getIdentifier("tbG" + (i + 1), "id", "skhu.cse.network.omni_stadium");
            btArr[i] = (ToggleButton) findViewById(resource);
        }
        new UpdateSeatsTask().execute(zone);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //NFC 활성화 검사
        if (mNfcAdapter.isEnabled() != true) {//NFC 기능이 비활성화 되어 있으면
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("설정에서 NFC를 켜주세요.")
                    .setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    //API 17 부터 NFC 설정 환경이 변경됨
                                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN)
                                        startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
                                    else
                                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                                }
                            })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setCancelable(false)
                    .show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mResumed = true;

        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {//이 인텐트가 시작된 것이 NFC 인텐트 때문이라면
            NdefMessage[] messages = getNdefMessages(getIntent());//인텐트에서 텍스트를 꺼내서
            byte[] payload = messages[0].getRecords()[0].getPayload();//payload에 실제 데이터가 저장된다.
            setNoteBody(new String(payload));//화면에 표시한다.
            setIntent(new Intent());//이 인텐트를 삭제한다.
        }
        enableNdefExchangeMode();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mResumed = false;
        mNfcAdapter.disableForegroundDispatch(NFCActivity.this);
    }

    @Override
    protected void onNewIntent(Intent intent) {//액티비티가 인텐트를 받으면 모드를 봐서 읽거나 쓴다.
        //NDEF 교환 모드
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            NdefMessage[] msgs = getNdefMessages(intent);//nfc2
            promptForContent(msgs[0]);//nfc3
        }
    }

    private TextWatcher mTextWatcher = new TextWatcher() {

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

        }

        @Override
        public void afterTextChanged(Editable arg0) {
            if (mResumed)
                mNfcAdapter.setNdefPushMessage(getNoteAsNdef(), NFCActivity.this);
        }
    };

    private void promptForContent(final NdefMessage msg) {//nfc4
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if (((OmniApplication) getApplicationContext()).getSeat_no() == null) {//자유석 티켓은 있으나 좌석 등록이 되어 있지 않으면(신규 등록이면)
            isNewMode = true;//모드 변경
            builder.setTitle("이 좌석으로 등록 하시겠습니까?");
        } else {
            isNewMode = false;//모드 변경
            builder.setTitle("현재 좌석을 이 좌석으로 바꾸시겠습니까?");
        }

        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                body = new String(msg.getRecords()[0].getPayload());
                try {
                    jsonBody = new JSONObject(body);

                    //티켓 정보와 NFC 태그에서 읽은 정보 비교
                    if (!(isNewMode) && ((OmniApplication) getApplicationContext()).getSeat_zone().equals(jsonBody.getString("zone")) && ((OmniApplication) getApplicationContext()).getSeat_no() == jsonBody.getInt("seat_no")) {
                        toast("동일한 좌석입니다.");
                        finish();
                    }
                    //웹과 연결
                    else {
                        try {
                            new NFCTask(NFCActivity.this).execute(jsonBody.getString("zone"), String.valueOf(jsonBody.getInt("seat_id")), ((OmniApplication) getApplicationContext()).getMem_id());//좌석 등록 요청
                        } catch (JSONException e) {

                        }
                    }
                } catch (JSONException e) {

                }
            }
        })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                })
                .show();
    }

    private void setNoteBody(String body) {//필요 없어질 예정
        Editable text = mNote.getText();
        text.clear();
        text.append(body);
    }

    private NdefMessage getNoteAsNdef() {//NDEF 메시지로 변환한다.
        byte[] textBytes = mNote.getText().toString().getBytes();
        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/plain".getBytes(),
                new byte[]{}, textBytes);
        return new NdefMessage(new NdefRecord[]{
                textRecord
        });
    }

    NdefMessage[] getNdefMessages(Intent intent) {//인텐트에서 NDEF 메시지를 추출한다. nfc1
        //인텐트를 파싱한다.
        NdefMessage[] msgs = null;
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                //알 수 없는 태그 타입
                byte[] empty = new byte[]{};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[]{
                        record
                });
                msgs = new NdefMessage[]{
                        msg
                };
            }
        } else {
            Log.d(TAG, "Unknown intent.");
            finish();
        }
        return msgs;
    }

    private void enableNdefExchangeMode() {
        mNfcAdapter.setNdefPushMessage(getNoteAsNdef(), NFCActivity.this);
        mNfcAdapter.enableForegroundDispatch(this, mNfcPendingIntent, mNdefExchangeFilters, null);
    }

    //자유석 등록
    private class NFCTask extends AsyncTask<String, Void, JSONObject> {

        private AppCompatActivity activity;

        public NFCTask(AppCompatActivity activity) {
            this.activity = activity;
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            URL url = null;
            HttpURLConnection httpCon = null;
            JSONObject getJSON = null;

            try {
                url = new URL("http://192.168.63.25:51223/AndroidClientTicketingRequestPost/FreeSeatPick");
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
        protected void onPostExecute(JSONObject jsonObject) {//Runs on the UI thread after doInBackground()
            super.onPostExecute(jsonObject);
            try {
                int result = jsonObject.getInt("결과");//0:좌석등록성공 else:등록된좌석
                String msg = jsonObject.getString("메시지");
                if (result == 0) {//좌석이 비어있어서 좌석이 등록됨
                    //setNoteBody(msg);
                    setNoteBody("고객님의 좌석\n구역: " + jsonBody.getString("zone") + "\n열: " + jsonBody.getInt("row") + "\n좌석 번호: " + jsonBody.getInt("seat_no"));
                    ((OmniApplication) getApplicationContext()).setSeat_no(jsonBody.getInt("seat_no"));
                    ((OmniApplication) getApplicationContext()).setSeat_row(jsonBody.getInt("row"));
                    ((OmniApplication) getApplicationContext()).setSeat_zone(jsonBody.getString("zone"));
                    toast(msg);
                    Log.d("body", body);
                } else {
                    toast(msg);
                    finish();
                }
            } catch (Exception e) {

            }
        }
    }

    //좌석 갱신
    private class UpdateSeatsTask extends AsyncTask<String, Void, JSONArray> {

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
                        //이미 점유된 좌석의 상태 변경
                        btArr[seat_no].setEnabled(false);
                        btArr[seat_no].setTextColor(Color.parseColor("#afaeae"));
                    }
                }
            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }
}