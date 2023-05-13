package com.efunhub.ticktok.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;

import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.graphics.Typeface;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.CamcorderProfile;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.opengl.GLException;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.CountDownTimer;
import android.os.Environment;

import android.os.Handler;
import android.os.HandlerThread;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.provider.Settings;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.RelativeSizeSpan;
import android.util.Log;

import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;
import com.arthenica.mobileffmpeg.LogCallback;
import com.arthenica.mobileffmpeg.LogMessage;
import com.arthenica.mobileffmpeg.Statistics;
import com.arthenica.mobileffmpeg.StatisticsCallback;
import com.coremedia.iso.boxes.Container;
import com.daasuu.gpuv.composer.FillMode;
import com.daasuu.gpuv.composer.GPUMp4Composer;
import com.daasuu.gpuv.composer.Rotation;
import com.efunhub.ticktok.R;
import com.efunhub.ticktok.adapter.EffectAdapter;
import com.efunhub.ticktok.adapter.MusicAdapter;
import com.efunhub.ticktok.adapter.VideoSpeedAdapter;
import com.efunhub.ticktok.backgroundservice.BackgroundSoundService;
import com.efunhub.ticktok.effect.FilterType;
import com.efunhub.ticktok.fragments.EffectFragments;


import com.efunhub.ticktok.model.VideoSpeedModel;
import com.efunhub.ticktok.videorcording.AutoFitTextureView;
import com.efunhub.ticktok.widget.SampleCameraGLView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.tabs.TabLayout;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static android.media.CamcorderProfile.QUALITY_HIGH_SPEED_LOW;


public class UploadVideoActivity extends AppCompatActivity implements View.OnClickListener, ActivityCompat.OnRequestPermissionsResultCallback , VideoSpeedAdapter.VideoSpeedInterface {

    private String TAG1="UploadVideoActivity";
    private static int VIDEO_REQUEST = 101;
  //  private static int SELECT_VIDEO = 22;
    View root;
    Uri videoUri;
    ImageView imgStartVideo,imgNext,imgback,imgMusic1;
    ImageView imgGallery, imgEffect,img_back;
    LinearLayout llVideoMenu;
    RelativeLayout llMusic,llFlip,rlTimer,rlSpeed;

    RecyclerView rvEffect,rcVideoSpeed;

    //private ListView lv;

    TextView tvTimer,tv_song_name;
    String filePathMergeVideoAudio="";

    //mute video
    //private GPUMp4Composer GPUMp4Composer;
    private com.daasuu.gpuv.composer.GPUMp4Composer GPUMp4Composer;
    ProgressDialog progressDialog;
    private String muteVideofilepath;

    //video recording

    private boolean mIsRecordingVideo;

    private static final String TAG = "Camera2VideoFragment";
    private static final int REQUEST_VIDEO_PERMISSIONS = 1;
    private static final String FRAGMENT_DIALOG = "dialog";

    private Integer mSensorOrientation;
    private String mNextVideoAbsolutePath;
    private CaptureRequest.Builder mPreviewBuilder;
    private Size mPreviewSize;
    private Size mVideoSize;
    private MediaRecorder mMediaRecorder;
    private HandlerThread mBackgroundThread;
    private Handler mBackgroundHandler;
    private Semaphore mCameraOpenCloseLock = new Semaphore(1);

    private static final int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90;
    private static final int SENSOR_ORIENTATION_INVERSE_DEGREES = 270;
    private static final SparseIntArray DEFAULT_ORIENTATIONS = new SparseIntArray();
    private static final SparseIntArray INVERSE_ORIENTATIONS = new SparseIntArray();


    static {
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_0, 90);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_90, 0);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_180, 270);
        DEFAULT_ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    static {
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_0, 270);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_90, 180);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_180, 90);
        INVERSE_ORIENTATIONS.append(Surface.ROTATION_270, 0);
    }

    private static final String[] VIDEO_PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };


    // private AutoFitTextureView mTextureView;
    private AutoFitTextureView mTextureView;
    private CameraDevice mCameraDevice;
    private CameraCaptureSession mPreviewSession;

    public static final String CAMERA_FRONT = "1";
    public static final String CAMERA_BACK = "0";

    private String cameraId="1";

    String musicPath="";
    String mVideoSpeed="";

    private static final int PERMISSION_REQUEST_CODE = 200;
    String settingFlag="";

    String SpeedVideofilePath;
    private String mergeAudioVideoPath;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_upload_video);
        initData();
        onCreateActivity();
       /* if (!hasPermissionsGranted(VIDEO_PERMISSIONS)) {
            requestVideoPermissions();
            return;
        }*/
        if (!checkPermission()) {
            requestPermission();
        }

    }

    private void initData() {
        llFlip=findViewById(R.id.llFlip);
        llFlip.setOnClickListener(this);

        llMusic=findViewById(R.id.llMusic);
        llMusic.setOnClickListener(this);

        rlTimer=findViewById(R.id.rlTimer);
        rlTimer.setOnClickListener(this);

        imgStartVideo = findViewById(R.id.imgStartVideo);
        imgStartVideo.setOnClickListener(this);

        imgGallery = findViewById(R.id.imggallery);
        imgGallery.setOnClickListener(this);

       // imgEffect = findViewById(R.id.imgEffect);
       // imgEffect.setOnClickListener(this);

        llVideoMenu=findViewById(R.id.llVideoMenu);

        tvTimer=findViewById(R.id.tvTimer);
        tv_song_name=findViewById(R.id.tv_song_name);

        imgNext=findViewById(R.id.imgNext);
        imgNext.setOnClickListener(this);

        imgback=findViewById(R.id.img_back1);
        imgback.setOnClickListener(this);

        img_back=findViewById(R.id.img_back);
        img_back.setOnClickListener(this);

        rlSpeed=findViewById(R.id.rlSpeed);
        rlSpeed.setOnClickListener(this);

        imgMusic1=findViewById(R.id.imgMusic1);
        imgMusic1.setOnClickListener(this);

        rcVideoSpeed=findViewById(R.id.rcVideoSpeed);

        //video
        mTextureView = (AutoFitTextureView) findViewById(R.id.texture);

        setSpeedRecyclerView();

        if (getIntent().hasExtra("musicPath")) {
            musicPath = getIntent().getStringExtra("musicPath");
            tv_song_name.setText(musicPath);
            tv_song_name.setVisibility(View.VISIBLE);
        }

        //loadFFMpegBinary();
    }

    private void setSpeedRecyclerView() {
        //set recycler view
        ArrayList<VideoSpeedModel> videoSpeedArray=new ArrayList<>();
        videoSpeedArray.add(new VideoSpeedModel("Slow","0"));
        videoSpeedArray.add(new VideoSpeedModel("Normal","1"));
        videoSpeedArray.add(new VideoSpeedModel("Fast","0"));
        LinearLayoutManager layoutManager= new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rcVideoSpeed.setLayoutManager(layoutManager);
        VideoSpeedAdapter videoSpeedAdapter = new VideoSpeedAdapter(UploadVideoActivity.this,videoSpeedArray);
        rcVideoSpeed.setAdapter(videoSpeedAdapter);
    }

    private void startTimer(String startMusicFlag){
        tvTimer.setVisibility(View.VISIBLE);
        new CountDownTimer(5000,1000) {
            int counter=5;
            @Override
            public void onTick(long millisUntilFinished) {
                playAlertSound(R.raw.beep);
                tvTimer.setText(String.valueOf(counter));
                counter--;
            }
            @Override
            public void onFinish() {
                tvTimer.setVisibility(View.GONE);
                startRecordingVideo();
                if(startMusicFlag.equalsIgnoreCase("Yes"))
                {
                   //start music
                    if(isMyServiceRunning(BackgroundSoundService.class))
                    {
                        //stop previous running service
                        Intent myService = new Intent(UploadVideoActivity.this, BackgroundSoundService.class);
                        stopService(myService);
                        tv_song_name.setText("");
                        tv_song_name.setVisibility(View.GONE);
                        //start new service
                        Intent serviceIntent = new Intent(UploadVideoActivity.this, BackgroundSoundService.class);
                        serviceIntent.putExtra("MusicPath",musicPath);
                        startService(serviceIntent);
                        tv_song_name.setText(musicPath);
                        tv_song_name.setVisibility(View.VISIBLE);
                    }else {
                        //start new service
                        Intent serviceIntent = new Intent(UploadVideoActivity.this, BackgroundSoundService.class);
                        serviceIntent.putExtra("MusicPath",musicPath);
                        startService(serviceIntent);
                        tv_song_name.setText(musicPath);
                        tv_song_name.setVisibility(View.VISIBLE);
                    }
                }
            }
        }.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgStartVideo:

                if (mIsRecordingVideo) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        mMediaRecorder.pause();
//                    }else
                    stopRecordingVideo();

                        //stopService();

                } else {
                    if(musicPath.isEmpty()){
                        startRecordingVideo();
                    }else {
                        startTimer("yes");
                    }
                }
                break;
            case R.id.imggallery:

                startActivity(new Intent(UploadVideoActivity.this,DeviceVideoListActivity.class).putExtra("FromActivity","Recording"));
                break;

            /*case R.id.imgEffect:
               // OpenBottomSheet();
                break;*/

            case R.id.llFlip:
                switchCamera();
                break;
            case R.id.imgMusic1:
                Intent intentMusic = new Intent(UploadVideoActivity.this, MusicActivity.class).putExtra("MuxAudioVideo","No");
                startActivityForResult(intentMusic, 2);
                break;
            case R.id.imgNext:
                stopService();
                if(mVideoSpeed.isEmpty() || mVideoSpeed.equalsIgnoreCase("Normal")){
                    if(musicPath!="")
                    {
                        stopRecordingVideo();
                        startCodec(mNextVideoAbsolutePath);
                    }else {
                        startActivity(new Intent(UploadVideoActivity.this,KEditVideoActivity.class).putExtra("video_uri",mNextVideoAbsolutePath));
                    }
                }else {
                    executeSpeedVideoCommand();
                }
                break;
            case R.id.img_back:
                if(isMyServiceRunning(BackgroundSoundService.class))
                {
                    stopService();
                }
                finish();
                break;
            case R.id.img_back1:
                stopService();
                finish();
                break;
            case R.id.rlTimer:
                startTimer("No");
                break;
            case R.id.rlSpeed:
                if(rcVideoSpeed.getVisibility()==View.VISIBLE)
                {
                    rcVideoSpeed.setVisibility(View.INVISIBLE);
                }else {
                    rcVideoSpeed.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.llMusic:
                Intent intentMusic1 = new Intent(UploadVideoActivity.this, Music_FromApp.class).putExtra("MuxAudioVideo","No");
                startActivityForResult(intentMusic1, 2);
                break;

        }
    }

    private void startCodec(String sourceVideoPath) {
        muteVideofilepath= getVideoFilePath1();
//        System.out.println("source video file path "+sourceVideoPath);
//        System.out.println("mute video file path "+muteVideofilepath);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        try {
            GPUMp4Composer = null;
            GPUMp4Composer = new GPUMp4Composer(sourceVideoPath, muteVideofilepath)
                    .rotation(Rotation.NORMAL)
                    .size(720, 720)
                    .fillMode(FillMode.PRESERVE_ASPECT_CROP)
                    .mute(true)
                    // .videoBitrate((int) (0.25 * 30 * 720 * 720))

                    // .mute(muteCheckBox.isChecked())
                    // .flipHorizontal(flipHorizontalCheckBox.isChecked())
                    // .flipVertical(flipVerticalCheckBox.isChecked())
                    .listener(new GPUMp4Composer.Listener() {
                        @Override
                        public void onProgress(double progress) {
                            Log.d(TAG, "onProgress = " + progress);
                            //  runOnUiThread(() -> progressBar.setProgress((int) (progress * 100)));
                        }

                        @Override
                        public void onCompleted() {
                            Log.d(TAG, "onCompleted()");
                            runOnUiThread(() -> {

                                if (progressDialog != null) {
                                    progressDialog.dismiss();
                                }

                                calculateDuration(muteVideofilepath);
                            });
                        }

                        @Override
                        public void onCanceled() {

                        }

                        @Override
                        public void onFailed(Exception exception) {
                            Log.d(TAG, "onFailed()");
                        }
                    })
                    .start();

        } catch (NumberFormatException e) {
            System.out.println("Number format Exception"+e);
        }

    }

    public  String getVideoFilePath1() {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("videoDir", Context.MODE_PRIVATE);
        File file = new File(directory,System.currentTimeMillis() + ".mp4");
        Uri imgUri = Uri.fromFile(file);
        String fileAbsolutePath = file.getAbsolutePath();
        System.out.println("File absolute Path"+fileAbsolutePath);
        return fileAbsolutePath;
    }

    public void calculateDuration(String finalVideoPath){

        long VideoDuration;
        long audioDuration;

//        progressDialog = new ProgressDialog(this);
//        progressDialog.setTitle(null);
//        progressDialog.setCancelable(false);

        try{
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(finalVideoPath);
            long durationVideo = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            VideoDuration = TimeUnit.MILLISECONDS.toSeconds(durationVideo);
            Log.d("VideoDuration", String.valueOf(VideoDuration));

            retriever.release();

            MediaMetadataRetriever retriever1 = new MediaMetadataRetriever();
            retriever1.setDataSource(musicPath);
            long durationMusic = Long.parseLong(retriever1.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            audioDuration = TimeUnit.MILLISECONDS.toSeconds(durationMusic);
            Log.d("AudioMusic", String.valueOf(audioDuration));

            retriever1.release();

            long outputDuration = Math.max(VideoDuration, audioDuration);

            String whichDurationGreater;
            if(audioDuration>VideoDuration)
            {
                outputDuration=VideoDuration;
                whichDurationGreater="AudioLengthGreater";
            }else if(VideoDuration>audioDuration){
                whichDurationGreater="VideoLengthGreater";
            }else {
                whichDurationGreater="equal";
            }
            //call merge audio and video
            MergeAudioVideo(whichDurationGreater,outputDuration);
            System.out.println("Output Duration"+String.valueOf(outputDuration));

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void MergeAudioVideo(String whichFileDurationGreater,long outputDuration) {
        Uri uri=Uri.parse(muteVideofilepath);
        File Videofile = new File(String.valueOf(uri));

        Uri MusicUri=Uri.parse(musicPath);
        File fileurimusicPath = new File(String.valueOf(MusicUri));

        mergeAudioVideoPath = getMergeFilePath().getAbsolutePath();

        if(whichFileDurationGreater.equalsIgnoreCase("AudioLengthGreater"))
        {
            //if audio length is max than video .final output is same as video (Woking as per requirement)

            String[] complexcommand = new String[]{"-y","-i", Videofile.getAbsolutePath(),"-i", String.valueOf(fileurimusicPath),"-map","1:a","-map","0:v","-codec","copy","-t",String.valueOf(outputDuration),mergeAudioVideoPath};
            execFFmpegMergeAudioVideo(complexcommand);

        }else if(whichFileDurationGreater.equalsIgnoreCase("VideoLengthGreater")){
            String[] complexcommand={ "-i" , String.valueOf(fileurimusicPath), "-i" ,Videofile.getAbsolutePath(), "-y", "-acodec", "copy", "-vcodec" ,"copy", mergeAudioVideoPath };
            execFFmpegMergeAudioVideo(complexcommand);
        }else {
            String[] complexcommand={ "-i" , String.valueOf(fileurimusicPath), "-i" ,Videofile.getAbsolutePath(), "-y", "-acodec", "copy", "-vcodec" ,"copy", mergeAudioVideoPath };
            execFFmpegMergeAudioVideo(complexcommand);
        }
    }

    private void execFFmpegMergeAudioVideo(final String[] command) {

        Config.enableLogCallback(new LogCallback() {
            @Override
            public void apply(LogMessage message) {
                Log.e(Config.TAG, message.getText());
            }
        });
        Config.enableStatisticsCallback(new StatisticsCallback() {
            @Override
            public void apply(Statistics newStatistics) {
                Log.e(Config.TAG, String.format("frame: %d, time: %d", newStatistics.getVideoFrameNumber(), newStatistics.getTime()));
                Log.d(TAG, "Started command : ffmpeg " + Arrays.toString(command));
            }
        });
        Log.d(TAG, "Started command : ffmpeg " + Arrays.toString(command));
//        progressDialog.setMessage("Processing...");
//        progressDialog.show();

        long executionId = com.arthenica.mobileffmpeg.FFmpeg.executeAsync(command, (executionId1, returnCode) -> {
            if (returnCode == Config.RETURN_CODE_SUCCESS) {

                Log.d(TAG, "Finished command : ffmpeg " + Arrays.toString(command));
//                if (progressDialog != null)
//                    progressDialog.dismiss();

                stopService();

                if(isMyServiceRunning(BackgroundSoundService.class))
                {
                    Intent myService = new Intent(UploadVideoActivity.this, BackgroundSoundService.class);
                    getApplicationContext().stopService(myService);
                }

                startActivity(new Intent(UploadVideoActivity.this,KEditVideoActivity.class).putExtra("video_uri",mergeAudioVideoPath));

//                Intent intent=new Intent();
//                intent.putExtra("musicPath",musicPath);
//                intent.putExtra("mergeAudioVideoPath",mergeAudioVideoPath);
//                setResult(2,intent);
//                finish();//finishing activity

            } else if (returnCode == Config.RETURN_CODE_CANCEL) {

                Log.e(TAG, "Async command execution cancelled by user.");
//                if (progressDialog != null)
//                    progressDialog.dismiss();
            } else {
                Log.e(TAG, String.format("Async command execution failed with returnCode=%d.", returnCode));
//                if (progressDialog != null)
//                    progressDialog.dismiss();
            }
        });
        Log.e(TAG, "execFFmpegMergeVideo executionId-" + executionId);
    }

    public File getMergeFilePath() {
        return new File(this.getFilesDir(), "merge-audio-video_1.mp4");
    }

    @Override
    public void selectVideoSpeed(String speed) {
         mVideoSpeed=speed;
       // loadFFMpegBinary(speed);
    }
    public void playAlertSound(int sound) {

        MediaPlayer mp = MediaPlayer.create(getBaseContext(), sound);
        mp.start();
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mTitles = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return mFragments.get(i);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mTitles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }

    }

    private boolean shouldShowRequestPermissionRationale(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Requests permissions needed for recording video.
     */
   /* private void requestVideoPermissions() {
        if (shouldShowRequestPermissionRationale(VIDEO_PERMISSIONS)) {
            new ConfirmationDialog().show(this.getSupportFragmentManager(), FRAGMENT_DIALOG);
        } else {
            ActivityCompat.requestPermissions(this, VIDEO_PERMISSIONS, REQUEST_VIDEO_PERMISSIONS);
        }
    }*/

   /* @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult");
        if (requestCode == REQUEST_VIDEO_PERMISSIONS) {
            if (grantResults.length == VIDEO_PERMISSIONS.length) {
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                        ErrorDialog.newInstance(getString(R.string.permission_request))
                                .show(this.getSupportFragmentManager(), FRAGMENT_DIALOG);
                        Toast.makeText(UploadVideoActivity.this, getString(R.string.permission_request), Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
            } else {
                ErrorDialog.newInstance(getString(R.string.permission_request))
                        .show(this.getSupportFragmentManager(), FRAGMENT_DIALOG);
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }*/

   /* private boolean hasPermissionsGranted(String[] permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(UploadVideoActivity.this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }*/


    public static class ErrorDialog extends DialogFragment {

        private static final String ARG_MESSAGE = "message";

        public static ErrorDialog newInstance(String message) {
            ErrorDialog dialog = new ErrorDialog();
            Bundle args = new Bundle();
            args.putString(ARG_MESSAGE, message);
            dialog.setArguments(args);
            return dialog;
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Activity activity = getActivity();
            return new AlertDialog.Builder(activity)
                    .setMessage(getArguments().getString(ARG_MESSAGE))
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity.finish();
                        }
                    })
                    .create();
        }

    }

    public  class ConfirmationDialog extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
           // final Fragment parent = getParentFragment();
            return new AlertDialog.Builder(getActivity())
                    .setMessage(R.string.permission_request)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(UploadVideoActivity.this, VIDEO_PERMISSIONS,
                                    REQUEST_VIDEO_PERMISSIONS);
                        }
                    })
                    .setNegativeButton(android.R.string.cancel,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                    .create();
        }

    }

    private void stopService(){
        if( isMyServiceRunning(BackgroundSoundService.class))
        {
            //stop previous running service
            Intent myService = new Intent(UploadVideoActivity.this, BackgroundSoundService.class);
            stopService(myService);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }


    //effect
    protected void onCreateActivity() {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    public static void exportMp4ToGallery(Context context, String filePath) {
        final ContentValues values = new ContentValues(2);
        values.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        values.put(MediaStore.Video.Media.DATA, filePath);
        context.getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                values);
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse("file://" + filePath)));
    }

    public static String getVideoFilePath() {
        return getAndroidMoviesFolder().getAbsolutePath() + "/" + new SimpleDateFormat("yyyyMM_dd-HHmmss").format(new Date()) + "Recording.mp4";
    }

    public static File getAndroidMoviesFolder() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
    }

    public static File getAndroidImageFolder() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    }

    //video

    private TextureView.SurfaceTextureListener mSurfaceTextureListener
            = new TextureView.SurfaceTextureListener() {

        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture,
                                              int width, int height) {
            openCamera(width, height);
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture,
                                                int width, int height) {
            configureTransform(width, height);
        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return true;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        }

    };

    private CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(@NonNull CameraDevice cameraDevice) {
            mCameraDevice = cameraDevice;
            startPreview();
            mCameraOpenCloseLock.release();
            if (null != mTextureView) {
                configureTransform(mTextureView.getWidth(), mTextureView.getHeight());
            }
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice cameraDevice) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice cameraDevice, int error) {
            mCameraOpenCloseLock.release();
            cameraDevice.close();
            mCameraDevice = null;
            finish();
        }
    };


    private static Size chooseVideoSize(Size[] choices) {
        for (Size size : choices) {
            if (size.getWidth() == size.getHeight() * 4 / 3 && size.getWidth() <= 1080) {
                return size;
            }
        }
        Log.e(TAG, "Couldn't find any suitable video size");
        return choices[choices.length - 1];
    }


    private static Size chooseOptimalSize(Size[] choices, int width, int height, Size aspectRatio) {
        // Collect the supported resolutions that are at least as big as the preview Surface
        List<Size> bigEnough = new ArrayList<>();
        int w = aspectRatio.getWidth();
        int h = aspectRatio.getHeight();
        for (Size option : choices) {
            if (option.getHeight() == option.getWidth() * h / w &&
                    option.getWidth() >= width && option.getHeight() >= height) {
                bigEnough.add(option);
            }
        }

        // Pick the smallest of those, assuming we found any
        if (bigEnough.size() > 0) {
            return Collections.min(bigEnough, new CompareSizesByArea());
        } else {
            Log.e(TAG, "Couldn't find any suitable preview size");
            return choices[0];
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("called","onResumecalled");
        if (settingFlag.equals("Settings")){
            settingFlag="";
            if (!checkPermission()) {
                requestPermission();
            }else {
                Log.d("SetUpUi","onResume");
                setUpCamera();
            }
        } else {
            if(checkPermission()){
                setUpCamera();
            }
        }
    }
    public void setUpCamera(){
        startBackgroundThread();
        if (mTextureView.isAvailable()) {
            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    @Override
    public void onPause() {
        if (checkPermission()) {
            closeCamera();
            stopBackgroundThread();
        }
        super.onPause();
    }


    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("CameraBackground");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    /**
     * Stops the background thread and its {@link Handler}.
     */
    private void stopBackgroundThread() {
        mBackgroundThread.quitSafely();
        try {
            mBackgroundThread.join();
            mBackgroundThread = null;
            mBackgroundHandler = null;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Tries to open a {@link CameraDevice}. The result is listened by `mStateCallback`.
     */
    @SuppressWarnings("MissingPermission")
    private void openCamera(int width, int height) {
        /*if (!hasPermissionsGranted(VIDEO_PERMISSIONS)) {
            requestVideoPermissions();
            return;
        }*/

        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            Log.d(TAG, "tryAcquire");
            if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
                throw new RuntimeException("Time out waiting to lock camera opening.");
            }
            // String cameraId = manager.getCameraIdList()[0];

            // cameraId = manager.getCameraIdList()[1];
            Log.d("cameraId",cameraId);

            // Choose the sizes for camera preview and video recording
            CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
            StreamConfigurationMap map = characteristics
                    .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            if (map == null) {
                throw new RuntimeException("Cannot get available preview/video sizes");
            }
            mVideoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
            mPreviewSize = chooseOptimalSize(map.getOutputSizes(SurfaceTexture.class),
                    width, height, mVideoSize);

            int orientation = getResources().getConfiguration().orientation;
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mTextureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight()/2);
            } else {
                mTextureView.setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
            }
            configureTransform(width, height);
            mMediaRecorder = new MediaRecorder();
            manager.openCamera(cameraId, mStateCallback, null);
        } catch (CameraAccessException e) {
            Toast.makeText(UploadVideoActivity.this, "Cannot access the camera.", Toast.LENGTH_SHORT).show();
            finish();
        } catch (NullPointerException e) {
            // Currently an NPE is thrown when the Camera2API is used but not supported on the
            // device this code runs.
            Toast.makeText(this, getString(R.string.camera_error), Toast.LENGTH_SHORT).show();
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera opening.");
        }
    }

    private void closeCamera() {
        try {
            mCameraOpenCloseLock.acquire();
            closePreviewSession();
            if (null != mCameraDevice) {
                mCameraDevice.close();
                mCameraDevice = null;
            }
            if (null != mMediaRecorder) {
                mMediaRecorder.release();
                mMediaRecorder = null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while trying to lock camera closing.");
        } finally {
            mCameraOpenCloseLock.release();
        }
    }

    /**
     * Start the camera preview.
     */
    private void startPreview() {
        if (null == mCameraDevice || !mTextureView.isAvailable() || null == mPreviewSize) {
            return;
        }
        try {
            closePreviewSession();
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            Surface previewSurface = new Surface(texture);
            mPreviewBuilder.addTarget(previewSurface);

            mCameraDevice.createCaptureSession(Collections.singletonList(previewSurface),
                    new CameraCaptureSession.StateCallback() {

                        @Override
                        public void onConfigured(@NonNull CameraCaptureSession session) {
                            mPreviewSession = session;
                            updatePreview();
                        }

                        @Override
                        public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                            Toast.makeText(UploadVideoActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Update the camera preview. {@link #startPreview()} needs to be called in advance.
     */
    private void updatePreview() {
        if (null == mCameraDevice) {
            return;
        }
        try {
            setUpCaptureRequestBuilder(mPreviewBuilder);
            HandlerThread thread = new HandlerThread("CameraPreview");
            thread.start();
            mPreviewSession.setRepeatingRequest(mPreviewBuilder.build(), null, mBackgroundHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setUpCaptureRequestBuilder(CaptureRequest.Builder builder) {
        builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
    }

    private void configureTransform(int viewWidth, int viewHeight) {
        if (null == mTextureView || null == mPreviewSize) {
            return;
        }
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mPreviewSize.getHeight(),
                    (float) viewWidth / mPreviewSize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        }
        mTextureView.setTransform(matrix);
    }

    private void setUpMediaRecorder() throws IOException {
        // final Activity activity = getActivity();
       /* if (null == activity) {
            return;
        }*/


        //previous

        if(musicPath.isEmpty()){
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            Log.d("musicPathSelected","No");
        }else {
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.UNPROCESSED);
            Log.d("musicPathSelected","Yes");
        }

        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        if (mNextVideoAbsolutePath == null || mNextVideoAbsolutePath.isEmpty()) {
            mNextVideoAbsolutePath = getVideoFilePath(UploadVideoActivity.this);
        }
        mMediaRecorder.setOutputFile(mNextVideoAbsolutePath);
        mMediaRecorder.setVideoEncodingBitRate(10000000);
        mMediaRecorder.setVideoFrameRate(30);

        mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);

        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);

        //restrict recording duration for 1 min
        mMediaRecorder.setMaxDuration(60000);

        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        switch (mSensorOrientation) {
            case SENSOR_ORIENTATION_DEFAULT_DEGREES:
                mMediaRecorder.setOrientationHint(DEFAULT_ORIENTATIONS.get(rotation));
                break;
            case SENSOR_ORIENTATION_INVERSE_DEGREES:
                mMediaRecorder.setOrientationHint(INVERSE_ORIENTATIONS.get(rotation));
                break;
        }
        mMediaRecorder.prepare();
    }

    private String getVideoFilePath(Context context) {
        final File dir = context.getExternalFilesDir(null);
        return (dir == null ? "" : (dir.getAbsolutePath() + "/"))
                + System.currentTimeMillis() + ".mp4";
    }

    private void startRecordingVideo() {
        //once video start invisible other layout and visible next button

        rcVideoSpeed.setVisibility(View.INVISIBLE);
        llVideoMenu.setVisibility(View.GONE);
        imgGallery.setVisibility(View.INVISIBLE);
        imgMusic1.setVisibility(View.INVISIBLE);
        imgNext.setVisibility(View.VISIBLE);
        imgback.setVisibility(View.VISIBLE);

        imgStartVideo.setImageResource(R.drawable.ic_video_pause);
       // imgEffect.setVisibility(View.INVISIBLE);

        if (null == mCameraDevice || !mTextureView.isAvailable() || null == mPreviewSize) {
            return;
        }
        try {
            closePreviewSession();
            setUpMediaRecorder();
            SurfaceTexture texture = mTextureView.getSurfaceTexture();
            assert texture != null;
            texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
            mPreviewBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
            List<Surface> surfaces = new ArrayList<>();

            // Set up Surface for the camera preview
            Surface previewSurface = new Surface(texture);
            surfaces.add(previewSurface);
            mPreviewBuilder.addTarget(previewSurface);

            // Set up Surface for the MediaRecorder
            Surface recorderSurface = mMediaRecorder.getSurface();
            surfaces.add(recorderSurface);
            mPreviewBuilder.addTarget(recorderSurface);

            // Start a capture session
            // Once the session starts, we can update the UI and start recording
            mCameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                    mPreviewSession = cameraCaptureSession;
                    updatePreview();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // UI
                            // mButtonVideo.setText("Stop");
                            imgStartVideo.setImageResource(R.drawable.ic_video_pause);
                            mIsRecordingVideo = true;

                            // Start recording
                            mMediaRecorder.start();

                        }
                    });
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {

                    Toast.makeText(UploadVideoActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
            }, mBackgroundHandler);
        } catch (CameraAccessException | IOException e) {
            e.printStackTrace();
        }
    }

    private void closePreviewSession() {
        if (mPreviewSession != null) {
            mPreviewSession.close();
            mPreviewSession = null;
        }
    }

    private void stopRecordingVideo() {
        llVideoMenu.setVisibility(View.VISIBLE);
        //imgEffect.setVisibility(View.VISIBLE);

        imgGallery.setVisibility(View.INVISIBLE);
        imgMusic1.setVisibility(View.INVISIBLE);
        imgNext.setVisibility(View.VISIBLE);
        imgback.setVisibility(View.VISIBLE);

        // UI
        mIsRecordingVideo = false;
        // mButtonVideo.setText("Record");
        imgStartVideo.setImageResource(R.drawable.ic_video_recording);
        // Stop recording
        try{
            mMediaRecorder.stop();
        }catch (Exception e){
            e.printStackTrace();
        }

        mMediaRecorder.reset();

        Activity activity =this;
        if (null != activity) {
            Toast.makeText(activity, "Video saved: " + mNextVideoAbsolutePath,
                    Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Video saved: " + mNextVideoAbsolutePath);
        }
        //uncomment this when want to remove previous path
        //mNextVideoAbsolutePath = null;
        startPreview();
    }

    /**
     * Compares two {@code Size}s based on their areas.
     */
    static class CompareSizesByArea implements Comparator<Size> {

        @Override
        public int compare(Size lhs, Size rhs) {
            // We cast here to ensure the multiplications won't overflow
            return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
                    (long) rhs.getWidth() * rhs.getHeight());
        }

    }


    public void switchCamera() {
        if (cameraId.equals(CAMERA_FRONT)) {
            cameraId = CAMERA_BACK;
            closeCamera();
            reopenCamera();

        } else if (cameraId.equals(CAMERA_BACK)) {
            cameraId = CAMERA_FRONT;
            closeCamera();
            reopenCamera();
        }
    }

    public void reopenCamera() {
        if (mTextureView.isAvailable()) {
            openCamera(mTextureView.getWidth(), mTextureView.getHeight());
        } else {
            mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 2) {
                 musicPath = data.getStringExtra("musicPath");

                if (musicPath == null) {
                    return;
                }else
                {
                    tv_song_name.setText(musicPath);
                    tv_song_name.setVisibility(View.VISIBLE);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private boolean checkPermission() {
        //int result13 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA);

        int result4 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO);


        return result == PackageManager.PERMISSION_GRANTED && result4 == PackageManager.PERMISSION_GRANTED &&
                result1 == PackageManager.PERMISSION_GRANTED ;

    }

    private void requestPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            ActivityCompat.requestPermissions(UploadVideoActivity.this, new String[]{

                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.CAMERA,
                            Manifest.permission.RECORD_AUDIO
                    },
                    PERMISSION_REQUEST_CODE);
        } /*else {

            Log.d("SetUpUi","requestPermission");
            setUpUi();

        }*/

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean flag = true;

                    for (int i = 0; i < grantResults.length; i++) {
                        if (!(grantResults[i] == PackageManager.PERMISSION_GRANTED)) {
                            flag = false;
                        }
                    }
                    if (!flag) {
                        openSettingsDialog();

                    } else {
                        // Log.d("SetUpUi","onRequestPermissionsResult");
                    }
                }

                break;
        }
    }

    private void openSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(UploadVideoActivity.this);
        builder.setCancelable(false);
        builder.setTitle("Required Permissions");
        builder.setMessage("We are taking these permission to record and upload video.Grant them in app settings.");
        builder.setPositiveButton("Take Me To SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                settingFlag="Settings";
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                showPermissionInformationMsg();

            }
        });
        builder.show();

    }

    public void showPermissionInformationMsg() {
        String subtitle = "We are taking these permission to record and upload video";
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadVideoActivity.this);
        builder.setMessage(subtitle)
                .setCancelable(false)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!checkPermission()) {
                            requestPermission();
                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void executeSpeedVideoCommand() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(null);
        progressDialog.setCancelable(false);

        Uri uri=Uri.parse(mNextVideoAbsolutePath);
        File file = new File(String.valueOf(uri));

        Log.d(TAG, "startTrim: src: " + file);

        SpeedVideofilePath = getVideoWithSubtitlesFile().getAbsolutePath();

        if(mVideoSpeed.equalsIgnoreCase("fast"))
        {
            String[] complexCommand = {"-y", "-i", file.getAbsolutePath(), "-filter_complex", "[0:v]setpts=0.5*PTS[v];[0:a]atempo=2.0[a]", "-map", "[v]", "-map", "[a]", "-b:v", "2097k", "-r", "60", "-vcodec", "mpeg4", SpeedVideofilePath};
            execFFmpegVideoSpeed(complexCommand);
        }else if(mVideoSpeed.equalsIgnoreCase("Slow")) {
            String[] complexCommand = {"-y", "-i", file.getAbsolutePath(), "-filter_complex", "[0:v]setpts=2.0*PTS[v];[0:a]atempo=0.5[a]", "-map", "[v]", "-map", "[a]", "-b:v", "2097k", "-r", "60", "-vcodec", "mpeg4", SpeedVideofilePath};
            execFFmpegVideoSpeed(complexCommand);
        }

      //  String[] complexcommand = new String[]{"-i",file.getAbsolutePath(),"-i",fileurimusicPath.getAbsolutePath(),"-map","1:a","-map","0:v","-codec","copy","-shortest",SpeedVideofilePath};


    }

    private void execFFmpegVideoSpeed(final String[] command) {

        Config.enableLogCallback(new LogCallback() {
            @Override
            public void apply(LogMessage message) {
                Log.e(Config.TAG, message.getText());
            }
        });
        Config.enableStatisticsCallback(new StatisticsCallback() {
            @Override
            public void apply(Statistics newStatistics) {
                Log.e(Config.TAG, String.format("frame: %d, time: %d", newStatistics.getVideoFrameNumber(), newStatistics.getTime()));
                Log.d(TAG, "Started command : ffmpeg " + Arrays.toString(command));
            }
        });
        Log.d(TAG, "Started command : ffmpeg " + Arrays.toString(command));
        progressDialog.setMessage("Processing...");
        progressDialog.show();

        long executionId = com.arthenica.mobileffmpeg.FFmpeg.executeAsync(command, (executionId1, returnCode) -> {
            if (returnCode == Config.RETURN_CODE_SUCCESS) {
                Log.d(TAG, "Finished command : ffmpeg " + Arrays.toString(command));
                if (progressDialog != null)
                    progressDialog.dismiss();
                startActivity(new Intent(UploadVideoActivity.this,KEditVideoActivity.class).putExtra("video_uri",SpeedVideofilePath));
            } else if (returnCode == Config.RETURN_CODE_CANCEL) {
                Log.e(TAG, "Async command execution cancelled by user.");
                if (progressDialog != null)
                    progressDialog.dismiss();
            } else {
                Log.e(TAG, String.format("Async command execution failed with returnCode=%d.", returnCode));
                if (progressDialog != null)
                    progressDialog.dismiss();
            }
        });
        Log.e(TAG, "execFFmpegMergeVideo executionId-" + executionId);
    }

    public File getVideoWithSubtitlesFile() {
        return new File(this.getFilesDir(), "video-with-speed.mp4");
    }

}
