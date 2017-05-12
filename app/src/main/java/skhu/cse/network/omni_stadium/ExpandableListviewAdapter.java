package skhu.cse.network.omni_stadium;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpandableListviewAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<String> group_menu;
    private HashMap<String, ArrayList<String>> litem_menu;
    private HashMap<String, ArrayList<String>> ritem_menu;

    public  ExpandableListviewAdapter(Context context, ArrayList<String> group_menu , HashMap<String, ArrayList<String>> litem_menu,  HashMap<String, ArrayList<String>> ritem_menu){
        this.mContext = context;
        this.group_menu = group_menu;
        this.litem_menu = litem_menu;
        this.ritem_menu =ritem_menu;
    }


    @Override
    public String getGroup(int groupPosition) {
        return group_menu.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return group_menu.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) { // ParentList의 View
        if(convertView == null){
            LayoutInflater groupInfla = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // ParentList의 layout 연결. root로 argument 중 parent를 받으며 root로 고정하지는 않음
            convertView = groupInfla.inflate(R.layout.order_group, parent, false);
        }

        // ParentList의 Layout 연결 후, 해당 layout 내 TextView를 연결
        TextView parentText = (TextView)convertView.findViewById(R.id.tvgroup);
        parentText.setText(getGroup(groupPosition));
        return convertView;
    }

    /* 여기서부터 ChildListView에 대한 method */
    @Override
    public Object getChild(int groupPosition, int childPosition) { // groupPostion과 childPosition을 통해 childList의 원소를 얻어옴
        return this.litem_menu.get(this.group_menu.get(groupPosition)).get(childPosition);

    }

    @Override
    public int getChildrenCount(int groupPosition) { // ChildList의 크기를 int 형으로 반환
        return this.litem_menu.get(this.group_menu.get(groupPosition)).size();

    }

    @Override
    public long getChildId(int groupPosition, int childPosition) { // ChildList의 ID로 long 형 값을 반환
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        // ChildList의 View. 위 ParentList의 View를 얻을 때와 비슷하게 Layout 연결 후, layout 내 TextView 2개를 연결

        String itemName = (String)getChild(groupPosition,childPosition);

        if(convertView == null){
            LayoutInflater childInfla = (LayoutInflater) this.mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = childInfla.inflate(R.layout.order_item, null);
        }
        TextView litem_menu = (TextView) convertView.findViewById(R.id.tvLitem);
        TextView ritem_menu = (TextView) convertView.findViewById(R.id.tvRitem);
        litem_menu.setText(itemName);
        ritem_menu.setText(itemName);
        return convertView;

    }

    @Override
    public boolean hasStableIds() { return true; } // stable ID인지 boolean 값으로 반환

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) { return true; } // 선택여부를 boolean 값으로 반환

}


