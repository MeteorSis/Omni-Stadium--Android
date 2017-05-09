package skhu.cse.network.omni_stadium;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView SignUp = (TextView)findViewById(R.id.tvregister);
        TextView SearchId = (TextView) findViewById(R.id.tvSearchId);
        TextView SearchPw = (TextView) findViewById(R.id.tvSearchPW);
        Button Login = (Button)findViewById(R.id.btlogin);

        SignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });

        SearchId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SidActivity.class);
                startActivity(intent);
            }
        });

        SearchPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SpwActivity.class);
                startActivity(intent);
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
            }
        });

    }

    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox)view).isChecked();
        if(checked){
            Toast.makeText(getApplicationContext(),"Auto Login", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(),"Auto Login Canceled", Toast.LENGTH_SHORT).show();
        }
    }


}
