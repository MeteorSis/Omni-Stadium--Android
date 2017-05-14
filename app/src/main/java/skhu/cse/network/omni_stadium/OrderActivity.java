package skhu.cse.network.omni_stadium;

import android.database.DataSetObserver;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;


public class OrderActivity extends AppCompatActivity{

    private ArrayList<String> group_list = new ArrayList<String>();
    private HashMap<String, ArrayList<String>> item_list = new HashMap<String, ArrayList<String>>();
    private  ExpandableListAdapter listAdapter;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        ExpandableListView lvorder = (ExpandableListView)findViewById(R.id.explv_order);
        group_list.add("치킨");
        group_list.add("피자");
        group_list.add("햄버거");
        group_list.add("맥주");



        ArrayList<String> chicken = new ArrayList<String>();
    /*    ArrayList<String> chicken_cost = new ArrayList<String>();*/

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

        /*chicken_cost.add("15000원");
        chicken_cost.add("16000원");
        chicken_cost.add("16000원");
        chicken_cost.add("16000원");
        chicken_cost.add("17000원");
        chicken_cost.add("18000원");
        chicken_cost.add("18000원");
        chicken_cost.add("18000원");
        chicken_cost.add("19000원");
        chicken_cost.add("19000원");*/

        ArrayList<String> pizza = new ArrayList<String>();
        /*ArrayList<String> pizza_cost = new ArrayList<String>();*/
        pizza.add("콤비네이션피자");
        pizza.add("페퍼로니피자");
        pizza.add("치즈피자");
        pizza.add("불고기피자");
        pizza.add("포테이토피자");
        pizza.add("고구마피자");
        pizza.add("도이치소시지피자");
        pizza.add("스위트골드피자");
        pizza.add("핫스파이스피자");
        pizza.add("바이트골드피자");

        /*pizza_cost.add("7900원");
        pizza_cost.add("7900원");
        pizza_cost.add("7900원");
        pizza_cost.add("8900원");
        pizza_cost.add("8900원");
        pizza_cost.add("9900원");
        pizza_cost.add("9900원");
        pizza_cost.add("9900원");
        pizza_cost.add("10900원");
        pizza_cost.add("12000원");*/

        ArrayList<String> hamburger = new ArrayList<String>();
        ArrayList<String> hamburger_cost = new ArrayList<String>();
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

     /*   hamburger_cost.add("2700원");
        hamburger_cost.add("3000원");
        hamburger_cost.add("3000원");
        hamburger_cost.add("4300원");
        hamburger_cost.add("2900원");
        hamburger_cost.add("6700원");
        hamburger_cost.add("5600원");
        hamburger_cost.add("5600원");
        hamburger_cost.add("6200원");
        hamburger_cost.add("6500원");*/


        ArrayList<String> beer = new ArrayList<String>();
      /*  ArrayList<String> beer_cost = new ArrayList<String>();*/

        beer.add("생맥주");
        beer.add("크림 생맥주");
        beer.add("더치 맥주");
        beer.add("자몽 맥주");
        beer.add("라임 맥주");
        beer.add("청포도 맥주");
        beer.add("병맥주");

       /* beer_cost.add("3000원");
        beer_cost.add("3500원");
        beer_cost.add("4500원");
        beer_cost.add("4500원");
        beer_cost.add("4500원");
        beer_cost.add("4500원");
        beer_cost.add("5000원");*/

        listAdapter = new Order_ExplvAdapter(this, group_list, item_list);
        lvorder.setAdapter(listAdapter);

        item_list.put(group_list.get(0), chicken);
        item_list.put(group_list.get(1), pizza);
        item_list.put(group_list.get(2),  hamburger);
        item_list.put(group_list.get(3),  beer);




        lvorder.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
             /*   ExpandableListAdapter adapter = parent.getExpandableListAdapter();
                Map<String, Object> childMap = (Map<String, Object>) adapter.getChild(groupPosition, childPosition);*/

                if(groupPosition == 0)
                {
                    Toast.makeText(getApplicationContext(),"치킨 메뉴",Toast.LENGTH_SHORT).show();
                }

                else if(groupPosition == 1)
                {
                    Toast.makeText(getApplicationContext(),"피자 메뉴",Toast.LENGTH_SHORT).show();
                }

                else  if(groupPosition == 2)
                {
                    Toast.makeText(getApplicationContext(),"햄버거 메뉴",Toast.LENGTH_SHORT).show();
                }

                else
                {
                    Toast.makeText(getApplicationContext(),"맥주 메뉴",Toast.LENGTH_SHORT).show();
                }

                return false;
            }
        });
    }
}
