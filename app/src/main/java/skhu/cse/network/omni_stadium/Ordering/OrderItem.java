package skhu.cse.network.omni_stadium.Ordering;

public class Itemlist {
    private String name;
    private String cost;

    public Itemlist(String name, String cost)
    {
        this.name = name;
        this.cost = cost;
    }

    public String getName()
    {
        return name;
    }

    public String getCost()
    {
        return cost;
    }
}
