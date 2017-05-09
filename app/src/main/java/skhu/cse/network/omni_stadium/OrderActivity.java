package skhu.cse.network.omni_stadium;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class OrderActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_main);
        ImageView Chicken = (ImageView)findViewById(R.id.ivChicken);
        ImageView Pizza = (ImageView)findViewById(R.id.ivPizza);
        ImageView Beer = (ImageView)findViewById(R.id.ivBeer);
        ImageView Hamburger = (ImageView)findViewById(R.id.ivHamburger);

        Chicken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Cintent = new Intent(getApplicationContext(),ChickenActivity.class);
                startActivity(Cintent);
            }
        });

       Pizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Pintent = new Intent(getApplicationContext(),MenuActivity.class);
                startActivity(Pintent);
            }
        });

       Beer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Bintent = new Intent(getApplicationContext(),MenuActivity.class);
                startActivity(Bintent);
            }
        });

       Hamburger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Hintent = new Intent(getApplicationContext(),MenuActivity.class);
                startActivity(Hintent);
            }
        });





    }

}
