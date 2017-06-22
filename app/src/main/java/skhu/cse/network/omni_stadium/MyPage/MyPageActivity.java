package skhu.cse.network.omni_stadium.MyPage;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ExpandableListView;
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
import java.util.ArrayList;
import java.util.HashMap;

import skhu.cse.network.omni_stadium.AsyncTask.LogoutTask;
import skhu.cse.network.omni_stadium.OmniApplication;
import skhu.cse.network.omni_stadium.R;

public class MyPageActivity extends AppCompatActivity {

    private ArrayList<String> arrayGroup = new ArrayList<String>();
    private HashMap<String, ArrayList<String>> arrayChild = new HashMap<String, ArrayList<String>>();
    private String mem_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);

        mem_id = ((OmniApplication) getApplicationContext()).getMem_id();

        final ExpandableListView lvMyPage = (ExpandableListView) findViewById(R.id.explv_Setting);
        arrayGroup.add("로그아웃");
        arrayGroup.add("티켓관리");
        arrayGroup.add("회원정보 관리");

        ArrayList<String> arrayTicket = new ArrayList<String>();
        arrayTicket.add("· 자유석 해제");
        arrayTicket.add("· 티켓환불");

        ArrayList<String> arrayManage = new ArrayList<String>();
        arrayManage.add("· 회원정보 수정");
        arrayManage.add("· 회원탈퇴");

        arrayChild.put(arrayGroup.get(0), new ArrayList<String>());
        arrayChild.put(arrayGroup.get(1), arrayTicket);
        arrayChild.put(arrayGroup.get(2), arrayManage);

        lvMyPage.setAdapter(new ExplvAdapterSign(this, arrayGroup, arrayChild));
        lvMyPage.setGroupIndicator(null);  //OFF Indicator

        lvMyPage.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (groupPosition == 0) {
                    new AlertDialog.Builder(MyPageActivity.this)
                            .setMessage("로그아웃 하시겠습니까?")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    new LogoutTask(MyPageActivity.this).execute(((OmniApplication) getApplicationContext()).getMem_id());
                                }
                            })
                            .setNegativeButton("취소", null)
                            .show();
                }
                return false;
            }
        });

        lvMyPage.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (groupPosition == 1) {
                    if (childPosition == 0) {
                        new AlertDialog.Builder(MyPageActivity.this)
                                .setMessage("자유석을 해제하시겠습니까?")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String seat_zone = ((OmniApplication) getApplicationContext()).getSeat_zone();
                                        if(seat_zone == null)
                                            Toast.makeText(MyPageActivity.this, "티켓을 구매하지 않았습니다.", Toast.LENGTH_SHORT).show();
                                        else if(((OmniApplication)getApplicationContext()).getSeat_no() == null
                                                || ((OmniApplication)getApplicationContext()).getSeat_row() == null){
                                            Toast.makeText(MyPageActivity.this, "자유석을 등록하지 않았습니다.", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            if (seat_zone.equals("1루 외야그린석") || seat_zone.equals("3루 외야그린석"))
                                                new UnreservedSeat_ClearTask().execute(mem_id);
                                            else
                                                Toast.makeText(MyPageActivity.this, "자유석 티켓이 아닙니다.", Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                })
                                .setNegativeButton("취소", null)
                                .show();
                    } else {
                        new AlertDialog.Builder(MyPageActivity.this)
                                .setMessage("티켓을 환불하시겠습니까?")
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        new TicketRefundTask().execute(mem_id);
                                    }
                                })
                                .setNegativeButton("취소", null)
                                .show();
                    }
                } else if (groupPosition == 2) {
                    if (childPosition == 0) {
                        final EditText etpw = new EditText(MyPageActivity.this);
                        etpw.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
                        etpw.setSingleLine();
                        etpw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        etpw.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                new GetPasswordTask_ToSignChange().execute(mem_id, etpw.getText().toString());
                                return true;
                            }
                        });

                        new AlertDialog.Builder(MyPageActivity.this)
                                .setTitle("회원정보 수정")
                                .setMessage("비밀번호를 입력하세요.")
                                .setView(etpw)
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        new GetPasswordTask_ToSignChange().execute(mem_id, etpw.getText().toString());
                                    }
                                })
                                .setCancelable(false)
                                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(MyPageActivity.this, "취소되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .show()
                                .setCancelable(false); // 백버튼 비활성화
                    } else {
                        final EditText etpw = new EditText(MyPageActivity.this);
                        etpw.setFilters(new InputFilter[]{new InputFilter.LengthFilter(16)});
                        etpw.setSingleLine();
                        etpw.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        etpw.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                            @Override
                            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                                new GetPasswordTask_ToLeave().execute(mem_id, etpw.getText().toString());
                                return true;
                            }
                        });

                        new AlertDialog.Builder(MyPageActivity.this)
                                .setTitle("회원탈퇴")
                                .setMessage("비밀번호를 입력하세요.")
                                .setView(etpw)
                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        new GetPasswordTask_ToLeave().execute(mem_id, etpw.getText().toString());
                                    }
                                })
                                .setCancelable(false)
                                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Toast.makeText(MyPageActivity.this, "취소되었습니다.", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .show()
                                .setCancelable(false); // 백버튼 비활성화
                    }
                }
                return false;
            }
        });
    }

    private class GetPasswordTask_ToLeave extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            URL url = null;
            HttpURLConnection httpCon = null;
            JSONObject getJSON = null;

            try {
                url = new URL("http://192.168.63.25:51223/AndroidClientAccountRequestPost/ConfirmPassword");
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
                    new AlertDialog.Builder(MyPageActivity.this)
                            .setMessage("회원을 탈퇴하시겠습니까?\n(주의: 구매한 티켓이 삭제됩니다.)")
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    new LeaveTask().execute(mem_id);
                                }
                            })
                            .setNegativeButton("취소", null)
                            .show();
                }/*
                else if(result==1)
                    Toast.makeText(MainActivity.this, "접속 중인 아이디가 있습니다.", Toast.LENGTH_SHORT).show();
                */ else if (result == 1)
                    Toast.makeText(MyPageActivity.this, "비밀번호가 잘못 입력되었습니다.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MyPageActivity.this, "서버 에러입니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {

            }
        }
    }

    private class LeaveTask extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            URL url = null;
            HttpURLConnection httpCon = null;
            JSONObject getJSON = null;

            try {
                url = new URL("http://192.168.63.25:51223/AndroidClientAccountRequestPost/DeleteAccount");
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
                if (result != 0) {
                    Toast.makeText(MyPageActivity.this, "서버 에러입니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MyPageActivity.this, "회원탈퇴가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            } catch (Exception e) {

            }
        }
    }

    private class GetPasswordTask_ToSignChange extends AsyncTask<String, Void, JSONObject> {
        @Override
        protected JSONObject doInBackground(String... params) {
            URL url = null;
            HttpURLConnection httpCon = null;
            JSONObject getJSON = null;

            try {
                url = new URL("http://192.168.63.25:51223/AndroidClientAccountRequestPost/ConfirmPassword");
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
                    Intent intent = new Intent(MyPageActivity.this, SignChangeActivity.class);
                    startActivity(intent);
                }/*
                else if(result==1)
                    Toast.makeText(MainActivity.this, "접속 중인 아이디가 있습니다.", Toast.LENGTH_SHORT).show();
                */ else if (result == 1)
                    Toast.makeText(MyPageActivity.this, "비밀번호가 잘못 입력되었습니다.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(MyPageActivity.this, "서버 에러입니다. 다시 시도해주세요", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {

            }
        }
    }

    private class UnreservedSeat_ClearTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            URL url = null;
            HttpURLConnection httpCon = null;
            JSONObject getJSON = null;

            try {
                url = new URL("http://192.168.63.25:51223/AndroidClientTicketingRequestPost/FreeSeatClear");
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
                int result = jsonObject.getInt("결과"); // 예매 성공: 0 예매 실패: else
                String msg = jsonObject.getString("메시지");
                if (result == 0) {
                    ((OmniApplication) getApplicationContext()).setSeat_row(null);
                    ((OmniApplication) getApplicationContext()).setSeat_no(null);
                    Toast.makeText(MyPageActivity.this, msg, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(MyPageActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
            }
        }
    }

    private class TicketRefundTask extends AsyncTask<String, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... params) {
            URL url = null;
            HttpURLConnection httpCon = null;
            JSONObject getJSON = null;

            try {
                url = new URL("http://192.168.63.25:51223/AndroidClientTicketingRequestPost/Refund");
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
                int result = jsonObject.getInt("결과"); // 예매 성공: 0 예매 실패: else
                String msg = jsonObject.getString("메시지");
                if (result == 0) {
                    ((OmniApplication) getApplicationContext()).setTicket_no(null);
                    ((OmniApplication) getApplicationContext()).setSeat_zone(null);
                    ((OmniApplication) getApplicationContext()).setSeat_row(null);
                    ((OmniApplication) getApplicationContext()).setSeat_no(null);
                    Toast.makeText(MyPageActivity.this, msg, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(MyPageActivity.this, msg, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //NavUtils.navigateUpFromSameTask(this);
        finish();
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
}
