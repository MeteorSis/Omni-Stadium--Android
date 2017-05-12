package skhu.cse.network.omni_stadium;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import org.w3c.dom.Text;

import java.io.BufferedOutputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private BackPressCloseHandler backPressCloseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView SignUp = (TextView)findViewById(R.id.tvregister);
        TextView SearchId = (TextView) findViewById(R.id.tvSearchId);
        TextView SearchPw = (TextView) findViewById(R.id.tvSearchPW);

        final EditText etID = (EditText)findViewById(R.id.input_id);
        final EditText etPW = (EditText)findViewById(R.id.input_password);

        Button Login = (Button)findViewById(R.id.btlogin);
        CheckBox cbAutoLogin = (CheckBox)findViewById(R.id.check_autologin);

        SharedPreferences autoSetting;
        final SharedPreferences.Editor editor;

        Glide.with(this).load(R.drawable.omni_stadium_logo).into((ImageView)findViewById(R.id.imgView_Main_Logo));

        autoSetting = getSharedPreferences("Auto_Login", MODE_PRIVATE);
        editor = autoSetting.edit();

        cbAutoLogin.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    String ID = etID.getText().toString();
                    String PW = etPW.getText().toString();

                    editor.putString("ID",ID);
                    editor.putString("PW",PW);
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
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
            }
        });

        backPressCloseHandler = new BackPressCloseHandler(this);
    }


    private class Login extends AsyncTask<String, Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(String... params) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            JSONObject getJSON = null;

            try {
                url = new URL("http://172.20.10.2:8080/app/login.jsp");
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                JSONObject outJson = new JSONObject();
                outJson.put("ID", params[0]);
                outJson.put("PW", params[1]);

                OutputStream out = new BufferedOutputStream(urlConnection.getOutputStream());
                out.write(outJson.toString().getBytes());
                out.flush();
            }
             catch (Exception e) {
                }
            finally {
                    urlConnection.disconnect();
                }
                return getJSON;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
        }
    }

    @Override
    public void onBackPressed()
    {
        //super.onBackPressed();
        backPressCloseHandler.onBackPressed();
    }
}
