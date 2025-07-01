package com.example.cuahangtienloi.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuahangtienloi.Adapters.SanPhamAdapter;
import com.example.cuahangtienloi.Database.DatabaseHelper;
import com.example.cuahangtienloi.Models.SanPham;
import com.example.cuahangtienloi.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class SanPhamTheoLoaiActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1001;

    RecyclerView rvSanPhamTheoLoai;
    SanPhamAdapter adapter;
    List<SanPham> list;
    FloatingActionButton fabAddSanPham;

    int loaihang_id;
    private Uri imageUri;
    private ImageView imgSanPhamDialog;
    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sanpham_theo_loai);

        rvSanPhamTheoLoai = findViewById(R.id.rvSanPhamTheoLoai);
        fabAddSanPham = findViewById(R.id.fabAddSanPham);

        loaihang_id = getIntent().getIntExtra("loaihang_id", -1);

        if (loaihang_id != -1) {
            loadData();

            fabAddSanPham.setOnClickListener(v -> {
                Intent intent = new Intent(this, ThemSanPhamActivity.class);
                intent.putExtra("loaihang_id", loaihang_id);
                startActivity(intent);
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (loaihang_id != -1) {
            loadData();
        }
    }

    private void loadData() {
        DatabaseHelper db = DatabaseHelper.getInstance(this);
        list = db.getSanPhamTheoLoai(loaihang_id);
        adapter = new SanPhamAdapter(this, list);
        rvSanPhamTheoLoai.setLayoutManager(new LinearLayoutManager(this));
        rvSanPhamTheoLoai.setAdapter(adapter);

        adapter.setOnItemClickListener(new SanPhamAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(SanPham sp, int position) {
                showDialogSuaSanPham(sp, position);
            }

            @Override
            public void onDeleteClick(SanPham sp) {
                showDialogXoaSanPham(sp);
            }
        });
    }

    private void showDialogSuaSanPham(SanPham sp, int position) {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.sua_sanpham, null);

        EditText edtTenSP = dialogView.findViewById(R.id.edtTenSP);
        EditText edtSoLuong = dialogView.findViewById(R.id.edtSoLuong);
        EditText edtGia = dialogView.findViewById(R.id.edtGia);
        imgSanPhamDialog = dialogView.findViewById(R.id.imgSanPham);
        Button btnChonAnh = dialogView.findViewById(R.id.btnChonAnh);

        edtTenSP.setText(sp.getTensp());
        edtSoLuong.setText(String.valueOf(sp.getSoluong()));
        edtGia.setText(String.valueOf(sp.getGia()));

        imagePath = sp.getHinhanh();
        showImage(imagePath); // gọi hàm dùng chung để hiển thị ảnh

        btnChonAnh.setOnClickListener(v -> openImagePicker());

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setView(dialogView)
                .setTitle("Sửa sản phẩm")
                .setPositiveButton("Cập nhật", null)
                .setNegativeButton("Hủy", null)
                .create();

        dialog.setOnShowListener(d -> {
            Button btnLuu = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
            btnLuu.setOnClickListener(v -> {
                String ten = edtTenSP.getText().toString().trim();
                String slStr = edtSoLuong.getText().toString().trim();
                String giaStr = edtGia.getText().toString().trim();

                if (ten.isEmpty() || slStr.isEmpty() || giaStr.isEmpty()) {
                    Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                int soluong;
                double gia;
                try {
                    soluong = Integer.parseInt(slStr);
                    gia = Double.parseDouble(giaStr);
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Số lượng hoặc giá không hợp lệ", Toast.LENGTH_SHORT).show();
                    return;
                }

                sp.setTensp(ten);
                sp.setSoluong(soluong);
                sp.setGia(gia);
                sp.setHinhanh(imagePath);

                boolean success = DatabaseHelper.getInstance(this).updateSanPham(sp);
                if (success) {
                    Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                    list.set(position, sp);
                    adapter.notifyItemChanged(position);
                    dialog.dismiss();
                } else {
                    Toast.makeText(this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }

    private void showDialogXoaSanPham(SanPham sp) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa sản phẩm \"" + sp.getTensp() + "\" không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    DatabaseHelper db = DatabaseHelper.getInstance(this);
                    boolean deleted = db.deleteSanPham(sp.getId());
                    if (deleted) {
                        Toast.makeText(this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                        loadData();
                    } else {
                        Toast.makeText(this, "Xóa không thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            imagePath = saveImageToInternalStorage(imageUri);
            if (imagePath != null) {
                showImage(imagePath);
            }
        }
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

            return imageFile.getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // ✅ Hiển thị ảnh từ drawable hoặc đường dẫn nội bộ
    private void showImage(String path) {
        if (path != null && !path.isEmpty()) {
            if (path.startsWith("/")) {
                Glide.with(this).load(path).into(imgSanPhamDialog);
            } else {
                int resId = getResources().getIdentifier(path, "drawable", getPackageName());
                if (resId != 0) {
                    imgSanPhamDialog.setImageResource(resId);
                } else {
                    imgSanPhamDialog.setImageResource(R.drawable.ic_launcher_background);
                }
            }
        } else {
            imgSanPhamDialog.setImageResource(R.drawable.ic_launcher_background);
        }
    }
}
