package com.efunhub.ticktok.activity;

import static com.efunhub.ticktok.retrofit.Constant.SERVER_URL;
import static com.efunhub.ticktok.utility.ConstantVariables.USER_PROFILE;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.efunhub.ticktok.R;
import com.efunhub.ticktok.application.SessionManager;
import com.efunhub.ticktok.fragments.AllVideoFragment;
import com.efunhub.ticktok.interfaces.IResult;
import com.efunhub.ticktok.model.User_Profile_Model.UserProfile;
import com.efunhub.ticktok.retrofit.APICallback;
import com.efunhub.ticktok.retrofit.AlertDialogs;
import com.efunhub.ticktok.retrofit.BaseServiceResponseModel;
import com.efunhub.ticktok.retrofit.PrintUtil;
import com.efunhub.ticktok.services.UpdatePofileWithoutPicSP;
import com.efunhub.ticktok.services.UpdateProfileSP;
import com.efunhub.ticktok.utility.VolleyService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserDetailsRegistration extends AppCompatActivity {

    EditText Fname,District,State,Country,Insta,Facebook,Youthtube,Other;
    Button Submit,Cancel;
    String  id;
    ProgressDialog progressDialog;
    private VolleyService mVolleyService;
    private IResult mResultCallBack = null;
    String Profile_User = "get_myprofile";
    String mobile="";
    String email1="";
    private AlertDialogs Alert;
    CheckBox ischeckd;
    private UpdatePofileWithoutPicSP mUpdateProfilepicSP;

    private static SharedPreferences mSharedPreferences;
   ArrayList<UserProfile> userProfileModel_List = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details_registration);
        initData();
        getUser_Profile();

    }

    private void initData() {
        mSharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        mUpdateProfilepicSP=new UpdatePofileWithoutPicSP(this);
        id = SessionManager.onGetAutoCustomerId();
        Fname = findViewById(R.id.fname);
        District = findViewById(R.id.district);
        State = findViewById(R.id.state);
        Country = findViewById(R.id.country);
        Insta = findViewById(R.id.insta);
        Facebook = findViewById(R.id.facebook);
        Youthtube = findViewById(R.id.youthtube);
        Other = findViewById(R.id.other);
        Submit = findViewById(R.id.submit);
        Cancel = findViewById(R.id.cancel);
        ischeckd=findViewById(R.id.check);

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               startActivity(new Intent(UserDetailsRegistration.this, MainActivity.class));
                //onBackPressed();
            }
        });

        progressDialog = new ProgressDialog(UserDetailsRegistration.this);

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ischeckd.isChecked()) {
                    if (Fname.getText().toString().isEmpty()) {
                        Fname.setError("Enter the first name");
                    } else if (District.getText().toString().isEmpty()) {
                        District.setError("Enter the district");
                    } else if (State.getText().toString().isEmpty()) {
                        State.setError("Enter the state");
                    } else if (Country.getText().toString().isEmpty()) {
                        Country.setError("Enter the country");
                    } else {
                        try {

                            if (userProfileModel_List.get(0).getMobile() != null) {
                                mobile = userProfileModel_List.get(0).getMobile();
                            }
                            if (userProfileModel_List.get(0).getEmail() != null) {
                                email1 = userProfileModel_List.get(0).getEmail();
                            }

                            RequestBody user_id = RequestBody.create(MediaType.parse("text/plain"), SessionManager.onGetAutoCustomerId());
                            RequestBody mobile_no = RequestBody.create(MediaType.parse("text/plain"), mobile);
                            RequestBody custname = RequestBody.create(MediaType.parse("text/plain"), Fname.getText().toString());
                            RequestBody email = RequestBody.create(MediaType.parse("text/plain"), email1);
                            RequestBody district = RequestBody.create(MediaType.parse("text/plain"), District.getText().toString());
                            RequestBody state = RequestBody.create(MediaType.parse("text/plain"), State.getText().toString());
                            RequestBody country = RequestBody.create(MediaType.parse("text/plain"), Country.getText().toString());
                            RequestBody insta_id = RequestBody.create(MediaType.parse("text/plain"), Insta.getText().toString());
                            RequestBody facebook = RequestBody.create(MediaType.parse("text/plain"), Facebook.getText().toString());
                            RequestBody youthtube = RequestBody.create(MediaType.parse("text/plain"), Youthtube.getText().toString());
                            RequestBody other = RequestBody.create(MediaType.parse("text/plain"), Other.getText().toString());

                            UpdateProfileApi(user_id, mobile_no, custname, email, district, state, country, insta_id, facebook, youthtube, other
                            );
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }else {
                    Toast.makeText(UserDetailsRegistration.this, "please agree terms and conditions", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void UpdateProfileApi(RequestBody user_auto_id, RequestBody mobile_number,
                                  RequestBody name, RequestBody Email,
                                  RequestBody district, RequestBody state,
                                  RequestBody country, RequestBody insta_id, RequestBody facebook_id,
                                  RequestBody youthtube_id, RequestBody other
    ) {
        mUpdateProfilepicSP.CallUpdateProfileWithoutPic(user_auto_id,mobile_number,name,Email,
                district,state,country,insta_id,facebook_id,youthtube_id,other,
                new APICallback() {
                    @Override
                    public <T> void onSuccess(T serviceResponse) {
                        int Status = ((BaseServiceResponseModel) serviceResponse).getStatus();
                        try {
                            String message = ((BaseServiceResponseModel) serviceResponse).getMsg();
                            if (Status == 1) {
                                SessionManager.onSaveAiifaRegistration("Yes");
                                Toast.makeText(UserDetailsRegistration.this, "Registration Done Successsfully", Toast.LENGTH_SHORT).show();
                                //Alert.onShowProgressDialog(UserDetailsRegistration.this, false);
                                startActivity(new Intent(UserDetailsRegistration.this,UploadActvity.class));
                                finish();
                            } else {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            Alert.onShowProgressDialog(UserDetailsRegistration.this, false);
                        }
                    }

                    @Override
                    public <T> void onFailure(T apiErrorModel, T extras) {
                        try {

                            PrintUtil.showNetworkAvailableToast(UserDetailsRegistration.this);

                        } catch (Exception e) {
                            e.printStackTrace();
                            PrintUtil.showNetworkAvailableToast(UserDetailsRegistration.this);
                        } finally {
                            Alert.onShowProgressDialog(UserDetailsRegistration.this, false);
                        }
                    }
                });
    }
    private void getUser_Profile() {
        userProfileModel_List.clear();
        initVolleyCallback();
        mVolleyService = new VolleyService(mResultCallBack, this);

        Map<String, String> params = new HashMap<>();
        params.put("user_id", id);
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
                                Toast.makeText(UserDetailsRegistration.this, msg, Toast.LENGTH_SHORT).show();
                            } else if (Integer.parseInt(status) == 1) {
                                String current_order = jsonObject.getString("user_profile");
                                GsonBuilder gsonBuilder = new GsonBuilder();
                                Gson gson = gsonBuilder.create();
                                UserProfile userProfiles = gson.fromJson(current_order, UserProfile.class);
                                userProfileModel_List = new ArrayList<>(Arrays.asList(userProfiles));
                                if (!userProfileModel_List.isEmpty()) {
                                    Fname.setText(userProfileModel_List.get(0).getName());
                                    Country.setText(userProfileModel_List.get(0).getCountry());
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

                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(UserDetailsRegistration.this);
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





}