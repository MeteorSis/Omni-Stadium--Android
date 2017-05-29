package skhu.cse.network.omni_stadium.Reservation;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import pl.polidea.view.ZoomView;
import skhu.cse.network.omni_stadium.R;
import skhu.cse.network.omni_stadium.ViewHolderHelper;

public class ReservActivity extends AppCompatActivity {

    private Bitmap bitmap_ZoomView;
    private CustomZoomView zoomView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserv_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.reserv_zoom, null, false);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        zoomView = new CustomZoomView(this);
        zoomView.addView(v);
        zoomView.setLayoutParams(layoutParams);
        zoomView.setMiniMapEnabled(false); // 좌측 상단 검은색 미니맵 설정안함
        zoomView.setMaxZoom(4f); // 줌 Max 배율 설정  1f 로 설정하면 줌 안됩니다.

        ConstraintLayout container = (ConstraintLayout) findViewById(R.id.container);
        container.addView(zoomView);

        Glide.with(ReservActivity.this).load(R.drawable.noun_1018844_cc).into((ImageView)findViewById(R.id.ivzoom_info));
        Glide.with(ReservActivity.this).load(R.drawable.seatimageview).into((ImageView)findViewById(R.id.ivseat));

        zoomView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if(event.getAction()==MotionEvent.ACTION_UP)
                {
                    int pixel = bitmap_ZoomView.getPixel((int) event.getX(), (int) event.getY());
                    int redPixel = Color.red(pixel);
                    int greenPixel = Color.green(pixel);
                    int bluePixel = Color.blue(pixel);

                    Log.v("Pixel", redPixel+", "+greenPixel+", "+bluePixel);
                    if(redPixel==0 && greenPixel==94 && bluePixel==221)
                    {
                        //가운데 연파랑 영역
                        Intent intent = new Intent(getApplicationContext(),DetailReservActivity.class);
                        intent.putExtra("Sector", "중앙 블루석A");
                        startActivity(intent);
                        Log.v("Event Test", "가운데 연파랑 영역");
                    }
                    else if(redPixel==221 && greenPixel==0 && bluePixel==42)
                    {
                        //아래 왼쪽 빨강 영역
                        Intent intent = new Intent(getApplicationContext(),DetailReservActivity.class);
                        intent.putExtra("Sector", "3루 레드석");
                        startActivity(intent);
                        Log.v("Event Test", "아래 왼쪽 빨강 영역");
                    }
                    else if(redPixel==221 && greenPixel==1 && bluePixel==42)
                    {
                        //아래 오른쪽 빨강 영역
                        Intent intent = new Intent(getApplicationContext(),DetailReservActivity.class);
                        intent.putExtra("Sector", "1루 레드석");
                        startActivity(intent);
                        Log.v("Event Test", "아래 오른쪽 빨강 영역");
                    }
                    else if(redPixel==36 && greenPixel==41 && bluePixel==172)
                    {
                        //가운데 진파랑 영역
                        Intent intent = new Intent(getApplicationContext(),DetailReservActivity.class);
                        intent.putExtra("Sector", "중앙 블루석B");
                        startActivity(intent);
                        Log.v("Event Test", "가운데 진파랑 영역");
                    }
                    else if(redPixel==36 && greenPixel==40 && bluePixel==83)
                    {
                        //아래 왼쪽 남색 영역
                        Intent intent = new Intent(getApplicationContext(),DetailReservActivity.class);
                        intent.putExtra("Sector", "3루 네이비석");
                        startActivity(intent);
                        Log.v("Event Test", "아래 왼쪽 남색 영역");
                    }
                    else if(redPixel==36 && greenPixel==41 && bluePixel==83)
                    {
                        //아래 오른쪽 남색 영역
                        Intent intent = new Intent(getApplicationContext(),DetailReservActivity.class);
                        intent.putExtra("Sector", "1루 네이비석");
                        startActivity(intent);
                        Log.v("Event Test", "아래 오른쪽 남색 영역");
                    }
                    else if(redPixel==52 && greenPixel==150 && bluePixel==0)
                    {
                        //왼쪽 위 그린 영역
                        Intent intent = new Intent(getApplicationContext(),DetailReservActivity.class);
                        intent.putExtra("Sector", "3루 외야그린석");
                        startActivity(intent);
                        Log.v("Event Test", "왼쪽 위 그린 영역");
                    }
                    else if(redPixel==52 && greenPixel==150 && bluePixel==1)
                    {
                        //오른쪽 위 그린 영역
                        Intent intent = new Intent(getApplicationContext(),DetailReservActivity.class);
                        intent.putExtra("Sector", "1루 외야그린석");
                        startActivity(intent);
                        Log.v("Event Test", "오른쪽 위 그린 영역");
                    }
                }
                return false;
            }
        });
    }

    private class CustomZoomView extends ZoomView
    {
        public CustomZoomView(Context context) {
            super(context);
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            if(bitmap_ZoomView==null)
            {
                bitmap_ZoomView = Bitmap.createBitmap(zoomView.getWidth(), zoomView.getHeight(), Bitmap.Config.ARGB_8888);
                Log.v("Test", zoomView.getWidth() + ", " + zoomView.getHeight());
                Canvas myCanvas = new Canvas(bitmap_ZoomView);
                zoomView.draw(myCanvas);
            }
            return super.dispatchTouchEvent(ev);
        }
    }
}
