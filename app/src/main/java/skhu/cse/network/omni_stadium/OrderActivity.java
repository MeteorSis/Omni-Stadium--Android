package skhu.cse.network.omni_stadium;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;


public class OrderActivity extends AppCompatActivity{

    private ArrayList<String> group_list = new ArrayList<String>();
    private HashMap<String, ArrayList<Itemlist>> item_list = new HashMap<String, ArrayList<Itemlist>>();
    private  ExpandableListAdapter listAdapter;
    private String name[];                        //전달받은 이름 데이터를 저장하는 배열
    private int count[];                        // 전달받은 수량 데이터를 저장하는 배열
    private int price[];                       // 전달받은 가격 데이터를 저장하는 배열

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button order_basket = (Button)findViewById(R.id.btbasket);
        Button order = (Button)findViewById(R.id.btorder);
        ExpandableListView lvorder = (ExpandableListView)findViewById(R.id.explv_order);
        group_list.add("치킨");
        group_list.add("피자");
        group_list.add("햄버거");
        group_list.add("맥주");

        ArrayList<Itemlist> chicken = new ArrayList<Itemlist>();

        chicken.add(new Itemlist("후라이드치킨", "15000원"));
        chicken.add(new Itemlist("양념치킨", "16000원"));
        chicken.add(new Itemlist("간장치킨", "16000원"));
        chicken.add(new Itemlist("반반치킨", "16000원"));
        chicken.add(new Itemlist("파닭", "17000원"));
        chicken.add(new Itemlist("핫치킨", "18000원"));
        chicken.add(new Itemlist("커리치킨", "18000원"));
        chicken.add(new Itemlist("곡물치킨", "18000원"));
        chicken.add(new Itemlist("허니갈릭스치킨", "19000원"));
        chicken.add(new Itemlist("치즐링치킨", "19000원"));

        ArrayList<Itemlist> pizza = new ArrayList<Itemlist>();

        pizza.add(new Itemlist("콤비네이션피자", "7900원"));
        pizza.add(new Itemlist("페퍼로니피자", "7900원"));
        pizza.add(new Itemlist("치즈피자", "7900원"));
        pizza.add(new Itemlist("불고기피자", "8900원"));
        pizza.add(new Itemlist("포테이토피자", "8900원"));
        pizza.add(new Itemlist("고구마피자", "9900원"));
        pizza.add(new Itemlist("도이치소시지피자", "9900원"));
        pizza.add(new Itemlist("스위트골드피자", "9900원"));
        pizza.add(new Itemlist("핫스파이스피자", "10900원"));
        pizza.add(new Itemlist("바이트골드피자", "12900원"));

        ArrayList<Itemlist> hamburger = new ArrayList<Itemlist>();

        hamburger.add(new Itemlist("햄버거", "2700원"));
        hamburger.add(new Itemlist("불고기 버거", "3000원"));
        hamburger.add(new Itemlist("치즈 버거", "3000원"));
        hamburger.add(new Itemlist("크런치치킨 버거", "4300원"));
        hamburger.add(new Itemlist("와일드갈릭 버거", "2900원"));
        hamburger.add(new Itemlist("갈릭스테이크 버거", "6700원"));
        hamburger.add(new Itemlist("와퍼", "5600원"));
        hamburger.add(new Itemlist("불고기 와퍼", "5600원"));
        hamburger.add(new Itemlist("치즈 와퍼", "6200원"));
        hamburger.add(new Itemlist("통새우 와퍼", "6500원"));

        ArrayList<Itemlist> beer = new ArrayList<Itemlist>();

        beer.add(new Itemlist("생맥주", "3000원"));
        beer.add(new Itemlist("크림 생맥주", "3500원"));
        beer.add(new Itemlist("더치 맥주", "4500원"));
        beer.add(new Itemlist("자몽 맥주", "4500원"));
        beer.add(new Itemlist("라임 맥주", "4500원"));
        beer.add(new Itemlist("청포도 맥주", "4500원"));
        beer.add(new Itemlist("병맥주", "5000원"));

        listAdapter = new Order_ExplvAdapter(this, group_list, item_list);
        lvorder.setAdapter(listAdapter);

        item_list.put(group_list.get(0), chicken);
        item_list.put(group_list.get(1), pizza);
        item_list.put(group_list.get(2),  hamburger);
        item_list.put(group_list.get(3),  beer);

        lvorder.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                switch (groupPosition) {
                    case 0:
                        if (childPosition == 0) {
                            Intent intent = new Intent(getApplicationContext(),FriedChicken.class);
                            startActivity(intent);
                        } else if (childPosition == 1) {
                            Toast.makeText(getApplicationContext(), "양념 치킨 ", Toast.LENGTH_SHORT).show();
                        } else if (childPosition == 2) {
                            Toast.makeText(getApplicationContext(), "간장 치킨 ", Toast.LENGTH_SHORT).show();
                        } else if (childPosition == 3) {
                            Toast.makeText(getApplicationContext(), "반반 치킨 ", Toast.LENGTH_SHORT).show();
                        } else if (childPosition == 3) {
                            Toast.makeText(getApplicationContext(), "반반 치킨 ", Toast.LENGTH_SHORT).show();
                        }

                        break;

                    case 1:
                        Toast.makeText(getApplicationContext(), "피자 메뉴", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        Toast.makeText(getApplicationContext(), "햄버거 메뉴", Toast.LENGTH_SHORT).show();
                        break;
                    case 3:
                        Toast.makeText(getApplicationContext(), "맥주 메뉴", Toast.LENGTH_SHORT).show();
                        break;
                }
                return false;
            }
        });
        Intent gtintent =  getIntent();
        name = gtintent.getStringArrayExtra("chicken_name");

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrderListActivity.class);
                startActivity(intent);

            }
        });
    }
}
