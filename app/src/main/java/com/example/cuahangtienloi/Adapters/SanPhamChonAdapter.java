package com.example.cuahangtienloi.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cuahangtienloi.Models.SanPham;
import com.example.cuahangtienloi.Models.SanPhamChon;
import com.example.cuahangtienloi.R;

import java.util.ArrayList;
import java.util.List;

public class SanPhamChonAdapter extends RecyclerView.Adapter<SanPhamChonAdapter.ViewHolder> {

    private Context context;
    private List<SanPhamChon> list;

    public SanPhamChonAdapter(Context context, List<SanPhamChon> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sanpham_chon, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SanPhamChon item = list.get(position);
        SanPham sp = item.getSanPham();

        holder.tvTen.setText("Tên sản phẩm: " + sp.getTensp());
        holder.tvGia.setText(String.format("Giá: %.0f VNĐ", sp.getGia()));
        holder.tvSoLuong.setText("Số lượng: " + item.getSoLuong());

        holder.itemView.setOnClickListener(v -> {
            EditText input = new EditText(context);
            input.setInputType(InputType.TYPE_CLASS_NUMBER);
            input.setHint("Nhập số lượng");

            new AlertDialog.Builder(context)
                    .setTitle("Nhập số lượng")
                    .setView(input)
                    .setPositiveButton("OK", (dialog, which) -> {
                        String text = input.getText().toString().trim();
                        if (text.isEmpty()) {
                            Toast.makeText(context, "Vui lòng nhập số lượng", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        try {
                            int sl = Integer.parseInt(text);
                            if (sl < 0) {
                                Toast.makeText(context, "Số lượng không được âm", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            item.setSoLuong(sl);
                            notifyItemChanged(position);
                        } catch (NumberFormatException e) {
                            Toast.makeText(context, "Số không hợp lệ", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Huỷ", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // Lấy danh sách sản phẩm đã chọn (số lượng > 0)
    public List<SanPhamChon> getSelected() {
        List<SanPhamChon> selected = new ArrayList<>();
        for (SanPhamChon sp : list) {
            if (sp.getSoLuong() > 0) selected.add(sp);
        }
        return selected;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTen, tvGia, tvSoLuong;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTen = itemView.findViewById(R.id.tvTenSP);
            tvGia = itemView.findViewById(R.id.tvGiaSP);
            tvSoLuong = itemView.findViewById(R.id.tvSoLuongSP);
        }
    }
}
