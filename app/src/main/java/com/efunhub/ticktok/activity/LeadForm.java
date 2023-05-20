package com.efunhub.ticktok.activity;

import static com.efunhub.ticktok.retrofit.Constant.SERVER_URL;
import static com.efunhub.ticktok.utility.ConstantVariables.CAMPAIGN_LIST;
import static com.efunhub.ticktok.utility.ConstantVariables.USER_PROFILE;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.efunhub.ticktok.CampaignModel.CampaignModelData;
import com.efunhub.ticktok.R;
import com.efunhub.ticktok.application.SessionManager;
import com.efunhub.ticktok.interfaces.IResult;
import com.efunhub.ticktok.model.User_Profile_Model.UserProfile;
import com.efunhub.ticktok.utility.DialogsUtils;
import com.efunhub.ticktok.utility.VolleyService;
import com.efunhub.ticktok.utility.VolleySingleton;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LeadForm extends AppCompatActivity {
    EditText name, mobile_no, email_id,requirement;
    Button Submit;
    ProgressDialog progressDialog;
    String add_lead = "add_lead";
    //String user_auto_id = "64390423339405e88706f562";
    String user_auto_id = "",campaign_user_auto_id="";
    ArrayList<UserProfile> userProfileModel_List = new ArrayList<>();
    private VolleyService mVolleyService;
    private IResult mResultCallBack = null;
    String Profile_User = "get_myprofile";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_form);
        initData();
    }

    private void initData() {
        user_auto_id= SessionManager.onGetAutoCustomerId();
        name = findViewById(R.id.yname);
        mobile_no = findViewById(R.id.mobile_no);
        email_id = findViewById(R.id.email_id);
        requirement = findViewById(R.id.requirement);
        progressDialog = new ProgressDialog(LeadForm.this);
        Submit = findViewById(R.id.submit);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            campaign_user_auto_id = bundle.getString("campaign_user_auto_id");
        }
        getUser_Profile();
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation();
            }
        });
    }
    private void getUser_Profile() {
        userProfileModel_List.clear();
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallBack, LeadForm.this);

        Map<String, String> params = new HashMap<>();
        params.put("user_id", user_auto_id);
        System.out.println("params=>" + params.toString());
        mVolleyService.postDataVolleyParameters(USER_PROFILE, SERVER_URL + Profile_User, params);

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
                                Toast.makeText(LeadForm.this, msg, Toast.LENGTH_SHORT).show();
                            } else
                            if (Integer.parseInt(status) == 1) {
                                String current_order = jsonObject.getString("user_profile");
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                Gson gson = gsonBuilder.create();
                                UserProfile userProfiles = gson.fromJson(current_order, UserProfile.class);
                                userProfileModel_List = new ArrayList<>(Arrays.asList(userProfiles));
                                if (!userProfileModel_List.isEmpty()) {
                                    name.setText(userProfileModel_List.get(0).getName());
                                    mobile_no.setText(userProfileModel_List.get(0).getMobile());
                                    email_id.setText(userProfileModel_List.get(0).getEmail());
                                }
                            }

                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                        break;
                }
            }

            @Override
            public void notifyError(int requestId, VolleyError error) {

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(LeadForm.this);
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

    private void Validation() {
        if (name.getText().toString().isEmpty()) {
            name.setError("Enter the name");
        }  else if (mobile_no.getText().toString().isEmpty() || mobile_no.getText().toString().length() < 10) {
            mobile_no.setError("Enter the valid mobile number");
        } else if (email_id.getText().toString().isEmpty()) {
            email_id.setError("Enter the email");
        }  else if (requirement.getText().toString().isEmpty()) {
            requirement.setError("Add requirement");
        }else {
            leadformRegister(name.getText().toString() ,email_id.getText().toString(), mobile_no.getText().toString(),requirement.getText().toString(),campaign_user_auto_id);

        }
    }
    private void leadformRegister(String name,  String email,String mobile, String requirement,String campaign_user_auto_id) {
        System.out.println("Data=> params " + name + " " + email + " " + mobile + " " + requirement + " " );
        String Urls = SERVER_URL + add_lead;
        System.out.println(Urls);
        progressDialog = DialogsUtils.showProgressDialog(LeadForm.this, "Please Wait");
        progressDialog.setCancelable(false);
        StringRequest postRequest = new StringRequest(Request.Method.POST, Urls, response -> {
            // response
            Log.d("Response", response);
            System.out.println("Data=> responce " + response);
            //progressDialog.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(response);
                String msg = jsonObject.getString("msg");
                int status = jsonObject.getInt("status");

                System.out.println("Data=> status " + status);

                if (status == 1) {
                    progressDialog.dismiss();
                    Toast.makeText(LeadForm.this, "Submitted Successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LeadForm.this, MainActivity.class));

                } else if (status == 0) {
                    progressDialog.dismiss();
                    Toast.makeText(LeadForm.this, msg, Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();

                    Toast.makeText(LeadForm.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                progressDialog.dismiss();
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Server Error", Toast.LENGTH_LONG).show();
            }

        },
                error -> {
                    progressDialog.dismiss();
                    // error
                    Log.d("Error.Response", String.valueOf(error));
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_auto_id", user_auto_id);
                params.put("name", name);
                params.put("mail_id", email);
                params.put("mobile_number", mobile);
                params.put("datails", requirement);
                params.put("campaign_user_auto_id", campaign_user_auto_id);
                System.out.println("params"+params);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(postRequest);
    }

}