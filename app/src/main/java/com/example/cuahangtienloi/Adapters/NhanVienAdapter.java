package com.example.cuahangtienloi.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cuahangtienloi.Models.NhanVien;
import com.example.cuahangtienloi.R;

import java.util.List;

public class NhanVienAdapter extends RecyclerView.Adapter<NhanVienAdapter.ViewHolder> {
    private Context context;
    private List<NhanVien> list;
    private OnNhanVienClickListener listener;

    public NhanVienAdapter(Context context, List<NhanVien> list, OnNhanVienClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public void setData(List<NhanVien> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    public interface OnNhanVienClickListener {
        void onSuaClick(NhanVien nv);
        void onXoaClick(NhanVien nv);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View btnXoa, btnSua;
        ImageView imgNhanVien;
        TextView tvTen, tvTuoi, tvDiaChi;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgNhanVien = itemView.findViewById(R.id.imgNhanVien);
            tvTen = itemView.findViewById(R.id.tvTenNV);
            tvTuoi = itemView.findViewById(R.id.tvTuoiNV);
            tvDiaChi = itemView.findViewById(R.id.tvDiachiNV);
            btnSua = itemView.findViewById(R.id.btnSua);
            btnXoa = itemView.findViewById(R.id.btnXoa);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_nhanvien, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NhanVien nv = list.get(position);
        holder.tvTen.setText(nv.getTen());
        holder.tvTuoi.setText("Tuổi: " + nv.getTuoi());
        holder.tvDiaChi.setText("Địa chỉ: " + nv.getDiachi());

        holder.btnSua.setOnClickListener(v -> {
            if (listener != null) listener.onSuaClick(nv);
        });

        holder.btnXoa.setOnClickListener(v -> {
            if (listener != null) listener.onXoaClick(nv);
        });

        // Xử lý hiển thị ảnh
        String imageString = nv.getHinhanh();

        if (imageString != null && !imageString.isEmpty()) {
            if (imageString.startsWith("drawable:")) {
                // Ảnh drawable
                String drawableName = imageString.substring("drawable:".length());
                int resId = context.getResources().getIdentifier(drawableName, "drawable", context.getPackageName());
                if (resId != 0) {
                    holder.imgNhanVien.setImageResource(resId);
                } else {
                    holder.imgNhanVien.setImageResource(R.drawable.ic_person);
                }
            } else if (imageString.startsWith("uri:")) {
                // Ảnh URI (đường dẫn content://)
                try {
                    Uri uri = Uri.parse(imageString.substring("uri:".length()));
                    Glide.with(context)
                            .load(uri)
                            .placeholder(R.drawable.ic_person)
                            .error(R.drawable.ic_person)
                            .into(holder.imgNhanVien);
                } catch (Exception e) {
                    e.printStackTrace();
                    holder.imgNhanVien.setImageResource(R.drawable.ic_person);
                }
            } else {
                // Nếu chuỗi không có tiền tố, thử coi như Uri bình thường
                try {
                    Uri uri = Uri.parse(imageString);
                    Glide.with(context)
                            .load(uri)
                            .placeholder(R.drawable.ic_person)
                            .error(R.drawable.ic_person)
                            .into(holder.imgNhanVien);
                } catch (Exception e) {
                    e.printStackTrace();
                    holder.imgNhanVien.setImageResource(R.drawable.ic_person);
                }
            }
        } else {
            // Ảnh mặc định nếu không có dữ liệu
            holder.imgNhanVien.setImageResource(R.drawable.ic_person);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
