package skhu.cse.network.omni_stadium.Ordering;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import skhu.cse.network.omni_stadium.R;
import skhu.cse.network.omni_stadium.ViewHolderHelper;

public class CartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        CartManager cartManager=(CartManager)getIntent().getSerializableExtra("CartManager");

        ListView lvCart=(ListView)findViewById(R.id.lvCart);
        ArrayList<OrderItem> cartList=cartManager.getArrList();
        CartListAdapter adapter = new CartListAdapter(CartActivity.this, R.layout.cart_list_child, cartList);
        lvCart.setAdapter(adapter);
    }

    private class CartListAdapter extends ArrayAdapter<OrderItem>
    {
        public CartListAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<OrderItem> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if(convertView==null)
            {
                convertView=getLayoutInflater().inflate(R.layout.cart_list_child, parent);
                Button btRemoveItem=(Button)findViewById(R.id.btRemoveItem);
                btRemoveItem.setBackgroundResource(R.drawable.button_x);
            }

            final OrderItem item=getItem(position);

            Button btRemoveItem=ViewHolderHelper.get(convertView, R.id.btRemoveItem);
            btRemoveItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    remove(item);
                }
            });

            TextView tvItemName=ViewHolderHelper.get(convertView, R.id.tvItemName);
            tvItemName.setText(item.getMenu_name());

            TextView tvPrice=ViewHolderHelper.get(convertView, R.id.tvPrice);
            String strPrice=item.getMenu_price()+"원";
            tvPrice.setText(strPrice);

            final TextView tvCount=ViewHolderHelper.get(convertView, R.id.tvCount);
            String strCount=String.valueOf(item.getMenu_count());
            tvCount.setText(strCount);

            final TextView tvSumPrice=ViewHolderHelper.get(convertView, R.id.tvSumPrice);
            String strSumPrice=item.getMenu_price()*item.getMenu_count()+"원";
            tvSumPrice.setText(strSumPrice);

            Button btMinus=ViewHolderHelper.get(convertView, R.id.btMinus);
            btMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(item.getMenu_count()>1)
                    {
                        item.setMenu_count(item.getMenu_count() - 1);
                        tvCount.setText(item.getMenu_count());
                        String strSumPrice=item.getMenu_price()*item.getMenu_count()+"원";
                        tvSumPrice.setText(strSumPrice);
                    }
                }
            });

            Button btPlus=ViewHolderHelper.get(convertView, R.id.btPlus);
            btPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.setMenu_count(item.getMenu_count()+1);
                    tvCount.setText(item.getMenu_count());
                    String strSumPrice=item.getMenu_price()*item.getMenu_count()+"원";
                    tvSumPrice.setText(strSumPrice);
                }
            });

            return convertView;
        }
    }
}
