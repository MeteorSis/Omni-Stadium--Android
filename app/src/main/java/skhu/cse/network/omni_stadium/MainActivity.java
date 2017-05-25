package skhu.cse.network.omni_stadium;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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

import skhu.cse.network.omni_stadium.MemberManagement.SidActivity;
import skhu.cse.network.omni_stadium.MemberManagement.SignUpActivity;
import skhu.cse.network.omni_stadium.MemberManagement.SpwActivity;

public class MainActivity extends AppCompatActivity {
    private BackPressCloseHandler backPressCloseHandler;
    private EditText etID, etPW;
    private CheckBox cbAutoLogin;
    private String strID, strPW;
    static final int REQ_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView SignUp = (TextView)findViewById(R.id.tvregister);
        TextView SearchId = (TextView) findViewById(R.id.tvSearchId);
        TextView SearchPw = (TextView) findViewById(R.id.tvSearchPW);

        etID = (EditText)findViewById(R.id.input_id);
        etPW = (EditText)findViewById(R.id.input_password);
        etPW.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                loginFunc();
                return true;
            }
        });

        Button Login = (Button)findViewById(R.id.btlogin);
        cbAutoLogin = (CheckBox)findViewById(R.id.check_autologin);

        SharedPreferences autoSetting;
        final SharedPreferences.Editor editor;

        Glide.with(this).load(R.drawable.omni_stadium_logo).into((ImageView)findViewById(R.id.imgView_Main_Logo));

        autoSetting = getSharedPreferences("Auto_Login", MODE_PRIVATE);
        editor = autoSetting.edit();

        cbAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    strID = etID.getText().toString();
                    strPW = etPW.getText().toString();

                    editor.putString("ID", strID);
                    editor.putString("PW", strPW);
                    editor.putBoolean("Auto_Login_enabled",true);
                    editor.commit();
                }
                else{
                    editor.remove("ID");
                    editor.remove("PW");
                    editor.remove("Auto_Login_enabled");
                    editor.clear();
                    editor.commit();
                }
            }
        });

        if(autoSetting.getBoolean("Auto_Login_enabled", false)){
            etID.setText(autoSetting.getString("ID",""));
            etPW.setText(autoSetting.getString("PW",""));
            cbAutoLogin.setChecked(true);
            loginFunc();
        }


        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        SearchId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SidActivity.class);
                startActivity(intent);
            }
        });

        SearchPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SpwActivity.class);
                startActivity(intent);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginFunc();
            }
        });

        backPressCloseHandler = new BackPressCloseHandler(this);
    }
    private void loginFunc()
    {
        strID=etID.getText().toString();
        strPW=etPW.getText().toString();
        String IDregex = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$";
        Pattern IDpattern = Pattern.compile(IDregex);
        Matcher IDmatcher = IDpattern.matcher(strID);

        if(strID.matches("") || strPW.matches("") || !IDmatcher.find() || strPW.length()<6)
            Toast.makeText(MainActivity.this, "아이디 혹은 비밀번호가 잘못 입력되었습니다.", Toast.LENGTH_SHORT).show();
        else
            new LoginTask().execute(strID, strPW);
    }

    private class LoginTask extends AsyncTask<String, Void, JSONObject>
    {
        @Override
        protected JSONObject doInBackground(String... params) {
            URL url = null;
            HttpURLConnection httpCon = null;
            JSONObject getJSON = null;

            try {
                url = new URL("http://192.168.63.25:51223/AndroidClientLogInRequestPost");
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
                outJson.put("비밀번호", params[1]);

                OutputStream out = new BufferedOutputStream(httpCon.getOutputStream());
                out.write(outJson.toString().getBytes("UTF-8"));
                out.flush();

                int responseCode = httpCon.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream=httpCon.getInputStream();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    StringBuilder result = new StringBuilder();
                    while((line = bufferedReader.readLine()) != null)
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
                int result=jsonObject.getInt("결과");
                if(result==0||result==1)
                {
                    OmniApplication omniApplication=(OmniApplication)getApplicationContext();
                    omniApplication.setId(strID);
                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                    startActivityForResult(intent, REQ_CODE);
                }/*
                else if(result==1)
                    Toast.makeText(MainActivity.this, "접속 중인 아이디가 있습니다.", Toast.LENGTH_SHORT).show();
                */else
                    Toast.makeText(MainActivity.this, "아이디 혹은 비밀번호가 잘못 입력되었습니다.", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {

            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        etID.setText("");
        etPW.setText("");
        cbAutoLogin.setChecked(false);
        etID.requestFocus();
    }

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_CODE){
            if(resultCode == RESULT_OK){
                finish();
            }
        }
    }
}
