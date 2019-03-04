package giftadeed.kshantechsoft.com.giftadeed.Needdetails;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.R;

/**
 * Created by I-Sys on 05-Mar-18.
 */

public class CommentAdapter extends ArrayAdapter<Comment> {
    List<Comment> item;
    Context context;
    public CommentAdapter(Context context, int resource, List<Comment> item) {
        super(context, resource, item);
        this.item=item;
        this.context=context;

    }
    private class Viewholder {

        TextView fName;
        TextView txtComment;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Viewholder holder = null;
        final Comment rowItem = getItem(position);
        LayoutInflater minflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = minflater.inflate(R.layout.commentcard, null);
            holder = new Viewholder();
            holder.fName = (TextView) convertView.findViewById(R.id.txtcomment_fname);
            holder.txtComment = (TextView) convertView.findViewById(R.id.txtcomment_comment);
           // holder.imgid = (ImageView) convertView.findViewById(R.id.icon);
           // holder.deleteicon = (ImageView) convertView.findViewById(R.id.delete);
            convertView.setTag(holder);

        } else
            holder = (Viewholder) convertView.getTag();

        //holder.imgid.setImageResource(item.get(position).getImageid());
        if (!item.get(position).getPrivacy().equals("Anonymous")) {

            holder.fName.setText(item.get(position).getFName());
        }

        holder.txtComment.setText(item.get(position).getComment());


       /* holder.imgid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
            }
        });*/
        return convertView;


    }


}
