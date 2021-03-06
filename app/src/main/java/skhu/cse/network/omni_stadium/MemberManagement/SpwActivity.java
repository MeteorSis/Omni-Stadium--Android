package skhu.cse.network.omni_stadium.MemberManagement;


import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

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

public class SpwActivity extends AppCompatActivity {
    private EditText etID, etName,
            etPhoneFront, etPhoneMiddle, etPhoneBack,
            etBirthYYYY, etBirthMM, etBirthDD;

    private EditText change_PW;
    private EditText confirm_PW;

    private String setID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spw_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button btOK = (Button) findViewById(R.id.btSpwOK);

        etID = (EditText) findViewById(R.id.etID);
        etName = (EditText) findViewById(R.id.etName);
        etPhoneFront = (EditText) findViewById(R.id.etPhoneFront);
        etPhoneMiddle = (EditText) findViewById(R.id.etPhoneMiddle);
        etPhoneBack = (EditText) findViewById(R.id.etPhoneBack);
        etBirthYYYY = (EditText) findViewById(R.id.etBirthYYYY);
        etBirthMM = (EditText) findViewById(R.id.etBirthMM);
        etBirthDD = (EditText) findViewById(R.id.etBirthDD);

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
        String IdData = etID.getText().toString();
        String NameData = etName.getText().toString();
        String PhoneFrontData = etPhoneFront.getText().toString();
        String PhoneMiddleData = etPhoneMiddle.getText().toString();
        String PhoneBackData = etPhoneBack.getText().toString();
        String BirthYYYYData = etBirthYYYY.getText().toString();
        String BirthMMData = etBirthMM.getText().toString();
        String BirthDDData = etBirthDD.getText().toString();
        String Empty = "";

        if (IdData.matches("")) {
            Empty = Empty + "아이디 ";
        } else {
            String Idregex = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$";
            Pattern IDpattern = Pattern.compile(Idregex);
            Matcher IDmatcher = IDpattern.matcher(IdData);
            if (!IDmatcher.find()) {
                Empty = Empty + "아이디 ";
            }
        }
        setID = IdData;

        if (NameData.matches("")) {
            Empty = Empty + "이름 ";
        } else {
            String Nameregex = "^[가-힣]{2,4}$";
            Pattern Namepattern = Pattern.compile(Nameregex);
            Matcher Namematcher = Namepattern.matcher(NameData);
            if (!Namematcher.find()) {
                Empty = Empty + "이름 ";
            }
        }
        if (PhoneFrontData.matches("") || PhoneMiddleData.matches("") || PhoneBackData.matches("")) {
            Empty = Empty + "핸드폰 번호 ";
        } else {
            String PhoneFrontregex = "^01[016789]$";
            String PhoneMiddleregex = "^[0-9]{3,4}$";
            String PhoneBackregex = "^[0-9]{3,4}$";
            Pattern PhoneFrontpattern = Pattern.compile(PhoneFrontregex);
            Pattern PhoneMiddlepattern = Pattern.compile(PhoneMiddleregex);
            Pattern PhoneBackpattern = Pattern.compile(PhoneBackregex);
            Matcher PhoneFrontmatcher = PhoneFrontpattern.matcher(PhoneFrontData);
            Matcher PhoneMiddlematcher = PhoneMiddlepattern.matcher(PhoneMiddleData);
            Matcher PhoneBackmatcher = PhoneBackpattern.matcher(PhoneBackData);
            if (!PhoneFrontmatcher.find() || !PhoneMiddlematcher.find() || !PhoneBackmatcher.find()) {
                Empty = Empty + "핸드폰 번호 ";
            }
        }
        if (BirthYYYYData.matches("") || BirthMMData.matches("") || BirthDDData.matches("")) {
            Empty = Empty + "생년월일 ";
        } else {
            String BYYYYregex = "^(19|20)[0-9]{2}$";
            String BMMregex = "^(0[1-9]|1[012]|[1-9])$";
            String BDDregex = "^([1-9]|0[1-9]|[12][0-9]|3[01])$";

            Pattern BYYYYpattern = Pattern.compile(BYYYYregex);
            Pattern BMMpattern = Pattern.compile(BMMregex);
            Pattern BDDpattern = Pattern.compile(BDDregex);
            Matcher BYYYYmatcher = BYYYYpattern.matcher(BirthYYYYData);
            Matcher BMMmatcher = BMMpattern.matcher(BirthMMData);
            Matcher BDDmatcher = BDDpattern.matcher(BirthDDData);
            if (!BYYYYmatcher.find() || !BMMmatcher.find() || !BDDmatcher.find()) {
                Empty = Empty + "생년월일 ";
            }
        }

        if (Empty.matches("")) {
            //Toast.makeText(getApplicationContext(), "입력 완료", Toast.LENGTH_SHORT).show();
            new FindPW().execute(IdData, NameData,
                    PhoneFrontData + "-" + PhoneMiddleData + "-" + PhoneBackData,
                    BirthYYYYData + "-" + BirthMMData + "-" + BirthDDData);
        } else {
            new AlertDialog.Builder(SpwActivity.this)
                    .setMessage(Empty + "을(를) 다시 확인해주세요.")
                    .setPositiveButton("OK", null)
                    .show();
        }
    }

    private class FindPW extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {

            URL url = null;
            HttpURLConnection httpCon = null;
            JSONObject getJSON = null;

            try {
                url = new URL("http://192.168.63.25:51223/AndroidClientAccountRequestPost/FindPassword");
                httpCon = (HttpURLConnection) url.openConnection();

                httpCon.setRequestMethod("POST");
                httpCon.setDoInput(true);
                httpCon.setDoOutput(true);
                httpCon.setRequestProperty("Cache-Control", "no-cache");
                //서버에 요청할 Response Data Type
                httpCon.setRequestProperty("Accept", "application/json");
                //서버에 전송할 Data Type
                //httpCon.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                httpCon.setRequestProperty("Content-Type", "application/json");

                JSONObject outJson = new JSONObject();
                outJson.put("아이디", params[0]);
                outJson.put("이름", params[1]);
                outJson.put("전화", params[2]);
                outJson.put("생일", params[3]);

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
            LayoutInflater inflater = getLayoutInflater();
            final TableLayout tableLayout = (TableLayout)inflater.inflate(R.layout.change_pw, null);
            change_PW = (EditText) tableLayout.findViewById(R.id.etNewPW);
            confirm_PW = (EditText) tableLayout.findViewById(R.id.etConfirm);

            try {
                int result = jsonObject.getInt("결과");
                if (result == 0) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SpwActivity.this);
                    final AlertDialog alertDialog=builder.setMessage("비밀번호 변경")
                            .setView(tableLayout)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (!(change_PW.getText().toString().equals(confirm_PW.getText().toString()))
                                            || change_PW.getText().toString().length() < 6) {
                                        Toast.makeText(getApplicationContext(), "비밀번호를 다시 입력해주세요", Toast.LENGTH_SHORT).show();
                                        ((ViewGroup) tableLayout.getParent()).removeView(tableLayout);
                                    } else {
                                        new ChangePW().execute(setID, change_PW.getText().toString());
                                        dialog.dismiss();
                                        ((ViewGroup) tableLayout.getParent()).removeView(tableLayout);
                                    }
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    ((ViewGroup) tableLayout.getParent()).removeView(tableLayout);
                                }
                            })
                            .setCancelable(false)
                            .create();
                    alertDialog.show();
                    confirm_PW.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                        @Override
                        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                            if (!(change_PW.getText().toString().equals(confirm_PW.getText().toString()))
                                    || change_PW.getText().toString().length() < 6) {
                                Toast.makeText(getApplicationContext(), "비밀번호를 다시 입력해주세요", Toast.LENGTH_SHORT).show();
                                ((ViewGroup) tableLayout.getParent()).removeView(tableLayout);
                            } else {
                                new ChangePW().execute(setID, change_PW.getText().toString());
                                alertDialog.dismiss();
                                ((ViewGroup) tableLayout.getParent()).removeView(tableLayout);
                            }
                            return true;
                        }
                    });

                }
                else if(result == 1)
                    Toast.makeText(getApplicationContext(), "회원정보를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "서버 에러입니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
            }
        }

    }

    private class ChangePW extends AsyncTask<String, Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(String... params) {
            URL url = null;
            HttpURLConnection httpCon = null;
            JSONObject getJSON = null;

            try {
                url = new URL("http://192.168.63.25:51223/AndroidClientAccountRequestPost/UpdatePassword");
                httpCon = (HttpURLConnection) url.openConnection();

                httpCon.setRequestMethod("POST");
                httpCon.setDoInput(true);
                httpCon.setDoOutput(true);
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
                if (result == 0) {
                    new AlertDialog.Builder(SpwActivity.this)
                            .setMessage("비밀번호 변경이 완료되었습니다.\n새로운 비밀번호로 로그인 해주세요.")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .show();
                }
                else if(result == 1)
                    Toast.makeText(getApplicationContext(), "비밀번호를 다시 입력해주세요.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(getApplicationContext(), "서버 에러입니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
            }
        }
    }

}
