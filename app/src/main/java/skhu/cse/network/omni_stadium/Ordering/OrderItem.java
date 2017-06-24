package skhu.cse.network.omni_stadium.Ordering;

import java.io.Serializable;

public class OrderItem implements Serializable{

    private String food_name;
    private int food_id;
    private int menu_id;
    private String menu_name;
    private int menu_price;
    private String menu_info;
    private int menu_count;

    public OrderItem(String food_name, int food_id,
                     int menu_id, String menu_name, int menu_price, String menu_info, int menu_count)
    {
        this.food_name=food_name;
        this.food_id=food_id;
        this.menu_id=menu_id;
        this.menu_name=menu_name;
        this.menu_price=menu_price;
        this.menu_info=menu_info;
        this.menu_count=menu_count;
    }

    public String getFood_name()
    {
        return food_name;
    }

    public int getFood_id()
    {
        return food_id;
    }

    public int getMenu_id()
    {
        return menu_id;
    }

    public String getMenu_name()
    {
        return menu_name;
    }

    public int getMenu_price()
    {
        return menu_price;
    }

    public String getMenu_info()
    {
        return menu_info;
    }

    public int getMenu_count()
    {
        return menu_count;
    }
    public void setMenu_count(int menu_count)
    {
        this.menu_count=menu_count;
    }

}
