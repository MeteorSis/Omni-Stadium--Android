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
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import skhu.cse.network.omni_stadium.R;


public class NFCActivity extends AppCompatActivity {

    private static final String TAG = "NFCActivity";
    private boolean mResumed = false;
    private boolean mWriteMode = false;
    NfcAdapter mNfcAdapter;
    EditText mNote;

    PendingIntent mNfcPendingIntent;
    IntentFilter[] mWriteTagFilters;
    IntentFilter[] mNdefExchangeFilters;

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

        // Handle all of our received NFC intents in this activity.
        mNfcPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        // Intent filters for reading a note from a tag or exchanging over p2p.
        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefDetected.addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) { }
        mNdefExchangeFilters = new IntentFilter[] { ndefDetected };

        // Intent filters for writing to a tag
        IntentFilter tagDetected = new IntentFilter(NfcAdapter.ACTION_TAG_DISCOVERED);
        mWriteTagFilters = new IntentFilter[] { tagDetected };
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mNfcAdapter.isEnabled() != true) {
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
        // Sticky notes received from Android
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())) {
            NdefMessage[] messages = getNdefMessages(getIntent());
            byte[] payload = messages[0].getRecords()[0].getPayload();
            setNoteBody(new String(payload));
            setIntent(new Intent()); // Consume this intent.
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
    protected void onNewIntent(Intent intent) {
        // NDEF exchange mode
        if (!mWriteMode && NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
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
                            setNoteBody("현재 좌석: "+body);
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
                            setNoteBody("현재 좌석: "+body);
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

    private NdefMessage getNoteAsNdef() {
        byte[] textBytes = mNote.getText().toString().getBytes();
        NdefRecord textRecord = new NdefRecord(NdefRecord.TNF_MIME_MEDIA, "text/plain".getBytes(),
                new byte[] {}, textBytes);
        return new NdefMessage(new NdefRecord[] {
                textRecord
        });
    }

    NdefMessage[] getNdefMessages(Intent intent) {
        // Parse the intent
        NdefMessage[] msgs = null;
        String action = intent.getAction();
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }
            } else {
                // Unknown tag type
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
}