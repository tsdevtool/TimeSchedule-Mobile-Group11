package com.example.timeschedule_mobile_group11;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.models.Event;
import com.example.timeschedule_mobile_group11.databinding.ItemDetailEventBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

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
        addEvents();
    }

    private void addEvents() {
        binding.btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    String userId = currentUser.getUid();

                    // Nhận dữ liệu sự kiện từ Intent
                    Intent intent = getIntent();
                    Event event = (Event) intent.getSerializableExtra("event");

                    if (event != null) {
                        // Lấy thời gian hiện tại
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                        try {
                            Date eventDate = sdf.parse(event.getTime()); // Thời gian diễn ra sự kiện
                            Date currentDate = new Date(); // Thời gian hiện tại

                            // Kiểm tra nếu thời gian hiện tại chưa quá thời gian sự kiện
                            if (currentDate.before(eventDate)) {
                                String eventId = String.valueOf(event.getId()); // Lấy ID sự kiện

                                // Lấy câu hỏi người dùng đã nhập
                                String userQuestion = binding.edtQuestion.getText().toString();
                                if (userQuestion.isEmpty()) {
                                    userQuestion = "No question"; // Đặt giá trị mặc định nếu không có câu hỏi
                                }

                                // Tham chiếu đến nút event_details và tạo ID ngẫu nhiên cho mỗi lần đăng ký
                                DatabaseReference eventDetailsRef = FirebaseDatabase.getInstance()
                                        .getReference("event_details")
                                        .push(); // Tạo một khóa mới với ID ngẫu nhiên

                                // Tạo đối tượng lưu trữ thông tin đăng ký
                                Map<String, Object> eventDetails = new HashMap<>();
                                eventDetails.put("eventId", eventId);
                                eventDetails.put("question", userQuestion);
                                eventDetails.put("userId", userId);

                                // Lưu vào Firebase
                                eventDetailsRef.setValue(eventDetails).addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        showAlertDialog("Thành công", "Đăng ký thành công!");
                                    } else {
                                        showAlertDialog("Thất bại", "Đăng ký thất bại. Vui lòng thử lại.");
                                    }
                                });
                            } else {
                                // Hiển thị AlertDialog nếu sự kiện đã qua
                                showAlertDialog("Sự kiện đã hết hạn", "Sự kiện này đã kết thúc, bạn hãy đăng kí sự kiện khác. ");
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                            showAlertDialog("Lỗi", "Lỗi khi xử lý thời gian sự kiện!");
                        }
                    }
                } else {
                    showAlertDialog("Đăng nhập", "Vui lòng đăng nhập để đăng ký sự kiện.");
                }
            }
        });
    }

    private void showAlertDialog(String title, String message) {
        new AlertDialog.Builder(EventDetailActivity.this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
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
