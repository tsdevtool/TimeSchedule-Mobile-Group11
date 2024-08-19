package com.example.timeschedule_mobile_group11.fragment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.models.Event;
import com.example.timeschedule_mobile_group11.EventDetailActivity;
import com.example.timeschedule_mobile_group11.R;
import com.example.timeschedule_mobile_group11.databinding.ItemEventsBinding;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CustomEventAdapter extends ArrayAdapter<Event> {
    ItemEventsBinding binding;
    private Context mContext;
    private List<Event> eventList;

    public CustomEventAdapter(Context context, List<Event> list) {
        super(context, 0, list);
        mContext = context;
        eventList = list;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ItemEventsBinding binding;
        if (convertView == null) {
            // Inflate layout với View Binding
            binding = ItemEventsBinding.inflate(LayoutInflater.from(mContext), parent, false);
            convertView = binding.getRoot();
            convertView.setTag(binding);
        } else {
            // Sử dụng View Binding đã lưu trữ trong Tag
            binding = (ItemEventsBinding) convertView.getTag();
        }

        // Lấy sự kiện hiện tại
        Event currentEvent = eventList.get(position);

        // Load và hiển thị hình ảnh từ URL bằng Picasso
        String imageUrl = currentEvent.getImage(); // URL lưu trong Firebase Realtime Database
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get()
                    .load(imageUrl)// Ảnh hiển thị khi có lỗi
                    .into(binding.imvLvImage);              // ImageView để hiển thị ảnh
        }

        // Set dữ liệu vào các view
        binding.tvTitleEvent.setText(currentEvent.getTitle());

        String formattedTime = formatEventTime(currentEvent.getTime());
        binding.tvDateEvent.setText(formattedTime);

        // Thiết lập sự kiện click vào item nếu cần
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, EventDetailActivity.class);
                intent.putExtra("event", currentEvent);
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }
    // Hàm để định dạng thời gian sự kiện
    private String formatEventTime(String time) {
        try {
            // Giả sử thời gian lưu dưới dạng chuỗi ISO 8601: "2024-08-15T09:00:00"
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            // Định dạng thời gian hiển thị cho người dùng bằng tiếng Việt
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMMM yyyy, HH:mm", new Locale("vi", "VN"));
            return outputFormat.format(isoFormat.parse(time));
        } catch (Exception e) {
            e.printStackTrace();
            return time; // Nếu lỗi xảy ra, trả về chuỗi gốc
        }
    }
}
