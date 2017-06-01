package skhu.cse.network.omni_stadium.Ordering;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import skhu.cse.network.omni_stadium.R;

public class Order_ExplvAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private ArrayList<String> group_menu;
    private HashMap<String, ArrayList<OrderItem>> item_menu;

    public Order_ExplvAdapter(Context context, ArrayList<String> group_menu , HashMap<String, ArrayList<OrderItem>> item_menu)
    {
        super();
        this.mContext = context;
        this.group_menu = group_menu;
        this.item_menu = item_menu;
    }

    @Override
    public int getGroupCount() {
        return group_menu.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) { // ChildList의 크기를 int 형으로 반환
        return item_menu.get(group_menu.get(groupPosition)).size();
    }


    @Override
    public Object getGroup(int groupPosition) {
        return group_menu.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) { // groupPostion과 childPosition을 통해 childList의 원소를 얻어옴
        return item_menu.get(group_menu.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) { // ChildList의 ID로 long 형 값을 반환
        return childPosition;
    }

    @Override
    public boolean hasStableIds() { return false; } // stable ID인지 boolean 값으로 반환

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) { // ParentList의 View
        String groupName = group_menu.get(groupPosition);

        if(convertView == null){
            LayoutInflater groupInfla = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // ParentList의 layout 연결.
            convertView = groupInfla.inflate(R.layout.order_group, null);
        }
        // ParentList의 Layout 연결 후, 해당 layout 내 TextView를 연결
        TextView parentText = (TextView)convertView.findViewById(R.id.tvgroup);
        parentText.setText(groupName);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // ChildList의 View. 위 ParentList의 View를 얻을 때와 비슷하게 Layout 연결 후, layout 내 TextView 2개를 연결
        OrderItem child = (OrderItem)getChild(groupPosition, childPosition);
        if(convertView == null){

            LayoutInflater childInfla = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = childInfla.inflate(R.layout.order_item, null);
        }
        TextView name = (TextView)convertView.findViewById(R.id.tvLitem);
        TextView cost = (TextView)convertView.findViewById(R.id.tvRitem);
        name.setText(child.getMenu_name());
        if(child.getMenu_count()!=0)
        {
            String strCost = String.valueOf(child.getMenu_price()) + "원";
            cost.setText(strCost);
            cost.setTextColor(Color.parseColor("#20d56F"));
        }
        else
        {
            cost.setText("재고 없음");
            cost.setTextColor(Color.parseColor("#FF0000"));
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;  // 선택여부를 boolean 값으로 반환
    }
}









