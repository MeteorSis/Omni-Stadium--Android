package skhu.cse.network.omni_stadium;

import android.app.Application;

public class OmniApplication extends Application {

    private String id=null;

    public void setId(String id)
    {
        this.id=id;
    }
    public String getId()
    {
        return id;
    }
}
