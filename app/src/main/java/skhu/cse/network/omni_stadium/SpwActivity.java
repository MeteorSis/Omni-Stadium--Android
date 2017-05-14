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

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SpwActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spw_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button btOK = (Button)findViewById(R.id.btSpwOK);

        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etID = (EditText)findViewById(R.id.etID);
                EditText etName = (EditText)findViewById(R.id.etName);
                EditText etPhone = (EditText)findViewById(R.id.etPhone);
                EditText etBdate = (EditText)findViewById(R.id.etBdate);

                String IdData = etID.getText().toString();
                String NameData = etName.getText().toString();
                String PhoneData = etPhone.getText().toString();
                String BdateData = etBdate.getText().toString();

                String[] SpwData = {IdData, NameData, PhoneData, BdateData};
                String Empty = "";

                if(SpwData[0].matches("")){
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
                if(SpwData[1].matches("")&& Empty.matches("")){
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
                if(SpwData[2].matches("")&& Empty.matches("")){
                    Empty = Empty + "핸드폰 번호";
                }
                else {
                    String Phoneregex = "010([0-9]{3,4})([0-9]{4})";
                    Pattern Phonepattern = Pattern.compile(Phoneregex);
                    Matcher Phonematcher = Phonepattern.matcher(PhoneData);
                    if (!Phonematcher.find() && Empty.matches("")) {
                        Empty = Empty + "핸드폰 번호";
                    }
                }
                if(SpwData[3].matches("")&& Empty.matches("")){
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
                    //new SignUpActivity.SignUp().execute(SignupData[0], SignupData[1], SignupData[2], SignupData[3], SignupData[4], SignupData[5]);
                    new SpwActivity.FindPW().execute(SpwData[0], SpwData[1], SpwData[2], SpwData[3]);
                }
                else{
                    new AlertDialog.Builder(SpwActivity.this)
                            .setMessage(Empty+"을(를) 다시 확인해주세요.")
                            .setPositiveButton("OK",null)
                            .show();
                }
            }
        });
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
