package com.example.cuahangtienloi.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuahangtienloi.Models.DonHang;
import com.example.cuahangtienloi.R;

import java.util.List;

public class DonHangAdapter extends RecyclerView.Adapter<DonHangAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(DonHang donHang);
    }

    public interface OnItemDeleteClickListener {
        void onItemDeleteClick(DonHang donHang, int position);
    }

    public interface OnItemEditClickListener {
        void onItemEditClick(DonHang donHang, int position);
    }

    private Context context;
    private List<DonHang> list;
    private OnItemClickListener listener;
    private OnItemDeleteClickListener deleteListener;
    private OnItemEditClickListener editListener;

    public DonHangAdapter(Context context, List<DonHang> list) {
        this.context = context;
        this.list = list;
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        listener = l;
    }

    public void setOnItemDeleteClickListener(OnItemDeleteClickListener l) {
        this.deleteListener = l;
    }

    public void setOnItemEditClickListener(OnItemEditClickListener l) {
        this.editListener = l;
    }

    @NonNull
    @Override
    public DonHangAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_donhang, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DonHangAdapter.ViewHolder holder, int position) {
        DonHang dh = list.get(position);
        holder.tvId.setText("Đơn hàng #" + dh.getId());
        holder.tvTenKhachHang.setText(dh.getTenKhachHang());
        holder.tvNgay.setText(dh.getNgaylap());
        holder.tvTongTien.setText(String.format("%.0f VNĐ", dh.getTongtien()));
        holder.tvTrangThai.setText(dh.getTrangthai());

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(dh);
            }
        });

        holder.btnXoa.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onItemDeleteClick(dh, holder.getAdapterPosition());
            }
        });

        holder.btnSua.setOnClickListener(v -> {
            if (editListener != null) {
                editListener.onItemEditClick(dh, holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    // Xóa theo vị trí
    public void removeItem(int position) {
        if (position >= 0 && position < list.size()) {
            list.remove(position);
            notifyItemRemoved(position);
        }
    }

    // Xóa theo đối tượng DonHang
    public void removeItem(DonHang donHang) {
        int position = list.indexOf(donHang);
        if (position != -1) {
            removeItem(position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvId, tvTenKhachHang, tvNgay, tvTongTien, tvTrangThai;
        Button btnXoa, btnSua;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tvDonHangId);
            tvTenKhachHang = itemView.findViewById(R.id.tvTenKhachHang);
            tvNgay = itemView.findViewById(R.id.tvNgayLap);
            tvTongTien = itemView.findViewById(R.id.tvTongTien);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            btnXoa = itemView.findViewById(R.id.btnXoa);
            btnSua = itemView.findViewById(R.id.btnSua);
        }
    }
}
