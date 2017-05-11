package skhu.cse.network.omni_stadium;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ChickenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_chicken_main);
        ImageView fchick = (ImageView)findViewById(R.id.ivfchicken);
        ImageView ychick = (ImageView)findViewById(R.id.ivychicken);
        ImageView gchick = (ImageView)findViewById(R.id.ivgchicken);
        ImageView hchick = (ImageView)findViewById(R.id.ivhalfchicken);
        ImageView hotchick = (ImageView)findViewById(R.id.ivhotchicken);
        ImageView pachick = (ImageView)findViewById(R.id.ivpachicken);
        ImageView currychick = (ImageView)findViewById(R.id.ivcurrychicken);
        ImageView gokmul = (ImageView)findViewById(R.id.ivgokmul);
        ImageView honey = (ImageView)findViewById(R.id.ivhoney);
        ImageView cheese = (ImageView)findViewById(R.id.ivcheese);


        Glide.with(this).load(R.drawable.fchicken).into(fchick);
        Glide.with(this).load(R.drawable.ychicken).into(ychick);
        Glide.with(this).load(R.drawable.gchicken).into(gchick);
        Glide.with(this).load(R.drawable.halfchicken).into(hchick);
        Glide.with(this).load(R.drawable.papachick).into(pachick);
        Glide.with(this).load(R.drawable.size_hotchicken).into(hotchick);
        Glide.with(this).load(R.drawable.size_currychicken).into(currychick);
        Glide.with(this).load(R.drawable.size_gokmul).into(gokmul);
        Glide.with(this).load(R.drawable.size_honeychicken).into(honey);
        Glide.with(this).load(R.drawable.size_cheese).into(cheese);


    }
}
