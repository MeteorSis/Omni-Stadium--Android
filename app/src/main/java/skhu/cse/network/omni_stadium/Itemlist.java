package skhu.cse.network.omni_stadium;

/**
 * Created by 6109P-09 on 2017-05-15.
 */

public class Itemlist {
    private String name;
    private String cost;

    public Itemlist(String name, String cost)
    {
        this.name = name;
        this.cost = cost;
    }

    String getName()
    {
        return name;
    }

    String getCost()
    {
        return cost;
    }


}
