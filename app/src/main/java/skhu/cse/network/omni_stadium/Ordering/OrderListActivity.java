package skhu.cse.network.omni_stadium.Ordering;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import skhu.cse.network.omni_stadium.R;
import skhu.cse.network.omni_stadium.ViewHolderHelper;

public class OrderListActivity extends AppCompatActivity {

    private CartManager cartManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_list);
        cartManager=(CartManager)getIntent().getSerializableExtra("CartManager");

        ListView lvCart=(ListView)findViewById(R.id.lvOrderList);
        ArrayList<OrderItem> orderList=cartManager.getArrList();
        OrderListAdapter adapter = new OrderListAdapter(OrderListActivity.this, R.layout.order_list_child, orderList);
        lvCart.setAdapter(adapter);

        TextView tvAllPrice=(TextView)findViewById(R.id.tvAllPrice);
        String strAllPrice=String.valueOf(cartManager.getAllPrice())+"원";
        tvAllPrice.setText(strAllPrice);

        Button btOrder=(Button)findViewById(R.id.btOrder);
        btOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OrderListActivity.this, "주문 완료", Toast.LENGTH_SHORT).show();
            }
        });
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
}
