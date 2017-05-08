package skhu.cse.network.omni_stadium;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
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

        //이미지 확대 축소
        detail_seat = (ImageView) findViewById(R.id.ivDetailSeat);
        mAttacher = new PhotoViewAttacher(detail_seat);

        SeatOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(DetailReservActivity.this);
                dlg.setTitle("예매 정보");
                dlg.setMessage("해당 좌석을 결제 하시겠습니까?");

                dlg.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                        Toast.makeText(getApplicationContext(),"결제가 완료 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(getApplicationContext(),"결제가 취소 되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                });
                dlg.show();
                dlg.setCancelable(false); // 백버튼 비활성화

            }
        });



    }
}
