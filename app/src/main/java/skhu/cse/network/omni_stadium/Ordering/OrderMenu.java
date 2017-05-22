package skhu.cse.network.omni_stadium.Ordering;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import skhu.cse.network.omni_stadium.R;

public class OrderMenu extends AppCompatActivity {
    private  int mCount=1; //후라이드 치킨 수량을 저장하는 변수
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_menu_info);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Button minus = (Button)findViewById(R.id.btminus);
        final TextView count = (TextView)findViewById(R.id.tvCountinfo);
        final Button basket = (Button)findViewById(R.id.btbasket);
        final TextView name = (TextView) findViewById(R.id.tvmenu_name);
        final TextView info = (TextView)findViewById(R.id.tvmenu_info);
        final TextView price = (TextView)findViewById(R.id.tvmenu_price);
        final TextView all_price = (TextView)findViewById(R.id.tvall_price);
        Button plus = (Button)findViewById(R.id.btplus);


        Intent intent = getIntent();
        String cname = intent.getStringExtra("menu_name");
        String cinfo = intent.getStringExtra("menu_info");
        final int cprice = intent.getIntExtra("menu_price",16000);
        final int call_price = intent.getIntExtra("menu_allprice",16000);
        price.setText(cprice+"원");
        name.setText(cname);
        info.setText(cinfo);
        count.setText(""+mCount);
        all_price.setText(mCount*cprice+"원");

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(--mCount<0)
                {
                    mCount=0;
                    return;
                }
                count.setText(""+mCount);
                all_price.setText(mCount*cprice+"원");
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++mCount;
                count.setText(""+mCount);
                all_price.setText(mCount*cprice+"원");
            }
        });

        basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
