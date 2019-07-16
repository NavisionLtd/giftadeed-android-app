package giftadeed.kshantechsoft.com.giftadeed.EmergencyPositioning;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.R;

public class EmergencyTypesAdapter extends
        RecyclerView.Adapter<EmergencyTypesAdapter.ViewHolder> {
    private List<EmergencyTypes> stList;

    public EmergencyTypesAdapter(List<EmergencyTypes> students) {
        this.stList = students;
    }

    // Create new views
    @Override
    public EmergencyTypesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                             int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.checkbox_row, null);
        // create ViewHolder
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final int pos = position;
        viewHolder.chkSelected.setText(stList.get(position).getType());
        viewHolder.chkSelected.setChecked(stList.get(position).isSelected());
        viewHolder.chkSelected.setTag(stList.get(position));

        viewHolder.chkSelected.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                EmergencyTypes contact = (EmergencyTypes) cb.getTag();
                contact.setSelected(cb.isChecked());
                stList.get(pos).setSelected(cb.isChecked());
            }
        });
    }

    // Return the size arraylist
    @Override
    public int getItemCount() {
        return stList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public CheckBox chkSelected;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            chkSelected = (CheckBox) itemLayoutView
                    .findViewById(R.id.chkSelected);
        }
    }

    // method to access in activity after updating selection
    public List<EmergencyTypes> getCheckedList() {
        return stList;
    }
}