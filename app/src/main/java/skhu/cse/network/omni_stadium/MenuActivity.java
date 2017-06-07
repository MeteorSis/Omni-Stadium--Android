package skhu.cse.network.omni_stadium;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import skhu.cse.network.omni_stadium.MyPage.MyPageActivity;
import skhu.cse.network.omni_stadium.Ordering.OrderActivity;
import skhu.cse.network.omni_stadium.RegistrationUnreservedSeat.NFCActivity;
import skhu.cse.network.omni_stadium.Reservation.ReserveActivity;
import skhu.cse.network.omni_stadium.Streaming.MultiVideoActivity;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private BackPressCloseHandler backPressCloseHandler;
    private OmniApplication omniApplication;
    private CustomActionBarDrawerToggle toggle;
    private NavigationView navView;

    static final int REQ_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);
        omniApplication=(OmniApplication)getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        toggle = new CustomActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);

        navView = (NavigationView) findViewById(R.id.nav_view);

        navView.setNavigationItemSelectedListener(this);
        navView.setItemIconTintList(null);

        View navHeaderView = getLayoutInflater().inflate(R.layout.nav_header_menu, navView);

        ImageView imgView_Nav_header_menu=(ImageView)navHeaderView.findViewById(R.id.imgView_Nav_header_menu);
        Glide.with(this).load(R.drawable.omni_stadium_logo).into(imgView_Nav_header_menu);

        TextView tvAppName=(TextView)findViewById(R.id.tvAppName);
        tvAppName.setSelected(true);

        TextView tvDate=(TextView)findViewById(R.id.tvDate);
        TextView tvDayOfTheWeek=(TextView)findViewById(R.id.tvDayOfTheWeek);
        Date curDate=new Date(System.currentTimeMillis());
        tvDate.setText(new SimpleDateFormat("yyyy.MM.dd", Locale.KOREA).format(curDate));
        tvDayOfTheWeek.setText(new SimpleDateFormat("E", Locale.KOREA).format(curDate));

        ImageView ivLogo1=(ImageView)findViewById(R.id.ivLogo1);
        ImageView ivLogo2=(ImageView)findViewById(R.id.ivLogo2);
        Glide.with(this).load(R.drawable.heroes_emblem).into(ivLogo1);
        Glide.with(this).load(R.drawable.doosan_emblem).into(ivLogo2);

        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(omniApplication.getTicket_no()==null)
        {
            AlertDialog.Builder dlg = new AlertDialog.Builder(MenuActivity.this);
            dlg.setMessage("구매한 티켓이 없습니다. 예매 페이지로 이동합니다.");
            dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent tintent = new Intent(omniApplication, ReserveActivity.class);
                    startActivity(tintent);
                }
            });
            dlg.setNegativeButton("취소", null);
            dlg.setCancelable(true);
            dlg.show();
        }

        TextView tvTicketNo=(TextView)findViewById(R.id.tvTicketNo);
        TextView tvOwner=(TextView)findViewById(R.id.tvOwner);
        TextView tvZone=(TextView)findViewById(R.id.tvZone);
        TextView tvRow=(TextView)findViewById(R.id.tvRow);
        TextView tvSeatNo=(TextView)findViewById(R.id.tvSeatNo);

        Log.d("app Test", omniApplication.getMem_id()+", "+omniApplication.getMem_name()+", "+omniApplication.getTicket_no()+", "+omniApplication.getSeat_zone()+", "+omniApplication.getSeat_row()+", "+omniApplication.getSeat_no());
        Integer ticket_no=omniApplication.getTicket_no();
        if(ticket_no!=null)
        {
            tvTicketNo.setText(String.valueOf(ticket_no));
            tvOwner.setText(omniApplication.getMem_name());

            String seat_zone=omniApplication.getSeat_zone();
            tvZone.setText(seat_zone);

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                if(seat_zone.equals("1루 외야그린석")||seat_zone.equals("3루 외야그린석"))
                    tvZone.setTextColor(getResources().getColor(R.color.colorGreenZone));
                else if(seat_zone.equals("1루 레드석")||seat_zone.equals("3루 레드석"))
                    tvZone.setTextColor(getResources().getColor(R.color.colorRedZone));
                else if(seat_zone.equals("1루 네이비석")||seat_zone.equals("3루 네이비석"))
                    tvZone.setTextColor(getResources().getColor(R.color.colorNavyZone));
                else if(seat_zone.equals("중앙 블루석A"))
                    tvZone.setTextColor(getResources().getColor(R.color.colorBlueAZone));
                else if(seat_zone.equals("중앙 블루석B"))
                    tvZone.setTextColor(getResources().getColor(R.color.colorBlueBZone));
            }
            else{
                if(seat_zone.equals("1루 외야그린석")||seat_zone.equals("3루 외야그린석"))
                    tvZone.setTextColor(getResources().getColor(R.color.colorGreenZone, null));
                else if(seat_zone.equals("1루 레드석")||seat_zone.equals("3루 레드석"))
                    tvZone.setTextColor(getResources().getColor(R.color.colorRedZone, null));
                else if(seat_zone.equals("1루 네이비석")||seat_zone.equals("3루 네이비석"))
                    tvZone.setTextColor(getResources().getColor(R.color.colorNavyZone, null));
                else if(seat_zone.equals("중앙 블루석A"))
                    tvZone.setTextColor(getResources().getColor(R.color.colorBlueAZone, null));
                else if(seat_zone.equals("중앙 블루석B"))
                    tvZone.setTextColor(getResources().getColor(R.color.colorBlueBZone, null));
            }

            Integer seat_row=omniApplication.getSeat_row();
            Integer seat_no=omniApplication.getSeat_no();
            if(seat_row!=null && seat_no!=null)
            {
                tvRow.setText(String.valueOf(seat_row));
                tvSeatNo.setText(String.valueOf(seat_no));
            }
            else
            {
                tvRow.setText("(미등록)");
                tvSeatNo.setText("(미등록)");
            }
        }
        else
        {
            tvTicketNo.setText("(없음)");
            tvOwner.setText("(없음)");
            tvZone.setText("(없음)");
            tvRow.setText("(없음)");
            tvSeatNo.setText("(없음)");
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            backPressCloseHandler.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_streaming:
                if(omniApplication.getTicket_no()!=null)
                {
                    Intent intent = new Intent(omniApplication, MultiVideoActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(omniApplication, "티켓을 구매해야 이용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_ticket:
                Intent tintent = new Intent(omniApplication, ReserveActivity.class);
                startActivity(tintent);
                break;
            case R.id.nav_nfc:
                if(omniApplication.getTicket_no()!=null)
                {
                    if(omniApplication.getSeat_zone().equals("1루 외야그린석") || omniApplication.getSeat_zone().equals("3루 외야그린석"))
                    {
                        Intent nintent = new Intent(omniApplication, NFCActivity.class);
                        startActivity(nintent);
                    }
                    else
                    {
                        Toast.makeText(omniApplication, "구매한 티켓은 자유석 티켓이 아닙니다.", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(omniApplication, "티켓을 구매해야 이용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.nav_order:
                if(omniApplication.getTicket_no()!=null)
                {
                    Intent ointent = new Intent(omniApplication, OrderActivity.class);
                    startActivity(ointent);
                }
                else
                {
                    Toast.makeText(omniApplication, "티켓을 구매해야 이용할 수 있습니다.", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.nav_mypage:
                Intent mintent = new Intent(omniApplication, MyPageActivity.class);
                startActivityForResult(mintent, REQ_CODE);
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class CustomActionBarDrawerToggle extends ActionBarDrawerToggle
    {
        public CustomActionBarDrawerToggle(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, @StringRes int openDrawerContentDescRes, @StringRes int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            Menu menu=navView.getMenu();
            for(int i=0; i<menu.size(); ++i)
            {
                MenuItem menuItem=menu.getItem(i);
                if(menuItem.isChecked())
                    menuItem.setChecked(false);
            }
            super.onDrawerClosed(drawerView);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_CODE){
            if(resultCode == RESULT_OK){
                finish();
            }
        }
    }
}
