package skhu.cse.network.omni_stadium.Ordering;

import java.io.Serializable;
import java.util.ArrayList;

public class CartManager implements Serializable{

    private OrderItem[][] cart;

    public CartManager(OrderItem[][] cart)
    {
        this.cart=cart;
    }

    public int getItemCount()
    {
        int count=0;

        for(int row=0; row<cart.length; ++row)
        {
            for(int col=0; col<cart[row].length; ++col)
            {
                if(cart[row][col]!=null)
                    ++count;
            }
        }

        return count;
    }

    public int getAllPrice()
    {
        int allPrice=0;

        for(int row=0; row<cart.length; ++row)
        {
            for(int col=0; col<cart[row].length; ++col)
            {
                OrderItem item=cart[row][col];
                if(item!=null)
                    allPrice+=item.getMenu_price()*item.getMenu_count();
            }
        }
        return allPrice;
    }

    public ArrayList<OrderItem> getArrList()
    {
        ArrayList<OrderItem> retArr=new ArrayList<>();
        for(int row=0; row<cart.length; ++row)
        {
            for(int col=0; col<cart[row].length; ++col)
            {
                if(cart[row][col]!=null)
                    retArr.add(cart[row][col]);
            }
        }
        return retArr;
    }

    public void addOrderItem(OrderItem orderItem)
    {
        int row=orderItem.getFood_id();
        int col=orderItem.getMenu_id();
        if(cart[row][col]==null)
            cart[row][col]=orderItem;
        else
            cart[row][col].setMenu_count(cart[row][col].getMenu_count()+orderItem.getMenu_count());
    }
    public void removeOrderItem(OrderItem orderItem)
    {
        int row=orderItem.getFood_id();
        int col=orderItem.getMenu_id();
        cart[row][col]=null;
    }

    public void plusOrderItem(OrderItem orderItem)
    {
        int row=orderItem.getFood_id();
        int col=orderItem.getMenu_id();
        cart[row][col].setMenu_count(cart[row][col].getMenu_count()+1);
    }

    public void minusOrderItem(OrderItem orderItem)
    {
        int row=orderItem.getFood_id();
        int col=orderItem.getMenu_id();
        if(cart[row][col].getMenu_count()>1)
            cart[row][col].setMenu_count(cart[row][col].getMenu_count()-1);
    }
}
