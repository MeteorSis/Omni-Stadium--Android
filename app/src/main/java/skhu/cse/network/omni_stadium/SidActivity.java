package skhu.cse.network.omni_stadium;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SidActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sid_main);

        Button btOK = (Button)findViewById(R.id.btSidOK);

        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText etName = (EditText)findViewById(R.id.etName);
                EditText etPhone = (EditText)findViewById(R.id.etPhone);
                EditText etBdate = (EditText)findViewById(R.id.etBdate);

                String NameData = etName.getText().toString();
                String PhoneData = etPhone.getText().toString();
                String BdateData = etBdate.getText().toString();

                String[] SidData = {NameData, PhoneData, BdateData};
                String Empty = "";

                if(SidData[0].matches("")&& Empty.matches("")){
                    Empty = Empty + "이름";
                }
                else{
                    String Nameregex = "^[가-힣]{2,4}$";
                    Pattern Namepattern = Pattern.compile(Nameregex);
                    Matcher Namematcher = Namepattern.matcher(NameData);
                    if (!Namematcher.find()&& Empty.matches("")){
                        Empty = Empty + "이름";
                    }
                }
                if(SidData[1].matches("")&& Empty.matches("")){
                    Empty = Empty + "핸드폰 번호";
                }
                else{
                    String Phoneregex = "010([0-9]{3,4})([0-9]{4})";
                    Pattern Phonepattern = Pattern.compile(Phoneregex);
                    Matcher Phonematcher = Phonepattern.matcher(PhoneData);
                    if (!Phonematcher.find()&& Empty.matches("")){
                        Empty = Empty + "핸드폰 번호";
                    }
                }
                if(SidData[2].matches("")&& Empty.matches("")){
                    Empty = Empty + "생년월일";
                }
                else{
                    String Bdateregex = "(19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12][0-9]|3[01])$";
                    Pattern Bdatepattern = Pattern.compile(Bdateregex);
                    Matcher Bdatematcher = Bdatepattern.matcher(BdateData);
                    if (!Bdatematcher.find()&& Empty.matches("")){
                        Empty = Empty + "생년월일";
                    }
                }

                if(Empty.matches("")){
                    //Toast.makeText(getApplicationContext(), "입력 완료", Toast.LENGTH_SHORT).show();
                    //new SignUpActivity.SignUp().execute(SignupData[0], SignupData[1], SignupData[2], SignupData[3], SignupData[4], SignupData[5]);
                }
                else{
                    new AlertDialog.Builder(SidActivity.this)
                            .setMessage(Empty+"을(를) 다시 확인해주세요.")
                            .setPositiveButton("OK",null)
                            .show();
                }
            }
        });
    }
}
