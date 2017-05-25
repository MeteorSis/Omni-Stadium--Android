package skhu.cse.network.omni_stadium;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import skhu.cse.network.omni_stadium.AsyncTask.LogoutTask;

public class BackPressCloseHandler
{
    private long backKeyPressedTime = 0;
    private Toast toast;
    private AppCompatActivity activity;

    public BackPressCloseHandler(AppCompatActivity context)
    {
        this.activity = context;
    }

    public void onBackPressed()
    {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000)
        {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000)
        {
            new LogoutTask(activity).execute(((OmniApplication)activity.getApplicationContext()).getId());
            toast.cancel();
        }
    }

    public void showGuide()
    {
        toast = Toast.makeText(activity, "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }
}