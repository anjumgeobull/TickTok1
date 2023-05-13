package com.efunhub.ticktok.adapter;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.efunhub.ticktok.R;
import com.efunhub.ticktok.activity.NotificationDetails;
import com.efunhub.ticktok.interfaces.onItemClick_Listener;
import com.efunhub.ticktok.model.Notification_Model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
    Context context;
    String date;
    ArrayList<Notification_Model> NotificationList = new ArrayList<>();
    ArrayList<Notification_Model> SelectedList = new ArrayList<>();
    Context activity;
    private LayoutInflater mInflater;
    onItemClick_Listener onItemClick_listener;

    public NotificationAdapter(Context context, ArrayList<Notification_Model> notification_list) {
        this.activity = context;
        this.mInflater = LayoutInflater.from(context);
        this.NotificationList = notification_list;
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.notification_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotificationAdapter.ViewHolder holder, int position) {

        holder.txtNameNotification.setText(NotificationList.get(position).getTitle());
        holder.txt_descriptionName.setText(NotificationList.get(position).getMessage());

        String pDate = NotificationList.get(0).getCreated_at();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date fDate = format.parse(pDate);

            System.out.println(new SimpleDateFormat("dd-MM-yyyy").format(fDate));
            date=new SimpleDateFormat("dd-MM-yyyy").format(fDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.txt_time.setText(date);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //activity.startActivity(new Intent(activity, FilterActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return NotificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtNameNotification,txt_descriptionName,txt_time,txt_moreDetails;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNameNotification = itemView.findViewById(R.id.txtNameNotification);
            txt_descriptionName = itemView.findViewById(R.id.txtdescriptionName);
            txt_time = itemView.findViewById(R.id.txtTime);
            txt_moreDetails=itemView.findViewById(R.id.moreDetails);
            txt_moreDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Intent i = new Intent(activity, NotificationDetails.class);
                    i.putParcelableArrayListExtra("notification",NotificationList);
                    i.putExtra("Position",String.valueOf(position) );
                    System.out.println("Positiom==>"+position);
                    activity.startActivity(i);

                }
            });

        }
    }
}
