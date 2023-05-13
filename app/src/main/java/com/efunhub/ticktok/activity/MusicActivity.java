package com.efunhub.ticktok.activity;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.LogCallback;
import com.arthenica.mobileffmpeg.LogMessage;
import com.arthenica.mobileffmpeg.Statistics;
import com.arthenica.mobileffmpeg.StatisticsCallback;
import com.daasuu.gpuv.composer.FillMode;
import com.daasuu.gpuv.composer.GPUMp4Composer;
import com.daasuu.gpuv.composer.Rotation;
import com.efunhub.ticktok.R;
import com.efunhub.ticktok.adapter.MusicAdapter;
import com.efunhub.ticktok.backgroundservice.BackgroundSoundService;
import com.efunhub.ticktok.interfaces.onItemClick_Listener;
import com.efunhub.ticktok.model.AudioModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class MusicActivity extends AppCompatActivity implements MusicAdapter.ApplyMusicInterface {

    private String TAG="MusicActivity";
    RecyclerView rcMusic;
    ImageView imgClose;
    String muxAudioVideoFlag;
    String from_activity="";
    private ProgressDialog progressDialog;
    private String muteVideofilepath;
    private String mMusicPath;
    private com.daasuu.gpuv.composer.GPUMp4Composer GPUMp4Composer;
    private String mergeAudioVideoPath;
    private String video_uri;
    ArrayList<AudioModel> musicList;
    MusicAdapter musicAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        setContentView(R.layout.activity_music);
        init();
    }

    private void init() {
        rcMusic=findViewById(R.id.rcMusic);
        imgClose=findViewById(R.id.imgClose);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stop service
                stopService();
                finish();
            }
        });

        if (getIntent().hasExtra("MuxAudioVideo")) {
            muxAudioVideoFlag = getIntent().getStringExtra("MuxAudioVideo");
            video_uri=getIntent().getStringExtra("video_uri");
        }
        if(getIntent().hasExtra("from_activity"))
        {
            from_activity=getIntent().getStringExtra("from_activity");
        }
        musicList=getAllAudioFromDevice(this);
        //set recycler view
        setRecyclerView();

    }

    private void setRecyclerView() {
        LinearLayoutManager layoutManager= new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcMusic.setLayoutManager(layoutManager);
        musicAdapter = new MusicAdapter(MusicActivity.this,  musicList);
        rcMusic.setAdapter(musicAdapter);

    }

    public ArrayList<AudioModel> getAllAudioFromDevice(final Context context) {
        final ArrayList<AudioModel> tempAudioList = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA,MediaStore.Audio.AudioColumns.TITLE ,MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST,};
        // Cursor c = context.getContentResolver().query(uri, projection, MediaStore.Audio.Media.DATA + " like ? ", new String[]{"%utm%"}, null);

        Cursor c = context.getContentResolver().query(uri,
                projection,
                null,
                null,
                null);
        if (c != null) {
            while (c.moveToNext()) {
                // Create a model object.
                AudioModel audioModel = new AudioModel();

                String path = c.getString(0);   // Retrieve path.
                String name = c.getString(1);   // Retrieve name.
                String album = c.getString(2);  // Retrieve album name.
                String artist = c.getString(3); // Retrieve artist name.

                // Set data to the model object.
                audioModel.setaName(name);
                audioModel.setaAlbum(album);
                audioModel.setaArtist(artist);
                audioModel.setaPath(path);

                Log.e("Name :" + name, " Album :" + album);
                Log.e("Path :" + path, " Artist :" + artist);

                // Add the model object to the list .
                tempAudioList.add(audioModel);
            }
            c.close();
        }

        // Return the list.
        return tempAudioList;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //stop service
        stopService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        //stop service
        stopService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //stop service
        stopService();
    }

    private void stopService(){
        if( isMyServiceRunning(BackgroundSoundService.class))
        {
            //stop previous running service
            Intent myService = new Intent(MusicActivity.this, BackgroundSoundService.class);
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

    @Override
    public void selectAndApplyMusic(String musicPath) {
        mMusicPath=musicPath;
        if(from_activity.equals("Audio") ) {
            Intent intent=new Intent(MusicActivity.this,UploadVideoActivity.class);
            intent.putExtra("musicPath",musicPath);
            startActivity(intent);
            finish();
        }else {
            if (muxAudioVideoFlag.equalsIgnoreCase("Yes")) {
                startCodec(video_uri);
            } else {
                Intent intent = new Intent();
                intent.putExtra("musicPath", musicPath);
                setResult(2, intent);
                finish();//finishing activity
            }
        }
    }

    private void startCodec(String sourceVideoPath) {
        muteVideofilepath= getVideoFilePath();
//        System.out.println("source video file path "+sourceVideoPath);
//        System.out.println("mute video file path "+muteVideofilepath);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);

        progressDialog.show();

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
                        // exportMp4ToGallery(getApplicationContext(), muteVideofilepath);
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

    }

    public void calculateDuration(String finalVideoPath){

        long VideoDuration;
        long audioDuration;

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(null);
        progressDialog.setCancelable(false);

        try{
            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(finalVideoPath);
            long durationVideo = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
            VideoDuration = TimeUnit.MILLISECONDS.toSeconds(durationVideo);
            Log.d("VideoDuration", String.valueOf(VideoDuration));

            retriever.release();

            MediaMetadataRetriever retriever1 = new MediaMetadataRetriever();
            retriever1.setDataSource(mMusicPath);
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

    public  String getVideoFilePath() {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getDir("videoDir", Context.MODE_PRIVATE);
        File file = new File(directory,System.currentTimeMillis() + ".mp4");
        Uri imgUri = Uri.fromFile(file);
        String fileAbsolutePath = file.getAbsolutePath();
        System.out.println("File absolute Path"+fileAbsolutePath);
        return fileAbsolutePath;
    }

    private void MergeAudioVideo(String whichFileDurationGreater,long outputDuration) {
        Uri uri=Uri.parse(muteVideofilepath);
        File Videofile = new File(String.valueOf(uri));

        Uri MusicUri=Uri.parse(mMusicPath);
        File fileurimusicPath = new File(String.valueOf(MusicUri));

        mergeAudioVideoPath = getMergeFilePath().getAbsolutePath();

        if(whichFileDurationGreater.equalsIgnoreCase("AudioLengthGreater"))
        {
            //if audio length is max than video .final output is same as video (Woking as per requirenent)

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
               /* if (choice == 8)
                    progressDialog.setMessage("progress : splitting video " + newStatistics.toString());
                else if (choice == 9)
                    progressDialog.setMessage("progress : reversing splitted videos " + newStatistics.toString());
                else if (choice == 10)
                    progressDialog.setMessage("progress : concatenating reversed videos " + newStatistics.toString());
                else
                    progressDialog.setMessage("progress : " + newStatistics.toString());
                Log.d(TAG, "progress : " + newStatistics.toString());*/
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

                stopService();

                if(isMyServiceRunning(BackgroundSoundService.class))
                {
                    Intent myService = new Intent(MusicActivity.this, BackgroundSoundService.class);
                    getApplicationContext().stopService(myService);
                }

                Intent intent=new Intent();
                intent.putExtra("musicPath",mMusicPath);
                intent.putExtra("mergeAudioVideoPath",mergeAudioVideoPath);
                setResult(2,intent);
                finish();//finishing activity

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

    public File getMergeFilePath() {
        return new File(this.getFilesDir(), "merge-audio-video_1.mp4");
    }
}