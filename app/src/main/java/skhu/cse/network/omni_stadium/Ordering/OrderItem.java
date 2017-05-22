package skhu.cse.network.omni_stadium.Ordering;

import java.io.Serializable;

public class OrderItem implements Serializable{

    private String name;
    private int cost;
    private int count;

    public OrderItem(String name, int cost, int count)
    {
        this.name = name;
        this.cost = cost;
        this.count=count;
    }
    public OrderItem(String name, int cost)
    {
        this.name = name;
        this.cost = cost;
        this.count=0;
    }

    public String getName()
    {
        return name;
    }
    public void setName(String name)
    {
        this.name=name;
    }

    public int getCost()
    {
        return cost;
    }
    public void setCost(int cost)
    {
        this.cost=cost;
    }

    public int getCount()
    {
        return count;
    }
    public void setCount(int count)
    {
        this.count=count;
    }
}
