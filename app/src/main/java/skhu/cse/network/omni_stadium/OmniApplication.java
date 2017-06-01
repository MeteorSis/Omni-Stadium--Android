package skhu.cse.network.omni_stadium;

import android.app.Application;

public class OmniApplication extends Application {

    private String mem_id=null;
    private String mem_name=null;


    public void setMem_id(String mem_id)
    {
        this.mem_id=mem_id;
    }
    public String getMem_id()
    {
        return mem_id;
    }

    public void setMem_name(String mem_name)
    {
        this.mem_name=mem_name;
    }
    public String getMem_name()
    {
        return mem_name;
    }


}
