package com.example.timeschedule_mobile_group11.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.timeschedule_mobile_group11.LoginActivity;
import com.example.timeschedule_mobile_group11.R;
import com.example.timeschedule_mobile_group11.databinding.ActivityUserFragmentBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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
                // Lấy giá trị của trường sex (giới tính)
                Boolean sex = dataSnapshot.child("sex").getValue(Boolean.class);
                String sexFormatted;
                if (sex != null && sex) {
                    sexFormatted = "Nam"; // true -> Nam
                } else {
                    sexFormatted = "Nữ"; // false -> Nữ
                }

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
                binding.txtSex.setText(sexFormatted);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi nếu có
                Toast.makeText(UserFragment.this, "Failed to load user data", Toast.LENGTH_SHORT).show();
            }
        });

        // Xử lý sự kiện khi nhấn vào nút Đổi Avatar
        binding.imvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        // Xử lý sự kiện khi nhấn vào nút Đổi mật khẩu
        binding.btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentPassword = binding.txtCurrentPassword.getText().toString().trim(); // Người dùng nhập mật khẩu hiện tại
                String newPassword = binding.txtNewPassword.getText().toString().trim(); // Người dùng nhập mật khẩu mới

                if (TextUtils.isEmpty(currentPassword)) {
                    // Hiển thị thông báo nếu mật khẩu cũ trống
                    new AlertDialog.Builder(UserFragment.this)
                            .setTitle("Lỗi")
                            .setMessage("Vui lòng nhập mật khẩu hiện tại.")
                            .setPositiveButton(android.R.string.ok, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return; // Dừng lại nếu không có mật khẩu cũ
                }

                if (TextUtils.isEmpty(newPassword)) {
                    // Hiển thị thông báo nếu mật khẩu mới trống
                    new AlertDialog.Builder(UserFragment.this)
                            .setTitle("Lỗi")
                            .setMessage("Vui lòng nhập mật khẩu mới.")
                            .setPositiveButton(android.R.string.ok, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return; // Dừng lại nếu không có mật khẩu mới
                }

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);

                    // Xác thực lại người dùng
                    user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                // Xác thực thành công, tiến hành đổi mật khẩu
                                changePassword(newPassword);
                            } else {
                                // Xác thực thất bại, thông báo lỗi
                                new AlertDialog.Builder(UserFragment.this)
                                        .setTitle("Lỗi")
                                        .setMessage("Mật khẩu hiện tại không chính xác. Vui lòng thử lại.")
                                        .setPositiveButton(android.R.string.ok, null)
                                        .setIcon(android.R.drawable.ic_dialog_alert)
                                        .show();
                            }
                        }
                    });
                }
            }
        });

// Xử lý sự kiện khi nhấn vào nút Show/Hide mật khẩu cũ
        binding.btnShowCurrentPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.txtCurrentPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    // Hiển thị mật khẩu
                    binding.txtCurrentPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    binding.btnShowCurrentPassword.setImageResource(R.drawable.eye_26); // Biểu tượng mở khóa
                } else {
                    // Ẩn mật khẩu
                    binding.txtCurrentPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    binding.btnShowCurrentPassword.setImageResource(R.drawable.invisible_26); // Biểu tượng khóa
                }
                // Đưa con trỏ về cuối đoạn văn bản
                binding.txtCurrentPassword.setSelection(binding.txtCurrentPassword.getText().length());
            }
        });
        // Xử lý sự kiện khi nhấn vào nút hiển thị mật khẩu
        binding.btnShowPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.txtNewPassword.getInputType() == (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)) {
                    // Hiển thị mật khẩu
                    binding.txtNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    binding.btnShowPassword.setImageResource(R.drawable.eye_26); // Biểu tượng mở khóa
                } else {
                    // Ẩn mật khẩu
                    binding.txtNewPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    binding.btnShowPassword.setImageResource(R.drawable.invisible_26); // Biểu tượng khóa
                }
                // Đưa con trỏ về cuối đoạn văn bản
                binding.txtNewPassword.setSelection(binding.txtNewPassword.getText().length());
            }
        });
        //Xử lý đăng xuất tài khoản
        binding.btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Đăng xuất người dùng khỏi Firebase
                FirebaseAuth.getInstance().signOut();

                // Điều hướng người dùng về màn hình đăng nhập (hoặc bất kỳ màn hình nào bạn muốn)
                Intent intent = new Intent(UserFragment.this, LoginActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa tất cả các activity trước đó khỏi stack
                startActivity(intent);
                finish(); // Kết thúc activity hiện tại
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
                                // Hiển thị hộp thoại thông báo thành công
                                new AlertDialog.Builder(UserFragment.this)
                                        .setTitle("Success")
                                        .setMessage("Mật khẩu của bạn đã được thay đổi thành công.")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                // Trả người dùng về màn hình đăng nhập hoặc thực hiện một hành động khác
                                                dialog.dismiss(); // Đóng dialog
                                            }
                                        })
                                        .setIcon(R.drawable.success)
                                        .show();
                            } else {
                                Log.e("Firebase", "Failed to update password", task.getException());
                                // Hiển thị hộp thoại thông báo lỗi
                                new AlertDialog.Builder(UserFragment.this)
                                        .setTitle("Error")
                                        .setMessage("Có lỗi xảy ra khi đổi mật khẩu. Vui lòng thử lại.")
                                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss(); // Đóng dialog
                                            }
                                        })
                                        .setIcon(android.R.drawable.ic_dialog_info)
                                        .show();
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
                        ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                Log.d("Firebase", "File URL: " + imageUrl);

                                saveImageUrlToDatabase(imageUrl);

                                Glide.with(UserFragment.this)
                                        .load(imageUrl)
                                        .into(binding.imvAvatar);

                                // Hiển thị hộp thoại thông báo thành công
                                new AlertDialog.Builder(UserFragment.this)
                                        .setTitle("Cập nhập ảnh đại diện")
                                        .setMessage("Thay đổi ảnh đại diện thành công")
                                        .setPositiveButton(android.R.string.ok, null)
                                        .show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("Firebase", "Failed to get download URL", e);
                                new AlertDialog.Builder(UserFragment.this)
                                        .setTitle("Error")
                                        .setMessage("Thay đổi ảnh đại diện thất bại.")
                                        .setPositiveButton(android.R.string.ok, null)
                                        .show();
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Firebase", "Failed to upload avatar", e);
                        new AlertDialog.Builder(UserFragment.this)
                                .setTitle("Error")
                                .setMessage("Failed to upload avatar.")
                                .setPositiveButton(android.R.string.ok, null)
                                .show();
                    }
                });
    } else {
        Log.e("Firebase", "File path is null");
        new AlertDialog.Builder(UserFragment.this)
                .setTitle("Error")
                .setMessage("File path is null.")
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}


    private void saveImageUrlToDatabase(String imageUrl) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users").child(userId);
        userRef.child("photoUrl").setValue(imageUrl);
    }
}
