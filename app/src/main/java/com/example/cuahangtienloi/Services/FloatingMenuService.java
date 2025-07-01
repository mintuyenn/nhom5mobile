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

        menuView.findViewById(R.id.imgZalo).setOnClickListener(v -> {
            String zaloLink = "https://zalo.me/id-cuaban"; // Thay bằng ID Zalo hợp lệ
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(zaloLink));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Không mở được Zalo hoặc link không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });


        menuView.findViewById(R.id.imgMessenger).setOnClickListener(v -> {
            String fbUserId = "tuyen.cao.993194";
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("fb-messenger://user-thread/" + fbUserId));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                startActivity(intent);
            } catch (Exception e) {

                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://m.me/" + fbUserId));
                webIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    startActivity(webIntent);
                } catch (Exception ex) {
                    Toast.makeText(this, "Messenger chưa được cài hoặc không hỗ trợ", Toast.LENGTH_SHORT).show();
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
