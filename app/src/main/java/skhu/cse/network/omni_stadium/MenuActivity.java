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
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class MenuActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navView = (NavigationView) findViewById(R.id.nav_view);

        navView.setNavigationItemSelectedListener(this);

        ImageView imgView_Nav_header_menu=(ImageView)getLayoutInflater().inflate(R.layout.nav_header_menu, navView).findViewById(R.id.imgView_Nav_header_menu);
        Glide.with(this).load(R.drawable.seatimageview).into((ImageView)findViewById(R.id.ivContentSeat));
        Glide.with(this).load(R.drawable.omni_stadium_logo).into(imgView_Nav_header_menu);
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
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
