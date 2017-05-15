package skhu.cse.network.omni_stadium;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Glide.with(this).load(R.drawable.omni_stadium_logo).into((ImageView)findViewById(R.id.imgView_SignUp_Logo));

        TextView tvLogin = (TextView)findViewById(R.id.link_login);
        Button btAccount = (Button)findViewById(R.id.btn_signup);

        btAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText etID = (EditText)findViewById(R.id.etID);
                EditText etPW = (EditText)findViewById(R.id.etPW);
                EditText etMail = (EditText)findViewById(R.id.etMail);
                EditText etPhone = (EditText)findViewById(R.id.etPhone);
                EditText etName = (EditText)findViewById(R.id.etName);
                EditText etBdate = (EditText)findViewById(R.id.etBdate);

                String IdData = etID.getText().toString();
                String PwData = etPW.getText().toString();
                String MailData = etMail.getText().toString();
                String PhoneData = etPhone.getText().toString();
                String NameData = etName.getText().toString();
                String BdateData = etBdate.getText().toString();

                String[] SignupData = {IdData, PwData, MailData, PhoneData, NameData, BdateData};
                String Empty = "";

                if(SignupData[0].matches("")){
                    Empty = Empty + "아이디";
                }
                else{
                    String Idregex = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$";
                    Pattern IDpattern = Pattern.compile(Idregex);
                    Matcher IDmatcher = IDpattern.matcher(IdData);
                    if(!IDmatcher.find()) {
                        Empty = Empty + "아이디";
                    }
                }
                if((SignupData[1].matches("") || (SignupData[1].length()<6)) && Empty.matches("")){
                    Empty = Empty + "비밀번호";
                }

                if(SignupData[2].matches("") && Empty.matches("")){
                    Empty = Empty + "Email";
                }
                else {
                    String Mailregex = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+";
                    Pattern Mailpattern = Pattern.compile(Mailregex);
                    Matcher Mailmatcher = Mailpattern.matcher(MailData);
                    if (!Mailmatcher.find() && Empty.matches("")){
                        Empty = Empty + "Email";
                    }
                }

                if(SignupData[3].matches("")&& Empty.matches("")){
                    Empty = Empty + "핸드폰 번호";
                }
                else{
                    String Phoneregex = "010([0-9]{4})([0-9]{4})$";
                    Pattern Phonepattern = Pattern.compile(Phoneregex);
                    Matcher Phonematcher = Phonepattern.matcher(PhoneData);
                    if (!Phonematcher.find()&& Empty.matches("")){
                        Empty = Empty + "핸드폰 번호";
                    }
                }
                if(SignupData[4].matches("")&& Empty.matches("")){
                    Empty = Empty + "이름";
                }
                else{
                    String Nameregex = "^[가-힣]{2,4}$";
                    Pattern Namepattern = Pattern.compile(Nameregex);
                    Matcher Namematcher = Namepattern.matcher(NameData);
                    if (!Namematcher.find()&& Empty.matches("")){
                        Empty = Empty + "이름";
                    }
                }
                if(SignupData[5].matches("")&& Empty.matches("")){
                    Empty = Empty + "생년월일";
                }
                else{
                    String Bdateregex = "(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$";
                    Pattern Bdatepattern = Pattern.compile(Bdateregex);
                    Matcher Bdatematcher = Bdatepattern.matcher(BdateData);
                    if (!Bdatematcher.find()&& Empty.matches("")){
                        Empty = Empty + "생년월일";
                    }
                }

                if(Empty.matches("")){
                    //Toast.makeText(getApplicationContext(), "입력 완료", Toast.LENGTH_SHORT).show();
                    Log.d("test1", "test1");
                    new SignUp().execute(SignupData[0], SignupData[1], SignupData[2], SignupData[3], SignupData[4], SignupData[5]);
                    Toast.makeText(getApplicationContext(), "회원가입 완료", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else{
                    new AlertDialog.Builder(SignUpActivity.this)
                            .setMessage(Empty+"을(를) 다시 확인해주세요.")
                            .setPositiveButton("OK",null)
                            .show();
                }
            }
        });

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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