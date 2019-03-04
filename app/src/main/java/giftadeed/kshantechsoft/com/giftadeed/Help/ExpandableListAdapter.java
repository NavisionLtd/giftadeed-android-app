package giftadeed.kshantechsoft.com.giftadeed.Help;

/**
 * Created by ADMIN on 23-06-2016.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import giftadeed.kshantechsoft.com.giftadeed.R;
import java.util.HashMap;
import java.util.List;

import static android.text.Layout.JUSTIFICATION_MODE_INTER_WORD;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;
    Typeface fonttype;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
       // this.fonttype=fonttype;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_item, null);
        }

        /*TextView txtListChild = (TextView) convertView
                .findViewById(R.id.lblListItem);*/


        WebView txtListChild=convertView.findViewById(R.id.lblListItem);
        txtListChild.loadData(childText, "text/html; charset=UTF-8", null);
        txtListChild.setBackgroundResource(R.drawable.edit_text_border);
        //txtListChild.setTypeface(fonttype);

       /* Typeface tfTitlename = Typeface.createFromAsset(this._context.getAssets(), "Helvetica.otf");

        txtListChild.setTypeface(tfTitlename);*/
       // txtListChild.setText(childText);

        /*txtListChild.setJustificationMode(JUSTIFICATION_MODE_INTER_WORD);*/
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        TextView lblListcount=convertView.findViewById(R.id.lblListcount);
        lblListHeader.setTypeface(fonttype);
        lblListcount.setText(String.valueOf(String.format("%02d", groupPosition+1))+".");
       /* Typeface tfTitlename = Typeface.createFromAsset(this._context.getAssets(), "Helvetica.otf");

        lblListHeader.setTypeface(tfTitlename);*/
        lblListHeader.setText(headerTitle);

        ImageView lblListHeaderImage = (ImageView) convertView
                .findViewById(R.id.imageViewadd);


        if (isExpanded) {
            lblListHeaderImage.setImageResource(R.drawable.up_arrow);
        } else {
            lblListHeaderImage.setImageResource(R.drawable.down_arrow);
        }

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}