package skhu.cse.network.omni_stadium;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.provider.ContactsContract;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import pl.polidea.view.ZoomView;

import static skhu.cse.network.omni_stadium.R.drawable.green;

public class ReservActivity extends AppCompatActivity {

    private Bitmap bitmap_ZoomView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserv_main);

        View v = ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.reserv_zoom, null, false);

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        ZoomView zoomView = new ZoomView(this);
        zoomView.addView(v);
        zoomView.setLayoutParams(layoutParams);
        zoomView.setMiniMapEnabled(false); // 좌측 상단 검은색 미니맵 설정안함
        zoomView.setMaxZoom(4f); // 줌 Max 배율 설정  1f 로 설정하면 줌 안됩니다.

        ConstraintLayout container = (ConstraintLayout) findViewById(R.id.container);
        container.addView(zoomView);

        zoomView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(bitmap_ZoomView==null)
                {
                    bitmap_ZoomView = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(bitmap_ZoomView);
                    v.draw(canvas);
                }
                return false;
            }
        });

        /*ImageView ivseat=(ImageView)findViewById(R.id.ivseat);
        final Bitmap bitmap=((BitmapDrawable)ivseat.getDrawable()).getBitmap();
        ivseat.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.v("pixel test1", event.getX()+", "+event.getY());
                Log.v("pixel test2", event.getRawX()+", "+event.getRawY());
                Log.v("screen test", size.x+", "+size.y);
                //((BitmapDrawable)((ImageView)v).getDrawable()).getBitmap().getPix);
                //Log.v("test", bitmap.getWidth()+", "+bitmap.getHeight());
                //Log.v("test", event.get+", "+bitmap.getHeight());
                int pixel=bitmap.getPixel((int)event.getRawY(), (int)event.getRawY());
                //Log.v("pixel test", Color.red(pixel)+", "+Color.green(pixel)+", "+Color.blue(pixel));
                return false;
            }
        });*/
    }
}
