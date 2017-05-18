package skhu.cse.network.omni_stadium;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class FriedChicken extends AppCompatActivity {
    private  int fcount; //치킨 수량을 저장하는 변수
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_fried_chicken);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        

    }
}
