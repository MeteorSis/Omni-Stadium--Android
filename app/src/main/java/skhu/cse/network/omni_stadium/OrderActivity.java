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
    static final int REQ_CODE_CHICKEN_0 =0;
    static final int REQ_CODE_PIZZA_1 = 1;
    static final int REQ_CODE_HAMBURGER_2 = 2;
    static final int REQ_CODE_BEER_3 = 3;
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

        final ArrayList<Itemlist> chicken = new ArrayList<Itemlist>();

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
        hamburger.add(new Itemlist("불고기버거", "3000원"));
        hamburger.add(new Itemlist("치즈버거", "3000원"));
        hamburger.add(new Itemlist("크런치치킨버거", "4300원"));
        hamburger.add(new Itemlist("와일드갈릭버거", "2900원"));
        hamburger.add(new Itemlist("갈릭스테이크버거", "6700원"));
        hamburger.add(new Itemlist("와퍼", "5600원"));
        hamburger.add(new Itemlist("불고기와퍼", "5600원"));
        hamburger.add(new Itemlist("치즈와퍼", "6200원"));
        hamburger.add(new Itemlist("통새우와퍼", "6500원"));

        ArrayList<Itemlist> beer = new ArrayList<Itemlist>();

        beer.add(new Itemlist("생맥주", "3000원"));
        beer.add(new Itemlist("크림생맥주", "3500원"));
        beer.add(new Itemlist("더치맥주", "4500원"));
        beer.add(new Itemlist("자몽맥주", "4500원"));
        beer.add(new Itemlist("라임맥주", "4500원"));
        beer.add(new Itemlist("청포도맥주", "4500원"));
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
                            Intent intent = new Intent(getApplicationContext(),OrderMenu.class);
                            intent.putExtra("menu_name", "후라이드치킨");
                            intent.putExtra("menu_price", 15000);
                            intent.putExtra("menu_info", "황금빛 파우더가 선사하는 잊을 수 없는 바삭바삭함과 \n"+"육즙 가득 퍼지는 부드러운 속살이 환상적인 맛!");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",15000);
                            startActivityForResult(intent,REQ_CODE_CHICKEN_0);
                        } else if (childPosition == 1) {
                            Intent intent = new Intent(getApplicationContext(),OrderMenu.class);
                            intent.putExtra("menu_name", "양념치킨");
                            intent.putExtra("menu_price", 16000);
                            intent.putExtra("menu_info","새콤달콤한 과실의 산뜻함으로 시작하여 부드러움으로\n"+ "마무리 되는 클래스가 다른 양념 치킨");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",16000);
                            startActivityForResult(intent,REQ_CODE_CHICKEN_0);
                        } else if (childPosition == 2) {
                            Intent intent = new Intent(getApplicationContext(),OrderMenu.class);
                            intent.putExtra("menu_name", "간장치킨");
                            intent.putExtra("menu_price", 16000);
                            intent.putExtra("menu_info","진한 마늘소스와 간장소스로 맛을 낸 쫄깃한 치킨");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",16000);
                            startActivityForResult(intent,REQ_CODE_CHICKEN_0);
                        } else if (childPosition == 3) {
                            Intent intent = new Intent(getApplicationContext(),OrderMenu.class);
                            intent.putExtra("menu_name", "반반치킨");
                            intent.putExtra("menu_price", 16000);
                            intent.putExtra("menu_info","한입 바삭하게 베어물면 느껴지는 고소한 육즙의 후라이드치킨,"+"입안에 퍼지는 부드러운 매콤한 맛의 양념치킨을 반반으로\n"+"한번에 즐겨보세요!");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",16000);
                            startActivityForResult(intent,REQ_CODE_CHICKEN_0);
                        } else if (childPosition == 4) {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "파닭");
                            intent.putExtra("menu_price", 17000);
                            intent.putExtra("menu_info","상큼한 파와 치킨의 환상적인 만남~ 신선하고 상큼한 파와\n"+"치킨이 어우러져 입맛을 살려주는 매력적인 파닭치킨");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",17000);
                            startActivityForResult(intent, REQ_CODE_CHICKEN_0);
                        } else if (childPosition == 5) {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "핫치킨");
                            intent.putExtra("menu_price", 18000);
                            intent.putExtra("menu_info","바삭하게 튀겨낸 치킨을 레드핫 칠리페퍼소스로\n"+"버무린 맛있게 더 매운 치킨 ");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",18000);
                            startActivityForResult(intent, REQ_CODE_CHICKEN_0);
                        }
                         else if (childPosition == 6) {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "커리치킨");
                            intent.putExtra("menu_price", 18000);
                            intent.putExtra("menu_info","정통인도커리와 로스팅한 갈릭시즈닝으로\n"+"입안 가득 진한 커리맛과 향이 더해지는 커리맛 치킨");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",18000);
                            startActivityForResult(intent, REQ_CODE_CHICKEN_0);
                        }
                        else if (childPosition == 7) {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "곡물치킨");
                            intent.putExtra("menu_price", 18000);
                            intent.putExtra("menu_info","깨끗한 해바라기유와 고소한 곡물들이 만났다!" +
                                    "검은콩, 현미, 아몬드, 옥수수 등을 갈아넣은 비스킷 같이 크런키한 식감의\n"+ "고소한 치킨 치킨이 이렇게 바삭하고 고소 할 수가~");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",18000);
                            startActivityForResult(intent, REQ_CODE_CHICKEN_0);
                        }
                        else if (childPosition == 8) {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "허니갈릭스치킨");
                            intent.putExtra("menu_price", 19000);
                            intent.putExtra("menu_info","달콤~한 꿀마늘향 허니갈릭스! 향긋하고 달콤한 아카시아 꿀,\n"+ "알싸한 마늘향이 듬뿍 들어있는 갈릭 간장 소스가 만났다!");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",19000);
                            startActivityForResult(intent, REQ_CODE_CHICKEN_0);
                        }
                        else if (childPosition == 9) {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "치즐링치킨");
                            intent.putExtra("menu_price", 19000);
                            intent.putExtra("menu_info","마스카포네치즈와 체다치즈가 잘 조화된 풍부한 맛의\n"+"치즈 파우더로 듬뿍듬뿍 버무린 치킨으로 입 안 깊숙히\n"+ "풍성하게 스며드는 치즈맛이 특제 파우더로 조리된 치킨과 잘 어우러진 환상의 맛!");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",19000);
                            startActivityForResult(intent, REQ_CODE_CHICKEN_0);
                        }
                        break;

                    case 1:
                        if (childPosition == 0)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "콤비네이션피자");
                            intent.putExtra("menu_price", 7900);
                            intent.putExtra("menu_info","다양한 고기류와 신선한 야채가 들어간 \n"+"대중적으로 가장 사랑받는 피자");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",7900);
                            startActivityForResult(intent,REQ_CODE_PIZZA_1);
                        }
                        else if (childPosition == 1)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "페퍼로니피자");
                            intent.putExtra("menu_price", 7900);
                            intent.putExtra("menu_info","페퍼로니 특유의 향과 짭조름한 풍미가 일품인\n"+"가장 서양적인 피자");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",7900);
                            startActivityForResult(intent,REQ_CODE_PIZZA_1);
                        }
                        else if (childPosition == 2)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "치즈피자");
                            intent.putExtra("menu_price", 7900);
                            intent.putExtra("menu_info","특제 토마토 소스와 고소한콘과 담백한 모짜렐라 치즈가\n"+"듬뿍 들어간 피자");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",7900);
                            startActivityForResult(intent,REQ_CODE_PIZZA_1);
                        }
                        else if (childPosition == 3)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "불고기피자");
                            intent.putExtra("menu_price", 8900);
                            intent.putExtra("menu_info","양념 불고기를 양파와 버섯을 함께 곁들인\n"+"한국적인 맛의 피자");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",8900);
                            startActivityForResult(intent,REQ_CODE_PIZZA_1);
                        }
                        else if (childPosition == 4)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "포테이토피자");
                            intent.putExtra("menu_price", 8900);
                            intent.putExtra("menu_info","담백한 맛의 감자와 옥수수, 버섯 등 각종 야채가 토핑 된\n"+"여성들이 선호하는 피자");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",8900);
                            startActivityForResult(intent,REQ_CODE_PIZZA_1);
                        }
                        else if (childPosition == 5)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "고구마피자");
                            intent.putExtra("menu_price", 9900);
                            intent.putExtra("menu_info","풍성하게 토핑 된 달콤한 고구마와 고소한 치즈와의 만남");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",9900);
                            startActivityForResult(intent,REQ_CODE_PIZZA_1);
                        }
                        else if (childPosition == 6)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "도이치소시지피자");
                            intent.putExtra("menu_price", 9900);
                            intent.putExtra("menu_info","독일식 소시지,그리고 화이트 소스와 스테이크 소스의\n"+"환상적인 궁합");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",9900);
                            startActivityForResult(intent,REQ_CODE_PIZZA_1);
                        }
                        else if (childPosition == 7)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "스위트골드피자");
                            intent.putExtra("menu_price", 9900);
                            intent.putExtra("menu_info","부드럽고 스위트한 고구마 무스를 피자위에 추가해\n"+"더욱 더 맛있게 즐길 수 있는 피자");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",9900);
                            startActivityForResult(intent,REQ_CODE_PIZZA_1);
                        }
                        else if (childPosition == 8)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "핫스파이스피자");
                            intent.putExtra("menu_price", 10900);
                            intent.putExtra("menu_info","숯불바베큐치킨과 매콤한 피자");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",10900);
                            startActivityForResult(intent,REQ_CODE_PIZZA_1);
                        }
                        else if (childPosition == 9)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "바이트골드피자");
                            intent.putExtra("menu_price", 12900);
                            intent.putExtra("menu_info","달콤한 고구마 무스와 고소한 치즈크러스트의 만남,\n"+"빵 가장자리까지! 끝까지 맛있게");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",12900);
                            startActivityForResult(intent,REQ_CODE_PIZZA_1);
                        }

                        break;
                    case 2:
                        if (childPosition == 0)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "햄버거");
                            intent.putExtra("menu_price", 2700);
                            intent.putExtra("menu_info","불에 구운 소고기 패티가 쏙~ 실속 있게 즐긴다!");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",2700);
                            startActivityForResult(intent,REQ_CODE_HAMBURGER_2);
                        }
                        else if (childPosition == 1)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "불고기버거");
                            intent.putExtra("menu_price", 3000);
                            intent.putExtra("menu_info","달콤한 불고기소스를 더한 실속 만점의 버거.\n"+"크기는 깜찍, 맛은 어메이징!");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",3000);
                            startActivityForResult(intent,REQ_CODE_HAMBURGER_2);
                        }
                        else if (childPosition == 2)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "치즈버거");
                            intent.putExtra("menu_price", 3000);
                            intent.putExtra("menu_info","불에 구운 쇠고기 패티와 사르르 치즈까지, 작지만 알차다!");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",3000);
                            startActivityForResult(intent,REQ_CODE_HAMBURGER_2);
                        }
                        else if (childPosition == 3)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "크런치치킨버거");
                            intent.putExtra("menu_price", 4300);
                            intent.putExtra("menu_info","매콤한 치킨과 바삭한 옥수수의 조화, 크런치치킨");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",4300);
                            startActivityForResult(intent,REQ_CODE_HAMBURGER_2);
                        }
                        else if (childPosition == 4)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "와일드갈릭버거");
                            intent.putExtra("menu_price", 2900);
                            intent.putExtra("menu_info","바삭한 갈릭칩과 매콤한 핫페퍼칠리소스가 들어간 버거 ");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",2900);
                            startActivityForResult(intent,REQ_CODE_HAMBURGER_2);
                        }
                        else if (childPosition == 5)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "갈릭스테이크버거");
                            intent.putExtra("menu_price", 6700);
                            intent.putExtra("menu_info","두툼한 스테이크 패티, 향긋한 갈릭, 달콤한 볶음양파의\n"+"맛있는 조화!");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",6700);
                            startActivityForResult(intent,REQ_CODE_HAMBURGER_2);
                        }
                        else if (childPosition == 6)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "와퍼");
                            intent.putExtra("menu_price", 5600);
                            intent.putExtra("menu_info","불에 직접 구운 순 쇠고기 패티에 싱싱한 야채가 한가득~ 버거킹의 대표 메뉴!");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",5600);
                            startActivityForResult(intent,REQ_CODE_HAMBURGER_2);
                        }
                        else if (childPosition == 7)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "불고기와퍼");
                            intent.putExtra("menu_price", 5600);
                            intent.putExtra("menu_info","불에 직접 구운 순 쇠고기 패티가 들어간 와퍼에\n"+"달콤한 불고기 소스까지!");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",5600);
                            startActivityForResult(intent,REQ_CODE_HAMBURGER_2);
                        }
                        else if (childPosition == 8)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "치즈와퍼");
                            intent.putExtra("menu_price", 6200);
                            intent.putExtra("menu_info","불에 직접 구운 순 쇠고기 패티가 들어간 와퍼에\n"+"고소한 치즈까지!");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",6200);
                            startActivityForResult(intent,REQ_CODE_HAMBURGER_2);
                        }
                        else if (childPosition == 9)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "통새우와퍼");
                            intent.putExtra("menu_price", 6500);
                            intent.putExtra("menu_info","직화 방식으로 구운 100% 순쇠고기 패티에\n"+"갈릭페퍼 통새우와 스파이시토마토소스가 더해진 프리미엄버거");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",6500);
                            startActivityForResult(intent,REQ_CODE_HAMBURGER_2);
                        }
                        break;

                    case 3:
                        if (childPosition == 0)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "생맥주");
                            intent.putExtra("menu_price", 3000);
                            intent.putExtra("menu_info","100% 맥아로 풍부한 몰트와 쌉쌀한 홉의 생맥주");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",3000);
                            startActivityForResult(intent,REQ_CODE_BEER_3);
                        }
                        else if (childPosition == 1)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "크림생맥주");
                            intent.putExtra("menu_price", 3500);
                            intent.putExtra("menu_info","100% 맥아로 풍부한 몰트와 쌉쌀한 홉, 부드러운 크리미\n"+"거품이 조화로운 깊고 풍부한 맛의 맥주");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",3500);
                            startActivityForResult(intent,REQ_CODE_BEER_3);
                        }
                        else if (childPosition == 2)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "더치맥주");
                            intent.putExtra("menu_price", 4500);
                            intent.putExtra("menu_info","더치커피원액을 넣은 맥주");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",4500);
                            startActivityForResult(intent,REQ_CODE_BEER_3);
                        }
                        else if (childPosition == 3)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "자몽맥주");
                            intent.putExtra("menu_price", 4500);
                            intent.putExtra("menu_info","달콤쌉쌀한 자몽과 맥주의 만남!");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",4500);
                            startActivityForResult(intent,REQ_CODE_BEER_3);
                        }
                        else if (childPosition == 4)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "라임맥주");
                            intent.putExtra("menu_price", 4500);
                            intent.putExtra("menu_info","새콤달콤한 맥주의 만남!");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",4500);
                            startActivityForResult(intent,REQ_CODE_BEER_3);
                        }
                        else if (childPosition == 5)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "청포도맥주");
                            intent.putExtra("menu_price", 4500);
                            intent.putExtra("menu_info","상큼한 청포도와 맥주의 만남!");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",4500);
                            startActivityForResult(intent,REQ_CODE_BEER_3);
                        }
                        else if (childPosition == 6)
                        {
                            Intent intent = new Intent(getApplicationContext(), OrderMenu.class);
                            intent.putExtra("menu_name", "병맥주");
                            intent.putExtra("menu_price", 5000);
                            intent.putExtra("menu_info","세계 각국의 맥주를 맛볼 수 있는 기회!\n"+"맥주마시고 세계일주하자");
                            intent.putExtra("menu_count",1);
                            intent.putExtra("menu_allprice",5000);
                            startActivityForResult(intent,REQ_CODE_BEER_3);
                        }
                        break;
                }
                return false;
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrderListActivity.class);
                startActivity(intent);

            }
        });
    }

    private class IntentData
    {
        private String name;                        //전달받은 이름 데이터를 저장
        private int count,price;                  // 전달받은 수량과 가격 데이터를 저장
        IntentData(String name, int count, int price)
        {
            this.name = name;
            this.count = count;
            this.price = price;
        }

        String getName()
        {
            return name;
        }

        int getCount()
        {
            return count;
        }

        int getPrice()
        {
            return price;
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode){
            case REQ_CODE_CHICKEN_0:
                break;

            case REQ_CODE_PIZZA_1:
                break;

            case REQ_CODE_HAMBURGER_2:
                break;

            case REQ_CODE_BEER_3:
                break;
        }


    }
}
