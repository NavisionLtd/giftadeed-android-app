package giftadeed.kshantechsoft.com.giftadeed.settings;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import java.util.ArrayList;

import giftadeed.kshantechsoft.com.giftadeed.Filter.CategoryPOJO;
import giftadeed.kshantechsoft.com.giftadeed.Group.GroupPOJO;
import giftadeed.kshantechsoft.com.giftadeed.Group.RecyclerViewClickListener;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Utils.DatabaseAccess;

public class CatListSettingsAdapter extends RecyclerView.Adapter<CatListSettingsAdapter.ViewHolder> {
    ArrayList<CategoryPOJO> list = new ArrayList<>();
    Context context;
    private RecyclerViewClickListener mListener;
    DatabaseAccess databaseAccess;

    public CatListSettingsAdapter(ArrayList<CategoryPOJO> groups, Context context) {
        this.list = groups;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        databaseAccess = DatabaseAccess.getInstance(context);
        databaseAccess.open();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_category_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.catName.setText(list.get(position).getName());

        if (list.get(position).isChecked()) {
            holder.catName.setChecked(true);
        } else {
            holder.catName.setChecked(false);
        }

        holder.catName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    list.get(position).setChecked(true);
                    databaseAccess.Update_Category_Details(list.get(position).getId(),"true");
                } else {
                    list.get(position).setChecked(false);
                    databaseAccess.Update_Category_Details(list.get(position).getId(),"false");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Switch catName;

        public ViewHolder(View view) {
            super(view);
            catName = (Switch) view.findViewById(R.id.switch_cat_name);
        }
    }
}
