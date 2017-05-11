package skhu.cse.network.omni_stadium;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderActivity extends AppCompatActivity{

    private ArrayList<String> parent_menu = new ArrayList<String>();
    private HashMap<String, ArrayList<String>> child_menu = new HashMap<String, ArrayList<String>>();

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ExpandableListView lvorder = (ExpandableListView)findViewById(R.id.explv_order);
        parent_menu.add("치킨");
        parent_menu.add("피자");
        parent_menu.add("햄버거");
        parent_menu.add("맥주");


        ArrayList<String> chicken = new ArrayList<String>();
        chicken.add("후라이드치킨");
        chicken.add("양념치킨");
        chicken.add("간장치킨");
        chicken.add("반반치킨");
        chicken.add("파닭");
        chicken.add("핫치킨");
        chicken.add("커리치킨");
        chicken.add("곡물치킨");
        chicken.add("허니갈릭스치킨");
        chicken.add("치즐링치킨");

        ArrayList<String> pizza = new ArrayList<String>();
        pizza.add(" 콤비네이션피자");
        pizza.add("페퍼로니피자");
        pizza.add("치즈피자");
        pizza.add("불고기피자");
        pizza.add("포테이토피자");
        pizza.add("고구마피자");
        pizza.add("도이치소시지피자");
        pizza.add("스위트골드피자");
        pizza.add("핫스파이스피자");
        pizza.add("바이트골드피자");

        ArrayList<String> hamburger = new ArrayList<String>();
        hamburger.add("햄버거");
        hamburger.add("불고기 버거");
        hamburger.add("치즈 버거");
        hamburger.add("크런치치킨 버거");
        hamburger.add("와일드갈릭 버거");
        hamburger.add("갈릭스테이크 버거");
        hamburger.add("와퍼");
        hamburger.add("불고기 와퍼");
        hamburger.add("치즈 와퍼");
        hamburger.add("통새우 와퍼");


        ArrayList<String> beer = new ArrayList<String>();
        beer.add("생맥주");
        beer.add("크림 생맥주");
        beer.add("더치 맥주");
        beer.add("자몽 맥주");
        beer.add("라임 맥주");
        beer.add("청포도 맥주");
        beer.add("병맥주");

        child_menu.put(parent_menu.get(0), chicken);
        child_menu.put(parent_menu.get(1), pizza);
        child_menu.put(parent_menu.get(2),  hamburger);
        child_menu.put(parent_menu.get(3),  beer);
        lvorder.setAdapter(new ExplvAdapter(this, parent_menu, child_menu));





    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
