package skhu.cse.network.omni_stadium;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class FriedChicken extends AppCompatActivity {
    private  int fcount=1; //치킨 수량을 저장하는 변수
    private  int fprice = 15000; // 치킨 가격을 저장하는 변수
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_fried_chicken);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Button minus = (Button)findViewById(R.id.btminus);
        final TextView count = (TextView)findViewById(R.id.tvCountinfo);
        final TextView allprice = (TextView)findViewById(R.id.tvall_price);
        final Button basket = (Button)findViewById(R.id.btbasket);
        final String menu_name[] = {"후라이드 치킨","양념치킨"};
        Button plus = (Button)findViewById(R.id.btplus);
        count.setText(""+fcount);
        allprice.setText(fprice+"원");

        minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(--fcount<0)
                {
                    fcount=0;
                    return;
                }
                count.setText(""+fcount);
                allprice.setText(fprice*fcount+"원");
            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fcount;
                count.setText(""+fcount);
                allprice.setText(fprice*fcount+"원");
            }
        });

        basket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent intent = new Intent();
             intent.putExtra("chicken_name",menu_name);
             intent.putExtra("chicken_count",fcount);
             intent.putExtra("chicken_price",fcount*fprice);
             setResult(RESULT_OK, intent);
             finish();
            }
        });
    }
}
