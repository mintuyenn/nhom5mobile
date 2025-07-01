package com.example.cuahangtienloi.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.Build;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cuahangtienloi.Adapters.KhachHangAdapter;
import com.example.cuahangtienloi.Database.DatabaseHelper;
import com.example.cuahangtienloi.Models.KhachHang;
import com.example.cuahangtienloi.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class KhachHangActivity extends AppCompatActivity {

    RecyclerView rvKhachHang;
    FloatingActionButton fabAddKhachHang;

    KhachHangAdapter adapter;
    DatabaseHelper dbHelper;
    List<KhachHang> listKhachHang;

    // Uri ảnh được chọn trong dialog thêm khách hàng
    private Uri selectedImageUri = null;
    private ImageView currentImgAvatar = null;

    private ActivityResultLauncher<Intent> pickImageLauncher;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khachhang);

        rvKhachHang = findViewById(R.id.rvKhachHang);
        fabAddKhachHang = findViewById(R.id.fabAddKhachHang);

        dbHelper = new DatabaseHelper(this);

        listKhachHang = dbHelper.getAllKhachHang();
        adapter = new KhachHangAdapter(this, listKhachHang, new KhachHangAdapter.OnItemClickListener() {
            @Override
            public void onSuaClick(KhachHang khachHang, int position) {
                showDialogSuaKhachHang(khachHang, position);
            }

            @Override
            public void onXoaClick(KhachHang khachHang, int position) {
                confirmXoaKhachHang(khachHang, position);
            }
        });
        rvKhachHang.setLayoutManager(new LinearLayoutManager(this));
        rvKhachHang.setAdapter(adapter);

        // Khởi tạo launcher chọn ảnh từ thư viện
        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        selectedImageUri = result.getData().getData();
                        if (currentImgAvatar != null && selectedImageUri != null) {
                            Glide.with(this).load(selectedImageUri).into(currentImgAvatar);
                        }
                    }
                }
        );

        fabAddKhachHang.setOnClickListener(v -> showDialogThemKhachHang());
    }

    private void showDialogThemKhachHang() {
        View dialogView = getLayoutInflater().inflate(R.layout.them_khachhang, null);

        EditText edtTen = dialogView.findViewById(R.id.edtTen);
        EditText edtSDT = dialogView.findViewById(R.id.edtSDT);
        EditText edtDiaChi = dialogView.findViewById(R.id.edtDiaChi);
        EditText edtEmail = dialogView.findViewById(R.id.edtEmail);
        Button btnLuu = dialogView.findViewById(R.id.btnLuuKhachHang);
        Button btnChonAnh = dialogView.findViewById(R.id.btnChonAnh);
        currentImgAvatar = dialogView.findViewById(R.id.imgAvatar);

        selectedImageUri = null; // Reset ảnh khi mở dialog

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        btnChonAnh.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Android 13+ (API 33): dùng READ_MEDIA_IMAGES
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_MEDIA_IMAGES}, 100);
                } else {
                    openImagePicker();
                }
            } else {
                // Android < 13: dùng READ_EXTERNAL_STORAGE
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 100);
                } else {
                    openImagePicker();
                }
            }
        });

        btnLuu.setOnClickListener(v -> {
            String ten = edtTen.getText().toString().trim();
            String sdt = edtSDT.getText().toString().trim();
            String diachi = edtDiaChi.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();

            if (ten.isEmpty() || sdt.isEmpty() || diachi.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ tên, số điện thoại và địa chỉ", Toast.LENGTH_SHORT).show();
                return;
            }

            String ngaytao = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

            String hinhanh = selectedImageUri != null ? selectedImageUri.toString() : null;

            KhachHang kh = new KhachHang(0, ten, sdt, diachi, email, ngaytao, hinhanh);

            boolean success = dbHelper.insertKhachHang(kh);
            if (success) {
                Toast.makeText(this, "Câp nhật thông tin khách hàng thành công", Toast.LENGTH_SHORT).show();
                // Load lại dữ liệu và cập nhật adapter
                listKhachHang.clear();
                listKhachHang.addAll(dbHelper.getAllKhachHang());
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Cập nhật thông tin khách hàng thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        Intent chooser = Intent.createChooser(intent, "Chọn ảnh");
        pickImageLauncher.launch(chooser);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "Cần cấp quyền truy cập ảnh để chọn ảnh", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private void showDialogSuaKhachHang(KhachHang kh, int position) {
        View dialogView = getLayoutInflater().inflate(R.layout.sua_khachhang, null);

        EditText edtTen = dialogView.findViewById(R.id.edtTen);
        EditText edtSDT = dialogView.findViewById(R.id.edtSDT);
        EditText edtDiaChi = dialogView.findViewById(R.id.edtDiaChi);
        EditText edtEmail = dialogView.findViewById(R.id.edtEmail);
        Button btnLuu = dialogView.findViewById(R.id.btnLuuKhachHang);
        Button btnChonAnh = dialogView.findViewById(R.id.btnChonAnh);
        currentImgAvatar = dialogView.findViewById(R.id.imgAvatar);

        // Set dữ liệu hiện tại lên dialog
        edtTen.setText(kh.getTen());
        edtSDT.setText(kh.getSdt());
        edtDiaChi.setText(kh.getDiachi());
        edtEmail.setText(kh.getEmail());

        // Load ảnh nếu có
        if (kh.getHinhanh() != null && !kh.getHinhanh().isEmpty()) {
            Glide.with(this).load(Uri.parse(kh.getHinhanh())).into(currentImgAvatar);
            selectedImageUri = Uri.parse(kh.getHinhanh());
        } else {
            currentImgAvatar.setImageResource(R.drawable.ic_person);
            selectedImageUri = null;
        }

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .create();

        btnChonAnh.setOnClickListener(v -> {
            // Tương tự mở picker ảnh như thêm
            openImagePicker();
        });

        btnLuu.setOnClickListener(v -> {
            String ten = edtTen.getText().toString().trim();
            String sdt = edtSDT.getText().toString().trim();
            String diachi = edtDiaChi.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();

            if (ten.isEmpty() || sdt.isEmpty() || diachi.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ tên, số điện thoại và địa chỉ", Toast.LENGTH_SHORT).show();
                return;
            }

            String hinhanh = selectedImageUri != null ? selectedImageUri.toString() : null;

            // Cập nhật thông tin khách hàng
            kh.setTen(ten);
            kh.setSdt(sdt);
            kh.setDiachi(diachi);
            kh.setEmail(email);
            kh.setHinhanh(hinhanh);

            boolean success = dbHelper.updateKhachHang(kh);  // Bạn cần thêm hàm update trong DatabaseHelper
            if (success) {
                Toast.makeText(this, "Cập nhật khách hàng thành công", Toast.LENGTH_SHORT).show();
                listKhachHang.set(position, kh);
                adapter.notifyItemChanged(position);
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }
    private void confirmXoaKhachHang(KhachHang kh, int position) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa khách hàng")
                .setMessage("Bạn có chắc muốn xóa khách hàng này không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    boolean success = dbHelper.deleteKhachHang(kh.getId()); // Bạn cần thêm hàm xóa trong DatabaseHelper
                    if (success) {
                        Toast.makeText(this, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        listKhachHang.remove(position);
                        adapter.notifyItemRemoved(position);
                    } else {
                        Toast.makeText(this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }



}
