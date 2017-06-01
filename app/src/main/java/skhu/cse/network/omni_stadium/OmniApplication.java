package skhu.cse.network.omni_stadium;

import android.app.Application;

public class OmniApplication extends Application {

    private String mem_id=null;
    private String mem_name=null;

    private Integer ticket_no=null;

    private String seat_zone=null;
    private Integer seat_row=null;
    private Integer seat_no=null;

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

    public Integer getTicket_no() {
        return ticket_no;
    }
    public void setTicket_no(Integer ticket_no)
    {
        this.ticket_no = ticket_no;
    }

    public String getSeat_zone()
    {
        return seat_zone;
    }
    public void setSeat_zone(String seat_zone)
    {
        this.seat_zone = seat_zone;
    }

    public Integer getSeat_row()
    {
        return seat_row;
    }
    public void setSeat_row(Integer seat_row)
    {
        this.seat_row = seat_row;
    }

    public Integer getSeat_no()
    {
        return seat_no;
    }
    public void setSeat_no(Integer seat_no)
    {
        this.seat_no = seat_no;
    }
}
