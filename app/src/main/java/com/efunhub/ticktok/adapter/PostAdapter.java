package com.efunhub.ticktok.adapter;

import static com.efunhub.ticktok.retrofit.Constant.VIDEO_URL;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.efunhub.ticktok.R;
import com.efunhub.ticktok.interfaces.onItemClick_Listener;
import com.efunhub.ticktok.model.User_Profile_Model.Post;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.Holder> {

    ArrayList<Post> postList= new ArrayList<>();
    Context activity;
    onItemClick_Listener onItemClickListener;
    private LayoutInflater mInflater;


    public PostAdapter(Context context, ArrayList<Post> postList, onItemClick_Listener onItemClickListener) {
        this.activity = context;
        this.mInflater = LayoutInflater.from(context);
        this.postList = postList;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.posts_list_adapter, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Holder holder, int position) {

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.isMemoryCacheable();
//        Glide.with(activity).asBitmap().load(VIDEO_URL+postList.get(position).getVideo()).into(holder.videoView);
        Glide.with(activity).setDefaultRequestOptions(requestOptions).load(VIDEO_URL+postList.get(position).getVideo()).into(holder.videoView);

    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        ImageView videoView;

        public Holder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.Item_ClickListner(position);
                        }
                    }
                }
            });
        }
    }

}

