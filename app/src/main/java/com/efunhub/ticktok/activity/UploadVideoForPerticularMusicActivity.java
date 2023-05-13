package com.efunhub.ticktok.activity;

import static com.efunhub.ticktok.retrofit.Constant.SERVER_URL;
import static com.efunhub.ticktok.utility.ConstantVariables.GET_AUDIO;
import static com.efunhub.ticktok.utility.ConstantVariables.SEARCH_PROFILE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.efunhub.ticktok.R;
import com.efunhub.ticktok.adapter.MusicAdapter;
import com.efunhub.ticktok.adapter.Music_From_AppAdapter;
import com.efunhub.ticktok.adapter.PostAdapterNew;
import com.efunhub.ticktok.adapter.Search_Adapter;
import com.efunhub.ticktok.application.SessionManager;
import com.efunhub.ticktok.interfaces.IResult;
import com.efunhub.ticktok.model.AudioFromAppModel;
import com.efunhub.ticktok.model.AudioModel;
import com.efunhub.ticktok.model.AudioSearchModel;
import com.efunhub.ticktok.model.Search_User_Model;
import com.efunhub.ticktok.utility.VolleyService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadVideoForPerticularMusicActivity extends AppCompatActivity implements Music_From_AppAdapter.ApplyMusicInterface {

    ArrayList<AudioFromAppModel.AudioList> audiolist = new ArrayList<>();
    RelativeLayout relativeUploadVideo;
    Toolbar toolbar;
    Button btn_browse, btn_search;
    AutoCompleteTextView ed_searchView;
    RecyclerView rv_search;
    ProgressDialog progressDialog;
    private VolleyService mVolleyService;
    private IResult mResultCallBack = null;
    TextView tv_no_result;
    String id;
    String Search_Profile = "search_audio";
    String Get_audios = "audios";
    private String mMusicPath;
    ArrayList audios = new ArrayList();
    Music_From_AppAdapter musicAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video_for_perticular_music);
        setUpToolbar();
        init();
        getaudios();
        relativeUploadVideo = findViewById(R.id.relativeUploadVideo);

        relativeUploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(UploadVideoForPerticularMusicActivity.this, UploadVideoActivity.class));
            }
        });
    }

    private void init() {
        btn_browse = findViewById(R.id.btn_browse);
        btn_search = findViewById(R.id.btn_search);
        ed_searchView = findViewById(R.id.searchView);
        rv_search = findViewById(R.id.rv_search);
        tv_no_result = findViewById(R.id.tv_no_result);
        id = SessionManager.onGetAutoCustomerId();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, audios);
        ed_searchView.setThreshold(1);//will start working from first character
        ed_searchView.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

        ed_searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //rv_search.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(UploadVideoForPerticularMusicActivity.this, MusicActivity.class,reques));
                Intent intentMusic1 = new Intent(UploadVideoForPerticularMusicActivity.this, MusicActivity.class);
                intentMusic1.putExtra("from_activity", "Audio");
                intentMusic1.putExtra("MuxAudioVideo", "No");
                startActivityForResult(intentMusic1, 2);
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ed_searchView.getText().toString().equals("")) {
                    Toast.makeText(UploadVideoForPerticularMusicActivity.this, "Please enter music name", Toast.LENGTH_LONG).show();
                } else {
                    Searchaudio();
                }
            }
        });

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);

    }

    private void getaudios() {
        audiolist.clear();
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        //shimmer_view_container.startShimmerAnimation();
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallBack, this);
        mVolleyService.getDataVolley(GET_AUDIO, SERVER_URL + Get_audios);
    }

    private void Searchaudio() {
        progressDialog = ProgressDialog.show(UploadVideoForPerticularMusicActivity.this, "Please Wait", null, true, true);
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallBack, this);

        Map<String, String> params = new HashMap<>();
        params.put("search_text", ed_searchView.getText().toString());

        System.out.println("params=>" + params.toString());
        mVolleyService.postDataVolleyParameters(SEARCH_PROFILE, SERVER_URL + Search_Profile, params);

    }

    private void initVolleyCallback() {
        mResultCallBack = new IResult() {
            @Override
            public void notifySuccess(int requestId, String response) {
                JSONObject jsonObject = null;
                switch (requestId) {
                    case SEARCH_PROFILE:
                        tv_no_result.setVisibility(View.GONE);
                        try {
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                            jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            String status = jsonObject.getString("status");


                            if (Integer.parseInt(status) == 0) {
                                Toast.makeText(UploadVideoForPerticularMusicActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                            } else if (Integer.parseInt(status) == 1) {
                                String user_search = jsonObject.getString("users");
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                Gson gson = gsonBuilder.create();
                                AudioFromAppModel.AudioList[] search_model = gson.fromJson(user_search, AudioFromAppModel.AudioList[].class);
                                audiolist = new ArrayList<>(Arrays.asList(search_model));
                                if (!audiolist.isEmpty()) {
                                    rv_search.setVisibility(View.VISIBLE);
                                    tv_no_result.setVisibility(View.GONE);
                                    setUpRecycler();
                                } else {
                                    tv_no_result.setVisibility(View.VISIBLE);
                                }

                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                            if (progressDialog != null) {
                                progressDialog.dismiss();
                            }
                        }
                        break;

                    case GET_AUDIO:
                        try {
                            jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            String status = jsonObject.getString("status");

                            if (Integer.parseInt(status) == 0) {
                                progressDialog.dismiss();
                                Toast.makeText(UploadVideoForPerticularMusicActivity.this, msg, Toast.LENGTH_SHORT).show();
                            } else if (Integer.parseInt(status) == 1) {
                                progressDialog.dismiss();
                                String current_data = jsonObject.getString("data");
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                Gson gson = gsonBuilder.create();
                                AudioFromAppModel.AudioList[] audiomodel = gson.fromJson(current_data, AudioFromAppModel.AudioList[].class);
                                audiolist = new ArrayList<>(Arrays.asList(audiomodel));
                                if (!audiolist.isEmpty()) {
                                    //setUpRecycler();
                                    for (int i = 0; i < audiolist.size(); i++) {
                                        audios.add(audiolist.get(i).getAudiofile());
                                    }
                                } else {
                                }
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
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(UploadVideoForPerticularMusicActivity.this);
                builder.setMessage("Server Error")
                        .setCancelable(true)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert = builder.create();
                alert.show();
                Log.v("Volley requestid ", String.valueOf(requestId));
                Log.v("Volley Error", String.valueOf(error));
            }
        };
    }

    private void setUpToolbar() {

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Music");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUpRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv_search.setLayoutManager(layoutManager);
        musicAdapter = new Music_From_AppAdapter(UploadVideoForPerticularMusicActivity.this, audiolist);
        rv_search.setAdapter(musicAdapter);
    }


    @Override
    public void selectAndApplyMusic(String musicPath) {
        mMusicPath = musicPath;

        Intent intent = new Intent(UploadVideoForPerticularMusicActivity.this, UploadVideoActivity.class);
        intent.putExtra("musicPath", musicPath);
        startActivity(intent);
        finish();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (requestCode == 2) {
                mMusicPath = data.getStringExtra("musicPath");
                if (mMusicPath == null) {
                    return;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
