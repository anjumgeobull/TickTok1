package com.efunhub.ticktok.activity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import com.efunhub.ticktok.R;
import com.efunhub.ticktok.model.Notification_Model;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
public class NotificationDetails extends AppCompatActivity {
    ArrayList<Notification_Model> NotificationList = new ArrayList<>();
    String position="0";
    String date;
    TextView tvdate,tvtilte,tvMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_details);
        TextView toolbar_head = findViewById(R.id.toolbar_head);
        toolbar_head.setText("Notification");
        ImageView bk_arrow = findViewById(R.id.bk_arrow);
        bk_arrow.setOnClickListener(v -> onBackPressed());
        initData();
    }
    private void initData()  {
        tvdate=findViewById(R.id.tvdate);
        tvtilte=findViewById(R.id.tvtitle);
        tvMsg=findViewById(R.id.tvMsg);
        //NotificationList = (ArrayList<Notification_Model>) getIntent().getSerializableExtra("notification");
        NotificationList = getIntent().getParcelableArrayListExtra("notification");
        if(position!=null){
            position =  getIntent().getStringExtra("Position");
            System.out.println("Position=="+position);
            String pDate = NotificationList.get(Integer.parseInt(position)).getCreated_at();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date fDate = format.parse(pDate);

                System.out.println(new SimpleDateFormat("dd-MM-yyyy").format(fDate));
                date=new SimpleDateFormat("dd-MM-yyyy").format(fDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            tvdate.setText(date);
            tvtilte.setText(NotificationList.get(Integer.parseInt(position)).getTitle());
            tvMsg.setText(NotificationList.get(Integer.parseInt(position)).getMessage());
        }
    }
}