package skhu.cse.network.omni_stadium.Etc;

import android.app.ProgressDialog;
import android.content.Context;

public class LoadingDialog extends ProgressDialog
{
    public LoadingDialog(Context context, CharSequence message)
    {
        super(context);
        setMessage(message);
        setIndeterminate(false);
        setCancelable(false);
    }
}
