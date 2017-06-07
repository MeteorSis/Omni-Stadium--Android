package skhu.cse.network.omni_stadium.Ordering;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
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
import java.util.HashMap;

import skhu.cse.network.omni_stadium.R;

public class OrderActivity extends AppCompatActivity{

    private ArrayList<String> group_list = new ArrayList<String>();
    private HashMap<String, ArrayList<OrderItem>> item_list = new HashMap<String, ArrayList<OrderItem>>();
    private ExpandableListAdapter listAdapter;
    private ExpandableListView lvOrder;
    static final int REQ_CODE_ORDERMENU =0;
    static final int REQ_CODE_CARTACTIVITY =1;
    private CartManager cartManager;
    private Button btCart, btOrder;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_main);
        btCart = (Button)findViewById(R.id.btCart);
        btOrder = (Button)findViewById(R.id.btOrder);
        lvOrder = (ExpandableListView)findViewById(R.id.explv_order);

        new GetFoodListTask().execute("메뉴요청");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        NavUtils.navigateUpFromSameTask(this);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode){
            case REQ_CODE_ORDERMENU:
                if(resultCode==RESULT_OK)
                    cartManager.addOrderItem((OrderItem)data.getSerializableExtra("OrderItem"));
                break;
            case REQ_CODE_CARTACTIVITY:
                if(resultCode==RESULT_OK)
                    cartManager=(CartManager)data.getSerializableExtra("CartManager");
                break;
        }
    }

    private class GetFoodListTask extends AsyncTask<String, Void, JSONArray>
    {
        @Override
        protected JSONArray doInBackground(String... params) {
            URL url = null;
            HttpURLConnection httpCon = null;
            JSONArray getJSONArray = null;

            try {
                url = new URL("http://192.168.63.25:51223/AndroidClientFoodOrderRequestPost/GetMenu");
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
                outJson.put(params[0], params[0]);

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
                    getJSONArray = new JSONArray(result.toString());
                }
            } catch (Exception e) {
            } finally {
                httpCon.disconnect();
            }
            return getJSONArray;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);
            try {
                int rowSizeJSONArray=jsonArray.length();
                final ArrayList<ArrayList<OrderItem>> foodList=new ArrayList<>();
                OrderItem[][] cart=new OrderItem[rowSizeJSONArray][];
                for(int row=0; row<rowSizeJSONArray; ++row)
                {
                    ArrayList<OrderItem> childFoodList=new ArrayList<>();
                    int colSizeJSONArray=jsonArray.getJSONArray(row).length();
                    cart[row]=new OrderItem[colSizeJSONArray];

                    for(int col=0; col<colSizeJSONArray; ++col)
                    {
                        JSONObject foodObject=jsonArray.getJSONArray(row).getJSONObject(col);
                        childFoodList.add(
                                new OrderItem(
                                        foodObject.getString("food_name"),
                                        foodObject.getInt("food_id")-1,
                                        foodObject.getInt("menu_id")-1,
                                        foodObject.getString("menu_name"),
                                        foodObject.getInt("menu_price"),
                                        foodObject.getString("menu_info").replace("\\n", "\n"),
                                        foodObject.getInt("menu_stock")));

                    }
                    foodList.add(childFoodList);
                    String food_name=childFoodList.get(0).getFood_name();
                    group_list.add(food_name);
                    item_list.put(food_name, childFoodList);
                }

                listAdapter = new Order_ExplvAdapter(OrderActivity.this, group_list, item_list);
                lvOrder.setAdapter(listAdapter);

                cartManager=new CartManager(cart);

                lvOrder.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                        OrderItem item=foodList.get(groupPosition).get(childPosition);
                        if(item.getMenu_count()>0)
                        {
                            Intent intent = new Intent(OrderActivity.this, OrderMenu.class);
                            intent.putExtra("OrderItem", item);
                            startActivityForResult(intent, REQ_CODE_ORDERMENU);
                        }
                        return false;
                    }
                });

                btCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(cartManager.getItemCount()>0)
                        {
                            Intent intent = new Intent(OrderActivity.this, CartActivity.class);
                            intent.putExtra("CartManager", cartManager);
                            startActivityForResult(intent, REQ_CODE_CARTACTIVITY);
                        }
                        else
                            Toast.makeText(OrderActivity.this, "장바구니가 비어있습니다.", Toast.LENGTH_SHORT).show();
                    }
                });

                btOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(cartManager.getAllPrice()>=10000)
                        {
                            Intent intent = new Intent(OrderActivity.this, OrderListActivity.class);
                            intent.putExtra("CartManager", cartManager);
                            startActivity(intent);
                        }
                        else
                            Toast.makeText(OrderActivity.this, "만 원 이상부터 주문 가능합니다.", Toast.LENGTH_SHORT).show();
                    }
                });

            } catch (Exception e) {

            }
        }
    }
}