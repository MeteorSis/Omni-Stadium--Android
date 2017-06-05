package skhu.cse.network.omni_stadium;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import skhu.cse.network.omni_stadium.MyPage.MyPageActivity;
import skhu.cse.network.omni_stadium.Ordering.OrderActivity;
import skhu.cse.network.omni_stadium.RegistrationUnreservedSeat.NFCActivity;
import skhu.cse.network.omni_stadium.Reservation.ReservActivity;
import skhu.cse.network.omni_stadium.Streaming.MultiVideoActivity;

public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private BackPressCloseHandler backPressCloseHandler;

    static final int REQ_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

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
    protected void onResume() {
        super.onResume();
        TextView tvTicketNo=(TextView)findViewById(R.id.tvTicketNo);
        TextView tvOwner=(TextView)findViewById(R.id.tvOwner);
        TextView tvZone=(TextView)findViewById(R.id.tvZone);
        TextView tvRow=(TextView)findViewById(R.id.tvRow);
        TextView tvSeatNo=(TextView)findViewById(R.id.tvSeatNo);

        OmniApplication omniApplication=(OmniApplication)getApplicationContext();
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
                Intent intent = new Intent(getApplicationContext(), MultiVideoActivity.class);
                startActivity(intent);
                break;
            case R.id.nav_ticket:
                Intent tintent = new Intent(getApplicationContext(), ReservActivity.class);
                startActivity(tintent);
                break;
            case R.id.nav_nfc:
                Intent nintent = new Intent(getApplicationContext(), NFCActivity.class);
                /********** dummy ***********/
                nintent.putExtra("Sector","3루 외야그린석");
                /********** dummy ***********/
                startActivity(nintent);
                break;

            case R.id.nav_order:
                Intent ointent = new Intent(getApplicationContext(), OrderActivity.class);
                startActivity(ointent);
                break;

            case R.id.nav_mypage:
                Intent mintent = new Intent(getApplicationContext(), MyPageActivity.class);
                startActivityForResult(mintent, REQ_CODE);
                break;
        }


        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
