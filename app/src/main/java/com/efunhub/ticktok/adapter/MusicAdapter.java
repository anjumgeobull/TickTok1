package com.efunhub.ticktok.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.efunhub.ticktok.R;
import com.efunhub.ticktok.backgroundservice.BackgroundSoundService;
import com.efunhub.ticktok.interfaces.onItemClick_Listener;
import com.efunhub.ticktok.model.AudioModel;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.ViewHolder> {

    ArrayList<AudioModel> musicList;
    Activity activity;
    private LayoutInflater mInflater;
    ApplyMusicInterface applyMusicInterface;
    String music;
    //onItemClick_Listener onItemClick_listener;
    private int music_position = RecyclerView.NO_POSITION;
    private int previous_position = RecyclerView.NO_POSITION;

    public MusicAdapter(Activity context, ArrayList<AudioModel> musicList) {
        this.activity = context;
        this.mInflater = LayoutInflater.from(context);
        this.musicList = musicList;
        applyMusicInterface = (ApplyMusicInterface) context;
        //this.onItemClick_listener = onItemClick_listener;
    }

    @NonNull
    @Override
    public MusicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.music_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.tvVideoName.setText(musicList.get(position).getaName());
        //Log.d("AudioPath",musicList.get(position).getaPath());

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(musicList.get(position).getaPath());
        long durationMusic = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        long Music = TimeUnit.MILLISECONDS.toSeconds(durationMusic);
        holder.bindData(position);

        retriever.release();

        holder.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  ((Activity)activity).finish();
                if(isMyServiceRunning(BackgroundSoundService.class))
                {
                    Intent myService = new Intent(activity, BackgroundSoundService.class);
                    activity.stopService(myService);
                }
                applyMusicInterface.selectAndApplyMusic(musicList.get(position).getaPath());
            }
        });

    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return musicList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvVideoName;
        ImageView img_pause, img_play;
        RelativeLayout rlMusic;
        Button btnApply;
        LinearLayout music_layout;


        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvVideoName = itemView.findViewById(R.id.tvVideoName);
            rlMusic = itemView.findViewById(R.id.rlMusic);
            btnApply = itemView.findViewById(R.id.btnApply);
            img_pause = itemView.findViewById(R.id.img_pause);
            img_play = itemView.findViewById(R.id.img_play);
            music_layout = itemView.findViewById(R.id.music_layout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if (onItemClick_listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {

                            if (isMyServiceRunning(BackgroundSoundService.class)) {
                                Log.d("AudioPath", musicList.get(getAdapterPosition()).getaPath());
                                //stop previous running service
                                if (previous_position == getAdapterPosition()) {
                                    Intent myService = new Intent(activity, BackgroundSoundService.class);
                                    activity.stopService(myService);

                                } else {
                                    Intent myService = new Intent(activity, BackgroundSoundService.class);
                                    activity.stopService(myService);
                                    myService.putExtra("MusicPath", musicList.get(getAdapterPosition()).getaPath());
                                    previous_position = getAdapterPosition();
                                    activity.startService(myService);
                                }
                            } else {

                                Intent serviceIntent = new Intent(activity, BackgroundSoundService.class);
                                serviceIntent.putExtra("MusicPath", musicList.get(position).getaPath());
                                music = musicList.get(position).getaPath();
                                previous_position = getAdapterPosition();
                                activity.startService(serviceIntent);
                            }

                            // Notify the adapter that the selected item has changed
                            notifyDataSetChanged();
                        }
                    }
              //  }
            });
        }

        public void bindData(int position) {
            // Set the background of the item view based on the selected position
            if (position == previous_position) {
                if (!isMyServiceRunning(BackgroundSoundService.class)) {
                    img_pause.setVisibility(View.GONE);
                    img_play.setVisibility(View.VISIBLE);
                    itemView.setBackgroundColor(Color.WHITE);
                } else {
                    img_pause.setVisibility(View.VISIBLE);
                    img_play.setVisibility(View.GONE);
                    itemView.setBackgroundColor(activity.getResources().getColor(R.color.lightprimary));
                }
            } else {
                img_pause.setVisibility(View.GONE);
                img_play.setVisibility(View.VISIBLE);
                itemView.setBackgroundColor(Color.WHITE);
            }
        }
    }

    public interface ApplyMusicInterface {
        public void selectAndApplyMusic(String musicPath);
    }
}

