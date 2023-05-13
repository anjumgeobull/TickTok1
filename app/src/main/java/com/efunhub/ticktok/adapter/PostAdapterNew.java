package com.efunhub.ticktok.adapter;


import static com.efunhub.ticktok.retrofit.Constant.VIDEO_URL;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.efunhub.ticktok.R;
import com.efunhub.ticktok.interfaces.StatusChange_Listener;
import com.efunhub.ticktok.interfaces.delete_Listener;
import com.efunhub.ticktok.interfaces.onVideoItemClick_Listener;
import com.efunhub.ticktok.model.User_Profile_Model.Post;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostAdapterNew extends RecyclerView.Adapter<PostAdapterNew.ViewHolder> {

   List<Post> videoList = new ArrayList<>();
    Context activity;
    private LayoutInflater mInflater;
    delete_Listener item_click;
    onVideoItemClick_Listener onItemClickListener;
    StatusChange_Listener onStatusChange_listener;

    public PostAdapterNew(Context context, List<Post> videoList,onVideoItemClick_Listener onItemClickListener,delete_Listener delete_clicklistener,StatusChange_Listener onStatusChange_listener) {
        this.activity = context;
        this.mInflater = LayoutInflater.from(context);
        this.videoList = videoList;
        this.onItemClickListener = onItemClickListener;
        this.item_click = delete_clicklistener;
        this.onStatusChange_listener = onStatusChange_listener;
    }

    @NonNull
    @Override
    public PostAdapterNew.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.myposts_list_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PostAdapterNew.ViewHolder holder, int position) {
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.isMemoryCacheable();
//        Glide.with(activity).asBitmap().load(VIDEO_URL+videoList.get(position).getVideo()).into(holder.videoView);
        Glide.with(activity).setDefaultRequestOptions(requestOptions).load(VIDEO_URL + videoList.get(position).getVideo()).into(holder.videoView);
        if(videoList.get(position).getVisibility().equals("private")) {
            holder.camcorder.setVisibility(View.GONE);
            holder.private_video.setVisibility(View.VISIBLE);
        }
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //activity.startActivity(new Intent(activity,PlayVideoActivity.class).putExtra("video_list", videoList.get(position)));
//                onItemClickListener.VideoItem_ClickListner(position);
//               // activity.startActivity(new Intent(activity,PlayVideoActivity.class));
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView videoView,private_video,camcorder;
        ImageView img_delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            img_delete = itemView.findViewById(R.id.img_delete);
            private_video = itemView.findViewById(R.id.private_video);
            camcorder = itemView.findViewById(R.id.video);
            img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (item_click != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            item_click.delete_click(position);
                        }
                    }
                }
            });
            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onItemClickListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onItemClickListener.VideoItem_ClickListner(position);
                        }
                    }
                }
            });
            videoView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (onStatusChange_listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            onStatusChange_listener.private_video_click(position);
                        }
                    }
                    return true;
                }
            });
        }
    }

}

