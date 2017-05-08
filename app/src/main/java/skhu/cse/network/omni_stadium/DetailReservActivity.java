package skhu.cse.network.omni_stadium;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import uk.co.senab.photoview.PhotoViewAttacher;

public class DetailReservActivity extends AppCompatActivity {
    Button SeatOk;
    ImageView detail_seat;
    PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserv_detail);
        SeatOk = (Button)findViewById(R.id.btSeatOk);

        SeatOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"결제가 완료되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        //이미지 확대 축소
        detail_seat = (ImageView) findViewById(R.id.ivDetailSeat);
        mAttacher = new PhotoViewAttacher(detail_seat);

    }
}
