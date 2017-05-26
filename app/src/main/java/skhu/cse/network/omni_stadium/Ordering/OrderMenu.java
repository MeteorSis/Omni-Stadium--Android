package skhu.cse.network.omni_stadium.Ordering;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import skhu.cse.network.omni_stadium.R;

public class OrderMenu extends AppCompatActivity {

    private int mCount=1; //메뉴 수량을 저장하는 변수
    private String strAllPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_menu_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView tvMenuName=(TextView)findViewById(R.id.tvMenuName);
        TextView tvMenuInfo=(TextView)findViewById(R.id.tvMenuInfo);
        TextView tvMenuPrice=(TextView)findViewById(R.id.tvMenuPrice);
        final TextView tvCount=(TextView)findViewById(R.id.tvCount);
        Button btMinus=(Button)findViewById(R.id.btMinus);
        Button btPlus=(Button)findViewById(R.id.btPlus);
        final TextView tvAllPrice=(TextView)findViewById(R.id.tvAllPrice);
        Button btCart=(Button)findViewById(R.id.btCart);

        final OrderItem orderItem=(OrderItem)getIntent().getSerializableExtra("OrderItem");

        tvMenuName.setText(orderItem.getMenu_name());
        tvMenuInfo.setText(orderItem.getMenu_info());
        final int menu_price=orderItem.getMenu_price();
        String strMenu_price=menu_price+"원";
        tvMenuPrice.setText(strMenu_price);
        strAllPrice=mCount*menu_price+"원";
        tvAllPrice.setText(strAllPrice);

        btMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(--mCount<0)
                {
                    mCount=0;
                    return;
                }
                String strCount=""+mCount;
                tvCount.setText(strCount);
                strAllPrice=mCount*menu_price+"원";
                tvAllPrice.setText(strAllPrice);
            }
        });

        btPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++mCount;
                String strCount=""+mCount;
                tvCount.setText(strCount);
                strAllPrice=mCount*menu_price+"원";
                tvAllPrice.setText(strAllPrice);
            }
        });

        btCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sIntent=new Intent();
                orderItem.setMenu_count(mCount);
                sIntent.putExtra("OrderItem", orderItem);
                setResult(RESULT_OK, sIntent);
                finish();
            }
        });
    }
}
