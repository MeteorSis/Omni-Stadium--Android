package skhu.cse.network.omni_stadium;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class OrderListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로 가기 버튼
        ListView orderlist = (ListView)findViewById(R.id.lvorderlist);
    }
}
