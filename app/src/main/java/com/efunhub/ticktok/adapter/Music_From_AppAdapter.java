package com.efunhub.ticktok.adapter;

import static com.efunhub.ticktok.retrofit.Constant.AudioPath;

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
import com.efunhub.ticktok.model.AudioFromAppModel;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class Music_From_AppAdapter extends RecyclerView.Adapter<Music_From_AppAdapter.ViewHolder> {

    ArrayList<AudioFromAppModel.AudioList> musicList;
    Activity activity;
    private LayoutInflater mInflater;
    ApplyMusicInterface applyMusicInterface;
    String music;
    private int music_position = RecyclerView.NO_POSITION;
    private int previous_position = RecyclerView.NO_POSITION;


    public Music_From_AppAdapter(Activity context, ArrayList<AudioFromAppModel.AudioList> musicList) {
        this.activity = context;
        this.mInflater = LayoutInflater.from(context);
        this.musicList = musicList;
        applyMusicInterface= (ApplyMusicInterface) context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.music_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindData(position);
        holder.tvVideoName.setText(musicList.get(position).getAudiofile());
        Log.d("AudioPath",musicList.get(position).getAudiofile());

        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(AudioPath+musicList.get(position).getAudiofile());
        long durationMusic = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        long Music = TimeUnit.MILLISECONDS.toSeconds(durationMusic);
        Log.d("secondsMusic", String.valueOf(Music) +musicList.get(position).getAudiofile());
        try {
            retriever.release();
        }
        catch (Exception e) {
        System.out.println(String.valueOf(e));
        }
//        holder.rlMusic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if( isMyServiceRunning(BackgroundSoundService.class))
//                {
//                    // AudioPath= musicList.get(position).getaPath();
//                    Log.d("AudioPath",musicList.get(position).getAudiofile());
//                    //stop previous running service
//                    if(position==music_position)
//                    {
//                            holder.img_pause.setVisibility(View.GONE);
//                            holder.img_play.setVisibility(View.VISIBLE);
//                            holder.music_layout.setBackgroundColor(getResources().getColor(R.color.white));
//                            Intent myService = new Intent(activity, BackgroundSoundService.class);
//                            activity.stopService(myService);
//
//                    }else {
//                        for(int i=0;i<musicList.size();i++) {
//                            if(i!= holder.getAdapterPosition()) {
//                                holder.img_pause.setVisibility(View.GONE);
//                                holder.img_play.setVisibility(View.VISIBLE);
//                                holder.itemView.setBackgroundColor(Color.WHITE);
//                                notifyItemChanged(i);
//                            }
//                        }
//                        notifyItemChanged(music_position);
//                        System.out.println("holder "+holder.getAdapterPosition());
//                        System.out.println("holder old position "+music_position);
//                        System.out.println("layout position "+holder.getLayoutPosition());
//                        Intent myService = new Intent(activity, BackgroundSoundService.class);
//                        activity.stopService(myService);
//                        holder.img_pause.setVisibility(View.VISIBLE);
//                        holder.img_play.setVisibility(View.GONE);
//                        holder.music_layout.setBackgroundColor(getResources().getColor(R.color.lightprimary));
//                        //start new service
//                        Intent myService1 = new Intent(activity, BackgroundSoundService.class);
//                        myService1.putExtra("MusicPath", AudioPath+musicList.get(holder.getAdapterPosition()).getAudiofile());
//                        System.out.println("music "+musicList.get(holder.getAdapterPosition()).getAudiofile());
//                        music=musicList.get(holder.getAdapterPosition()).getAudiofile();
//                        music_position=position;
//                        activity.startService(myService1);
//                    }
//
//                }else {
//                    // AudioPath= musicList.get(position).getaPath();
//                    Log.d("AudioPath",musicList.get(position).getAudiofile());
//                    holder.img_pause.setVisibility(View.VISIBLE);
//                    holder.img_play.setVisibility(View.GONE);
//                    holder.music_layout.setBackgroundColor(getResources().getColor(R.color.lightprimary));
//                    Intent serviceIntent = new Intent(activity, BackgroundSoundService.class);
//                    serviceIntent.putExtra("MusicPath", AudioPath+musicList.get(position).getAudiofile());
//                    music=musicList.get(position).getAudiofile();
//                    music_position=position;
//                    activity.startService(serviceIntent);
//                }
//            }
//        });

        holder.btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  ((Activity)activity).finish();
                applyMusicInterface.selectAndApplyMusic(AudioPath+musicList.get(position).getAudiofile());
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
        ImageView img_pause,img_play;
        //ImageView imgMusic;
        RelativeLayout rlMusic;
        Button btnApply;
        LinearLayout music_layout;

        public ViewHolder(@NonNull final View itemView) {
            super(itemView);
            tvVideoName = itemView.findViewById(R.id.tvVideoName);
            rlMusic=itemView.findViewById(R.id.rlMusic);
            btnApply=itemView.findViewById(R.id.btnApply);
            img_pause=itemView.findViewById(R.id.img_pause);
            img_play=itemView.findViewById(R.id.img_play);
            music_layout=itemView.findViewById(R.id.music_layout);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            //onItemClick_listener.Item_ClickListner(position);

                            // Get the position of the clicked item
                            int clickedPosition = getAdapterPosition();

                            // Set the selected position to the clicked position
                            music_position = clickedPosition;

                            if (isMyServiceRunning(BackgroundSoundService.class)) {
                                Log.d("AudioPath", musicList.get(getAdapterPosition()).getAudiofile());
                                //stop previous running service
                                if (previous_position == getAdapterPosition()) {
//                                    img_pause.setVisibility(View.GONE);
//                                    img_play.setVisibility(View.VISIBLE);
                                    Intent myService = new Intent(activity, BackgroundSoundService.class);
                                    activity.stopService(myService);

                                } else {
//                                    img_pause.setVisibility(View.VISIBLE);
//                                    img_play.setVisibility(View.GONE);
                                    Intent myService = new Intent(activity, BackgroundSoundService.class);
                                    activity.stopService(myService);
                                    myService.putExtra("MusicPath", AudioPath+musicList.get(getAdapterPosition()).getAudiofile());
                                    previous_position = getAdapterPosition();
                                    activity.startService(myService);
                                }
                            } else {

                                Intent serviceIntent = new Intent(activity, BackgroundSoundService.class);
                                serviceIntent.putExtra("MusicPath", AudioPath+musicList.get(position).getAudiofile());
                                music = musicList.get(position).getAudiofile();
                                previous_position = getAdapterPosition();
                                activity.startService(serviceIntent);
                            }

                            // Notify the adapter that the selected item has changed
                            notifyDataSetChanged();
                        }
                }
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

    public interface ApplyMusicInterface{
        public void selectAndApplyMusic(String musicPath);
    }
}

