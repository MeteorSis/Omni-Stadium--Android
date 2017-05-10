package skhu.cse.network.omni_stadium;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class NFCActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.nfc_main);
        Glide.with(this).load(R.drawable.nfctag).into((ImageView)findViewById(R.id.ivNFC));
    }
}



