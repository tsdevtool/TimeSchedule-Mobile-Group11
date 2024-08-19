package com.example.timeschedule_mobile_group11;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.models.Event;
import com.example.timeschedule_mobile_group11.databinding.ItemDetailEventBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EventDetailActivity extends AppCompatActivity {
    ItemDetailEventBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Sử dụng View Binding để thiết lập giao diện
        binding = ItemDetailEventBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Nhận dữ liệu sự kiện từ Intent
        Intent intent = getIntent();
        Event event = (Event) intent.getSerializableExtra("event");

        if (event != null) {
            // Hiển thị dữ liệu sự kiện
            binding.tvEventTitle.setText(event.getTitle());
            binding.tvClassroomCode.setText("Phòng: " + event.getClassroom_code());

            // Format lại thời gian
            String formattedTime = "Thời gian: " + formatTime(event.getTime());
            binding.tvEventTime.setText(formattedTime);

            binding.tvEventDescription.setText(event.getDescription());
            String imageUrl = event.getImage(); // URL lưu trong Firebase Realtime Database
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Picasso.get()
                        .load(imageUrl)
                        .placeholder(R.drawable.img) // Hình ảnh hiển thị trong khi chờ tải
                        .error(R.drawable.img) // Hình ảnh hiển thị khi xảy ra lỗi
                        .into(binding.imvEventPhoto);              // ImageView để hiển thị ảnh
            }
        }
    }

    private String formatTime(String rawTime) {
        // Định dạng thời gian gốc từ dữ liệu Firebase (ISO 8601)
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
        // Định dạng thời gian hiển thị cho người dùng bằng tiếng Việt
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd - M - yyyy, HH:mm", new Locale("vi", "VN"));
        try {
            Date date = originalFormat.parse(rawTime);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            // Nếu không thể parse được, trả về thời gian gốc
            return rawTime;
        }
    }
}
