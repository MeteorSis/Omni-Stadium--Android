package skhu.cse.network.omni_stadium.Ordering;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import skhu.cse.network.omni_stadium.LoadingDialog;
import skhu.cse.network.omni_stadium.MainActivity;
import skhu.cse.network.omni_stadium.OmniApplication;
import skhu.cse.network.omni_stadium.R;
import skhu.cse.network.omni_stadium.ViewHolderHelper;

public class OrderListActivity extends AppCompatActivity {

    private CartManager cartManager;
    private LoadingDialog lDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_list);
        cartManager=(CartManager)getIntent().getSerializableExtra("CartManager");

        ListView lvCart=(ListView)findViewById(R.id.lvOrderList);
        final ArrayList<OrderItem> orderList=cartManager.getArrList();
        OrderListAdapter adapter = new OrderListAdapter(OrderListActivity.this, R.layout.order_list_child, orderList);
        lvCart.setAdapter(adapter);

        TextView tvAllPrice=(TextView)findViewById(R.id.tvAllPrice);
        String strAllPrice=String.valueOf(cartManager.getAllPrice())+"원";
        tvAllPrice.setText(strAllPrice);

        Button btOrder=(Button)findViewById(R.id.btOrder);

        lDialog = new LoadingDialog(OrderListActivity.this, "결제중...");

        btOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lDialog.show();
                new OrderTask().execute(orderList);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent();
                setResult(OrderActivity.RESULT_ORDERLISTACITIVITY_BACK, intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(OrderActivity.RESULT_ORDERLISTACITIVITY_BACK, intent);
        finish();
    }

    private class OrderListAdapter extends ArrayAdapter<OrderItem>
    {
        public OrderListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<OrderItem> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView==null)
                convertView=getLayoutInflater().inflate(R.layout.order_list_child, null);

            final OrderItem item=getItem(position);

            TextView tvItemName=ViewHolderHelper.get(convertView, R.id.tvItemName);
            tvItemName.setText(item.getMenu_name());

            TextView tvPrice=ViewHolderHelper.get(convertView, R.id.tvPrice);
            String strPrice=String.valueOf(item.getMenu_price())+"원";
            tvPrice.setText(strPrice);

            TextView tvCount=ViewHolderHelper.get(convertView, R.id.tvCount);
            String strCount="× "+String.valueOf(item.getMenu_count());
            tvCount.setText(strCount);

            TextView tvSumPrice=ViewHolderHelper.get(convertView, R.id.tvSumPrice);
            String strSumPrice=item.getMenu_price()*item.getMenu_count()+"원";
            tvSumPrice.setText(strSumPrice);

            return convertView;
        }
    }

    private class OrderTask extends AsyncTask<ArrayList<OrderItem>, Void, JSONObject>
    {
        @Override
        protected JSONObject doInBackground(ArrayList<OrderItem>... params) {
            URL url = null;
            HttpURLConnection httpCon = null;
            JSONObject getJSON = null;

            try {
                url = new URL("http://192.168.63.25:51223/AndroidClientFoodOrderRequestPost/Order");
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
                outJson.put("아이디", ((OmniApplication)getApplicationContext()).getMem_id());
                outJson.put("총액", cartManager.getAllPrice());
                JSONArray orderListJSONArr=new JSONArray();
                for(int i=0; i<params[0].size(); ++i)
                {
                    OrderItem item=params[0].get(i);

                    JSONObject childJSONObject=new JSONObject();
                    childJSONObject.put("food_id", item.getFood_id()+1);
                    childJSONObject.put("menu_id", item.getMenu_id()+1);
                    childJSONObject.put("quantity", item.getMenu_count());

                    orderListJSONArr.put(childJSONObject);
                }
                outJson.put("리스트", orderListJSONArr);

                OutputStream out = new BufferedOutputStream(httpCon.getOutputStream());
                Log.v("outJson", outJson.toString());
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
                e.printStackTrace();
            } finally {
                httpCon.disconnect();
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return getJSON;
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            super.onPostExecute(jsonObject);
            try
            {
                lDialog.dismiss();
                if(jsonObject!=null)
                {
                    int result = jsonObject.getInt("결과");
                    if (result == 0) {
                        Toast.makeText(OrderListActivity.this, "주문이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        Toast.makeText(OrderListActivity.this, "주문이 실패하였습니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        setResult(RESULT_CANCELED, intent);
                        finish();
                    }
                }
                else
                {
                    Toast.makeText(OrderListActivity.this, "주문이 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    setResult(RESULT_CANCELED, intent);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
