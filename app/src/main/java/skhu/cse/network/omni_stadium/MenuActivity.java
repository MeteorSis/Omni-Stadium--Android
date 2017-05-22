package skhu.cse.network.omni_stadium;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import skhu.cse.network.omni_stadium.MyPage.MyPageActivity;
import skhu.cse.network.omni_stadium.Ordering.OrderActivity;
import skhu.cse.network.omni_stadium.RegistrationUnreservedSeat.NFCActivity;
import skhu.cse.network.omni_stadium.Reservation.ReservActivity;
import skhu.cse.network.omni_stadium.Streaming.MultiVideoActivity;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        DrawerLayout drawer;

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
        Glide.with(this).load(R.drawable.barcode).into((ImageView)findViewById(R.id.ivbarcode));
        Glide.with(this).load(R.drawable.omni_stadium_logo).into(imgView_Nav_header_menu);

/*        TextView tvMid = (TextView) navHeaderView.findViewById(R.id.tvMemuID);
        tvMid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MyPageActivity.class);
                startActivity(intent);
            }
        });*/
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
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
