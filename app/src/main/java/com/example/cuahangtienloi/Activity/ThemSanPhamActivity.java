package com.example.cuahangtienloi.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.cuahangtienloi.Database.DatabaseHelper;
import com.example.cuahangtienloi.Models.SanPham;
import com.example.cuahangtienloi.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ThemSanPhamActivity extends AppCompatActivity {

    EditText edtTenSP, edtSoLuong, edtGia, edtDonVi;
    ImageView imgHinhSP;
    Button btnChonAnh, btnLuu;

    Uri selectedImageUri = null;
    String imagePath = null;
    int loaihang_id;

    ActivityResultLauncher<Intent> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_sanpham);

        // Ánh xạ View
        edtTenSP = findViewById(R.id.edtTenSP);
        edtSoLuong = findViewById(R.id.edtSoLuong);
        edtGia = findViewById(R.id.edtGia);
        edtDonVi = findViewById(R.id.edtDonVi);
        imgHinhSP = findViewById(R.id.imgHinhSP);
        btnChonAnh = findViewById(R.id.btnChonAnh);
        btnLuu = findViewById(R.id.btnLuu);

        // Nhận ID loại hàng
        loaihang_id = getIntent().getIntExtra("loaihang_id", -1);

        // Khởi tạo launcher để chọn ảnh
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        imagePath = saveImageToInternalStorage(selectedImageUri);
                        if (imagePath != null) {
                            Glide.with(this).load(imagePath).into(imgHinhSP);
                        }
                    }
                }
        );

        btnChonAnh.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickImageLauncher.launch(intent);
        });

        btnLuu.setOnClickListener(v -> {
            String tenSP = edtTenSP.getText().toString().trim();
            String soLuongStr = edtSoLuong.getText().toString().trim();
            String giaStr = edtGia.getText().toString().trim();
            String donVi = edtDonVi.getText().toString().trim();

            if (tenSP.isEmpty() || soLuongStr.isEmpty() || giaStr.isEmpty() || donVi.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            int soLuong;
            double gia;

            try {
                soLuong = Integer.parseInt(soLuongStr);
                gia = Double.parseDouble(giaStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Số lượng và giá phải là số hợp lệ", Toast.LENGTH_SHORT).show();
                return;
            }

            SanPham sp = new SanPham(
                    0,              // ID tự tăng
                    tenSP,          // Tên sản phẩm
                    gia,            // Giá
                    soLuong,        // Số lượng
                    donVi,          // Đơn vị
                    loaihang_id,    // ID loại hàng
                    imagePath       // Đường dẫn hình ảnh
            );

            DatabaseHelper db = DatabaseHelper.getInstance(this);
            boolean success = db.insertSanPham(sp);

            if (success) {
                Toast.makeText(this, "Thêm sản phẩm thành công", Toast.LENGTH_SHORT).show();
                finish(); // Quay lại màn hình trước
            } else {
                Toast.makeText(this, "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String saveImageToInternalStorage(Uri uri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            File imageFile = new File(getFilesDir(), "sp_" + System.currentTimeMillis() + ".jpg");
            FileOutputStream outputStream = new FileOutputStream(imageFile);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }

            outputStream.close();
            inputStream.close();

            return imageFile.getAbsolutePath(); // Trả về đường dẫn file ảnh
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
