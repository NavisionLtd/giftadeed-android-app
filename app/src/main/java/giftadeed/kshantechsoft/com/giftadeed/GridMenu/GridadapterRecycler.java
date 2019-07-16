package giftadeed.kshantechsoft.com.giftadeed.GridMenu;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RectShape;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Utils.FontDetails;

//import androidx.cardview.widget.CardView;
//import com.squareup.picasso.Picasso;

/**
 * Created by I-Sys on 05-Dec-16.
 */

public class GridadapterRecycler extends RecyclerView.Adapter<GridadapterRecycler.ViewHolder> {
    private ArrayList<DataModel> arrlist;
    private Context context;
    OnItemClickListener monclicklistner;
    static int row_index;
    int color_pos;
    String count;

    public GridadapterRecycler(ArrayList<DataModel> arrlist, Context context, int pos, String count, OnItemClickListener litener) {
        this.arrlist = arrlist;
        this.context = context;
        this.monclicklistner = litener;
        this.color_pos = pos;
        this.count = count;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.full_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        DataModel model = arrlist.get(position);
        final ViewHolder mholder = (ViewHolder) holder;
        mholder.rowtext.setText(arrlist.get(position).getVersionName());
        mholder.rowImage.setImageResource(arrlist.get(position).getImageurl());
        if (arrlist.get(position).getVersionName().equals("NOTIFICATIONS")) {
            try {
                if (Integer.parseInt(count) > 0) {
                    mholder.notificationcount.setVisibility(View.VISIBLE);
                    mholder.notificationcount.setText(count);
                } else {
                    mholder.notificationcount.setVisibility(View.GONE);
                }
            } catch (Exception e) {

            }
        }


        //Picasso.with(context).load(arrlist.get(position).getImageurl()).resize(300, 250).into(mholder.rowImage);
        mholder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (monclicklistner != null) {
                    row_index = position;

                    monclicklistner.onItemClick(view, position);
                }
            }
        });
        if (color_pos == position) {
            ShapeDrawable shape = new ShapeDrawable(new RectShape());
            shape.getPaint().setColor(Color.parseColor("#FF7302"));
            shape.getPaint().setStyle(Paint.Style.STROKE);
            shape.getPaint().setStrokeWidth(3);
            mholder.row_relativelayout.setBackground(shape);

        } else {
            mholder.row_relativelayout.setBackgroundColor(Color.parseColor("#ffffff"));
            // mholder.tv1.setTextColor(Color.parseColor("#000000"));
        }

    }

    @Override
    public int getItemCount() {
        return arrlist.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView rowtext, notificationcount;
        private ImageView rowImage;
        private CardView card;
        private RelativeLayout row_relativelayout;

        public ViewHolder(View view) {
            super(view);
            rowtext = (TextView) view.findViewById(R.id.grid_text);
            rowImage = (ImageView) view.findViewById(R.id.grid_image);
            card = (CardView) view.findViewById(R.id.cardlaout);
            rowtext.setTypeface(new FontDetails(context).fontStandardForPage);
            row_relativelayout = view.findViewById(R.id.relativelayout_card);
            notificationcount = view.findViewById(R.id.notificationcount);
            rowtext.setTypeface(new FontDetails(context).fontStandardForPage);
        }

    }

    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }

    public void SetOnItemClickListener(OnItemClickListener mItemClickListener) {
        this.monclicklistner = mItemClickListener;
    }
}
