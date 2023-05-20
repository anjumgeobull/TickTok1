package com.efunhub.ticktok.fragments;

import static com.efunhub.ticktok.retrofit.Constant.SERVER_URL;
import static com.efunhub.ticktok.utility.ConstantVariables.CAMPAIGN_LIST;
import static com.efunhub.ticktok.utility.ConstantVariables.USER_PROFILE;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.efunhub.ticktok.CampaignModel.CampaignModelData;
import com.efunhub.ticktok.R;
import com.efunhub.ticktok.activity.CommentActivity;
import com.efunhub.ticktok.activity.LeadForm;
import com.efunhub.ticktok.activity.MainActivity;
import com.efunhub.ticktok.activity.UserDetailsRegistration;
import com.efunhub.ticktok.adapter.VideoAdapterNew;
import com.efunhub.ticktok.application.SessionManager;

import com.efunhub.ticktok.interfaces.Click_video_interface;
import com.efunhub.ticktok.interfaces.IResult;
import com.efunhub.ticktok.interfaces.Like_video_interface;
import com.efunhub.ticktok.interfaces.ShowCommentListener;
import com.efunhub.ticktok.interfaces.requestcallback_interface;
import com.efunhub.ticktok.model.AllVideoModel;
import com.efunhub.ticktok.model.User_Profile_Model.UserProfile;
import com.efunhub.ticktok.retrofit.APICallback;
import com.efunhub.ticktok.retrofit.AlertDialogs;
import com.efunhub.ticktok.retrofit.PrintUtil;
import com.efunhub.ticktok.services.AllVideoServiceProvider;
import com.efunhub.ticktok.services.Video_LIke_Service_Provider;
import com.efunhub.ticktok.utility.VolleyService;
import com.efunhub.ticktok.utility.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


public class AllVideoFragment extends Fragment implements ShowCommentListener, Like_video_interface , Click_video_interface , requestcallback_interface {

    private AllVideoServiceProvider allVideoServiceProvider;
    private Video_LIke_Service_Provider video_lIke_service_provider;
    private AlertDialogs mAlert;
    RecyclerView rvVideoView;
    View root;
    String user_id;
    String camp_user_id="64390423339405e88706f562";
    String img_url = "https://grobiz.app/tiktokadmin/images/phase_two_images/";
    String img_url1 = "https://grobiz.app/tiktokadmin/images/videos/";
    String camp_status="Active";
    boolean flag;
    VideoAdapterNew videoAdapter;
    String video_like = "video-like";
    String add_counts = "add_counts";

    String request_callback = "request_callback";
    String Profile_User = "get_myprofile";
    String CampaignImg ="",CampName="", CampLink ="";
    String get_campaign_list="get_image_campaign_list";
    private VolleyService mVolleyService;
    private IResult mResultCallBack = null;
    ArrayList<UserProfile> userProfileModel_List = new ArrayList<>();
    TextView campName;
    TextView camplink;
    RelativeLayout relativeLayout;
    ImageView CampImg,crossImg;
    Button Connectus;
    ArrayList<AllVideoModel.Data> videoArrayList=new ArrayList<>();
    ArrayList<CampaignModelData.Data> allCampaignList=new ArrayList<>();
    ArrayList<AllVideoModel.Data> PaginationArrayList;
    static int limit=10;
    int index=0;
    String camImg="";
    String camType="";
    private boolean isLoading = false;
    public AllVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_all_video, container, false);

        init();

    /*    View decorView = getActivity().getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
        decorView.setSystemUiVisibility(uiOptions);*/

        return root;
    }

    private void init() {
        mAlert = AlertDialogs.getInstance();
//        campName=root.findViewById(R.id.camName);
//        camplink=root.findViewById(R.id.link);
        CampImg=root.findViewById(R.id.campainImg);
        crossImg=root.findViewById(R.id.imgCross);
        Connectus=root.findViewById(R.id.connect);
        Connectus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        relativeLayout = root.findViewById(R.id.linearLayoutCamp);
        allVideoServiceProvider = new AllVideoServiceProvider(getActivity());
        video_lIke_service_provider = new Video_LIke_Service_Provider(getActivity());
        rvVideoView = root.findViewById(R.id.rvVideoView);
        user_id = SessionManager.onGetAutoCustomerId();
//        camp_user_id = SessionManager.onGetCampaignData();
//        System.out.println("CAMPAIGNUSERID"+camp_user_id);
        getUser_Profile();
        setGet_campaign_list();
        setAdapter();
        callGetAllVideoApi(SessionManager.onGetAutoCustomerId(),limit,index);
        flag = true;

//        Connectus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(getActivity(), LeadForm.class));
//            }
//        });
        crossImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                relativeLayout.setVisibility(View.GONE);
            }
        });

    }

    private void setAdapter() {

        SnapHelper snapHelper = new PagerSnapHelper();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1, RecyclerView.VERTICAL, false);
        rvVideoView.setLayoutManager(gridLayoutManager);
        videoAdapter = new VideoAdapterNew(getActivity(), videoArrayList, this, this,"for_you",this,this);
        rvVideoView.setAdapter(videoAdapter);
        // Set up the pagination logic
        snapHelper.attachToRecyclerView(rvVideoView);
        rvVideoView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Check if the user has scrolled to the end of the current list
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = layoutManager.getItemCount();
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                boolean endReached = lastVisibleItemPosition + 1 == totalItemCount;

                // If the end has been reached, load more data into the list
                if (endReached && !isLoading) {
                    // Increment the current page and retrieve the next set of items
                    //index=limit;
                    isLoading=true;
                    index=index+limit;
                    callGetAllVideoApi(SessionManager.onGetAutoCustomerId(),limit,index);
                    //List<Item> items = retrieveItems(currentPage, pageSize);

                    // If there are no more items to retrieve, disable pagination
                    if (videoArrayList.isEmpty()) {
                        recyclerView.removeOnScrollListener(this);
                    }
                }
            }
        });
    }


    private void callGetAllVideoApi(String login_user_id,int limit,int index) {
        // mAlert.onShowProgressDialog(getActivity(), true);
        System.out.println("Limit "+String.valueOf(limit)+" index "+String.valueOf(index));
        allVideoServiceProvider.CallGetAllVideo(login_user_id,limit,index ,new APICallback() {
            @Override
            public <T> void onSuccess(T serviceResponse) {
                int Status = ((AllVideoModel) serviceResponse).getStatus();
                try {
                    PaginationArrayList = ((AllVideoModel) serviceResponse).getData();
                    String message = ((AllVideoModel) serviceResponse).getMsg();
                    isLoading = false;
                    //System.out.println("videoArrayList=>" + PaginationArrayList.size());
                    if (Status == 1) {
                        //  mAlert.onShowToastNotification(getActivity(),"Success") ;
                        if (!PaginationArrayList.isEmpty()) {
                            videoArrayList.addAll(PaginationArrayList);
                            //setAdapter();
                            videoAdapter.notifyItemRangeInserted(videoArrayList.size() - PaginationArrayList.size(), PaginationArrayList.size());
                            System.out.println("Video list"+String.valueOf(videoArrayList.size()));
                        }

                    } else {
                        isLoading = false;
                        mAlert.onShowToastNotification(getActivity(), "Please Wait");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    //   mAlert.onShowProgressDialog(getActivity(), false);
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

    private void getUser_Profile() {
        userProfileModel_List.clear();
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallBack, getActivity());

        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_id);
        System.out.println("params=>" + params.toString());
        mVolleyService.postDataVolleyParameters(USER_PROFILE, SERVER_URL + Profile_User, params);

    }
    private void setGet_campaign_list() {
        allCampaignList.clear();
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallBack, getActivity());

//        Map<String, String> params = new HashMap<>();
//        params.put("user_auto_id", camp_user_id);
//        params.put("status", camp_status);
//        System.out.println("params=>" + params.toString());
        mVolleyService.getDataVolley(CAMPAIGN_LIST, SERVER_URL + get_campaign_list);

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
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                            } else
                                if (Integer.parseInt(status) == 1) {
                                    String current_order = jsonObject.getString("user_profile");
                                    GsonBuilder gsonBuilder = new GsonBuilder();
                                    Gson gson = gsonBuilder.create();
                                    UserProfile userProfiles = gson.fromJson(current_order, UserProfile.class);
                                    userProfileModel_List = new ArrayList<>(Arrays.asList(userProfiles));
                                    if (!userProfileModel_List.isEmpty()) {
                                        SessionManager.onSavePoints(userProfileModel_List.get(0).getPoint());
                                    } else {

                                    }
                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        break;
                    case CAMPAIGN_LIST:
                        try {
                            jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            String status = jsonObject.getString("status");

                            if (Integer.parseInt(status) == 0) {
                                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                            }

                            else if (Integer.parseInt(status) == 1) {
                                String data = jsonObject.getString("data");
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                Gson gson = gsonBuilder.create();

                                if (data.startsWith("[")) {
                                    // Handle JSON array
                                    CampaignModelData.Data[] dataList = gson.fromJson(data, CampaignModelData.Data[].class);
                                    allCampaignList = new ArrayList<>(Arrays.asList(dataList));
                                } else {
                                    // Handle JSON object
                                    CampaignModelData campaignModelData = gson.fromJson(data, CampaignModelData.class);
                                    allCampaignList = new ArrayList<>(Arrays.asList(campaignModelData.getDataList().toArray(new CampaignModelData.Data[0])));
                                }

                                System.out.println("Campaign Data");
                                for (CampaignModelData.Data campaign : allCampaignList) {
                                    System.out.println("cam Type"+ campaign.getIsVideo());
                                    camType=campaign.getImages();
//                                    if(camType=="Image"){
//
//                                    }

                                }

                                System.out.println("Campaign image info: " + allCampaignList.get(0).getImages());

                                for (CampaignModelData.Data campaignData : allCampaignList) {
                                    System.out.println("Campaign Link: " + campaignData.getLinks());
                                    System.out.println("Campaign name: " + campaignData.getCampaignName());
                                    System.out.println("Campaign img: " + campaignData.getImages());
                                    System.out.println("Campaign User_auo_id: " + campaignData.getUserAutoId());
                                    SessionManager.onSaveCampaignData(campaignData.getUserAutoId());

//                                   camplink.setText(campaignData.getLinks());
//                                    campName.setText(campaignData.getCampaignName());
                                    camImg=campaignData.getImages();
                                    if (!(camImg.isEmpty()) && !(camImg.equals(img_url)) && !camImg.equals("null"))
                                    {
                                        Picasso.with(getContext())
                                                .load(img_url1+camImg)
                                                .memoryPolicy(MemoryPolicy.NO_CACHE)
                                                .networkPolicy(NetworkPolicy.NO_CACHE)
                                                //.transform(new CircleTransform())
                                                .into(CampImg);
                                    }
                                    CampImg.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            String googleUrl = "https://www.google.com";
                                            String googleUrl2 =  campaignData.getLinks();
                                            System.out.println("Campaign URl: " +googleUrl2);
                                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(googleUrl));
                                            startActivity(intent);
                                        }
                                    });

                                    long duration = 20000; // 5 seconds

                                    Runnable hideImageViewRunnable = new Runnable() {
                                        @Override
                                        public void run() {
                                            CampImg.setVisibility(View.GONE);
                                            // Load new image here

                                        }
                                    };

                                    Handler handler = new Handler();
                                    handler.postDelayed(hideImageViewRunnable, duration);
                                }

                            } else {
                                // Handle other statuses
                                String message = jsonObject.getString("msg");
                                System.out.println("Status: " + status + ", msg: " + message);
                            }


                        } catch (JSONException e1) {
                            e1.printStackTrace();
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

    @Override
    public void showComments(String video_id, int position) {
//        Bundle args = new Bundle();
//        args.putString("video_id", video_id);
//        args.putString("position", String.valueOf(position));
        Intent intent = new Intent(getActivity(), CommentActivity.class);
        intent.putExtra("video_id", video_id);
        intent.putExtra("position", String.valueOf(position));
        intent.putExtra("FromActivity", "AllVideo");
        startActivity(intent);
//        BottomSheetDialogFragment bottomSheetFragment = new CommentBottomsheet();
//
//        bottomSheetFragment.setArguments(args);
//        bottomSheetFragment.show(getFragmentManager(), bottomSheetFragment.getTag());
    }


    @Override
    public void Like_video(String video_id, String like_status, int position) {

        String Urls = SERVER_URL + video_like;

        StringRequest postRequest = new StringRequest(Request.Method.POST, Urls,
                response -> {
                    // response
                    Log.d("Response", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String msg = jsonObject.getString("msg");
                        int status = jsonObject.getInt("status");

                        System.out.println("Like_Video_Data=> status " + status);

                        if (status == 1) {

                            int like = videoArrayList.get(position).getTotal_likes();
                            System.out.println("like=>" + like);
                            AllVideoModel.Data data = videoArrayList.get(position);
                            data.setTotal_likes(like + 1);
                            int point = Integer.parseInt(videoArrayList.get(position).getPoint());
                            int updated = point + 2;
                            String updated_point=Integer.toString(updated);
                            videoArrayList.get(position).setPoint(updated_point);

                            Toast.makeText(getActivity(), "Liked", Toast.LENGTH_SHORT).show();

                        } else if (status == 2) {

                            int like = videoArrayList.get(position).getTotal_likes();
                            System.out.println("like=>" + like);
                            AllVideoModel.Data data = videoArrayList.get(position);
                            data.setTotal_likes(like - 1);
                            int point = Integer.parseInt(videoArrayList.get(position).getPoint());
                            int updated = point - 2;
                            String updated_point=Integer.toString(updated);
                            videoArrayList.get(position).setPoint(updated_point);
                        } else {

                            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_LONG).show();
                    }

                },
                error -> {
                    // error
                    Log.d("Error.Response", String.valueOf(error));
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_id", user_id);
                params.put("video_id", video_id);
                params.put("like", like_status);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(postRequest);
    }


    public void Click_video(int position,String click,String view,String impression) {

        String Urls = SERVER_URL + add_counts;

        StringRequest postRequest = new StringRequest(Request.Method.POST, Urls,
                response -> {
                    // response
                    Log.d("Response", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String msg = jsonObject.getString("msg");
                        int status = jsonObject.getInt("status");

                        System.out.println("Click_Video_Data=> status " + status);

                        if (status == 1) {

//                            int like = videoArrayList.get(position).getTotal_likes();
//                            System.out.println("like=>" + like);
//                            AllVideoModel.Data data = videoArrayList.get(position);
//                            data.setTotal_likes(like + 1);
//                            int point = Integer.parseInt(videoArrayList.get(position).getPoint());
//                            int updated = point + 2;
//                            String updated_point=Integer.toString(updated);
//                            videoArrayList.get(position).setPoint(updated_point);

                            Toast.makeText(getActivity(), "Impression", Toast.LENGTH_SHORT).show();

                        } else if (status == 2) {
                            Toast.makeText(getActivity(), "not impression", Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_LONG).show();
                    }

                },
                error -> {
                    // error
                    Log.d("Error.Response", String.valueOf(error));
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("id", videoArrayList.get(position).get_id());
                params.put("user_auto_id", user_id);
                params.put("campaign_user_auto_id", videoArrayList.get(position).getCampaign_user_auto_id());
                params.put("campaign_auto_id", videoArrayList.get(position).getCampaign_auto_id());
                params.put("click", click);
                params.put("view", view);
                params.put("impresson", impression);
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(postRequest);
    }

    public void Callback_request_video(int position) {

        String Urls = SERVER_URL + request_callback;

        StringRequest postRequest = new StringRequest(Request.Method.POST, Urls,
                response -> {
                    // response
                    Log.d("Response", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String msg = jsonObject.getString("msg");
                        int status = jsonObject.getInt("status");

                        System.out.println("Click_Video_Data=> status " + status);

                        if (status == 1) {

                            Toast.makeText(getActivity(), "We have got your interest,we will call you back", Toast.LENGTH_SHORT).show();

                        } else if (status == 2) {
                            Toast.makeText(getActivity(), "not intrested", Toast.LENGTH_SHORT).show();
                        } else {

                            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                        Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_LONG).show();
                    }

                },
                error -> {
                    // error
                    Log.d("Error.Response", String.valueOf(error));
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_auto_id", user_id);
                params.put("campaign_user_auto_id", videoArrayList.get(position).getCampaign_user_auto_id());
                params.put("campaign_auto_id", videoArrayList.get(position).getCampaign_auto_id());
                params.put("name", userProfileModel_List.get(0).getName());
                params.put("contact", userProfileModel_List.get(0).getMobile());
                return params;
            }
        };
        VolleySingleton.getInstance(getActivity()).addToRequestQueue(postRequest);
    }
}