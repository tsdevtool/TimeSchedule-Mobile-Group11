package com.example.timeschedule_mobile_group11.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.models.Event;
import com.example.timeschedule_mobile_group11.R;

import java.util.List;

public class EventAdapter extends ArrayAdapter<Event> {

    private Context context;
    private List<Event> events;

    public EventAdapter(Context context, List<Event> events) {
        super(context, 0, events);
        this.context = context;
        this.events = events;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Kiểm tra xem view có cần tái sử dụng không
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_events, parent, false);
        }

        // Lấy item sự kiện hiện tại
        Event event = getItem(position);

        // Cập nhật tiêu đề sự kiện
        TextView tvEventTitle = convertView.findViewById(R.id.tvTitleEvent);
        tvEventTitle.setText(event.getTitle());
        TextView tvDateEvent = convertView.findViewById(R.id.tvDateEvent);
        tvDateEvent.setText(event.getTime());

        // Cập nhật hình ảnh sự kiện
        ImageView imvEventImage = convertView.findViewById(R.id.imvLvImage);
        int imageResId = getDrawableResourceByName(context, event.getImage());
        if (imageResId != 0) {
            imvEventImage.setImageResource(imageResId);
        } else {
            imvEventImage.setImageResource(R.drawable.img); // Hình ảnh mặc định nếu không tìm thấy
        }

        return convertView;
    }

    private int getDrawableResourceByName(Context context, String imageName) {
        return context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
    }
}