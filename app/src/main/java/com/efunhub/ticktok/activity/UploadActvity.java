package com.efunhub.ticktok.activity;

import static com.efunhub.ticktok.retrofit.Constant.SERVER_URL;
import static com.efunhub.ticktok.utility.ConstantVariables.CHANGE_VISIIBLITY;
import static com.efunhub.ticktok.utility.ConstantVariables.DELETE_VIDEO;
import static com.efunhub.ticktok.utility.ConstantVariables.USER_PROFILE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.efunhub.ticktok.R;
import com.efunhub.ticktok.adapter.PlayVideoActivity;
import com.efunhub.ticktok.adapter.PostAdapterNew;
import com.efunhub.ticktok.application.SessionManager;
import com.efunhub.ticktok.interfaces.IResult;
import com.efunhub.ticktok.interfaces.StatusChange_Listener;
import com.efunhub.ticktok.interfaces.delete_Listener;
import com.efunhub.ticktok.interfaces.onVideoItemClick_Listener;
import com.efunhub.ticktok.model.UploadVideoModel;
import com.efunhub.ticktok.model.User_Profile_Model.AllPost;
import com.efunhub.ticktok.model.User_Profile_Model.Post;
import com.efunhub.ticktok.model.User_Profile_Model.UserProfile;
import com.efunhub.ticktok.retrofit.APICallback;
import com.efunhub.ticktok.retrofit.AlertDialogs;
import com.efunhub.ticktok.retrofit.BaseServiceResponseModel;
import com.efunhub.ticktok.retrofit.PrintUtil;
import com.efunhub.ticktok.services.UploadVideoSP;
import com.efunhub.ticktok.utility.VolleyService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UploadActvity extends AppCompatActivity implements onVideoItemClick_Listener, delete_Listener,StatusChange_Listener {

    RecyclerView rvPosts;
    PostAdapterNew videoAdapter;
    String Profile_User = "get_myprofile";
    ProgressDialog progressDialog;
    TextView tv_no_result;
    ArrayList<UserProfile> userProfileModel_List = new ArrayList<>();
    ArrayList<Post> postList = new ArrayList<>();
    ArrayList<Post> private_postList = new ArrayList<>();
    ArrayList<AllPost> allvideos = new ArrayList<>();
    private VolleyService mVolleyService;
    private IResult mResultCallBack = null;
    String Delete_Video = "video-delete";
    String Change_visiblity = "update-video-status";
    Button btn_upload;
    LinearLayout upload_layout;
    String  id,videoPath="";
    ImageView videoView;
    UploadVideoSP uploadVideoSP;
    private AlertDialogs mAlert;
    ProgressBar progressBarshare;

    String mflagVariableImageEmpty="";

    Uri mImageCaptureUri;

    int todayscount=0;
    String settingFlag="";
    private static final int PERMISSION_REQUEST_CODE = 200;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_actvity);
        initData();
        getUser_Profile();
        if (!checkPermission()) {
            requestPermission();
        }
    }

    private boolean checkPermission() {
        //int result13 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED  ;

    }

    private void requestPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            ActivityCompat.requestPermissions(UploadActvity.this, new String[]{

                            Manifest.permission.READ_EXTERNAL_STORAGE,

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

        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActvity.this);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(UploadActvity.this);
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

    private void initData() {
        uploadVideoSP=new UploadVideoSP(this);
        mAlert=AlertDialogs.getInstance();
        id = SessionManager.onGetAutoCustomerId();
        rvPosts = findViewById(R.id.rvPosts);
        tv_no_result = findViewById(R.id.tv_no_result);
        videoView =findViewById(R.id.videoView);
        btn_upload = findViewById(R.id.Add_button);
        upload_layout = findViewById(R.id.upload_layout);

        if (getIntent().hasExtra("videoPath")) {
            videoPath = getIntent().getStringExtra("videoPath");
            System.out.println("video path"+videoPath);
            RequestOptions requestOptions = new RequestOptions();
            requestOptions.isMemoryCacheable();
            Glide.with(this).setDefaultRequestOptions(requestOptions).load(videoPath).into(videoView);
            videoView.setVisibility(View.VISIBLE);
            mImageCaptureUri= Uri.parse(videoPath);
            mflagVariableImageEmpty=mImageCaptureUri.getPath();
        }

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadActvity.this,DeviceVideoListActivity.class).putExtra("FromActivity","UploadVideo");
                startActivityForResult(intent, 3);
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(videoPath.isEmpty()) {
                    Intent intent = new Intent(UploadActvity.this, DeviceVideoListActivity.class).putExtra("FromActivity", "UploadVideo");
                    startActivityForResult(intent, 3);
                    finish();
                }else {
                    uploadVideo();
                }
            }
        });
    }

    private void getUser_Profile() {
        userProfileModel_List.clear();
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallBack,this);

        Map<String, String> params = new HashMap<>();
        params.put("user_id", id);
        System.out.println("params=>" + params.toString());
        mVolleyService.postDataVolleyParameters(USER_PROFILE, SERVER_URL + Profile_User, params);
    }

    private void status_change(String videoid,String following_user_id,String visiblity) {

        progressDialog=new ProgressDialog(UploadActvity.this);
        progressDialog.show();
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallBack,UploadActvity.this);

        Map<String, String> params = new HashMap<>();
        params.put("user_id", following_user_id);
        params.put("video_auto_id", videoid);
        params.put("visibility", visiblity);

        //System.out.println("params=>" + params.toString());
        mVolleyService.postDataVolleyParameters(CHANGE_VISIIBLITY, SERVER_URL + Change_visiblity, params);

    }

    private void initVolleyCallback() {
        mResultCallBack = new IResult() {

            @Override
            public void notifySuccess(int requestId, String response) {
                JSONObject jsonObject = null;
                switch (requestId) {
                    case USER_PROFILE:
                        try {
                            jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            String status = jsonObject.getString("status");
                            todayscount=0;
                            if (Integer.parseInt(status) == 0) {
                                rvPosts.setVisibility(View.VISIBLE);
                                tv_no_result.setVisibility(View.GONE);
                                Toast.makeText(UploadActvity.this, msg, Toast.LENGTH_SHORT).show();
                            } else if (Integer.parseInt(status) == 1) {
                                rvPosts.setVisibility(View.VISIBLE);
                                tv_no_result.setVisibility(View.GONE);
                                String current_order = jsonObject.getString("user_profile");
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                Gson gson = gsonBuilder.create();
                                UserProfile userProfiles = gson.fromJson(current_order, UserProfile.class);
                                userProfileModel_List = new ArrayList<>(Arrays.asList(userProfiles));
                                if (!userProfileModel_List.isEmpty()) {
                                    allvideos=(ArrayList<AllPost>) userProfileModel_List.get(0).getAllPosts();
                                    if(Objects.equals(allvideos.get(0).getPostType(), "private") && allvideos.get(0).getPostData().size()!=0)
                                    {
                                        private_postList = (ArrayList<Post>) allvideos.get(0).getPostData();
                                    }
                                    if(allvideos.get(1).getPostType().equals("public") && allvideos.get(1).getPostData().size()!=0)
                                    {
                                        postList = (ArrayList<Post>) allvideos.get(1).getPostData();
                                    }
                                    //AllPostList.addAll(postList);
                                    postList.addAll(private_postList);
                                    for(int i=0;i<postList.size();i++)
                                    {
                                        CheckTodaysCount(postList.get(i).getCreatedAt());
                                    }
                                    if(todayscount>=2)
                                    {
                                        System.out.println("todays count"+String.valueOf(todayscount));
                                        upload_layout.setVisibility(View.GONE);
                                        Toast.makeText(UploadActvity.this,"You have exceeded todays limit",Toast.LENGTH_LONG).show();
                                    }else
                                    {
                                        upload_layout.setVisibility(View.VISIBLE);
                                    }
                                    setUpRecycler();
                                }
                                else {
                                    rvPosts.setVisibility(View.GONE);
                                    tv_no_result.setVisibility(View.VISIBLE);
                                }
                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        break;

                    case DELETE_VIDEO:
                        try {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }

                            jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            String status = jsonObject.getString("status");


                            if (Integer.parseInt(status) == 0) {

                            } else if (Integer.parseInt(status) == 1) {

                                getUser_Profile();

                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                        }
                        break;
                    case CHANGE_VISIIBLITY:
                        try {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }

                            jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            String status = jsonObject.getString("status");


                            if (Integer.parseInt(status) == 0) {

                            } else if (Integer.parseInt(status) == 1) {
                                getUser_Profile();
                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                        }
                        break;
                }
            }

            @Override
            public void notifyError(int requestId, VolleyError error) {

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UploadActvity.this);
                //  builder.setIcon(R.drawable.icon_exit);
                builder.setMessage("Server Error")
                        .setCancelable(true)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                androidx.appcompat.app.AlertDialog alert = builder.create();
                alert.show();
                Log.v("Volley requestid ", String.valueOf(requestId));
                Log.v("Volley Error", String.valueOf(error));
            }
        };
    }

    private void setUpRecycler() {
        if (!postList.isEmpty())
        {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(UploadActvity.this, 3, RecyclerView.VERTICAL, false);
            rvPosts.setLayoutManager(gridLayoutManager);
            videoAdapter = new PostAdapterNew(UploadActvity.this, postList, this,this,this);
            rvPosts.setAdapter(videoAdapter);
        }
        else {
            rvPosts.setVisibility(View.GONE);
            tv_no_result.setVisibility(View.VISIBLE);
        }
    }

    void  CheckTodaysCount(String created_date) throws ParseException {

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // format of the string date
        Date date1 = dateFormat.parse(created_date); // parse the string date into a Date object
        Date date2 = new Date();
//        System.out.println("todays date  "+String.valueOf(date2));// get today's date as a Date object
//        System.out.println("given date  "+String.valueOf(date1));// get today's date as a Date object
        assert date1 != null;
        boolean isSameDate = date1.getYear() == date2.getYear()
                && date1.getMonth() == date2.getMonth()
                && date1.getDate() == date2.getDate(); // compare the year, month, and day fields
        if (isSameDate) {
            //System.out.println("The string date is the same as today's date");
            todayscount = todayscount + 1;
        } else {
            //System.out.println("The string date is not the same as today's date");
        }
    }


    @Override
    public void delete_click(int position) {
        showDeleteAlert(postList.get(position).getId(),postList.get(position).getUserId());
    }

    @Override
    public void VideoItem_ClickListner(int position) {
        Intent i = new Intent(UploadActvity.this, PlayVideoActivity.class);
        i.putParcelableArrayListExtra("video_list", postList);
        i.putExtra("position", String.valueOf(position));
        startActivity(i);
    }

    public void showDeleteAlert(String video_id,String user_id) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UploadActvity.this);

        // Setting Dialog Title
        alertDialog.setTitle("");

        // Setting Dialog Message
        alertDialog.setMessage("Do you want to delete this video?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                delete_Video(video_id,user_id);
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    private void delete_Video(String videoid,String following_user_id) {
        progressDialog=new ProgressDialog(UploadActvity.this);
        progressDialog.show();
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallBack, UploadActvity.this);

        Map<String, String> params = new HashMap<>();
        params.put("user_auto_id", following_user_id);
        params.put("video_auto_id", videoid);

        //System.out.println("params=>" + params.toString());
        mVolleyService.postDataVolleyParameters(DELETE_VIDEO, SERVER_URL + Delete_Video, params);

    }

    @Override
    public void private_video_click(int position) {
        showStatusAlert(postList.get(position).getId(),postList.get(position).getUserId(),postList.get(position).getVisibility());
    }

    public void showStatusAlert(String video_id,String user_id,String Visiblity) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(UploadActvity.this);

        // Setting Dialog Title
        alertDialog.setTitle("");

        // Setting Dialog Message
        alertDialog.setMessage("Do you want to change this video visiblity ?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if(Visiblity.equals("private")) {
                    status_change(video_id, user_id, "public");
                }else
                {
                    status_change(video_id, user_id, "private");
                }
            }
        });

        // on pressing cancel button
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    public void uploadVideo() {
        progressBarshare= new ProgressBar(UploadActvity.this,null,android.R.attr.progressBarStyleLarge);
        progressBarshare.setIndeterminate(true);
        progressBarshare.setVisibility(View.VISIBLE);
        File sourceFile=null;
        try {
            RequestBody requestFile;
            MultipartBody.Part ImageCaptured;
            if (!"".equals(mflagVariableImageEmpty)) {
                sourceFile = new File(mflagVariableImageEmpty);
            }

            RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), SessionManager.onGetAutoCustomerId());
            RequestBody visibility = RequestBody.create(MediaType.parse("text/plain"), "public");
            RequestBody video_caption=RequestBody.create(MediaType.parse("text/plain"),"");

            requestFile = RequestBody.create(MediaType.parse("***/*//*//**//*"), sourceFile);

            ImageCaptured =
                    MultipartBody.Part.createFormData("video", sourceFile.getName(), requestFile);

            callVideoUploadAPI(user_id,visibility,video_caption,ImageCaptured);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void callVideoUploadAPI(RequestBody user_id,RequestBody visibility,RequestBody video_caption, MultipartBody.Part imageCaptured) {

        mAlert.onShowProgressDialog(UploadActvity.this, true);

        uploadVideoSP.callUploadVideo(user_id,visibility,video_caption,imageCaptured,new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                try {
                    int Status = ((UploadVideoModel) serviceResponse).getStatus();

                    String message = ((UploadVideoModel) serviceResponse).getMsg();
                    if (Status == 1) {
                        progressBarshare.setVisibility(View.GONE);
                        mAlert.onShowToastNotification(UploadActvity.this, "Video added successfully");
                        getUser_Profile();
                        videoPath="";
                        //videoView.setVisibility(View.GONE);
//                        Intent i = new Intent(UploadActvity.this, MainActivity.class);
//                        startActivity(i);
                    } else if(Status == 0){
                        mAlert.onShowToastNotification(UploadActvity.this, message);
                    }else
                    {
                        mAlert.onShowToastNotification(UploadActvity.this, message);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mAlert.onShowProgressDialog(UploadActvity.this, false);
                }
            }

            @Override
            public <T> void onFailure(T apiErrorModel, T extras) {
                try {

                    // PrintUtil.showNetworkAvailableToast(UploadActvity.this);
                    if (apiErrorModel != null) {
                        PrintUtil.showToast(UploadActvity.this, ((BaseServiceResponseModel) apiErrorModel).getMsg());
                    } else {
                        PrintUtil.showNetworkAvailableToast(UploadActvity.this);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(UploadActvity.this);
                } finally {
                    mAlert.onShowProgressDialog(UploadActvity.this, false);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(UploadActvity.this,MainActivity.class));
        finish();
    }
}