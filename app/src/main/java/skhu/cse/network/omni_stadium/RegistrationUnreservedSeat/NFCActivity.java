package skhu.cse.network.omni_stadium.RegistrationUnreservedSeat;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import skhu.cse.network.omni_stadium.R;


public class NFCActivity extends AppCompatActivity {

    private static final String TAG = "NFCActivity";
    private boolean mResumed = false;
    NfcAdapter mNfcAdapter;//실제 NFC 하드웨어와의 다리 역할을 한다.
    EditText mNote;

    PendingIntent mNfcPendingIntent;
    IntentFilter[] mNdefExchangeFilters;

    /* -----------------------------------UI----------------------------------- */
    ViewPager viewPager;
    TextView tab_first;
    TextView tab_second;
    /* -----------------------------------UI----------------------------------- */

    String body = null;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc_main);

        Glide.with(this).load(R.drawable.nfctag).into((ImageView)findViewById(R.id.ivNFC));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mNote = ((EditText) findViewById(R.id.note));
        mNote.addTextChangedListener(mTextWatcher);

        /* -----------------------------------UI----------------------------------- */
        viewPager = (ViewPager)findViewById(R.id.vp);

        tab_first = (TextView)findViewById(R.id.tab_first);
        tab_second = (TextView)findViewById(R.id.tab_second);

        MyPagerAdapter adapter = new MyPagerAdapter();
        viewPager.setAdapter(adapter);

        tab_first.setOnClickListener(movePageListener);
        tab_second.setOnClickListener(movePageListener);

        tab_first.setSelected(true);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position)
            {
                switch (position){
                    case 0:
                        tab_first.setSelected(true);
                        tab_second.setSelected(false);
                        break;
                    case 1:
                        tab_first.setSelected(false);
                        tab_second.setSelected(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });
        /* -----------------------------------UI----------------------------------- */

        //이 액티비티에서 수신된 모든 NFC 인텐트를 처리
        mNfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        //태그로부터 텍스트를 읽거나 p2p를 통하여 교환할 때 필요한 인텐트 필터
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefDetected.addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) { }
        mNdefExchangeFilters = new IntentFilter[] { ndefDetected };
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mNfcAdapter.isEnabled() != true) {//NFC 기능이 활성화 되어 있는지 검사한다.
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
            byte[] payload = messages[0].getRecords()[0].getPayload();
            setNoteBody(new String(payload));//화면에 표시한다.
            setIntent(new Intent());//이 인텐트를 삭제한다.
        }
        enableNdefExchangeMode();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mResumed = false;
        //mNfcAdapter.disableForegroundNdefPush(this); deprecated
        mNfcAdapter.disableForegroundDispatch(NFCActivity.this);
    }

    @Override
    protected void onNewIntent(Intent intent) {//액티비티가 인텐트를 받으면 모드를 봐서 읽거나 쓴다.
        //NDEF 교환 모드
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            NdefMessage[] msgs = getNdefMessages(intent);
            promptForContent(msgs[0]);
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
            if (mResumed) {
                //mNfcAdapter.enableForegroundNdefPush(NFCActivity.this, getNoteAsNdef()); deprecated
                mNfcAdapter.setNdefPushMessage(getNoteAsNdef(),NFCActivity.this);
            }
        }
    };

    private void promptForContent(final NdefMessage msg) {
        if( body != null) {
            new AlertDialog.Builder(this).setTitle("현재 좌석을 이 좌석으로 바꾸시겠습니까?")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            body = new String(msg.getRecords()[0].getPayload());

                            JSONObject objBody = null;
                            try {
                                objBody = new JSONObject(body);
                            } catch (JSONException e) {

                            }
                            try {
                                setNoteBody("고객님의 좌석\n구역: " + objBody.getString("zone") + "\n열: " + objBody.getString("row") + "\n좌석 번호: " + objBody.getString("seat_no"));
                            } catch (JSONException e) {

                            }
                            //setNoteBody(body); 원본
                            Log.d("test2", body);
                            toast("좌석 변경이 완료되었습니다.");
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    }).show();
        }
        else {
            new AlertDialog.Builder(this).setTitle("이 좌석으로 등록 하시겠습니까?")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            body = new String(msg.getRecords()[0].getPayload());

                            JSONObject objBody = null;
                            try {
                                objBody = new JSONObject(body);
                            } catch (JSONException e) {

                            }
                            try {
                                setNoteBody("고객님의 좌석\n구역: " + objBody.getString("zone") + "\n열: " + objBody.getString("row") + "\n좌석 번호: " + objBody.getString("seat_no"));
                            } catch (JSONException e) {

                            }
                            //setNoteBody(body); 원본
                            Log.d("test3", body);
                            toast("새로운 좌석이 등록되었습니다.");
                        }
                    })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                        }
                    }).show();
        }
    }

    private void setNoteBody(String body) {
        Editable text = mNote.getText();
        text.clear();
        text.append(body);
    }

    private NdefMessage getNoteAsNdef() {//NDEF 메시지로 변환한다.
        byte[] textBytes = mNote.getText().toString().getBytes();
        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/plain".getBytes(),
                new byte[] {}, textBytes);
        return new NdefMessage(new NdefRecord[] {
                textRecord
        });
    }

    NdefMessage[] getNdefMessages(Intent intent) {//인텐트에서 NDEF 메시지를 추출한다.
        //인텐트를 파싱한다.
        NdefMessage[] msgs = null;
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action) || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                    Log.d("test1", msgs[i].toString());
                }
            } else {
                //알 수 없는 태그 타입
                byte[] empty = new byte[] {};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[] {
                        record
                });
                msgs = new NdefMessage[] {
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

    private void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }


    /* -----------------------------------UI----------------------------------- */
    View.OnClickListener movePageListener = new View.OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch(v.getId()){
                case R.id.tab_first:
                    tab_first.setSelected(true);
                    tab_second.setSelected(false);
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.tab_second:
                    tab_first.setSelected(false);
                    tab_second.setSelected(true);
                    viewPager.setCurrentItem(1);
                    break;
            }

        }
    };
    class MyPagerAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            int resId = 0;
            switch(position)
            {
                case 0:
                    resId = R.id.page_one;
                    break;
                case 1:
                    resId = R.id.page_two;
                    break;
            }
            return findViewById(resId);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
    /* -----------------------------------UI----------------------------------- */
}