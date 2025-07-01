package com.example.cuahangtienloi.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.cuahangtienloi.Adapters.ImageSliderAdapter;
import com.example.cuahangtienloi.Adapters.MenuAdapter;
import com.example.cuahangtienloi.Database.DatabaseHelper;
import com.example.cuahangtienloi.Models.MenuItem;
import com.example.cuahangtienloi.R;
import com.example.cuahangtienloi.Services.FloatingMenuService;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_OVERLAY_PERMISSION = 1234;
    private String username; // ✅ Khai báo biến thành viên

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ImageView imgMenu;

    private DatabaseHelper dbHelper;
    RecyclerView recyclerMenu;
    MenuAdapter adapter;
    List<MenuItem> menuList;

    ViewPager2 viewPagerImages;
    ImageSliderAdapter sliderAdapter;
    int[] imageResArray = {
            R.drawable.main1,
            R.drawable.main2,
            R.drawable.main3
    };

    private Handler sliderHandler = new Handler();
    private Runnable sliderRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ✅ Lấy username từ LoginActivity
        username = getIntent().getStringExtra("username");
        if (username == null) username = "";

        // Khởi tạo database helper
        dbHelper = new DatabaseHelper(this);
        dbHelper.getWritableDatabase();

        // Lấy quyền nhân viên từ database
        String quyen = dbHelper.getQuyenNhanVien(username);
        boolean isAdmin = "admin".equalsIgnoreCase(quyen);

        // Ánh xạ menu drawer
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        imgMenu = findViewById(R.id.imgMenu);

        imgMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Toast.makeText(this, "Trang chủ", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.nav_profile) {
                // ✅ Truyền username sang ProfileActivity
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
            } else if (id == R.id.nav_logout) {
                Toast.makeText(this, "Đăng xuất thành công", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Setup menu RecyclerView
        recyclerMenu = findViewById(R.id.recyclerMenu);
        menuList = new ArrayList<>();

        menuList.add(new MenuItem("Khách hàng", R.drawable.ic_khachhang));
        menuList.add(new MenuItem("Tồn kho", R.drawable.ic_tonkho));
        if (isAdmin) {
            menuList.add(new MenuItem("Thống kê", R.drawable.ic_thongke));
            menuList.add(new MenuItem("Nhân viên", R.drawable.ic_nhanvien));
        }
        menuList.add(new MenuItem("Loại hàng và sản phẩm", R.drawable.ic_loaihang));
        menuList.add(new MenuItem("Đơn hàng", R.drawable.ic_donhang));
        menuList.add(new MenuItem("Cài đặt", R.drawable.ic_caidat));

        adapter = new MenuAdapter(this, menuList);
        recyclerMenu.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerMenu.setAdapter(adapter);

        // Setup ViewPager2 slider ảnh
        viewPagerImages = findViewById(R.id.viewPagerImages);
        sliderAdapter = new ImageSliderAdapter(this, imageResArray);
        viewPagerImages.setAdapter(sliderAdapter);

        sliderRunnable = () -> {
            int current = viewPagerImages.getCurrentItem();
            int next = (current + 1) % imageResArray.length;
            viewPagerImages.setCurrentItem(next, true);
            sliderHandler.postDelayed(sliderRunnable, 3000);
        };
        sliderHandler.postDelayed(sliderRunnable, 3000);

        viewPagerImages.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 3000);
            }
        });

        checkOverlayPermissionAndStartService();
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkOverlayPermissionAndStartService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopService(new Intent(this, FloatingMenuService.class));
    }

    private void checkOverlayPermissionAndStartService() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, REQUEST_CODE_OVERLAY_PERMISSION);
            } else {
                startFloatingMenuService();
            }
        } else {
            startFloatingMenuService();
        }
    }

    private void startFloatingMenuService() {
        startService(new Intent(this, FloatingMenuService.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_OVERLAY_PERMISSION) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.canDrawOverlays(this)) {
                    Toast.makeText(this, "Đã cấp quyền hiển thị trên ứng dụng khác", Toast.LENGTH_SHORT).show();
                    startFloatingMenuService();
                } else {
                    Toast.makeText(this, "Bạn cần cấp quyền để bật menu nổi", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sliderHandler.removeCallbacks(sliderRunnable);
    }
}
