package skhu.cse.network.omni_stadium.AsyncTask;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import skhu.cse.network.omni_stadium.OmniApplication;

public class LogoutTask extends AsyncTask<String, Void, JSONObject>
{
    private AppCompatActivity activity;
    public LogoutTask(AppCompatActivity activity)
    {
        this.activity=activity;
    }

    @Override
    protected JSONObject doInBackground(String... params) {
        URL url = null;
        HttpURLConnection httpCon = null;
        JSONObject getJSON = null;

        try {
            url = new URL("http://192.168.63.25:51223/AndroidClientLogOutRequestPost");
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
            if(result==0)
            {
                ((OmniApplication)activity.getApplicationContext()).setMem_id(null);
                Intent intent = new Intent();
                activity.setResult(activity.RESULT_OK, intent);
                activity.finish();
            }
            else
                Toast.makeText(activity, "로그아웃이 실패하였습니다.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {

        }
    }
}