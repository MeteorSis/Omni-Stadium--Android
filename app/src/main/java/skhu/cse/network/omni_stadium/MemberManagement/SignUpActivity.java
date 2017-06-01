package skhu.cse.network.omni_stadium.MemberManagement;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import skhu.cse.network.omni_stadium.R;

public class SignUpActivity extends AppCompatActivity{
    private EditText etID, etPW, etMail, etMailDomain, etPhoneFront, etPhoneMiddle, etPhoneBack, etName, etBirthYYYY, etBirthMM, etBirthDD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Glide.with(this).load(R.drawable.omni_stadium_logo).into((ImageView)findViewById(R.id.imgView_SignUp_Logo));

        TextView tvLogin = (TextView)findViewById(R.id.link_login);
        Button btAccount = (Button)findViewById(R.id.btn_signup);

        etID=(EditText)findViewById(R.id.etID);
        etPW=(EditText)findViewById(R.id.etPW);
        etMail=(EditText)findViewById(R.id.etMail);
        etMailDomain=(EditText)findViewById(R.id.etMailDomain);
        etPhoneFront=(EditText)findViewById(R.id.etPhoneFront);
        etPhoneMiddle=(EditText)findViewById(R.id.etPhoneMiddle);
        etPhoneBack=(EditText)findViewById(R.id.etPhoneBack);
        etName=(EditText)findViewById(R.id.etName);
        etBirthYYYY=(EditText)findViewById(R.id.etBirthYYYY);
        etBirthMM=(EditText)findViewById(R.id.etBirthMM);
        etBirthDD=(EditText)findViewById(R.id.etBirthDD);

        etBirthDD.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                patternCheck();
                return true;
            }
        });


        btAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patternCheck();
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void patternCheck()
    {
        String IdData = etID.getText().toString();
        String PwData = etPW.getText().toString();
        String MailData = etMail.getText().toString();
        String MailDomainData=etMailDomain.getText().toString();
        String PhoneFrontData = etPhoneFront.getText().toString();
        String PhoneMiddleData = etPhoneMiddle.getText().toString();
        String PhoneBackData = etPhoneBack.getText().toString();
        String NameData = etName.getText().toString();
        String BirthYYYYData = etBirthYYYY.getText().toString();
        String BirthMMData = etBirthMM.getText().toString();
        String BirthDDData = etBirthDD.getText().toString();
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
        if(PwData.matches("") || (PwData.length()<6)){
            Empty = Empty + "비밀번호 ";
        }

        if(MailData.matches("") || MailDomainData.matches("")){
            Empty = Empty + "이메일 ";
        }
        else {
            String Mailregex = "^[_a-zA-Z0-9-\\.]+$";
            String MailDomainregex="^[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
            Pattern Mailpattern = Pattern.compile(Mailregex);
            Pattern MailDomainpattern=Pattern.compile(MailDomainregex);
            Matcher Mailmatcher = Mailpattern.matcher(MailData);
            Matcher MailDomainmatcher = MailDomainpattern.matcher(MailDomainData);
            if (!Mailmatcher.find() || !MailDomainmatcher.find() && Empty.matches("")){
                Empty = Empty + "이메일 ";
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
            new SignUp().execute(IdData, PwData,
                    MailData+"@"+MailDomainData,
                    PhoneFrontData+"-"+PhoneMiddleData+"-"+PhoneBackData,
                    NameData, BirthYYYYData+"-"+BirthMMData+"-"+BirthDDData);
        }
        else{
            new AlertDialog.Builder(SignUpActivity.this)
                    .setMessage(Empty+"을(를) 다시 확인해주세요.")
                    .setPositiveButton("OK",null)
                    .show();
        }
    }
    private class SignUp extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {

            URL url = null;
            HttpURLConnection httpCon = null;
            JSONObject getJSON = null;

            try {
                url = new URL("http://192.168.63.25:51223/AndroidClientRegisterRequestPost");
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
                outJson.put("ID", params[0]);
                outJson.put("PW", params[1]);
                outJson.put("Email", params[2]);
                outJson.put("Phone", params[3]);
                outJson.put("Name", params[4]);
                outJson.put("Bdate", params[5]);

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
                int result = jsonObject.getInt("결과");
                if (result == 0 ) {
                    Toast.makeText(SignUpActivity.this, "회원가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else if(result ==1){
                    Toast.makeText(getApplicationContext(), "서버 에러입니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(SignUpActivity.this, "아이디 혹은 비밀번호가 잘못 입력되었습니다.", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
            }
        }
    }
}