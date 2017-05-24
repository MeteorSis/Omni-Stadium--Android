/*
package skhu.cse.network.omni_stadium.Ordering;

import java.util.HashSet;
import java.util.Iterator;

public class CartManager {
    private OrderItem[] itemList;

    static CartManager inst=null;
    public static CartManager createManagerInst()
    {
        if(inst==null)
            inst=new CartManager();
        return inst;
    }
    private CartManager()
    {
        itemHashSet=new HashSet<>();
    }

    public Iterator<OrderItem> getIterator()
    {
        return itemHashSet.iterator();
    }

    public OrderItem search(String name)
    {
        Iterator<OrderItem> itr=itemHashSet.iterator();
        while(itr.hasNext())
        {
            OrderItem tmpItem=itr.next();
            if(tmpItem.getName().compareTo(name)==0)
                return tmpItem;
        }
        return null;
    }

    public void addOrderItem(OrderItem orderItem)
    {
        if(!itemHashSet.add(orderItem))
        {
            OrderItem existingItem = search(orderItem.getName());
            existingItem.setCount(existingItem.getCount()+orderItem.getCount());
        }
    }

    public boolean removeOrderItem(OrderItem orderItem)
    {
        return itemHashSet.remove(orderItem);
    }
}
*/
