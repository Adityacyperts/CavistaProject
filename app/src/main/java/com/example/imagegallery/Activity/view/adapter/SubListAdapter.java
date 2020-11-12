package com.example.imagegallery.Activity.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.imagegallery.Activity.view.DetailView;
import com.example.imagegallery.R;
import com.example.model.MyImage;
import com.example.utility.PiccasoTrustAll;
import com.example.utility.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SubListAdapter extends RecyclerView.Adapter<SubListAdapter.ViewHolder> {
    Context context;
    List<MyImage> list;

    public SubListAdapter(Context context, List<MyImage> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public SubListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_image_row_layout, parent, false);
        return new SubListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubListAdapter.ViewHolder viewHolder, int position) {
        MyImage model=list.get(position);
        if(model!=null) {
            if (Utils.isNetworkConnected(context) && !Utils.checkString(list.get(position).getLink())) {
                viewHolder.pbLoadingBar.setVisibility(View.VISIBLE);
                Picasso.with(context)
                        .load(model.getLink())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(viewHolder.img_android, new Callback() {
                            @Override
                            public void onSuccess() {
                                viewHolder.pbLoadingBar.setVisibility(View.GONE);

                            }

                            @Override
                            public void onError() {
                                //Try again online if cache failed
                                Picasso.with(context)
                                        .load(list.get(position).getLink())
                                        .error(R.mipmap.image_not_found)
                                        .into(viewHolder.img_android, new Callback() {
                                            @Override
                                            public void onSuccess() {
                                                viewHolder.pbLoadingBar.setVisibility(View.GONE);

                                            }

                                            @Override
                                            public void onError() {
                                                viewHolder.pbLoadingBar.setVisibility(View.GONE);
                                                viewHolder.img_android.setImageResource(R.mipmap.image_not_found);

                                            }
                                        });
                            }
                        });
            } else {
                viewHolder.pbLoadingBar.setVisibility(View.GONE);
//                viewHolder.img_android.setImageResource(R.mipmap.internet_access_error);
            }
            viewHolder.img_android.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GalleryDataAdapter.myImage = list.get(position);
                    Intent i = new Intent(context, DetailView.class);
                    context.startActivity(i);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_android;
        ProgressBar pbLoadingBar;
        public ViewHolder(@NonNull View view) {
            super(view);
            img_android = view.findViewById(R.id.img_view);
            pbLoadingBar = view.findViewById(R.id.pb_loading_bar);
        }
    }
}
