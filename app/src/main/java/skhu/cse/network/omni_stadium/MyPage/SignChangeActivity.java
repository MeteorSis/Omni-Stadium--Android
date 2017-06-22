package skhu.cse.network.omni_stadium.MyPage;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import skhu.cse.network.omni_stadium.OmniApplication;
import skhu.cse.network.omni_stadium.R;

public class SignChangeActivity extends AppCompatActivity {

    private String mem_id;

    private EditText confirm_PW;

    private EditText etID, etPW, etMail, etMailDomain, etPhoneFront, etPhoneMiddle, etPhoneBack, etName, etBirthYYYY, etBirthMM, etBirthDD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signchange);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mem_id = ((OmniApplication) getApplicationContext()).getMem_id();

        LayoutInflater inflater = getLayoutInflater();
        final TableLayout tableLayout = (TableLayout) inflater.inflate(R.layout.change_pw_mypage, null);
        confirm_PW = (EditText) tableLayout.findViewById(R.id.etConfirm);

        etID = (EditText) findViewById(R.id.etID);
        etPW = (EditText) findViewById(R.id.etPW);

        etMail=(EditText)findViewById(R.id.etMail);
        etMailDomain=(EditText)findViewById(R.id.etMailDomain);
        etPhoneFront=(EditText)findViewById(R.id.etPhoneFront);
        etPhoneMiddle=(EditText)findViewById(R.id.etPhoneMiddle);
        etPhoneBack=(EditText)findViewById(R.id.etPhoneBack);
        etName=(EditText)findViewById(R.id.etName);
        etBirthYYYY=(EditText)findViewById(R.id.etBirthYYYY);
        etBirthMM=(EditText)findViewById(R.id.etBirthMM);
        etBirthDD=(EditText)findViewById(R.id.etBirthDD);

        etID.setText(((OmniApplication) getApplicationContext()).getMem_id());

        Button btCPW = (Button) findViewById(R.id.btChangePW);
        Button btCMail = (Button) findViewById(R.id.btChangeEmail);
        Button btCPhone = (Button) findViewById(R.id.btChangePhone);
        Button btCName = (Button) findViewById(R.id.btChangeName);
        Button btCBdate = (Button) findViewById(R.id.btChangeBirth);

        btCPW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String PwData = etPW.getText().toString();
                if ((PwData.matches("") || (PwData.length() < 6))) {
                    new AlertDialog.Builder(SignChangeActivity.this)
                            .setMessage("비밀번호를 다시 확인해주세요.")
                            .setPositiveButton("OK", null)
                            .show();
                } else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignChangeActivity.this);
                    final AlertDialog alertDialog=builder.setMessage("비밀번호 변경")
                            .setView(tableLayout)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (!(confirm_PW.getText().toString().equals(etPW.getText().toString()))
                                            || confirm_PW.getText().toString().length() < 6) {
                                        Toast.makeText(getApplicationContext(), "비밀번호를 다시 입력해주세요", Toast.LENGTH_SHORT).show();
                                        ((ViewGroup) tableLayout.getParent()).removeView(tableLayout);
                                    } else {
                                        new ChangePW().execute(mem_id, PwData);
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
                            if (!(confirm_PW.getText().toString().equals(etPW.getText().toString()))
                                    || confirm_PW.getText().toString().length() < 6) {
                                Toast.makeText(getApplicationContext(), "비밀번호를 다시 입력해주세요", Toast.LENGTH_SHORT).show();
                                ((ViewGroup) tableLayout.getParent()).removeView(tableLayout);
                            } else {
                                new ChangePW().execute(mem_id, PwData);
                                alertDialog.dismiss();
                                ((ViewGroup) tableLayout.getParent()).removeView(tableLayout);
                            }
                            return true;
                        }
                    });
                }
            }
        });

        btCMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String MailData = etMail.getText().toString();
                String MailDomainData=etMailDomain.getText().toString();
                String Mailregex = "^[_a-zA-Z0-9-\\.]+$";
                String MailDomainregex="^[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
                Pattern Mailpattern = Pattern.compile(Mailregex);
                Pattern MailDomainpattern=Pattern.compile(MailDomainregex);
                Matcher Mailmatcher = Mailpattern.matcher(MailData);
                Matcher MailDomainmatcher = MailDomainpattern.matcher(MailDomainData);

                if (!Mailmatcher.find() || !MailDomainmatcher.find()) {
                    new AlertDialog.Builder(SignChangeActivity.this)
                            .setMessage("이메일을 다시 확인해주세요.")
                            .setPositiveButton("OK", null)
                            .show();
                } else
                   new ChangeEmail().execute(mem_id, MailData+"@"+MailDomainData);
            }
        });

        btCPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String PhoneFrontData = etPhoneFront.getText().toString();
                String PhoneMiddleData = etPhoneMiddle.getText().toString();
                String PhoneBackData = etPhoneBack.getText().toString();
                String PhoneFrontregex = "^01[016789]$";
                String PhoneMiddleregex="^[0-9]{3,4}$";
                String PhoneBackregex="^[0-9]{3,4}$";
                Pattern PhoneFrontpattern = Pattern.compile(PhoneFrontregex);
                Pattern PhoneMiddlepattern = Pattern.compile(PhoneMiddleregex);
                Pattern PhoneBackpattern = Pattern.compile(PhoneBackregex);
                Matcher PhoneFrontmatcher = PhoneFrontpattern.matcher(PhoneFrontData);
                Matcher PhoneMiddlematcher = PhoneMiddlepattern.matcher(PhoneMiddleData);
                Matcher PhoneBackmatcher = PhoneBackpattern.matcher(PhoneBackData);
                if (!PhoneFrontmatcher.find() || !PhoneMiddlematcher.find() || !PhoneBackmatcher.find())
                    new AlertDialog.Builder(SignChangeActivity.this)
                            .setMessage("핸드폰 번호를 다시 확인해주세요.")
                            .setPositiveButton("OK", null)
                            .show();
                else
                    new ChangePhone().execute(mem_id, PhoneFrontData+"-"+PhoneMiddleData+"-"+PhoneBackData);
            }
        });

        btCName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String NameData = etName.getText().toString();
                String Nameregex = "^[가-힣]{2,4}$";
                Pattern Namepattern = Pattern.compile(Nameregex);
                Matcher Namematcher = Namepattern.matcher(NameData);

                if (!Namematcher.find())
                    new AlertDialog.Builder(SignChangeActivity.this)
                            .setMessage("이름을 다시 확인해주세요.")
                            .setPositiveButton("OK", null)
                            .show();
                else
                    new ChangeName().execute(mem_id, NameData);
            }
        });

        btCBdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String BirthYYYYData = etBirthYYYY.getText().toString();
                String BirthMMData = etBirthMM.getText().toString();
                String BirthDDData = etBirthDD.getText().toString();

                String BYYYYregex="^(19|20)[0-9]{2}$";
                String BMMregex="^(0[1-9]|1[012]|[1-9])$";
                String BDDregex="^([1-9]|0[1-9]|[12][0-9]|3[01])$";

                Pattern BYYYYpattern = Pattern.compile(BYYYYregex);
                Pattern BMMpattern = Pattern.compile(BMMregex);
                Pattern BDDpattern = Pattern.compile(BDDregex);
                Matcher BYYYYmatcher = BYYYYpattern.matcher(BirthYYYYData);
                Matcher BMMmatcher = BMMpattern.matcher(BirthMMData);
                Matcher BDDmatcher = BDDpattern.matcher(BirthDDData);
                if (!BYYYYmatcher.find() || !BMMmatcher.find() || !BDDmatcher.find())
                    new AlertDialog.Builder(SignChangeActivity.this)
                            .setMessage("생년월일을 다시 확인해주세요.")
                            .setPositiveButton("OK", null)
                            .show();
                else
                    new ChangeBdate().execute(mem_id, BirthYYYYData+"-"+BirthMMData+"-"+BirthDDData);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ChangePW extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            URL url = null;
            HttpURLConnection httpCon = null;
            JSONObject getJSON = null;

            try {
                url = new URL("http://192.168.63.25:51223/AndroidClientAccountRequestPost/UpdateInfo_pw");
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
                    Toast.makeText(getApplicationContext(), "비밀번호 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "서버 에러입니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
            }
        }
    }

    private class ChangeEmail extends AsyncTask<String, Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(String... params) {
            URL url = null;
            HttpURLConnection httpCon = null;
            JSONObject getJSON = null;

            try {
                url = new URL("http://192.168.63.25:51223/AndroidClientAccountRequestPost/UpdateInfo_email");
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
                outJson.put("이메일", params[1]);

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
                    Toast.makeText(getApplicationContext(), "이메일 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "서버 에러입니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
            }
        }
    }

    private class ChangePhone extends AsyncTask<String, Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(String... params) {
            URL url = null;
            HttpURLConnection httpCon = null;
            JSONObject getJSON = null;

            try {
                url = new URL("http://192.168.63.25:51223/AndroidClientAccountRequestPost/UpdateInfo_phone");
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
                outJson.put("전화번호", params[1]);

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
                    Toast.makeText(getApplicationContext(), "핸드폰 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "서버 에러입니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
            }
        }
    }

    private class ChangeName extends AsyncTask<String, Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(String... params) {
            URL url = null;
            HttpURLConnection httpCon = null;
            JSONObject getJSON = null;

            try {
                url = new URL("http://192.168.63.25:51223/AndroidClientAccountRequestPost/UpdateInfo_name");
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
                    Toast.makeText(getApplicationContext(), "이름 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "서버 에러입니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
            }
        }
    }

    private class ChangeBdate extends AsyncTask<String, Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(String... params) {
            URL url = null;
            HttpURLConnection httpCon = null;
            JSONObject getJSON = null;

            try {
                url = new URL("http://192.168.63.25:51223/AndroidClientAccountRequestPost/UpdateInfo_bdate");
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
                outJson.put("생일", params[1]);

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
                    Toast.makeText(getApplicationContext(), "생년월일 변경이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(getApplicationContext(), "서버 에러입니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
            }
        }
    }

}