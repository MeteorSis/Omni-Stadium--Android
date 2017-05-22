package skhu.cse.network.omni_stadium.MemberManagement;

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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import skhu.cse.network.omni_stadium.R;

public class SignUpActivity extends AppCompatActivity{
    private EditText etID, etPW, etMail, etMailDomain, etPhoneFront, etPhoneMiddle, etPhoneBack, etName, etBirthYYYY, etBirthMM, etBirthDD;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
            Log.d("test1", "test1");
            new SignUp().execute(IdData, PwData,
                    MailData+"@"+MailDomainData,
                    PhoneFrontData+"-"+PhoneMiddleData+"-"+PhoneBackData,
                    NameData, BirthYYYYData+"-"+BirthMMData+"-"+BirthDDData);
            Toast.makeText(getApplicationContext(), "회원가입 완료 ", Toast.LENGTH_SHORT).show();
            finish();
        }
        else{
            new AlertDialog.Builder(SignUpActivity.this)
                    .setMessage(Empty+"을(를) 다시 확인해주세요.")
                    .setPositiveButton("OK",null)
                    .show();
        }
    }
    private class SignUp extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... params) {

            URL url = null;
            HttpURLConnection urlConnection = null;
            JSONObject getJSON = null;

            try{
                url = new URL("http://192.168.63.109:8080/json/print.jsp");
                urlConnection = (HttpURLConnection) url.openConnection();

                Log.d("test2", "test2");

                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                Log.d("test3", "test3");

                JSONObject putJSON = new JSONObject();
                Log.d("test4", "test4");
                putJSON.put("ID", params[0]);
                putJSON.put("PW", params[1]);
                putJSON.put("Email", params[2]);
                putJSON.put("Phone", params[3]);
                putJSON.put("Name", params[4]);
                putJSON.put("Bdate", params[5]);

                Log.d("testJSON1", putJSON.toString().getBytes().toString());
                Log.d("testJSON2", putJSON.toString());
                Log.d("testJSON3", new String(putJSON.toString().getBytes(), 0, putJSON.toString().getBytes().length));

                Log.d("test5", "test5");

                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                Log.d("test6", "test6");
                out.write(putJSON.toString().getBytes());
                Log.d("test7", "test7");
                out.flush();

                Log.d("test8", "test8");
            }
            catch (Exception e) {

            }
            finally {
                urlConnection.disconnect();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
        }
    }
}