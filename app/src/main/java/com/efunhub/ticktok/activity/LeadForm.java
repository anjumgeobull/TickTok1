package com.efunhub.ticktok.activity;

import static com.efunhub.ticktok.retrofit.Constant.SERVER_URL;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.efunhub.ticktok.R;
import com.efunhub.ticktok.application.SessionManager;
import com.efunhub.ticktok.utility.DialogsUtils;
import com.efunhub.ticktok.utility.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LeadForm extends AppCompatActivity {
    EditText name, mobile_no, email_id,requirement;
    Button Submit;
    ProgressDialog progressDialog;
    String add_lead = "add_lead";
    String user_auto_id = "64390423339405e88706f562";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lead_form);
//        user_auto_id = SessionManager.onGetCampaignData();
//        System.out.println("CAMPAIGNUSERID"+user_auto_id);
        initData();
    }

    private void initData() {

        name = findViewById(R.id.yname);
        mobile_no = findViewById(R.id.mobile_no);
        email_id = findViewById(R.id.email_id);
        requirement = findViewById(R.id.requirement);


        progressDialog = new ProgressDialog(LeadForm.this);
        Submit = findViewById(R.id.submit);
        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validation();

            }
        });


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
            leadformRegister(name.getText().toString() ,email_id.getText().toString(), mobile_no.getText().toString(),requirement.getText().toString());

        }
    }
    private void leadformRegister(String name,  String email,String mobile, String requirement) {
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
               // user_auto_id,name, mail_id,mobile_number,datails
                params.put("user_auto_id", user_auto_id);
                params.put("name", name);
                params.put("mail_id", email);
                params.put("mobile_number", mobile);
                params.put("datails", requirement);
                System.out.println("params"+params);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(postRequest);
    }

}