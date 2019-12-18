/*
 * Copyright (C) Navision Ltd. - All Rights Reserved
 * Unauthorized copying of this file, via any medium is strictly prohibited
 */

package giftadeed.kshantechsoft.com.giftadeed.AdvisoryBoard;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import giftadeed.kshantechsoft.com.giftadeed.R;
import giftadeed.kshantechsoft.com.giftadeed.Utils.WebServices;

import static java.security.AccessController.getContext;

/**
 * Created by roshan on 19/3/18.
 */

public class AdvisoryAdapter extends RecyclerView.Adapter<AdvisoryAdapter.ViewHolder> {
    private Context context;
    private List<AdvisoryResponse.Advisory> advisoryList;
    String img_fb_link = "", img_googlr_link = "", img_twitter_link = "", img_insta_link = "", img_linkedin_link = "", img_other_link = "";

    public AdvisoryAdapter(Context context, List<AdvisoryResponse.Advisory> advisoryList) {
        this.context = context;
        this.advisoryList = advisoryList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.card_advisory, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        AdvisoryResponse.Advisory advisory = advisoryList.get(position);


        final List<String> list_fb_link = new ArrayList<>();
        final List<String> list_google_link = new ArrayList<>();
        final List<String> list_twitter_link = new ArrayList<>();
        final List<String> list_linkedin_link = new ArrayList<>();
        final List<String> list_instagram_link = new ArrayList<>();
        final List<String> list_other_link = new ArrayList<>();

        String img_fb_link_new = advisoryList.get(position).getSocialLinks();
        if (!(img_fb_link_new.equals(""))) {

            //  if (img_fb_link_new.equals("")) {
            String img_fb_link_new_arr[] = img_fb_link_new.split(",");
            // }
            for (int s = 0; s < img_fb_link_new_arr.length; s++) {
                String strlink = img_fb_link_new_arr[s].toLowerCase().toString();
                if (strlink.contains("facebook")) {
//img_fb_link=img_fb_link_new_arr[s];
                    list_fb_link.add(img_fb_link_new_arr[s]);
                } else if (strlink.contains("google")) {
                    list_google_link.add(img_fb_link_new_arr[s]);
                } else if (strlink.contains("twitter")) {
                    list_twitter_link.add(img_fb_link_new_arr[s]);
                } else if (strlink.contains("instagram")) {
                    list_instagram_link.add(img_fb_link_new_arr[s]);
                } else if (strlink.contains("linkedin")) {
                    list_linkedin_link.add(img_fb_link_new_arr[s]);
                } else {
                    list_other_link.add(img_fb_link_new_arr[s]);
                }

            }
        }

        if (list_fb_link.size() == 1) {
            holder.imgFacebook.setVisibility(View.VISIBLE);
        } else {
            holder.imgFacebook.setVisibility(View.GONE);
        }


        if (list_google_link.size() == 1) {
            holder.imgGooglePlus.setVisibility(View.VISIBLE);
        } else {
            holder.imgGooglePlus.setVisibility(View.GONE);
        }

        if (list_linkedin_link.size() == 1) {
            holder.imgLinkedIn.setVisibility(View.VISIBLE);
        } else {
            holder.imgLinkedIn.setVisibility(View.GONE);
        }

        if (list_twitter_link.size() == 1) {
            holder.imgTwitter.setVisibility(View.VISIBLE);
        } else {
            holder.imgTwitter.setVisibility(View.GONE);
        }

        if (list_other_link.size() == 1) {
            holder.imgWorld.setVisibility(View.VISIBLE);
        } else {
            holder.imgWorld.setVisibility(View.GONE);
        }


        holder.imgLinkedIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                        /*Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(img_fb_link));
                        startActivity(myIntent);*/
                    String img_fb_link_new = advisoryList.get(position).getSocialLinks();
                    if (!(img_fb_link_new.equals(""))) {

                        //  if (img_fb_link_new.equals("")) {
                        String img_fb_link_new_arr[] = img_fb_link_new.split(",");
                        // }
                        for (int s = 0; s < img_fb_link_new_arr.length; s++) {
                            String strlink = img_fb_link_new_arr[s].toLowerCase().toString();
                            if (strlink.contains("facebook")) {
//img_fb_link=img_fb_link_new_arr[s];
                                list_fb_link.add(img_fb_link_new_arr[s]);
                            } else if (strlink.contains("google")) {
                                list_google_link.add(img_fb_link_new_arr[s]);
                            } else if (strlink.contains("twitter")) {
                                list_twitter_link.add(img_fb_link_new_arr[s]);
                            } else if (strlink.contains("instagram")) {
                                list_instagram_link.add(img_fb_link_new_arr[s]);
                            } else if (strlink.contains("linkedin")) {
                                list_linkedin_link.add(img_fb_link_new_arr[s]);
                            } else {
                                list_other_link.add(img_fb_link_new_arr[s]);
                            }

                        }
                    }

                    img_linkedin_link = list_linkedin_link.get(0).trim();
                    String url = img_linkedin_link;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);

                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "No application can handle this request. Please install a webbrowser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });


        holder.imgFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                        /*Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(img_fb_link));
                        startActivity(myIntent);*/

                    String img_fb_link_new = advisoryList.get(position).getSocialLinks();
                    if (!(img_fb_link_new.equals(""))) {

                        //  if (img_fb_link_new.equals("")) {
                        String img_fb_link_new_arr[] = img_fb_link_new.split(",");
                        // }
                        for (int s = 0; s < img_fb_link_new_arr.length; s++) {
                            String strlink = img_fb_link_new_arr[s].toLowerCase().toString();
                            if (strlink.contains("facebook")) {
//img_fb_link=img_fb_link_new_arr[s];
                                list_fb_link.add(img_fb_link_new_arr[s]);
                            } else if (strlink.contains("google")) {
                                list_google_link.add(img_fb_link_new_arr[s]);
                            } else if (strlink.contains("twitter")) {
                                list_twitter_link.add(img_fb_link_new_arr[s]);
                            } else if (strlink.contains("instagram")) {
                                list_instagram_link.add(img_fb_link_new_arr[s]);
                            } else if (strlink.contains("linkedin")) {
                                list_linkedin_link.add(img_fb_link_new_arr[s]);
                            } else {
                                list_other_link.add(img_fb_link_new_arr[s]);
                            }

                        }
                    }



                    img_fb_link = list_fb_link.get(0).trim();
                    String url = img_fb_link;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);

                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "No application can handle this request. Please install a webbrowser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        holder.imgGooglePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                        /*Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(img_fb_link));
                        startActivity(myIntent);*/

                    String img_fb_link_new = advisoryList.get(position).getSocialLinks();
                    if (!(img_fb_link_new.equals(""))) {

                        //  if (img_fb_link_new.equals("")) {
                        String img_fb_link_new_arr[] = img_fb_link_new.split(",");
                        // }
                        for (int s = 0; s < img_fb_link_new_arr.length; s++) {
                            String strlink = img_fb_link_new_arr[s].toLowerCase().toString();
                            if (strlink.contains("facebook")) {
//img_fb_link=img_fb_link_new_arr[s];
                                list_fb_link.add(img_fb_link_new_arr[s]);
                            } else if (strlink.contains("google")) {
                                list_google_link.add(img_fb_link_new_arr[s]);
                            } else if (strlink.contains("twitter")) {
                                list_twitter_link.add(img_fb_link_new_arr[s]);
                            } else if (strlink.contains("instagram")) {
                                list_instagram_link.add(img_fb_link_new_arr[s]);
                            } else if (strlink.contains("linkedin")) {
                                list_linkedin_link.add(img_fb_link_new_arr[s]);
                            } else {
                                list_other_link.add(img_fb_link_new_arr[s]);
                            }

                        }
                    }



                    img_googlr_link = list_google_link.get(0).trim();
                    String url = img_googlr_link;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);

                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "No application can handle this request. Please install a webbrowser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });


        holder.imgTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                        /*Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(img_fb_link));
                        startActivity(myIntent);*/

                    String img_fb_link_new = advisoryList.get(position).getSocialLinks();
                    if (!(img_fb_link_new.equals(""))) {

                        //  if (img_fb_link_new.equals("")) {
                        String img_fb_link_new_arr[] = img_fb_link_new.split(",");
                        // }
                        for (int s = 0; s < img_fb_link_new_arr.length; s++) {
                            String strlink = img_fb_link_new_arr[s].toLowerCase().toString();
                            if (strlink.contains("facebook")) {
//img_fb_link=img_fb_link_new_arr[s];
                                list_fb_link.add(img_fb_link_new_arr[s]);
                            } else if (strlink.contains("google")) {
                                list_google_link.add(img_fb_link_new_arr[s]);
                            } else if (strlink.contains("twitter")) {
                                list_twitter_link.add(img_fb_link_new_arr[s]);
                            } else if (strlink.contains("instagram")) {
                                list_instagram_link.add(img_fb_link_new_arr[s]);
                            } else if (strlink.contains("linkedin")) {
                                list_linkedin_link.add(img_fb_link_new_arr[s]);
                            } else {
                                list_other_link.add(img_fb_link_new_arr[s]);
                            }

                        }
                    }



                    img_twitter_link = list_twitter_link.get(0).trim();
                    String url = img_twitter_link;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);

                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "No application can handle this request. Please install a webbrowser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        holder.imgWorld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                        /*Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(img_fb_link));
                        startActivity(myIntent);*/

                    String img_fb_link_new = advisoryList.get(position).getSocialLinks();
                    if (!(img_fb_link_new.equals(""))) {

                        //  if (img_fb_link_new.equals("")) {
                        String img_fb_link_new_arr[] = img_fb_link_new.split(",");
                        // }
                        for (int s = 0; s < img_fb_link_new_arr.length; s++) {
                            String strlink = img_fb_link_new_arr[s].toLowerCase().toString();
                            if (strlink.contains("facebook")) {
//img_fb_link=img_fb_link_new_arr[s];
                                list_fb_link.add(img_fb_link_new_arr[s]);
                            } else if (strlink.contains("google")) {
                                list_google_link.add(img_fb_link_new_arr[s]);
                            } else if (strlink.contains("twitter")) {
                                list_twitter_link.add(img_fb_link_new_arr[s]);
                            } else if (strlink.contains("instagram")) {
                                list_instagram_link.add(img_fb_link_new_arr[s]);
                            } else if (strlink.contains("linkedin")) {
                                list_linkedin_link.add(img_fb_link_new_arr[s]);
                            } else {
                                list_other_link.add(img_fb_link_new_arr[s]);
                            }

                        }
                    }



                    img_other_link = list_other_link.get(0).trim();
                    String url = img_other_link;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);

                } catch (ActivityNotFoundException e) {
                    Toast.makeText(context, "No application can handle this request. Please install a webbrowser", Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }
            }
        });

        Picasso.with(context).load(WebServices.MAIN_SUB_URL + advisory.getImgUrl()).placeholder(R.drawable.advisory).into(holder.imageViewAdvisory);
        holder.imageViewAdvisory.setScaleType(ImageView.ScaleType.FIT_XY);
        holder.txtBoardPersonName.setText("" + advisory.getName());
        holder.advisoryDesc.setText("" + advisory.getDesc());
        holder.advisoryDesig.setText("" + advisory.getDesig());
    }

    @Override
    public int getItemCount() {
        return advisoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewAdvisory;
        private TextView txtBoardPersonName;
        private ImageView imgTwitter;
        private ImageView imgLinkedIn;
        private ImageView imgGooglePlus;
        private ImageView imgFacebook;
        private ImageView imgWorld;
        private TextView advisoryDesc;
        private TextView advisoryDesig;

        public ViewHolder(View view) {
            super(view);
            imageViewAdvisory = (ImageView) view.findViewById(R.id.imageViewAdvisory);
            txtBoardPersonName = (TextView) view.findViewById(R.id.txtBoardPersonName);
            advisoryDesig = (TextView) view.findViewById(R.id.txtAboutMe);

            imgTwitter = (ImageView) view.findViewById(R.id.imgTwitter);
            imgLinkedIn = (ImageView) view.findViewById(R.id.imgLinkedIn);
            imgGooglePlus = (ImageView) view.findViewById(R.id.imgGooglePlus);
            imgFacebook = (ImageView) view.findViewById(R.id.imgFacebook);
            imgWorld = (ImageView) view.findViewById(R.id.imgWorld);
            advisoryDesc = (TextView) view.findViewById(R.id.advisoryDesc);
        }
    }
}
