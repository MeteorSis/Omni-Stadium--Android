package skhu.cse.network.omni_stadium;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class OrderActivity extends AppCompatActivity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_main);
        ImageView Chicken = (ImageView)findViewById(R.id.ivChicken);
        Glide.with(this).load(R.drawable.chicken).into(Chicken);
        ImageView Pizza = (ImageView)findViewById(R.id.ivPizza);
        Glide.with(this).load(R.drawable.pizza).into(Pizza);
        ImageView Beer = (ImageView)findViewById(R.id.ivBeer);
        Glide.with(this).load(R.drawable.beer).into(Beer);
        ImageView Hamburger = (ImageView)findViewById(R.id.ivHamburger);
        Glide.with(this).load(R.drawable.burger).into(Hamburger);

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
