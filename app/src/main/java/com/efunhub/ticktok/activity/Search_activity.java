package com.efunhub.ticktok.activity;

import static com.efunhub.ticktok.retrofit.Constant.SERVER_URL;
import static com.efunhub.ticktok.utility.ConstantVariables.GET_ALLPROFILES;
import static com.efunhub.ticktok.utility.ConstantVariables.SEARCH_PROFILE;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.efunhub.ticktok.R;
import com.efunhub.ticktok.adapter.Search_Adapter;
import com.efunhub.ticktok.application.SessionManager;
import com.efunhub.ticktok.interfaces.IResult;
import com.efunhub.ticktok.interfaces.onItemClick_Listener;
import com.efunhub.ticktok.model.AllUserProfile;
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

public class Search_activity extends AppCompatActivity implements onItemClick_Listener {
    AutoCompleteTextView searchView;
    Button btn_search;
    String id;
    RecyclerView rv_search;
    ProgressDialog progressDialog;
    private VolleyService mVolleyService;
    private IResult mResultCallBack = null;
    Search_Adapter search_adapter;
    TextView tv_no_result;
    String All_Profile = "get_all_profile";
    String Search_Profile = "search";
    ArrayList<AllUserProfile> profilelist = new ArrayList<>();
    ArrayList<AllUserProfile.ProfileList> get_all_profiles = new ArrayList<>();
    List<Search_User_Model> search_user_modelList = new ArrayList<>();
    ArrayList searchprofile = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        iniData();
        getallprofiles();

    }

    private void iniData() {

        searchView = findViewById(R.id.searchViewprofile);
        tv_no_result = findViewById(R.id.tv_no_result);
        btn_search = findViewById(R.id.btn_search1);
        rv_search = findViewById(R.id.rv_search1);
        id = SessionManager.onGetAutoCustomerId();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, searchprofile);
        searchView.setThreshold(1);//will start working from first character
        searchView.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(searchView.getText().toString()))
                {
                    Search_Profile();
                }
                else {
                    Toast.makeText(Search_activity.this, "Enter something", Toast.LENGTH_SHORT).show();
                }

            }
        });
        searchView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);

    }

    private void Search_Profile() {
        if (!search_user_modelList.isEmpty())
        {
            search_user_modelList.clear();
        }

        progressDialog.show();
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallBack, this);

        Map<String, String> params = new HashMap<>();
        params.put("search_text", searchView.getText().toString());
        params.put("user_id", id);

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
                                Toast.makeText(Search_activity.this, "Something went wrong", Toast.LENGTH_SHORT).show();

                            } else if (Integer.parseInt(status) == 1) {
                                String user_search = jsonObject.getString("users");
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                Gson gson = gsonBuilder.create();
                                Search_User_Model[] search_user_model = gson.fromJson(user_search, Search_User_Model[].class);
                                search_user_modelList = new ArrayList<>(Arrays.asList(search_user_model));
                                if (!search_user_modelList.isEmpty()) {
                                    tv_no_result.setVisibility(View.GONE);
                                    setUpRecycler();

                                }
                                else {
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

                }
            }

            @Override
            public void notifyError(int requestId, VolleyError error) {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(Search_activity.this);
                //  builder.setIcon(R.drawable.icon_exit);
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

    private void getallprofiles() {
        profilelist.clear();
        progressDialog=new ProgressDialog(this);
        progressDialog.show();
        initVolleyCallback1();
        mVolleyService = new VolleyService(mResultCallBack, this);
        mVolleyService.getDataVolley(GET_ALLPROFILES, SERVER_URL + All_Profile);
    }

    private void initVolleyCallback1() {
        mResultCallBack = new IResult() {
            @Override
            public void notifySuccess(int requestId, String response) {
                JSONObject jsonObject = null;
                switch (requestId) {
                    case GET_ALLPROFILES:
                        try {
                            jsonObject = new JSONObject(response);
                            String msg = jsonObject.getString("msg");
                            String status = jsonObject.getString("status");

                            if (Integer.parseInt(status) == 0) {

                            } else if (Integer.parseInt(status) == 1) {
                                progressDialog.dismiss();
                                //tv_no_result.setVisibility(View.GONE);
                                String current_order = jsonObject.getString("data");
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                Gson gson = gsonBuilder.create();
                                AllUserProfile.ProfileList[] userProfiles = gson.fromJson(current_order,AllUserProfile.ProfileList[].class);
                                get_all_profiles = new ArrayList<>(Arrays.asList(userProfiles));
                                System.out.println("all profiles"+get_all_profiles.size());
                                if (!get_all_profiles.isEmpty()) {
                                    for (int i=0;i<get_all_profiles.size();i++) {
                                        searchprofile.add(get_all_profiles.get(i).getName());
                                    }
                                    System.out.println("profiles"+searchprofile.size());
                                }
                                else {
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
                AlertDialog.Builder builder = new AlertDialog.Builder(Search_activity.this);
                //  builder.setIcon(R.drawable.icon_exit);
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

    private void setUpRecycler() {

        SnapHelper snapHelper = new PagerSnapHelper();
        rv_search.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        search_adapter = new Search_Adapter(Search_activity.this, search_user_modelList, this);

        rv_search.setAdapter(search_adapter);
        rv_search.setHasFixedSize(true);
        rv_search.setOnFlingListener(null);
        snapHelper.attachToRecyclerView(rv_search);
    }

    @Override
    public void Item_ClickListner(int position) {
        if (!search_user_modelList.get(position).getId().equals(id)){
            startActivity(new Intent(Search_activity.this, ProfileActivity.class)
                    .putExtra("user_id", search_user_modelList.get(position).getId()));
        }
        else {
            Toast.makeText(Search_activity.this, "Please Search other profile,this is your profile", Toast.LENGTH_SHORT).show();
        }

    }
}