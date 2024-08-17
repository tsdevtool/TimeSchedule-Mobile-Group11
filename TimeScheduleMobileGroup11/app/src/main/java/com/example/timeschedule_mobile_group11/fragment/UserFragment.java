package com.example.timeschedule_mobile_group11.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.timeschedule_mobile_group11.R;
import com.example.timeschedule_mobile_group11.databinding.ActivityUserFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

public class UserFragment extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri filePath;
    private ActivityUserFragmentBinding binding;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserFragmentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo Firebase Storage
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        // Lấy userId của người dùng hiện tại
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Truy vấn thông tin người dùng từ Firebase Database
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Lấy dữ liệu người dùng từ Firebase
                String fullName = dataSnapshot.child("fullName").getValue(String.class);
                String userCode = dataSnapshot.child("userCode").getValue(String.class);
                String email = dataSnapshot.child("email").getValue(String.class);
                String dayOfBirth = dataSnapshot.child("dayOfBirth").getValue(String.class);
                String dayAdmission = dataSnapshot.child("dayAdmission").getValue(String.class);
                String photoUrl = dataSnapshot.child("photoUrl").getValue(String.class);
                // Kiểm tra và hiển thị ảnh đại diện nếu có
                if (photoUrl != null) {
                    Glide.with(UserFragment.this)
                            .load(photoUrl)
                            .into(binding.imvAvatar);
                } else {
                    // Bạn có thể đặt một ảnh đại diện mặc định ở đây nếu không có ảnh nào được lưu
                    binding.imvAvatar.setImageResource(R.drawable.logo_app);
                }

                // Cập nhật giao diện với dữ liệu người dùng
                binding.txtFullName.setText(fullName);
                binding.txtUserCode.setText(userCode);
                binding.txtEmail.setText(email);
                binding.txtdayOfBirth.setText(dayOfBirth);
                binding.txtdayAdmission.setText(dayAdmission);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                Toast.makeText(UserFragment.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện khi nhấn vào nút Đổi Avatar
        binding.btnChangeAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        // Xử lý sự kiện khi nhấn vào nút Đổi mật khẩu
        binding.btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newPassword = binding.txtNewPassword.getText().toString();
                if (!TextUtils.isEmpty(newPassword)) {
                    changePassword(newPassword);
                } else {
                    Toast.makeText(UserFragment.this, "Please enter a new password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void changePassword(String newPassword) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        newPassword = binding.txtNewPassword.getText().toString().trim();
        if (user != null) {
            user.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UserFragment.this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("Firebase", "Failed to update password", task.getException());
                                Toast.makeText(UserFragment.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                binding.imvAvatar.setImageBitmap(bitmap);

                // Tải ảnh lên Firebase Storage và sau đó cập nhật URL trong Firebase Authentication
                uploadImageToFirebase(filePath);

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void uploadImageToFirebase(Uri filePath) {
        if (filePath != null) {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference ref = storageReference.child("avatars/" + userId);

            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Lấy URL của tệp đã tải lên
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // URL đã lấy được
                                    String imageUrl = uri.toString();
                                    Log.d("Firebase", "File URL: " + imageUrl);

                                    // Lưu URL vào Firebase Realtime Database để hiển thị sau
                                    saveImageUrlToDatabase(imageUrl);

                                    // Hiển thị hình ảnh trong ImageView
                                    Glide.with(UserFragment.this)
                                            .load(imageUrl)
                                            .into(binding.imvAvatar);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e("Firebase", "Failed to get download URL", e);
                                    Toast.makeText(UserFragment.this, "Failed to get download URL", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("Firebase", "Failed to upload avatar", e);
                            Toast.makeText(UserFragment.this, "Failed to upload avatar", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Log.e("Firebase", "File path is null");
            Toast.makeText(UserFragment.this, "File path is null", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveImageUrlToDatabase(String imageUrl) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userRef.child("photoUrl").setValue(imageUrl);
    }
}
