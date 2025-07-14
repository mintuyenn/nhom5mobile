package com.example.cuahangtienloi.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuahangtienloi.Activity.DonHangActivity;
import com.example.cuahangtienloi.Activity.KhachHangActivity;
import com.example.cuahangtienloi.Activity.LoaiHangActivity;
import com.example.cuahangtienloi.Activity.NhanVienActivity;
import com.example.cuahangtienloi.Activity.SettingsActivity;
import com.example.cuahangtienloi.Activity.ThongKeActivity;
import com.example.cuahangtienloi.Activity.TonKhoActivity;
import com.example.cuahangtienloi.R;
import com.example.cuahangtienloi.Models.MenuItem;

import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {

    private Context context;
    private List<MenuItem> menuList;

    public MenuAdapter(Context context, List<MenuItem> menuList) {
        this.context = context;
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItem item = menuList.get(position);
        holder.tvTitle.setText(item.getTitle());
        holder.imgIcon.setImageResource(item.getIcon());

        holder.itemView.setOnClickListener(v -> {
            Toast.makeText(context, "Chọn: " + item.getTitle(), Toast.LENGTH_SHORT).show();

            if (item.getTitle().equals("Nhân viên")) {
                Intent intent = new Intent(context, NhanVienActivity.class);
                context.startActivity(intent);
            }
            else if (item.getTitle().equals("Khách hàng")) {
                Intent intent = new Intent(context, KhachHangActivity.class);
                context.startActivity(intent);
            }
            else if (item.getTitle().equals("Loại hàng và sản phẩm")) {
                Intent intent = new Intent(context, LoaiHangActivity.class);
                context.startActivity(intent);
            }
            else if (item.getTitle().equals("Tồn kho")) {
                Intent intent = new Intent(context, TonKhoActivity.class);
                context.startActivity(intent);
            }
            else if (item.getTitle().equals("Cài đặt")) {
                Intent intent = new Intent(context, SettingsActivity.class);
                context.startActivity(intent);
            }
            else if (item.getTitle().equals("Đơn hàng")) {
                Intent intent = new Intent(context, DonHangActivity.class);
                context.startActivity(intent);
            }
            else if (item.getTitle().equals("Thống kê")) {
                Intent intent = new Intent(context, ThongKeActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgIcon;
        TextView tvTitle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            tvTitle = itemView.findViewById(R.id.tvTitle);
        }
    }
}
