package skhu.cse.network.omni_stadium.Ordering;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import skhu.cse.network.omni_stadium.LoadingDialog;
import skhu.cse.network.omni_stadium.R;
import skhu.cse.network.omni_stadium.ViewHolderHelper;

public class CartActivity extends AppCompatActivity {

    private CartManager cartManager;
    private int allPrice;
    private TextView tvAllPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        cartManager=(CartManager)getIntent().getSerializableExtra("CartManager");

        ListView lvCart=(ListView)findViewById(R.id.lvCart);
        ArrayList<OrderItem> cartList=cartManager.getArrList();
        CartListAdapter adapter = new CartListAdapter(CartActivity.this, R.layout.cart_list_child, cartList);
        lvCart.setAdapter(adapter);

        tvAllPrice=(TextView)findViewById(R.id.tvAllPrice);
        allPrice=cartManager.getAllPrice();
        String strAllPrice=String.valueOf(allPrice)+"원";
        tvAllPrice.setText(strAllPrice);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent sIntent=new Intent();
                sIntent.putExtra("CartManager", cartManager);
                setResult(RESULT_OK, sIntent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
                convertView=getLayoutInflater().inflate(R.layout.cart_list_child, null);
                //Button btRemoveItem=(Button)convertView.findViewById(R.id.btRemoveItem);
                //btRemoveItem.setBackgroundResource(R.drawable.button_x);
            }

            final OrderItem item=getItem(position);

            Button btRemoveItem=ViewHolderHelper.get(convertView, R.id.btRemoveItem);
            btRemoveItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    allPrice-=item.getMenu_price()*item.getMenu_count();
                    String strAllPrice=String.valueOf(allPrice)+"원";
                    tvAllPrice.setText(strAllPrice);
                    cartManager.removeOrderItem(item);
                    remove(item);
                    if(isEmpty())
                    {
                        Toast.makeText(CartActivity.this, "장바구니가 비었습니다.", Toast.LENGTH_SHORT).show();
                        Intent sIntent=new Intent();
                        sIntent.putExtra("CartManager", cartManager);
                        setResult(RESULT_OK, sIntent);
                        finish();
                    }
                }
            });

            TextView tvItemName=ViewHolderHelper.get(convertView, R.id.tvItemName);
            tvItemName.setText(item.getMenu_name());

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
                        tvCount.setText(String.valueOf(item.getMenu_count()));
                        String strSumPrice=item.getMenu_price()*item.getMenu_count()+"원";
                        tvSumPrice.setText(strSumPrice);

                        allPrice-=item.getMenu_price();
                        String strAllPrice=String.valueOf(allPrice)+"원";
                        tvAllPrice.setText(strAllPrice);
                    }
                }
            });

            Button btPlus=ViewHolderHelper.get(convertView, R.id.btPlus);
            btPlus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.setMenu_count(item.getMenu_count()+1);
                    tvCount.setText(String.valueOf(item.getMenu_count()));
                    String strSumPrice=item.getMenu_price()*item.getMenu_count()+"원";
                    tvSumPrice.setText(strSumPrice);

                    allPrice+=item.getMenu_price();
                    String strAllPrice=String.valueOf(allPrice)+"원";
                    tvAllPrice.setText(strAllPrice);
                }
            });

            return convertView;
        }
    }

    @Override
    public void onBackPressed() {
        Intent sIntent=new Intent();
        sIntent.putExtra("CartManager", cartManager);
        setResult(RESULT_OK, sIntent);
        finish();
    }
}
