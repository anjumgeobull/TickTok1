package com.efunhub.ticktok.adapter;

import static com.efunhub.ticktok.retrofit.Constant.VIDEO_URL;

import static ly.img.android.pesdk.backend.decoder.ImageSource.getResources;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.efunhub.ticktok.CampaignModel.CampaignModelData;
import com.efunhub.ticktok.R;
import com.efunhub.ticktok.activity.LeadForm;
import com.efunhub.ticktok.activity.LoginActivity;
import com.efunhub.ticktok.activity.MainActivity;
import com.efunhub.ticktok.activity.MyProfile_Activity;
import com.efunhub.ticktok.activity.ProfileActivity;
import com.efunhub.ticktok.activity.Search_activity;
import com.efunhub.ticktok.activity.UploadActvity;
import com.efunhub.ticktok.activity.UploadVideoActivity;
import com.efunhub.ticktok.activity.UploadVideoForPerticularMusicActivity;
import com.efunhub.ticktok.activity.UserDetailsRegistration;
import com.efunhub.ticktok.application.SessionManager;
import com.efunhub.ticktok.backgroundservice.BackgroundSoundService;
import com.efunhub.ticktok.fragments.AllVideoFragment;
import com.efunhub.ticktok.fragments.NotificationsFragment;
import com.efunhub.ticktok.interfaces.Click_video_interface;
import com.efunhub.ticktok.interfaces.Like_video_interface;
import com.efunhub.ticktok.interfaces.ShowCommentListener;
import com.efunhub.ticktok.interfaces.requestcallback_interface;
import com.efunhub.ticktok.model.AllVideoModel;
import com.pedromassango.doubleclick.BuildConfig;
import com.pedromassango.doubleclick.DoubleClick;
import com.pedromassango.doubleclick.DoubleClickListener;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class VideoAdapterNew extends RecyclerView.Adapter<VideoAdapterNew.ViewHolder> {

    ArrayList<AllVideoModel.Data> allvideoList;

    Activity activity;
    Fragment fragment;
    private LayoutInflater mInflater;
    ShowCommentListener showCommentListener;
    Like_video_interface like_video_interface;
    Click_video_interface click_video_interface;

    requestcallback_interface requestcallbackInterface;
    int point;
    String type;
    String img_url = "https://grobiz.app/tiktokadmin/images/user_profiles/";

    private static final int PERMISSION_REQUEST_CODE = 200;

    public VideoAdapterNew(Activity context, ArrayList<AllVideoModel.Data> allvideoList, ShowCommentListener showCommentListener, Like_video_interface like_video_interface,
                           String type,Click_video_interface click_video_interface,requestcallback_interface requestcallback_interface) {
        this.activity = context;
        this.mInflater = LayoutInflater.from(context);
        this.allvideoList = allvideoList;
        this.showCommentListener = showCommentListener;
        this.like_video_interface = like_video_interface;
        this.click_video_interface = click_video_interface;
        this.requestcallbackInterface = requestcallback_interface;
        this.type = type;

        //this.userProfileModel_List = userProfileModel_List;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.new_video_list_adapter, parent, false);
        return new ViewHolder(view);
    }

    /*@Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.videoView.pause();
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        holder.videoView.start();
    }*/

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        System.out.println("VideoType===>"+allvideoList.get(position).getType());
        String videoType=allvideoList.get(position).getType();
        System.out.println(videoType);

        ///Get Video List normal and ads video
        holder.videoView.setVideoPath(VIDEO_URL + allvideoList.get(position).getVideo() +
                allvideoList.get(position).getcVideos());

        holder.videoView.start();

        holder.progressBar.setVisibility(View.VISIBLE);

        holder.btn_Wishlist.setChecked(allvideoList.get(position).getSelf_like() == 1); // Set the value of the checkbox based on self_like

        holder.videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            public void onPrepared(MediaPlayer mp) {
                mp.start();
                mp.setLooping(true);

                mp.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                    @Override
                    public void onVideoSizeChanged(MediaPlayer mp, int arg1, int arg2) {
                        holder.progressBar.setVisibility(View.GONE);
                        mp.start();
                        mp.setLooping(true);
                    }
                });
            }
        });

        RotateAnimation rotateAnimation = new RotateAnimation(0, 360f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(1000);
        rotateAnimation.setRepeatCount(Animation.INFINITE);

        if (SessionManager.onGetAutoCustomerId().isEmpty()) {
            holder.tv_show_points.setText("Free Registrations");
        } else {
            if (allvideoList.get(position).getPoint().equals("0") || allvideoList.get(position).getPoint().equals("") || allvideoList.get(position).getPoint()==null ) {
                holder.tv_show_points.setVisibility(View.GONE);
            } else {
                //System.out.println("Points" + SessionManager.onGetPoints());
                holder.tv_show_points.setVisibility(View.VISIBLE);
                holder.tv_show_points.setText("Points  " + allvideoList.get(position).getPoint());
            }
        }

        holder.itemView.setOnClickListener(new DoubleClick(new DoubleClickListener()
        {
            @Override
            public void onSingleClick(View view) {
                if (videoType.equals("normal_videos")) {
                    if (holder.llTopMenu.getVisibility() == View.VISIBLE) {
                        holder.videoView.start();
                        holder.llTopMenu.setVisibility(View.GONE);
                        holder.llSideMenu.setVisibility(View.GONE);
                        holder.lldetailsMenu.setVisibility(View.GONE);
                        holder.img_pause.setVisibility(View.GONE);
                        holder.skip_button.setVisibility(View.GONE);
                        holder.videodata.setVisibility(View.GONE);
                    } else {
                        holder.videoView.pause();
                        holder.llTopMenu.setVisibility(View.VISIBLE);
                        holder.llSideMenu.setVisibility(View.VISIBLE);
                        holder.skip_button.setVisibility(View.GONE);
                        holder.lldetailsMenu.setVisibility(View.VISIBLE);
                        holder.img_pause.setVisibility(View.VISIBLE);
                        //holder.videodata.setVisibility(View.VISIBLE);
                    }

                } else if (videoType.equals("ads_video"))
                {

                }else {
                    Log.e("Video", "activity is null");
                }
            }

            @Override
            public void onDoubleClick(View view) {
                new CountDownTimer(3000, 3000) {
                    public void onTick(long millisUntilFinished) {
                        if (holder.btn_Wishlist.isChecked() == false && allvideoList.get(position).getSelf_like() == 0) {
                            holder.heart_gif.setVisibility(View.VISIBLE);
                        }
                    }

                    public void onFinish() {
                        holder.llTopMenu.setVisibility(View.GONE);
                        holder.llSideMenu.setVisibility(View.GONE);
                        holder.lldetailsMenu.setVisibility(View.GONE);
                        holder.img_pause.setVisibility(View.GONE);
                        if (SessionManager.onGetAutoCustomerId().isEmpty()) {
                            activity.startActivity(new Intent(activity, LoginActivity.class));
                        } else {
                            if (holder.btn_Wishlist.isChecked() && allvideoList.get(position).getTotal_likes() != 0 && allvideoList.get(position).getSelf_like() == 1) {
                                allvideoList.get(position).setSelf_like(0);
                                holder.btn_Wishlist.setChecked(false);
                                like_video_interface.Like_video(allvideoList.get(position).get_id(), "0", position);
                                if(!SessionManager.onGetAutoCustomerId().equals(allvideoList.get(position).getUser_id())) {
                                    updatePoints(-2, holder.tv_show_points,allvideoList.get(position).getPoint(),holder.tvWishlistCount,position,-1);
                                }

                            } else if (!holder.btn_Wishlist.isChecked()) {
                                allvideoList.get(position).setSelf_like(1);
                                holder.heart_gif.setVisibility(View.VISIBLE);
                                holder.btn_Wishlist.setChecked(true);
                                like_video_interface.Like_video(allvideoList.get(position).get_id(), "1", position);
                                if(!SessionManager.onGetAutoCustomerId().equals(allvideoList.get(position).getUser_id())) {
                                    updatePoints(2, holder.tv_show_points,allvideoList.get(position).getPoint(),holder.tvWishlistCount,position,1);
                                }
                            }
                            //notifyDataSetChanged();
                        }
                        holder.heart_gif.setVisibility(View.GONE);

                        //onFinish();
                    }
                }.start();
                // Double tap here.
            }
        }));

        if (videoType.equals("normal_videos")) {
            holder.skip_button.setVisibility(View.GONE);
            holder.request_Button.setVisibility(View.GONE);
            holder.learnMore_button.setVisibility(View.GONE);
            holder.Enquire_Button.setVisibility(View.GONE);
        }else
        {
            //holder.videodata.setVisibility(View.VISIBLE);
            holder.skip_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    allvideoList.remove(position);
//                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                    // Calculate the position of the next video
                    int nextPosition = position + 1;

                    // Scroll to the next position if it's valid
//                    if (nextPosition < getItemCount()) {
//                        holder.scrollToPosition(nextPosition);
//                    }
                }
            });

//            if (activity != null) {
//                activity.startActivity(intent);
//            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    holder.skip_button.setVisibility(View.VISIBLE);
                }
            }, 10000);

            if(allvideoList.get(position).getType_of_campaign().equals("lead"))
            {
                holder.request_Button.setVisibility(View.GONE);
                holder.learnMore_button.setVisibility(View.GONE);
                holder.Enquire_Button.setVisibility(View.VISIBLE);
                click_video_interface.Click_video(position,"","","1");
            }else if(allvideoList.get(position).getType_of_campaign().equals("sales")){
                holder.request_Button.setVisibility(View.VISIBLE);
                holder.learnMore_button.setVisibility(View.GONE);
                holder.Enquire_Button.setVisibility(View.GONE);
                click_video_interface.Click_video(position,"","","1");

            }else if(allvideoList.get(position).getType_of_campaign().equals("product_consider"))
            {
                holder.request_Button.setVisibility(View.GONE);
                holder.learnMore_button.setVisibility(View.VISIBLE);
                holder.Enquire_Button.setVisibility(View.GONE);
                click_video_interface.Click_video(position,"","","1");
            }else {
                click_video_interface.Click_video(position,"","","1");
                holder.request_Button.setVisibility(View.GONE);
                holder.learnMore_button.setVisibility(View.GONE);
                holder.Enquire_Button.setVisibility(View.GONE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        click_video_interface.Click_video(position,"","1","0");
                    }
                }, 10000);
            }
        }
        holder.learnMore_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_video_interface.Click_video(position,"1","","");
                //redirect to provided link
                //String url=allvideoList.get(position).getLinks();
                String url="https://www.pepperfry.com/";
                System.out.println("URL LINK==>"+url);
                // Create an intent with the ACTION_VIEW action and the URL
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                // Launch the intent
                view.getContext().startActivity(intent);
            }
        });

        holder.request_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //apicall
                requestcallbackInterface.Callback_request_video(position);
            }
        });

        holder.Enquire_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, LeadForm.class)
                        .putExtra("campaign_user_auto_id", allvideoList.get(position).getCampaign_user_auto_id()));
            }
        });
//            holder.videoView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if(videoType.equals("ads_video")) {
//                        String url = "https://www.google.com"; // Replace with the desired URL
//                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
//                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
//                        if (activity != null) {
//                            activity.startActivity(intent);
//                        }
//                    }else {
//                        Log.e("Video", "activity is null");
//                    }
//                }
//            });


        holder.imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SessionManager.onGetAutoCustomerId().isEmpty()) {
                    activity.startActivity(new Intent(activity, LoginActivity.class));
                } else {
                    activity.startActivity(new Intent(activity, ProfileActivity.class)
                            .putExtra("user_id", allvideoList.get(position).getUser_id()));
                }
            }
        });

        holder.imgCreateVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.startActivity(new Intent(activity, UploadVideoActivity.class));
            }
        });

        holder.imgMusic.setOnClickListener(view -> activity.startActivity(new Intent(activity, UploadVideoForPerticularMusicActivity.class)));

        holder.imgShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                activity.setTheme(R.style.CustomBottomSheetDialogTheme);
                activity.startActivity(sendIntent);
            }
        });

        if (allvideoList.get(position).getProfile_image().equals(" "))
        {
            holder.imgProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_profile_user));
        }
        else {
            Picasso.with(activity)
                    .load(img_url + allvideoList.get(position).getProfile_image())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    //.transform(new CircleTransform())
                    .into(holder.imgProfile);
        }


        holder.img_Profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SessionManager.onGetAutoCustomerId().isEmpty()) {
                    activity.startActivity(new Intent(activity, LoginActivity.class));
                } else {
                    activity.startActivity(new Intent(activity, MyProfile_Activity.class));
                }
            }
        });

        holder.imgComment.setOnClickListener(view -> {
            if (SessionManager.onGetAutoCustomerId().isEmpty()) {
                activity.startActivity(new Intent(activity, LoginActivity.class));
            } else {
                showCommentListener.showComments(allvideoList.get(position).get_id(), position);
                holder.tvCommentCount.setText(String.valueOf(allvideoList.get(position).getTotal_comments()));
                notifyItemChanged(position);
            }
        });

        holder.tvCommentCount.setText(String.valueOf(allvideoList.get(position).getTotal_comments()));

        holder.img_myHome.setOnClickListener(v -> {
            activity.startActivity(new Intent(activity, MainActivity.class));

        });
        holder.tv_username.setText("@" + allvideoList.get(position).getName());

        holder.tv_video_caption.setText("#" + allvideoList.get(position).getVideo_caption() + " #" + allvideoList.get(position).getVisibility());

        holder.btn_Wishlist.setOnCheckedChangeListener(null);

        holder.btn_Wishlist.setOnClickListener(
                v -> {
                    if (SessionManager.onGetAutoCustomerId().isEmpty()) {
                        activity.startActivity(new Intent(activity, LoginActivity.class));
                    } else {
                        if (holder.btn_Wishlist.isChecked()) {
                            allvideoList.get(position).setSelf_like(1);
                            holder.btn_Wishlist.setChecked(true);
                            like_video_interface.Like_video(allvideoList.get(position).get_id(), "1", position);
                            if(!SessionManager.onGetAutoCustomerId().equals(allvideoList.get(position).getUser_id())) {
                                //updatePoints(2, holder.tv_show_points,allvideoList.get(position).getPoint());
                                updatePoints(2, holder.tv_show_points,allvideoList.get(position).getPoint(),holder.tvWishlistCount,position,1);
                            }

                        } else {
                            allvideoList.get(position).setSelf_like(0);
                            holder.btn_Wishlist.setChecked(false);
                            like_video_interface.Like_video(allvideoList.get(position).get_id(), "0", position);
                            if(!SessionManager.onGetAutoCustomerId().equals(allvideoList.get(position).getUser_id())) {
                               // updatePoints(-2, holder.tv_show_points,allvideoList.get(position).getPoint());
                                updatePoints(-2, holder.tv_show_points,allvideoList.get(position).getPoint(),holder.tvWishlistCount,position,-1);
//
                            }
                        }
                        //notifyDataSetChanged();
                    }

                }
        );


        holder.tvWishlistCount.setText(String.valueOf(allvideoList.get(position).getTotal_likes()));

        holder.imgSearch.setOnClickListener(v -> {
            activity.startActivity(new Intent(activity, Search_activity.class));
        });

        holder.et_serarch_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.Search_layout.setVisibility(View.GONE);
                activity.startActivity(new Intent(activity, Search_activity.class));
            }
        });

        holder.img_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.videoView.isPlaying() == true) {
                    holder.videoView.pause();
                    holder.img_pause.setVisibility(View.VISIBLE);
                } else {
                    holder.videoView.start();
                    holder.llTopMenu.setVisibility(View.GONE);
                    holder.llSideMenu.setVisibility(View.GONE);
                    holder.lldetailsMenu.setVisibility(View.GONE);
                    holder.img_pause.setVisibility(View.GONE);
                }
            }
        });

        holder.img_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fragment = new NotificationsFragment();
                FragmentManager manager = ((FragmentActivity) view.getContext()).getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.container, fragment);
                transaction.addToBackStack("AllVideo");
                transaction.commit();
            }
        });




        holder.tv_rgister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SessionManager.onGetAutoCustomerId().isEmpty()) {
                    holder.tv_rgister.setText("Free Registrations");
                    activity.startActivity(new Intent(activity, LoginActivity.class));
                } else {
                    if(SessionManager.onGetAiifaRegistration().equals("")) {
                         Intent i = new Intent(activity, UserDetailsRegistration.class);
                        activity.startActivity(i);
                    }else {
                        Intent i = new Intent(activity, UploadActvity.class);
                        activity.startActivity(i);
                    }
                }
            }
        });
    }

    private void updatePoints(int change, TextView pointsView,String points,TextView likeview,int position,int likes) {

        if (points.equals("")||points.equals("")) {
            point = 0;
        } else {
            point = Integer.parseInt(points);
        }
        int updatedPoints = point + change;

        int like= allvideoList.get(position).getTotal_likes() + likes;

//        allvideoList.get(position).setPoint(String.valueOf(updatedPoints));

        pointsView.setText("Points  " + String.valueOf(updatedPoints));
//        System.out.println("Total likes"+String.valueOf(allvideoList.get(position).getTotal_likes()));
        likeview.setText(String.valueOf(like));
        //notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        if (type.equals("for_you")) {
            return allvideoList.size();
        } else if (type.equals("following")) {
            return 3;
        }
        return allvideoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        VideoView videoView;
        ImageView imgCreateVideo, imgPause, imgShare, imgDownload, imgSearch, imgMusic, imgComment, img_pause, img_Profile, img_notification;
        //  HorizontalScrollView scroll;
        CircleImageView imgProfile;
        TextView tvWishlistCount, tvCommentCount, tv_show_points,tv_rgister;
        EditText et_serarch_view;
        ProgressBar progressBar = null;
        LinearLayout llTopMenu, llSideMenu, lldetailsMenu, Search_layout, img_myHome,videodata;
        ToggleButton btn_Wishlist;
        TextView tv_username, tv_video_caption;
        ImageView heart_gif;
        Button skip_button,Enquire_Button,request_Button,learnMore_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            videoView = itemView.findViewById(R.id.videoView);
            imgPause = itemView.findViewById(R.id.imgPause);
            imgCreateVideo = itemView.findViewById(R.id.imgCreateVideo);
            imgSearch = itemView.findViewById(R.id.imgSearch);
            img_pause = itemView.findViewById(R.id.img_pause);
            et_serarch_view = itemView.findViewById(R.id.et_serarch_view);
            tvCommentCount = itemView.findViewById(R.id.tvCommentCount);
            img_Profile = itemView.findViewById(R.id.profile_image_icon);
            btn_Wishlist = itemView.findViewById(R.id.btn_Wishlist);
            img_myHome = itemView.findViewById(R.id.img_myHome);
            imgProfile = itemView.findViewById(R.id.imgProfile);
            imgMusic = itemView.findViewById(R.id.imgMusic);
            Search_layout = itemView.findViewById(R.id.Search_layout);
            // scroll = itemView.findViewById(R.id.scroll);
            imgComment = itemView.findViewById(R.id.imgComment);
            imgShare = itemView.findViewById(R.id.imgShare);
            imgDownload = itemView.findViewById(R.id.imgDownload);
            tvWishlistCount = itemView.findViewById(R.id.tvWishlistCount);
            //tvShare=itemView.findViewById(R.id.tvShare);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progressbar);
            llTopMenu = itemView.findViewById(R.id.llTopMenu);
            llSideMenu = itemView.findViewById(R.id.llSideMenu);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_video_caption = itemView.findViewById(R.id.tv_video_caption);
            tv_show_points = itemView.findViewById(R.id.tv_show_points);
            tv_rgister = itemView.findViewById(R.id.register);
            //   llFirstMenu=itemView.findViewById(R.id.llFirstMenu);
            lldetailsMenu = itemView.findViewById(R.id.llDetailsMenu);
            heart_gif = itemView.findViewById(R.id.img_heart);
            img_notification = itemView.findViewById(R.id.imgNotification);
            skip_button = itemView.findViewById(R.id.skipButton);
            Enquire_Button = itemView.findViewById(R.id.EnquireButton);
            learnMore_button = itemView.findViewById(R.id.LearnmoreButton);
            request_Button = itemView.findViewById(R.id.RequestCallbackButton);
            videodata = itemView.findViewById(R.id.videodata);

        }

    }


}
