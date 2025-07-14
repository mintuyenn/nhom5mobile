package com.example.cuahangtienloi.Services;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.content.pm.PackageManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.cuahangtienloi.R;

public class FloatingMenuService extends Service {

    private WindowManager windowManager;
    private View menuView;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        int layoutFlag;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutFlag = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutFlag = WindowManager.LayoutParams.TYPE_PHONE;
        }

        WindowManager.LayoutParams paramsMenu = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutFlag,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        paramsMenu.gravity = Gravity.BOTTOM | Gravity.END;
        paramsMenu.x = 50;
        paramsMenu.y = 100;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        LayoutInflater inflater = LayoutInflater.from(this);
        menuView = inflater.inflate(R.layout.layout_floating_menu, null);

        windowManager.addView(menuView, paramsMenu);

        menuView.findViewById(R.id.imgCall).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:0779922122"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        // ZALO
        menuView.findViewById(R.id.imgZalo).setOnClickListener(v -> {
            PackageManager pm = getApplicationContext().getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage("com.zing.zalo");

            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                // Nếu Zalo chưa cài, mở trên Google Play
                Intent playIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.zing.zalo"));
                playIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    startActivity(playIntent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Không thể mở Zalo hoặc CH Play", Toast.LENGTH_SHORT).show();
                }
            }
        });

// MESSENGER
        menuView.findViewById(R.id.imgMessenger).setOnClickListener(v -> {
            PackageManager pm = getApplicationContext().getPackageManager();
            Intent intent = pm.getLaunchIntentForPackage("com.facebook.orca");

            if (intent != null) {
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                // Nếu Messenger chưa cài, mở trên Google Play
                Intent playIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.facebook.orca"));
                playIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    startActivity(playIntent);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Không thể mở Messenger hoặc CH Play", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (menuView != null && windowManager != null) {
            windowManager.removeView(menuView);
        }
    }
}
