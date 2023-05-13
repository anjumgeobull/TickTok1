package com.efunhub.ticktok.activity;

import static com.efunhub.ticktok.retrofit.Constant.SERVER_URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.efunhub.ticktok.R;
import com.efunhub.ticktok.adapter.CommentsAdapter;
import com.efunhub.ticktok.application.SessionManager;
import com.efunhub.ticktok.fragments.AllVideoFragment;
import com.efunhub.ticktok.interfaces.Comment_Like_video_interface;
import com.efunhub.ticktok.model.comment_model.User_Comment;
import com.efunhub.ticktok.utility.VolleySingleton;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommentActivity extends AppCompatActivity implements Comment_Like_video_interface {
    RecyclerView rvComments;
    ShimmerFrameLayout mShimmerViewContainer;
    CommentsAdapter commentsAdapter;
    TextView tv_no_comment,tv_total_comments;
    String video_id, position,fromactivity, user_id;
    String todaySDate;
    ProgressBar progressBar;
    EditText et_comment;
    ImageButton btn_comment;
    ArrayList<User_Comment> comment_modelList = new ArrayList<>();
    String VIDEO_COMMENT = "video-comment";
    String VIDEO_COMMENT_LIKE = "comment-like";
    String GET_VIDEO_COMMENT ="get-video-comments";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        initData();
        Load_Comment();
        btn_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_comment.getText().toString().isEmpty())
                {
                    Toast.makeText(CommentActivity.this, "please enter the comment", Toast.LENGTH_SHORT).show();
                }
                else {
                    Upload_Comment();
                }
            }
        });

    }

    private void initData() {
        //Bundle args = getArguments();
        Intent intent=getIntent();
        video_id = intent.getStringExtra("video_id");
        position = intent.getStringExtra("position");
        fromactivity = intent.getStringExtra("FromActivity");
        rvComments = findViewById(R.id.rvComments);
        tv_no_comment = findViewById(R.id.tv_no_comment);
        tv_total_comments = findViewById(R.id.tv_total_comments);
        progressBar = findViewById(R.id.progressbar);
        btn_comment = findViewById(R.id.btn_comment);
        et_comment = findViewById(R.id.et_comment);
        mShimmerViewContainer =findViewById(R.id.shimmer_view_container);
        user_id = SessionManager.onGetAutoCustomerId();
        Date todayDate = Calendar.getInstance().getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        todaySDate = formatter.format(todayDate);
    }

    private void setRecycler() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommentActivity.this, RecyclerView.VERTICAL, false);
        rvComments.setLayoutManager(linearLayoutManager);
        commentsAdapter = new CommentsAdapter(CommentActivity.this, comment_modelList,this);
        rvComments.setAdapter(commentsAdapter);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(fromactivity.equals("AllVideo"))
        startActivity(new Intent(CommentActivity.this, MainActivity.class));
        else if(fromactivity.equals("Profile"))
            finish();
    }

    private void Load_Comment() {
        comment_modelList.clear();
        mShimmerViewContainer.startShimmerAnimation();
        String Urls = SERVER_URL+ GET_VIDEO_COMMENT;

        StringRequest postRequest = new StringRequest(Request.Method.POST, Urls,
                response -> {
                    // response
                    Log.d("Response", response);
                    System.out.println("Like_Video_Data=> responce " + response);
                    //progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String msg = jsonObject.getString("msg");
                        int status = jsonObject.getInt("status");

                        System.out.println("Like_Video_Data=> status " + status);

                        if (status == 1) {
                            mShimmerViewContainer.stopShimmerAnimation();
                            mShimmerViewContainer.setVisibility(View.GONE);

                            //progressDialog.dismiss();
                            String data = jsonObject.getString("data");
                            System.out.println("data=>"+data);
                            if (data.isEmpty()|| data.equals("[]")) {
                                System.out.println("data=>");
                                tv_no_comment.setVisibility(View.VISIBLE);

                            } else {
                                tv_no_comment.setVisibility(View.GONE);
                                rvComments.setVisibility(View.VISIBLE);
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                Gson gson = gsonBuilder.create();
                                User_Comment[] comment_model = gson.fromJson(data, User_Comment[].class);
                                comment_modelList = new ArrayList<>(Arrays.asList(comment_model));
                                tv_total_comments.setText("Total Comments: "+String.valueOf(comment_modelList.size()));
                                tv_total_comments.setVisibility(View.VISIBLE);
                                setRecycler();
                            }

                        } else if (status == 0) {
                            // progressDialog.dismiss();
                            mShimmerViewContainer.stopShimmerAnimation();
                            mShimmerViewContainer.setVisibility(View.GONE);
                            Toast.makeText(CommentActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } else {
                            // progressDialog.dismiss();
                            mShimmerViewContainer.stopShimmerAnimation();
                            mShimmerViewContainer.setVisibility(View.GONE);
                            Toast.makeText(CommentActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        //  progressDialog.dismiss();
                        mShimmerViewContainer.stopShimmerAnimation();
                        mShimmerViewContainer.setVisibility(View.GONE);
                        e.printStackTrace();
                        System.out.println("Server Error" + e);

                        Toast.makeText(CommentActivity.this, "Server Error" + e, Toast.LENGTH_LONG).show();
                    }

                },
                error -> {
                    // progressDialog.dismiss();
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    // error
                    Log.d("Error.Response", String.valueOf(error));
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("login_user_id", user_id);
                params.put("video_id", video_id);
                System.out.println("params"+params.toString());

                return params;
            }
        };
        VolleySingleton.getInstance(CommentActivity.this).addToRequestQueue(postRequest);
    }

    private void Upload_Comment() {

        progressBar.setVisibility(View.VISIBLE);
        String Urls = SERVER_URL+VIDEO_COMMENT;

        StringRequest postRequest = new StringRequest(Request.Method.POST, Urls,
                response -> {
                    // response
                    Log.d("Response", response);
                    System.out.println("Like_Video_Data=> responce " + response);
                    //progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String msg = jsonObject.getString("msg");
                        int status = jsonObject.getInt("status");

                        System.out.println("Like_Video_Data=> status " + status);

                        if (status == 1) {

                            et_comment.setText("");
                            progressBar.setVisibility(View.GONE);
                            Load_Comment();

                        } else if (status == 0) {
                            // progressDialog.dismiss();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(CommentActivity.this, msg, Toast.LENGTH_SHORT).show();
                        } else {
                            // progressDialog.dismiss();
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(CommentActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        //  progressDialog.dismiss();
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                        System.out.println("Server Error" + e);

                        Toast.makeText(CommentActivity.this, "Server Error" + e, Toast.LENGTH_LONG).show();
                    }

                },
                error -> {
                    // progressDialog.dismiss();
                    progressBar.setVisibility(View.GONE);
                    // error
                    Log.d("Error.Response", String.valueOf(error));
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("video_id", video_id);
                params.put("user_id", user_id);
                params.put("comment_text", et_comment.getText().toString());
                params.put("date", todaySDate);

                return params;
            }
        };
        VolleySingleton.getInstance(CommentActivity.this).addToRequestQueue(postRequest);
    }

    @Override
    public void comment_Like_video(String video_id, String like_status, int position,String comment_id) {

        String Urls = SERVER_URL+VIDEO_COMMENT_LIKE;

        StringRequest postRequest = new StringRequest(Request.Method.POST, Urls,
                response -> {
                    // response
                    Log.d("Response", response);
                    System.out.println("Like_Video_Data=> responce "+response);
                    //progressDialog.dismiss();
                    try {
                        JSONObject jsonObject=new JSONObject(response);
                        String msg=jsonObject.getString("msg");
                        int status = jsonObject.getInt("status");

                        System.out.println("Like_Video_Data=> status "+status);

                        if (status==1) {

                            int like = comment_modelList.get(position).getSelfLike();
                            System.out.println("like=>"+like);

                        } else if(status==2){

                            int like = comment_modelList.get(position).getSelfLike();
                            System.out.println("dislike=>"+like);

                        }else {

                            Toast.makeText(CommentActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {

                        e.printStackTrace();
                        Toast.makeText(CommentActivity.this, "Server Error", Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    // error
                    Log.d("Error.Response", String.valueOf(error));
                }
        ) {
            @Override
            protected Map<String, String> getParams()
            { Map<String, String>  params = new HashMap<String, String>();

                params.put("user_id",user_id);
                params.put("video_id", video_id);
                params.put("like", like_status);
                params.put("comment_id", comment_id);
                System.out.println(params.toString());
                return params;
            }
        };
        VolleySingleton.getInstance(CommentActivity.this).addToRequestQueue(postRequest);
    }
}