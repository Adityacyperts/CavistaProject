package com.example.imagegallery.Activity.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.imagegallery.Activity.view.DetailView;
import com.example.imagegallery.R;
import com.example.model.DownloadResult;
import com.example.model.MyImage;
import com.example.model.MyResult;
import com.example.utility.PiccasoTrustAll;
import com.example.utility.Utils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GalleryDataAdapter extends RecyclerView.Adapter<GalleryDataAdapter.ViewHolder> implements Filterable {
    List<MyResult> mArrayList;
    List<MyResult> mFilteredList;
    Context context;
    GridLayoutManager gridLayoutManager;
    SubListAdapter mAdapter;
    public static MyResult result;
    public static MyImage myImage;

    public GalleryDataAdapter(List<MyResult> mArrayList, Context context) {
        this.mArrayList = mArrayList;
        this.mFilteredList = mArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_row_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        MyResult model = mFilteredList.get(position);
        if (model != null) {
            try {
                viewHolder.tv_description.setText((model.getDescription()));
            } catch (Exception e) {
                e.printStackTrace();
                viewHolder.tv_description.setText("");

            }
            try {
                viewHolder.tv_title.setText(model.getTitle());
            } catch (Exception e) {
                e.printStackTrace();
                viewHolder.tv_title.setText("");

            }

            if (Utils.isNetworkConnected(context) && !Utils.checkString(mArrayList.get(position).getLink())) {
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
                                        .load(mFilteredList.get(position).getLink())
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
            if (model.getList() != null && model.getList().size() > 0) {
                viewHolder.rv_sub_list.setVisibility(View.VISIBLE);
                gridLayoutManager = new GridLayoutManager(context, 3);
                viewHolder.rv_sub_list.setLayoutManager(gridLayoutManager);

                try {
                    mAdapter = new SubListAdapter(context, model.getList());
                    viewHolder.rv_sub_list.setAdapter(mAdapter);
                    viewHolder.rv_sub_list.setHasFixedSize(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            viewHolder.img_android.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    result = mFilteredList.get(position);
                    Intent i = new Intent(context, DetailView.class);
                    context.startActivity(i);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        try {
            return mFilteredList.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {
                    mFilteredList = mArrayList;
                } else {

                    ArrayList<MyResult> filteredList = new ArrayList<>();

                    for (MyResult list : mArrayList) {

                        if ((!Utils.checkString(list.getTitle())&&list.getTitle().toLowerCase().trim().contains(charString.toLowerCase().trim()))
                                ||(!Utils.checkString(list.getDescription())&& list.getDescription().toLowerCase().trim().contains(charString.toLowerCase().trim()))
                        ) {
                            filteredList.add(list);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<MyResult>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView img_android;
        ProgressBar pbLoadingBar;
        RecyclerView rv_sub_list;
        TextView tv_title, tv_description;

        public ViewHolder(@NonNull View view) {
            super(view);
            img_android = view.findViewById(R.id.img_view);
            pbLoadingBar = view.findViewById(R.id.pb_loading_bar);
            rv_sub_list = view.findViewById(R.id.rv_sub_list);
            tv_title = view.findViewById(R.id.tv_title);
            tv_description = view.findViewById(R.id.tv_description);

        }
    }
}
