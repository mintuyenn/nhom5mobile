package com.example.cuahangtienloi.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.cuahangtienloi.Database.DatabaseHelper;
import com.example.cuahangtienloi.R;

public class ProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1001;

    EditText edtName, edtPhone, edtAddress;
    EditText edtOldPass, edtNewPass;
    Button btnUpdate, btnSelectImage;
    ImageView imgAvatar;

    Uri selectedImageUri = null;
    String username;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        dbHelper = new DatabaseHelper(this);

        username = getIntent().getStringExtra("username");
        if (username == null || username.isEmpty()) {
            Toast.makeText(this, "Không có thông tin người dùng", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mappingViews();
        loadUserInfo();

        btnSelectImage.setOnClickListener(v -> chooseImageFromGallery());

        btnUpdate.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String phone = edtPhone.getText().toString().trim();
            String address = edtAddress.getText().toString().trim();

            boolean updated = dbHelper.updateUserInfo(username, name, phone, address);

            String oldPass = edtOldPass.getText().toString();
            String newPass = edtNewPass.getText().toString();
            boolean passwordUpdated = true;
            if (!oldPass.isEmpty() && !newPass.isEmpty()) {
                passwordUpdated = dbHelper.updatePassword(username, oldPass, newPass);
            }

            boolean imageUpdated = true;
            if (selectedImageUri != null) {
                try {
                    getContentResolver().takePersistableUriPermission(
                            selectedImageUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    );
                } catch (SecurityException e) {
                    e.printStackTrace();
                    // Có thể thông báo cho người dùng biết quyền không được cấp
                }

                imageUpdated = dbHelper.updateAvatarString(username, "uri:" + selectedImageUri.toString());
            }

            if (updated && passwordUpdated && imageUpdated) {
                Toast.makeText(this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                loadUserInfo(); // Load lại ảnh mới sau khi cập nhật
            } else {
                Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void mappingViews() {
        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtAddress = findViewById(R.id.edtAddress);
        edtOldPass = findViewById(R.id.edtOldPassword);
        edtNewPass = findViewById(R.id.edtNewPassword);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnSelectImage = findViewById(R.id.btnSelectImage);
        imgAvatar = findViewById(R.id.imgAvatar);
    }

    private void loadUserInfo() {
        String[] info = dbHelper.getUserInfo(username);
        if (info != null) {
            edtName.setText(info[0]);
            edtPhone.setText(info[1]);
            edtAddress.setText(info[2]);
        }

        String avatarString = dbHelper.getAvatarString(username);

        if (avatarString != null && !avatarString.isEmpty()) {
            if (avatarString.startsWith("drawable:")) {
                String drawableName = avatarString.substring("drawable:".length());
                int resId = getResources().getIdentifier(drawableName, "drawable", getPackageName());
                if (resId != 0) {
                    Glide.with(this)
                            .load(resId)
                            .placeholder(R.drawable.ic_person)
                            .error(R.drawable.ic_person)
                            .into(imgAvatar);
                } else {
                    imgAvatar.setImageResource(R.drawable.ic_person);
                }
            } else if (avatarString.startsWith("uri:")) {
                try {
                    Uri avatarUri = Uri.parse(avatarString.substring("uri:".length()));
                    Glide.with(this)
                            .load(avatarUri)
                            .placeholder(R.drawable.ic_person)
                            .error(R.drawable.ic_person)
                            .into(imgAvatar);
                } catch (Exception e) {
                    e.printStackTrace();
                    imgAvatar.setImageResource(R.drawable.ic_person);
                }
            } else {
                imgAvatar.setImageResource(R.drawable.ic_person);
            }
        } else {
            imgAvatar.setImageResource(R.drawable.ic_person);
        }
    }

    private void chooseImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    getContentResolver().takePersistableUriPermission(
                            selectedImageUri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    );
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                Glide.with(this)
                        .load(selectedImageUri)
                        .placeholder(R.drawable.ic_person)
                        .error(R.drawable.ic_person)
                        .into(imgAvatar);
            }
        }
    }
}
