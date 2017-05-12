package skhu.cse.network.omni_stadium;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class MyPageActivity extends AppCompatActivity {

    private ArrayList<String> arrayGroup = new ArrayList<String>();
    private HashMap<String, ArrayList<String>> arrayChild = new HashMap<String, ArrayList<String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_page);
        final ExpandableListView lvMyPage = (ExpandableListView)findViewById(R.id.explv_Setting);
        arrayGroup.add("로그아웃");
        arrayGroup.add("티켓관리");
        arrayGroup.add("회원정보 관리");

        /*ArrayList<String> arrayLogout = new ArrayList<String>();
        arrayLogout.add("· 로그아웃");*/

        ArrayList<String> arrayTicket = new ArrayList<String>();
        arrayTicket.add("· 자유석 해제");
        arrayTicket.add("· 티켓환불");

        ArrayList<String> arrayManage = new ArrayList<String>();
        arrayManage.add("· 회원정보 수정");
        arrayManage.add("· 회원탈퇴");

        arrayChild.put(arrayGroup.get(0), new ArrayList<String>());
        arrayChild.put(arrayGroup.get(1), arrayTicket);
        arrayChild.put(arrayGroup.get(2), arrayManage);

        lvMyPage.setAdapter(new ExplvAdapterSign(this, arrayGroup, arrayChild));
        lvMyPage.setGroupIndicator(null);  //OFF Indicator

        lvMyPage.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if(groupPosition == 0){
                    new AlertDialog.Builder(MyPageActivity.this)
                            .setMessage("로그아웃 하시겠습니까?")
                            .setPositiveButton("NO",null)
                            .setNegativeButton("OK",null)
                            .show();
                }
                return false;
            }
        });

        lvMyPage.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                if (groupPosition == 1) {
                    if (childPosition == 0) {
                        new AlertDialog.Builder(MyPageActivity.this)
                                .setMessage("자유석을 해제하시겠습니까?")
                                .setPositiveButton("NO", null)
                                .setNegativeButton("OK", null)
                                .show();
                    } else {
                        new AlertDialog.Builder(MyPageActivity.this)
                                .setMessage("티켓을 환불하시겠습니까?")
                                .setPositiveButton("NO", null)
                                .setNegativeButton("OK", null)
                                .show();
                    }
                } else {
                    if (childPosition == 0) {
                        Intent intent = new Intent(getApplicationContext(), SignChangeActivity.class);
                        startActivity(intent);
                    } else {
                        new AlertDialog.Builder(MyPageActivity.this)
                                .setMessage("회원을 탈퇴하시겠습니까?")
                                .setPositiveButton("OK", null)
                                .setNegativeButton("NO", null)
                                .show();
                    }
                }
                return false;
            }
        });

    }

}
