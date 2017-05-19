package skhu.cse.network.omni_stadium;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpwActivity extends AppCompatActivity{
    private EditText etID, etName,
            etPhoneFront, etPhoneMiddle, etPhoneBack,
            etBirthYYYY, etBirthMM, etBirthDD;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spw_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button btOK = (Button)findViewById(R.id.btSpwOK);

        etID = (EditText)findViewById(R.id.etID);
        etName = (EditText)findViewById(R.id.etName);
        etPhoneFront = (EditText)findViewById(R.id.etPhoneFront);
        etPhoneMiddle = (EditText)findViewById(R.id.etPhoneMiddle);
        etPhoneBack = (EditText)findViewById(R.id.etPhoneBack);
        etBirthYYYY = (EditText)findViewById(R.id.etBirthYYYY);
        etBirthMM = (EditText)findViewById(R.id.etBirthMM);
        etBirthDD = (EditText)findViewById(R.id.etBirthDD);

        etBirthDD.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                patternCheck();
                return true;
            }
        });

        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                patternCheck();
            }
        });
    }

    private void patternCheck() {
        String IdData=etID.getText().toString();
        String NameData=etName.getText().toString();
        String PhoneFrontData=etPhoneFront.getText().toString();
        String PhoneMiddleData=etPhoneMiddle.getText().toString();
        String PhoneBackData=etPhoneBack.getText().toString();
        String BirthYYYYData=etBirthYYYY.getText().toString();
        String BirthMMData=etBirthMM.getText().toString();
        String BirthDDData=etBirthDD.getText().toString();
        String Empty = "";

        if(IdData.matches("")){
            Empty = Empty + "아이디 ";
        }
        else{
            String Idregex = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$";
            Pattern IDpattern = Pattern.compile(Idregex);
            Matcher IDmatcher = IDpattern.matcher(IdData);
            if(!IDmatcher.find()) {
                Empty = Empty + "아이디 ";
            }
        }
        if(NameData.matches("")){
            Empty = Empty + "이름 ";
        }
        else{
            String Nameregex = "^[가-힣]{2,4}$";
            Pattern Namepattern = Pattern.compile(Nameregex);
            Matcher Namematcher = Namepattern.matcher(NameData);
            if (!Namematcher.find()){
                Empty = Empty + "이름 ";
            }
        }
        if(PhoneFrontData.matches("") || PhoneMiddleData.matches("") || PhoneBackData.matches("")){
            Empty = Empty + "핸드폰 번호 ";
        }
        else{
            String PhoneFrontregex = "^01[016789]$";
            String PhoneMiddleregex="^[0-9]{3,4}$";
            String PhoneBackregex="^[0-9]{3,4}$";
            Pattern PhoneFrontpattern = Pattern.compile(PhoneFrontregex);
            Pattern PhoneMiddlepattern = Pattern.compile(PhoneMiddleregex);
            Pattern PhoneBackpattern = Pattern.compile(PhoneBackregex);
            Matcher PhoneFrontmatcher = PhoneFrontpattern.matcher(PhoneFrontData);
            Matcher PhoneMiddlematcher = PhoneMiddlepattern.matcher(PhoneMiddleData);
            Matcher PhoneBackmatcher = PhoneBackpattern.matcher(PhoneBackData);
            if (!PhoneFrontmatcher.find() || !PhoneMiddlematcher.find() || !PhoneBackmatcher.find()){
                Empty = Empty + "핸드폰 번호 ";
            }
        }
        if(BirthYYYYData.matches("") || BirthMMData.matches("") || BirthDDData.matches("")){
            Empty = Empty + "생년월일 ";
        }
        else{
            String BYYYYregex="^(19|20)[0-9]{2}$";
            String BMMregex="^(0[1-9]|1[012]|[1-9])$";
            String BDDregex="^([1-9]|0[1-9]|[12][0-9]|3[01])$";

            Pattern BYYYYpattern = Pattern.compile(BYYYYregex);
            Pattern BMMpattern = Pattern.compile(BMMregex);
            Pattern BDDpattern = Pattern.compile(BDDregex);
            Matcher BYYYYmatcher = BYYYYpattern.matcher(BirthYYYYData);
            Matcher BMMmatcher = BMMpattern.matcher(BirthMMData);
            Matcher BDDmatcher = BDDpattern.matcher(BirthDDData);
            if (!BYYYYmatcher.find() || !BMMmatcher.find() || !BDDmatcher.find()){
                Empty = Empty + "생년월일 ";
            }
        }

        if(Empty.matches("")){
            //Toast.makeText(getApplicationContext(), "입력 완료", Toast.LENGTH_SHORT).show();
            new FindPW().execute(IdData, NameData,
                    PhoneFrontData+"-"+PhoneMiddleData+"-"+PhoneBackData,
                    BirthYYYYData+"-"+BirthMMData+"-"+BirthDDData);
        }
        else{
            new AlertDialog.Builder(SpwActivity.this)
                    .setMessage(Empty+"을(를) 다시 확인해주세요.")
                    .setPositiveButton("OK",null)
                    .show();
        }
    }

    private class FindPW extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {

            URL url = null;
            HttpURLConnection urlConnection = null;
            JSONObject getJSON = null;

            try {
                url = new URL("http://192.168.63.109:8080/json/print.jsp");
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                //urlConnection.setRequestProperty("Cache-Control","no-cache");
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                /*urlConnection.addRequestProperty("Accept", "application/json");
                urlConnection.setRequestProperty("Content-Type", "application/json");*/

                JSONObject putJSON = new JSONObject();
                putJSON.put("ID", params[0]);
                putJSON.put("Name", params[1]);
                putJSON.put("Phone", params[2]);
                putJSON.put("Bdate", params[3]);

                Log.d("testJSON1", putJSON.toString().getBytes().toString());
                Log.d("testJSON2", putJSON.toString());

                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                out.write(putJSON.toString().getBytes());
                out.flush();

            } catch (Exception e) {

            } finally {
                urlConnection.disconnect();
            }
            return null;
        }
    }
}
