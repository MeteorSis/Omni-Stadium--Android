package skhu.cse.network.omni_stadium;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class ExplvAdapterSign extends BaseExpandableListAdapter {

    private Context context;
    private ArrayList<String> arrayGroup;
    private HashMap<String, ArrayList<String>> arrayChild;

    public ExplvAdapterSign(Context context, ArrayList<String> arrayGroup, HashMap<String, ArrayList<String>> arrayChild){
        super();
        this.context = context;
        this.arrayGroup = arrayGroup;
        this.arrayChild = arrayChild;
    }

    @Override
    public int getGroupCount() {
        return arrayGroup.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return arrayChild.get(arrayGroup.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return arrayGroup.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return arrayChild.get(arrayGroup.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String groupName = arrayGroup.get(groupPosition);

        long group_id = getGroupId(groupPosition);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.explist_group, null);
        }

        ImageView ivGroup = (ImageView)convertView.findViewById(R.id.ivGroup);
        if(group_id == 0)
            ivGroup.setImageResource(R.drawable.ic_power_settings_new_black);
        else if(group_id == 1)
            ivGroup.setImageResource(R.drawable.ic_ticket_image);
        else
            ivGroup.setImageResource(R.drawable.ic_people_black);

        TextView expListHeader = (TextView) convertView.findViewById(R.id.textGroup);
        expListHeader.setTypeface(null, Typeface.BOLD);
        expListHeader.setText(groupName);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String childName = (String)getChild(groupPosition,childPosition);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.explist_child, null);
        }

        TextView expListChild = (TextView) convertView.findViewById(R.id.textChild);
        expListChild.setTypeface(null, Typeface.BOLD);
        expListChild.setText(childName);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }


}
