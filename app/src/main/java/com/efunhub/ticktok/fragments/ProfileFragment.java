package com.efunhub.ticktok.fragments;

import static android.app.Activity.RESULT_OK;
import static com.efunhub.ticktok.retrofit.Constant.SERVER_URL;
import static com.efunhub.ticktok.retrofit.Constant.VIDEO_URL;
import static com.efunhub.ticktok.utility.ConstantVariables.CHANGE_VISIIBLITY;
import static com.efunhub.ticktok.utility.ConstantVariables.DELETE_VIDEO;
import static com.efunhub.ticktok.utility.ConstantVariables.FOLLOW_USER;
import static com.efunhub.ticktok.utility.ConstantVariables.PICK_IMAGE_REQUEST;
import static com.efunhub.ticktok.utility.ConstantVariables.STORAGE_PERMISSION_CODE;
import static com.efunhub.ticktok.utility.ConstantVariables.USER_PROFILE;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.AndroidViewModel;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.efunhub.ticktok.BuildConfig;
import com.efunhub.ticktok.R;
import com.efunhub.ticktok.activity.LoginActivity;
import com.efunhub.ticktok.activity.PlayEditedVideoActivity;
import com.efunhub.ticktok.activity.ProfileActivity;
import com.efunhub.ticktok.activity.Showing_Followers_List;
import com.efunhub.ticktok.adapter.PlayVideoActivity;
import com.efunhub.ticktok.adapter.PostAdapter;
import com.efunhub.ticktok.adapter.PostAdapterNew;
import com.efunhub.ticktok.application.SessionManager;
import com.efunhub.ticktok.interfaces.IResult;
import com.efunhub.ticktok.interfaces.StatusChange_Listener;
import com.efunhub.ticktok.interfaces.delete_Listener;
import com.efunhub.ticktok.interfaces.onItemClick_Listener;
import com.efunhub.ticktok.interfaces.onVideoItemClick_Listener;
import com.efunhub.ticktok.model.ProfileModel;
import com.efunhub.ticktok.model.User_Profile_Model.AllPost;
import com.efunhub.ticktok.model.User_Profile_Model.Post;
import com.efunhub.ticktok.model.User_Profile_Model.PostDatum;
import com.efunhub.ticktok.model.User_Profile_Model.UserProfile;
import com.efunhub.ticktok.retrofit.APICallback;
import com.efunhub.ticktok.retrofit.AlertDialogs;
import com.efunhub.ticktok.retrofit.BaseServiceResponseModel;
import com.efunhub.ticktok.retrofit.PrintUtil;
import com.efunhub.ticktok.services.GetProfileSP;
import com.efunhub.ticktok.services.UpdatePofileWithoutPicSP;
import com.efunhub.ticktok.services.UpdateProfileSP;
import com.efunhub.ticktok.utility.VolleyService;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.github.drjacky.imagepicker.ImagePicker;
import com.github.drjacky.imagepicker.constant.ImageProvider;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileFragment extends Fragment implements View.OnClickListener,
        onVideoItemClick_Listener,
        delete_Listener ,
        StatusChange_Listener {
    View view;
    RecyclerView rvPosts;
    PostAdapterNew videoAdapter;
    Toolbar toolbar;
    Button btnEditProfile, btnLogout;
    BottomSheetDialog bottomSheetDialog;
    private AlertDialogs mAlert;
    private UpdateProfileSP mUpdateProfileSP;
    private UpdatePofileWithoutPicSP mUpdateProfilepicSP;
    String Profile_User = "get_myprofile";
    String Delete_Video = "video-delete";
    String Change_visiblity = "update-video-status";
    EditText edtMobileNo,edtName,edtemail;
    ProgressDialog progressDialog;
    TextView tv_no_result,tv_post,tv_follow,tv_following,tv_name,tv_user_country,tv_points,tv_referal_link;
    ArrayList<UserProfile> userProfileModel_List = new ArrayList<>();
    ArrayList<Post> postList = new ArrayList<>();
    //ArrayList<Post> AllPostList = new ArrayList<>();
    ArrayList<Post> private_postList = new ArrayList<>();
    ArrayList<AllPost> allvideos = new ArrayList<>();
    private VolleyService mVolleyService;
    private IResult mResultCallBack = null;
    String  id;
    ShimmerFrameLayout shimmer_view_container;
    LinearLayout profile_layout;
    Uri filePath;
    CircleImageView imgUser;
    CircleImageView user_profile;
    LinearLayout ll_followers,ll_following;
    String mflagVariableImageEmpty="";
    Uri mImageCaptureUri;
    String profile_img="",district1="",state1="",insta_id1="",facebook1="",youthtube1="",other1="",country1="";
    String img_url= "https://grobiz.app/tiktokadmin/images/user_profiles/";
    RequestBody requestFile;
    public ProfileFragment() {
        // Required empty public constructor
    }
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        initData();
        getUser_Profile();
        setUpToolbar();
        return view;
    }

    private void getUser_Profile() {
        userProfileModel_List.clear();
        shimmer_view_container.startShimmerAnimation();
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallBack, getActivity());

        Map<String, String> params = new HashMap<>();
        params.put("user_id", id);
        System.out.println("params=>" + params.toString());
        mVolleyService.postDataVolleyParameters(USER_PROFILE, SERVER_URL + Profile_User, params);

    }

    private void delete_Video(String videoid,String following_user_id) {
        shimmer_view_container.startShimmerAnimation();
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.show();
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallBack, getActivity());

        Map<String, String> params = new HashMap<>();
        params.put("user_auto_id", following_user_id);
        params.put("video_auto_id", videoid);

        //System.out.println("params=>" + params.toString());
        mVolleyService.postDataVolleyParameters(DELETE_VIDEO, SERVER_URL + Delete_Video, params);

    }

    private void status_change(String videoid,String following_user_id,String visiblity) {
        shimmer_view_container.startShimmerAnimation();
        progressDialog=new ProgressDialog(getActivity());
        progressDialog.show();
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallBack, getActivity());

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

                            if (Integer.parseInt(status) == 0) {
                                shimmer_view_container.stopShimmerAnimation();
                                shimmer_view_container.setVisibility(View.GONE);
                                profile_layout.setVisibility(View.VISIBLE);
                                rvPosts.setVisibility(View.VISIBLE);
                                tv_no_result.setVisibility(View.GONE);
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                            } else if (Integer.parseInt(status) == 1) {
                                shimmer_view_container.stopShimmerAnimation();
                                shimmer_view_container.setVisibility(View.GONE);
                                profile_layout.setVisibility(View.VISIBLE);
                                rvPosts.setVisibility(View.VISIBLE);
                                tv_no_result.setVisibility(View.GONE);
                                String current_order = jsonObject.getString("user_profile");
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                Gson gson = gsonBuilder.create();
                                UserProfile userProfiles = gson.fromJson(current_order, UserProfile.class);
                                userProfileModel_List = new ArrayList<>(Arrays.asList(userProfiles));
                                if (!userProfileModel_List.isEmpty()) {
                                    allvideos=(ArrayList<AllPost>) userProfileModel_List.get(0).getAllPosts();
                                    System.out.println(allvideos.size());
                                    //postList = (ArrayList<PostDatum>) userProfileModel_List.get(0).getPosts();
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

                                    setUpRecycler();
                                    if (userProfileModel_List.get(0).getTotalFollowers() != null) {

                                        tv_follow.setText(String.valueOf(userProfileModel_List.get(0).getTotalFollowers()));

                                    }
                                    if (userProfileModel_List.get(0).getTotalFollowings()!=null) {
                                        tv_following.setText(String.valueOf(userProfileModel_List.get(0).getTotalFollowings()));
                                    }
                                    tv_post.setText(String.valueOf(userProfileModel_List.get(0).getTotalPosts()));
                                    tv_name.setText(userProfileModel_List.get(0).getName());
                                    tv_user_country.setText("@"+userProfileModel_List.get(0).getCountry());
                                    profile_img=userProfileModel_List.get(0).getProfile_img();
                                    if (!(profile_img.isEmpty()) && !(profile_img.equals(img_url)) && !profile_img.equals("null"))
                                    {
                                        Picasso.with(getContext())
                                                .load(img_url+profile_img)
                                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                                //.transform(new CircleTransform())
                                                .into(user_profile);
                                    }
                                    if(userProfileModel_List.get(0).getPoint().equals(""))
                                    {
                                        tv_points.setText(String.valueOf("Points :    0"));
                                    }else {
                                        tv_points.setText("Points :   "+String.valueOf(userProfileModel_List.get(0).getPoint()));
                                    }
                                }
                                else {
                                    rvPosts.setVisibility(View.GONE);
                                    tv_no_result.setVisibility(View.VISIBLE);
                                }
                                // check_Follow_Status();

                            }

                        } catch (JSONException e1) {
                            shimmer_view_container.stopShimmerAnimation();
                            shimmer_view_container.setVisibility(View.GONE);
                            profile_layout.setVisibility(View.VISIBLE);
                            e1.printStackTrace();
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
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

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
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

    private void setUpToolbar() {

        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity)getActivity()).setTitle("Profile");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }

    private void setUpRecycler() {
        if (!postList.isEmpty())
        {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 3, RecyclerView.VERTICAL, false);
            rvPosts.setLayoutManager(gridLayoutManager);
            videoAdapter = new PostAdapterNew(getActivity(), postList, this,this,this);
            rvPosts.setAdapter(videoAdapter);
        }
        else {
            rvPosts.setVisibility(View.GONE);
            tv_no_result.setVisibility(View.VISIBLE);
        }
    }

    private void initData() {
        mUpdateProfileSP=new UpdateProfileSP(getActivity());
        mUpdateProfilepicSP=new UpdatePofileWithoutPicSP(getActivity());
        //mGetProfileSP=new GetProfileSP(getActivity());
        mAlert=AlertDialogs.getInstance();
        id = SessionManager.onGetAutoCustomerId();
        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        profile_layout = view.findViewById(R.id.profile_layout);
        user_profile=view.findViewById(R.id.user_profile);
        btnLogout = view.findViewById(R.id.btnLogout);
        shimmer_view_container = view.findViewById(R.id.shimmer_view_container);
        rvPosts = view.findViewById(R.id.rvPosts);
        tv_no_result = view.findViewById(R.id.tv_no_result);
        tv_name = view.findViewById(R.id.profile_name);
        tv_user_country = view.findViewById(R.id.tv_user_country);
        tv_follow = view.findViewById(R.id.tv_followers);
        tv_following = view.findViewById(R.id.tv_following);
        tv_post = view.findViewById(R.id.tv_posts);
        tv_points = view.findViewById(R.id.tv_points);
        tv_referal_link = view.findViewById(R.id.tv_referal);
        tv_referal_link.setText("htttps://aiifstar:id=7476");
        tv_referal_link.setOnClickListener((View.OnClickListener) this);
        ll_followers = view.findViewById(R.id.layout_followers);
        ll_following = view.findViewById(R.id.layout_following);
        btnEditProfile.setOnClickListener((View.OnClickListener) this);
        btnLogout.setOnClickListener((View.OnClickListener) this);
        ll_followers.setOnClickListener((View.OnClickListener)this);
        ll_following.setOnClickListener((View.OnClickListener)this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEditProfile:
                OpenBottomSheet();
                break;
            case R.id.btnLogout:
                showLogoutAlert();
                break;
            case R.id.layout_followers:
                ShowFollowing_List("followers");
                break;
            case R.id.layout_following:
                ShowFollowing_List("following");
                break;
            case R.id.tv_referal:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT,
                        "Hey check out my profile at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                sendIntent.setType("text/plain");
                getActivity().setTheme(R.style.CustomBottomSheetDialogTheme);
                getActivity().startActivity(sendIntent);
                break;
        }
    }

    private void ShowFollowing_List(String followers) {
        if (followers.equals("following")) {
            Intent i = new Intent(getActivity(), Showing_Followers_List.class);
            i.putExtra("follow_type", followers);
            i.putExtra("following_user_id", id);
            //  i.putParcelableArrayListExtra("follow_list", Following_List);
            startActivity(i);
        } else if (followers.equals("followers")) {
            Intent i = new Intent(getActivity(), Showing_Followers_List.class);
            i.putExtra("follow_type", followers);
            i.putExtra("following_user_id", id);

            //   i.putParcelableArrayListExtra("follow_list", follower_List);
            startActivity(i);

        }
    }

    private void OpenBottomSheet() {
        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setCanceledOnTouchOutside(false);
        View sheetView = getLayoutInflater().inflate(R.layout.edit_profile_bottomsheet, null);
        bottomSheetDialog.setContentView(sheetView);

        ImageView imgClose = sheetView.findViewById(R.id.imgClose);

        TextView tvSaveProfile=sheetView.findViewById(R.id.tvSaveProfile);
        imgUser=sheetView.findViewById(R.id.imgUser_edit);
        edtName=sheetView.findViewById(R.id.edtName);
        edtemail=sheetView.findViewById(R.id.edtEmail);
        edtMobileNo=sheetView.findViewById(R.id.edtMobileNo);

        setProfileData();

        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if( requestpermission())
//                {
//                    showFileChooser(PICK_IMAGE_REQUEST);
//                }
//                else {
//                   requestpermission();
//                }
                ImagePicker.Companion.with(getActivity())
                        .crop()
                        .cropOval()
                        .maxResultSize(512, 512, true)
                        .provider(ImageProvider.BOTH) //Or bothCameraGallery()
                        .createIntentFromDialog((Function1) (new Function1() {
                            public Object invoke(Object var1) {
                                this.invoke((Intent) var1);
                                return Unit.INSTANCE;
                            }

                            public final void invoke(@NotNull Intent it) {
                                Intrinsics.checkNotNullParameter(it, "it");
                                launcher.launch(it);
                            }
                        }));
            }
        });

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        tvSaveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String namePattern = "^[a-zA-Z -]+$";
                if(edtName.length()==0){
                    edtName.setError("Please enter name");
                }else if(!edtName.getText().toString().matches(namePattern)) {
                    edtName.setError("Please enter valid name");
                }else if(edtMobileNo.length()==0){
                    edtMobileNo.setError("Please enter mobile number");
                }else if(edtMobileNo.length()<10){
                    edtMobileNo.setError("Mobile number must be at least 10 digit");
                }else {
                    File sourceFile=null;
                    try {

                        MultipartBody.Part ImageCaptured;
                        if (!"".equals(mflagVariableImageEmpty)) {
                            System.out.println("mflag profile"+mflagVariableImageEmpty);
                            sourceFile = new File(mflagVariableImageEmpty);
                            requestFile = RequestBody.create(MediaType.parse("***/*//*//**//*"), sourceFile);
                        }

                        if(userProfileModel_List.get(0).getDistrict()!=null ){
                         district1=userProfileModel_List.get(0).getDistrict();
                        }if(userProfileModel_List.get(0).getState()!=null){
                         state1=userProfileModel_List.get(0).getState();
                        }if (userProfileModel_List.get(0).getCountry()!=null){
                         country1=userProfileModel_List.get(0).getCountry();
                        }if (userProfileModel_List.get(0).getInsta_id()!=null){
                         insta_id1=userProfileModel_List.get(0).getInsta_id();
                        }if (userProfileModel_List.get(0).getFacebook_id()!=null){
                         facebook1=userProfileModel_List.get(0).getFacebook_id();
                        }if (userProfileModel_List.get(0).getYoutube_id()!=null){
                         youthtube1=userProfileModel_List.get(0).getYoutube_id();
                        }if (userProfileModel_List.get(0).getOther()!=null){
                         other1=userProfileModel_List.get(0).getOther();
                        }
                        RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), SessionManager.onGetAutoCustomerId());
                        System.out.println("user id"+SessionManager.onGetAutoCustomerId());
                        RequestBody mobile_no = RequestBody.create(MediaType.parse("text/plain"), edtMobileNo.getText().toString());
                        RequestBody custname=RequestBody.create(MediaType.parse("text/plain"),edtName.getText().toString());
                        RequestBody email=RequestBody.create(MediaType.parse("text/plain"),edtemail.getText().toString());
                        RequestBody district = RequestBody.create(MediaType.parse("text/plain"), district1);
                        RequestBody state = RequestBody.create(MediaType.parse("text/plain"), state1);
                        RequestBody country = RequestBody.create(MediaType.parse("text/plain"), country1);
                        RequestBody insta_id = RequestBody.create(MediaType.parse("text/plain"), insta_id1);
                        RequestBody facebook = RequestBody.create(MediaType.parse("text/plain"), facebook1);
                        RequestBody youthtube = RequestBody.create(MediaType.parse("text/plain"), youthtube1);
                        RequestBody other = RequestBody.create(MediaType.parse("text/plain"), other1);
                        if (!"".equals(mflagVariableImageEmpty)) {
                            ImageCaptured =
                                    MultipartBody.Part.createFormData("profile_img", sourceFile.getName(), requestFile);
                            UpdateProfileApi(user_id,mobile_no,custname,email,ImageCaptured, district,state, country,insta_id, facebook, youthtube, other);
                        }else {
                            UpdateProfileWithoutPicApi(user_id,mobile_no,custname,email, district,state, country,insta_id, facebook, youthtube, other);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        bottomSheetDialog.show();
    }

    ActivityResultLauncher<Intent> launcher=
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),(ActivityResult result)->{
                if(result.getResultCode()==RESULT_OK){
                    Uri uri=result.getData().getData();
                    imgUser.setImageURI(uri);
                    mImageCaptureUri= Uri.parse(uri.toString());
                    mflagVariableImageEmpty=mImageCaptureUri.getPath();
                    // Use the uri to load the image
                }else if(result.getResultCode()==ImagePicker.RESULT_ERROR){
                    // Use ImagePicker.Companion.getError(result.getData()) to show an error
                }
            });


    private void showFileChooser(int pickImageRequest) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), pickImageRequest);
    }

    //handling the image chooser activity result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PICK_IMAGE_REQUEST) {


                filePath = data.getData();
                try {
                    Bitmap docBitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);

                    String profilepic = getResizedBitmap(docBitmap, 400);
                    //String profilepic = getStringImage(bitmap);
                    Log.v("Image", profilepic);
                    Log.v("profilepic ", profilepic);
                    imgUser.setImageBitmap(docBitmap);
                    if (!profilepic.equals("")) {

                        //uploadDocuments(docBitmap);

                    } else {
                        Toast.makeText(getActivity(), "Image not selected", Toast.LENGTH_SHORT).show();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(image, width, height, true);

        return getStringImage(scaledBitmap);
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 60, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void setProfileData() {
        System.out.println("in set profile");
        if (!(profile_img.isEmpty()) && !(profile_img.equals(img_url)) && !profile_img.equals("null"))
        {
            Picasso.with(getContext())
                    .load(img_url+profile_img)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    //.transform(new CircleTransform())
                    .into(imgUser);
        }

        edtMobileNo.setText(userProfileModel_List.get(0).getMobile());

        if(!userProfileModel_List.get(0).getName().isEmpty()){
            edtName.setText(userProfileModel_List.get(0).getName());
        }

        if(!userProfileModel_List.get(0).getEmail().isEmpty()){
            edtemail.setText(userProfileModel_List.get(0).getEmail());
        }
        SessionManager.onSavePoints(userProfileModel_List.get(0).getPoint());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        // ((MainActivity)getActivity()).clearBackStackInclusive("tag"); // tag (addToBackStack tag) should be the same which was used while transacting the F2 fragment
    }

    public void showLogoutAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

        // Setting Dialog Title
        alertDialog.setTitle("");

        // Setting Dialog Message
        alertDialog.setMessage("Do you want to logout?");

        // On pressing Settings button
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                SessionManager.onSaveLoginDetails("", "", "", "", "","","","","","");

                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
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

    public void showDeleteAlert(String video_id,String user_id) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

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

    private void UpdateProfileApi(RequestBody user_auto_id,RequestBody mobile_number,
                                  RequestBody name,RequestBody Email,MultipartBody.Part profile_pic,
                                  RequestBody district,RequestBody state,
                                  RequestBody country,RequestBody insta_id,RequestBody facebook_id,
                                  RequestBody youthtube_id,RequestBody other
                                  ) {

        mAlert.onShowProgressDialog(getActivity(), true);

        mUpdateProfileSP.CallUpdateProfile(user_auto_id,mobile_number,name,Email,profile_pic,
                district,state,country,insta_id,facebook_id,youthtube_id,other,
                new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                int Status = ((BaseServiceResponseModel) serviceResponse).getStatus();
                try {
                    String message = ((BaseServiceResponseModel) serviceResponse).getMsg();
                    if (Status == 1) {

                        Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                        mAlert.onShowProgressDialog(getActivity(), false);
                        bottomSheetDialog.dismiss();
                        onRefresh();
                    } else {

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    mAlert.onShowProgressDialog(getActivity(), false);
                }
            }

            @Override
            public <T> void onFailure(T apiErrorModel, T extras) {
                try {

                    PrintUtil.showNetworkAvailableToast(getActivity());
                   /* if (apiErrorModel != null) {
                        PrintUtil.showToast(LoginActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMsg());
                    } else {
                        PrintUtil.showNetworkAvailableToast(LoginActivity.this);
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                    PrintUtil.showNetworkAvailableToast(getActivity());
                } finally {
                    mAlert.onShowProgressDialog(getActivity(), false);
                }
            }
        });
    }

    private void UpdateProfileWithoutPicApi(RequestBody user_auto_id,RequestBody mobile_number,
                                  RequestBody name,RequestBody Email,
                                  RequestBody district,RequestBody state,
                                  RequestBody country,RequestBody insta_id,RequestBody facebook_id,
                                  RequestBody youthtube_id,RequestBody other
    ) {

        mAlert.onShowProgressDialog(getActivity(), true);

        mUpdateProfilepicSP.CallUpdateProfileWithoutPic(user_auto_id,mobile_number,name,Email,
                district,state,country,insta_id,facebook_id,youthtube_id,other,
                new APICallback() {
                    @Override
                    public <T> void onSuccess(T serviceResponse) {
                        int Status = ((BaseServiceResponseModel) serviceResponse).getStatus();
                        System.out.println("status"+String.valueOf(Status));
                        try {
                            String message = ((BaseServiceResponseModel) serviceResponse).getMsg();
                            if (Status == 1) {

                                Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                mAlert.onShowProgressDialog(getActivity(), false);
                                bottomSheetDialog.dismiss();
                                onRefresh();
                            } else {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            mAlert.onShowProgressDialog(getActivity(), false);
                        }
                    }

                    @Override
                    public <T> void onFailure(T apiErrorModel, T extras) {
                        try {

                            PrintUtil.showNetworkAvailableToast(getActivity());
                   /* if (apiErrorModel != null) {
                        PrintUtil.showToast(LoginActivity.this, ((BaseServiceResponseModel) apiErrorModel).getMsg());
                    } else {
                        PrintUtil.showNetworkAvailableToast(LoginActivity.this);
                    }*/
                        } catch (Exception e) {
                            e.printStackTrace();
                            PrintUtil.showNetworkAvailableToast(getActivity());
                        } finally {
                            mAlert.onShowProgressDialog(getActivity(), false);
                        }
                    }
                });
    }

    public void onRefresh() {
        getUser_Profile();
    }
    @Override
    public void delete_click(int position) {
        showDeleteAlert(postList.get(position).getId(),postList.get(position).getUserId());
    }

    @Override
    public void VideoItem_ClickListner(int position) {
        Intent i = new Intent(getActivity(), PlayVideoActivity.class);
        i.putParcelableArrayListExtra("video_list", postList);
        i.putExtra("position", String.valueOf(position));
        startActivity(i);
    }

    @Override
    public void private_video_click(int position) {
        showStatusAlert(postList.get(position).getId(),postList.get(position).getUserId(),postList.get(position).getVisibility());
    }

    public void showStatusAlert(String video_id,String user_id,String Visiblity) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());

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
}
