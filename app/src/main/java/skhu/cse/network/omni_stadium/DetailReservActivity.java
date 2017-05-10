package skhu.cse.network.omni_stadium;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;


public class DetailReservActivity extends AppCompatActivity {

    private boolean isCheckedInArr=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reserv_detail);
        Button SeatOk = (Button)findViewById(R.id.btSeatOk);

        /*ToggleButton[][] btArray =new ToggleButton[5][10];
        int[] tbGIDArr={ R.id.tbG1, R.id.tbG2, R.id.tbG3, R.id.tbG4, R.id.tbG5, R.id.tbG6,  R.id.tbG7, R.id.tbG8, R.id.tbG9, R.id.tbG10,
                R.id.tbG11, R.id.tbG12, R.id.tbG13, R.id.tbG14, R.id.tbG15, R.id.tbG16,  R.id.tbG17, R.id.tbG18, R.id.tbG19, R.id.tbG20,
                R.id.tbG21, R.id.tbG22, R.id.tbG23, R.id.tbG24, R.id.tbG25, R.id.tbG26,  R.id.tbG27, R.id.tbG28, R.id.tbG29, R.id.tbG30,
                R.id.tbG31, R.id.tbG32, R.id.tbG33, R.id.tbG34, R.id.tbG35, R.id.tbG36,  R.id.tbG37, R.id.tbG38, R.id.tbG39, R.id.tbG40,
                R.id.tbG41, R.id.tbG42, R.id.tbG43, R.id.tbG44, R.id.tbG45, R.id.tbG46,  R.id.tbG47, R.id.tbG48, R.id.tbG49, R.id.tbG50 };
        int tbGIDArrCnt=0;
        for(int row= 0; row <btArray.length; row++)
        {
            for(int column= 0; column<btArray[row].length; column++)
            {
                btArray[row][column] = (ToggleButton)findViewById(tbGIDArr[tbGIDArrCnt++]);
                btArray[row][column].setOnClickListener(new ToggleButtonClickedListener());
            }
        }*/

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
     /*private class ToggleButtonCheckedListener implements CompoundButton.OnCheckedChangeListener
     {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked)
            {
                buttonView.setChecked(false);
                buttonView.setBackgroundColor(Color.GREEN);
                isCheckedInArr=false;
            }
            else {
                if(!isCheckedInArr)
                {
                    buttonView.setChecked(true);
                    buttonView.setBackgroundColor(Color.DKGRAY);
                    isCheckedInArr=true;
                }
            }
        }
    }*/
    public void onToggleClicked(View v)
    {
        ToggleButton tB=(ToggleButton)v;
        boolean on = tB.isChecked();

        if(on)
        {
            if(isCheckedInArr)
            {
                tB.toggle();
                Toast.makeText(this, "이미 선택된 좌석이 있습니다.", Toast.LENGTH_SHORT).show();
            }
            else
                isCheckedInArr=true;
        }
        else
            isCheckedInArr=false;
    }
}
