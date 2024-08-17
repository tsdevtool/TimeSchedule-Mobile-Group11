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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EventAdapter extends ArrayAdapter<Event> {

    private Context context;
    private List<Event> events;
    private SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
    private SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());

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
        String formattedDate = formatDate(event.getTime());
        tvDateEvent.setText(formattedDate);

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
    private String formatDate(String dateString) {
        try {
            Date date = inputFormat.parse(dateString);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return dateString; // Return original if parsing fails
        }
    }
}