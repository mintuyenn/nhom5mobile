package com.example.cuahangtienloi.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.cuahangtienloi.Models.NhanVien;
import com.example.cuahangtienloi.Models.KhachHang;
import com.example.cuahangtienloi.Models.LoaiHang;
import com.example.cuahangtienloi.Models.SanPham;
import com.example.cuahangtienloi.Models.ChiTietDonHang;
import com.example.cuahangtienloi.Models.DonHang;
import com.example.cuahangtienloi.Models.ThongKeDoanhThu;
import java.util.ArrayList;
import java.util.List;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.util.Locale;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import android.database.Cursor;
public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "cuahangtienloi.db";
    public static final int DB_VERSION = 2;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

        // Bảng nhân viên
        db.execSQL("CREATE TABLE nhanvien (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ten TEXT NOT NULL, " +
                "username TEXT UNIQUE NOT NULL, " +
                "password TEXT NOT NULL, " +
                "quyen TEXT NOT NULL, " +
                "diachi TEXT, " +
                "sodienthoai TEXT, " +
                "gioitinh TEXT, " +
                "tuoi INTEGER, " +
                "hinhanh TEXT" +
                ");");
        // Bảng khách hàng
        db.execSQL("CREATE TABLE khachhang (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ten TEXT NOT NULL, " +
                "sdt TEXT NOT NULL UNIQUE, " +
                "diachi TEXT, " +
                "email TEXT, " +
                "ngaytao TEXT," +
                "hinhanh TEXT" +
                ");");

        // Bảng loại hàng
        db.execSQL("CREATE TABLE loaihang (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tenloai TEXT NOT NULL UNIQUE" +
                ");");

        // Bảng sản phẩm
        db.execSQL("CREATE TABLE sanpham (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "tensp TEXT NOT NULL, " +
                "gia REAL NOT NULL, " +
                "soluong INTEGER NOT NULL, " +
                "donvi TEXT, " +
                "loaihang_id INTEGER, " +
                "hinhanh TEXT, " +
                "FOREIGN KEY(loaihang_id) REFERENCES loaihang(id)" +
                ");");

        // Bảng đơn hàng
        db.execSQL("CREATE TABLE donhang (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "ngaylap TEXT NOT NULL, " +
                "tongtien REAL NOT NULL, " +
                "khachhang_id INTEGER NOT NULL, " +
                "ghichu TEXT, " +
                "trangthai TEXT DEFAULT 'Đang xử lý', " +
                "FOREIGN KEY(khachhang_id) REFERENCES khachhang(id)" +
                ");");

        // Bảng chi tiết đơn hàng
        db.execSQL("CREATE TABLE chitiet_donhang (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "donhang_id INTEGER NOT NULL, " +
                "sanpham_id INTEGER NOT NULL, " +
                "soluong INTEGER NOT NULL, " +
                "dongia REAL NOT NULL, " +
                "FOREIGN KEY(donhang_id) REFERENCES donhang(id), " +
                "FOREIGN KEY(sanpham_id) REFERENCES sanpham(id)" +
                ");");



        db.execSQL("INSERT INTO nhanvien (ten, username, password, quyen, diachi, sodienthoai, gioitinh, tuoi, hinhanh) VALUES " +
                "('Cao Minh Tuyền', 'Minhtuyen', 'minhtuyen123', 'admin', '123 Đường ABC, Quận 1', '0912345678', 'Nam', 20, 'tuyen');");


        // Loại hàng mẫu
        db.execSQL("INSERT INTO loaihang (tenloai) VALUES " +
                "('Đồ uống'), ('Lương thực'), ('Thực phẩm'), ('Gia vị');");

        // Khách hàng mẫu
        db.execSQL("INSERT INTO khachhang (ten, sdt, diachi, email, ngaytao, hinhanh) VALUES " +
                "('Nguyễn Văn A', '0912345678', '123 Đường A, Quận 1', 'vana@gmail.com', '2025-06-13', 'kh1')," +
                "('Trần Thị B', '0987654321', '456 Đường B, Quận 2', 'thib@gmail.com', '2025-06-13', 'kh2');");


        db.execSQL("INSERT INTO sanpham (tensp, gia, soluong, donvi, loaihang_id, hinhanh) VALUES " +
                "('Gạo thơm', 18000, 50, 'kg', 2, 'gao')," +
                "('Nước suối', 5000, 100, 'chai', 1, 'nuoc')," +
                "('Bánh mì ', 20000, 30, 'ổ', 3, 'banhmi');");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS chitiet_donhang;");
        db.execSQL("DROP TABLE IF EXISTS donhang;");
        db.execSQL("DROP TABLE IF EXISTS sanpham;");
        db.execSQL("DROP TABLE IF EXISTS loaihang;");
        db.execSQL("DROP TABLE IF EXISTS khachhang;");
        db.execSQL("DROP TABLE IF EXISTS nhanvien;");
        onCreate(db);
    }

    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT 1 FROM NhanVien WHERE username = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        boolean exists = cursor.moveToFirst(); // nếu có kết quả tức username đã tồn tại
        cursor.close();
        return exists;
    }
    public boolean deleteNhanVien(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("nhanvien", "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }
    public boolean updateNhanVien(NhanVien nv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ten", nv.getTen());
        values.put("diachi", nv.getDiachi());
        values.put("tuoi", nv.getTuoi());
        // Có thể thêm các cột khác nếu bạn lưu chúng

        int rows = db.update("nhanvien", values, "id = ?", new String[]{String.valueOf(nv.getId())});
        return rows > 0;
    }
    public List<KhachHang> getAllKhachHang() {
        List<KhachHang> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM khachhang", null);
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String ten = cursor.getString(cursor.getColumnIndexOrThrow("ten"));
                String sdt = cursor.getString(cursor.getColumnIndexOrThrow("sdt"));
                String diachi = cursor.getString(cursor.getColumnIndexOrThrow("diachi"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                String ngaytao = cursor.getString(cursor.getColumnIndexOrThrow("ngaytao"));
                String hinhanh = cursor.getString(cursor.getColumnIndexOrThrow("hinhanh"));

                KhachHang kh = new KhachHang(id, ten, sdt, diachi, email, ngaytao, hinhanh);
                list.add(kh);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }
    public boolean insertKhachHang(KhachHang kh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ten", kh.getTen());
        values.put("sdt", kh.getSdt());
        values.put("diachi", kh.getDiachi());
        values.put("email", kh.getEmail());
        values.put("ngaytao", kh.getNgaytao());
        values.put("hinhanh", kh.getHinhanh());
        long result = db.insert("khachhang", null, values);
        return result != -1;
    }
    public boolean updateKhachHang(KhachHang kh) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("ten", kh.getTen());
        values.put("sdt", kh.getSdt());
        values.put("diachi", kh.getDiachi());
        values.put("email", kh.getEmail());
        values.put("hinhanh", kh.getHinhanh());
        // Thường không cập nhật 'ngaytao' khi sửa thông tin

        // Cập nhật theo id
        int result = db.update("khachhang", values, "id=?", new String[]{String.valueOf(kh.getId())});

        return result > 0;  // true nếu cập nhật thành công, false nếu không
    }
    public boolean deleteKhachHang(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("khachhang", "id=?", new String[]{String.valueOf(id)});
        return result > 0; // true nếu xóa thành công (có ít nhất 1 bản ghi bị xóa)
    }
    public List<LoaiHang> getAllLoaiHang() {
        List<LoaiHang> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM LoaiHang", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String ten = cursor.getString(1);
            list.add(new LoaiHang(id, ten));
        }
        cursor.close();
        return list;
    }
    public boolean insertLoaiHang(LoaiHang loaiHang) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenLoai", loaiHang.getTenloai());
        long result = db.insert("loaihang", null, values);
        return result != -1;
    }

    public int demSanPhamTheoLoai(int loaihang_id) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM SanPham WHERE loaihang_id = ?", new String[]{String.valueOf(loaihang_id)});
        int count = 0;
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();
        return count;
    }
    public List<SanPham> getSanPhamTheoLoai(int idLoai) {
        List<SanPham> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "SELECT * FROM sanpham WHERE loaihang_id = ?";

        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(idLoai)});
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String ten = cursor.getString(cursor.getColumnIndexOrThrow("tensp"));
                double gia = cursor.getDouble(cursor.getColumnIndexOrThrow("gia"));
                int soluong = cursor.getInt(cursor.getColumnIndexOrThrow("soluong"));
                String donvi = cursor.getString(cursor.getColumnIndexOrThrow("donvi"));
                int loaihang_id = cursor.getInt(cursor.getColumnIndexOrThrow("loaihang_id"));
                String hinhanh = cursor.getString(cursor.getColumnIndexOrThrow("hinhanh"));

                SanPham sp = new SanPham(id, ten, gia, soluong, donvi, loaihang_id, hinhanh);
                list.add(sp);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return list;
    }
    public boolean insertSanPham(SanPham sp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tensp", sp.getTensp());
        values.put("soluong", sp.getSoluong());
        values.put("gia", sp.getGia());
        values.put("loaihang_id", sp.getLoaihang_id());
        values.put("donvi", sp.getDonvi());
        values.put("hinhanh", sp.getHinhanh());

        long result = db.insert("sanpham", null, values);
        return result != -1;
    }

    public boolean deleteLoaiHang(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("loaihang", "id = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    public boolean updateSanPham(SanPham sp) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tensp", sp.getTensp());
        values.put("soluong", sp.getSoluong());
        values.put("gia", sp.getGia());
        values.put("hinhanh", sp.getHinhanh());
        values.put("loaihang_id", sp.getLoaihang_id());

        int rows = db.update("sanpham", values, "id = ?", new String[]{String.valueOf(sp.getId())});
        db.close();
        return rows > 0;
    }

    public boolean deleteSanPham(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rows = db.delete("sanpham", "id = ?", new String[]{String.valueOf(id)});
        db.close();
        return rows > 0;
    }

    public List<SanPham> getAllSanPham() {
        List<SanPham> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM sanpham", null);

        if (cursor != null && cursor.moveToFirst()) {
            int idxId = cursor.getColumnIndex("id");
            int idxTenSP = cursor.getColumnIndex("tensp");
            int idxGia = cursor.getColumnIndex("gia");
            int idxSoLuong = cursor.getColumnIndex("soluong");
            int idxDonVi = cursor.getColumnIndex("donvi"); // có thể = -1
            int idxLoaiHang = cursor.getColumnIndex("loaihang_id");
            int idxHinhAnh = cursor.getColumnIndex("hinhanh");

            do {
                int id = idxId != -1 ? cursor.getInt(idxId) : 0;
                String tensp = idxTenSP != -1 ? cursor.getString(idxTenSP) : "";
                double gia = idxGia != -1 ? cursor.getDouble(idxGia) : 0.0;
                int soluong = idxSoLuong != -1 ? cursor.getInt(idxSoLuong) : 0;
                String donvi = idxDonVi != -1 ? cursor.getString(idxDonVi) : "";
                int loaihang_id = idxLoaiHang != -1 ? cursor.getInt(idxLoaiHang) : 0;
                String hinhanh = idxHinhAnh != -1 ? cursor.getString(idxHinhAnh) : "";

                SanPham sp = new SanPham(id, tensp, gia, soluong, donvi, loaihang_id, hinhanh);
                list.add(sp);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return list;
    }
    public boolean clearSanPhamTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        int deletedRows = db.delete("sanpham", null, null);
        return deletedRows >= 0;
    }

    public List<ChiTietDonHang> getChiTietDonHangTheoDonHangId(int donhangId) {
        List<ChiTietDonHang> list = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String sql = "SELECT ctdh.id AS ctdh_id, ctdh.donhang_id, ctdh.sanpham_id, ctdh.soluong, ctdh.dongia, sp.tensp, sp.hinhanh " +
                "FROM chitiet_donhang ctdh " +
                "JOIN sanpham sp ON ctdh.sanpham_id = sp.id " +
                "WHERE ctdh.donhang_id = ?";

        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(donhangId)});
        if (cursor != null && cursor.moveToFirst()) {
            int idxId = cursor.getColumnIndex("ctdh_id");
            int idxDonHangId = cursor.getColumnIndex("donhang_id");
            int idxSanPhamId = cursor.getColumnIndex("sanpham_id");
            int idxSoLuong = cursor.getColumnIndex("soluong");
            int idxDonGia = cursor.getColumnIndex("dongia");
            int idxTenSP = cursor.getColumnIndex("tensp");
            int idxHinhAnh = cursor.getColumnIndex("hinhanh");

            do {
                ChiTietDonHang ct = new ChiTietDonHang();
                ct.setId(cursor.getInt(idxId));
                ct.setDonhang_id(cursor.getInt(idxDonHangId));
                ct.setSanpham_id(cursor.getInt(idxSanPhamId));
                ct.setSoluong(cursor.getInt(idxSoLuong));
                ct.setDongia(cursor.getDouble(idxDonGia));
                ct.setTenSanPham(cursor.getString(idxTenSP));
                ct.setHinhAnh(cursor.getString(idxHinhAnh));

                list.add(ct);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }


    public List<DonHang> getAllDonHang() {
        List<DonHang> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();


        String sql = "SELECT dh.id, dh.ngaylap, dh.tongtien, dh.khachhang_id, dh.ghichu, dh.trangthai, kh.ten AS ten_khachhang " +
                "FROM donhang dh " +
                "JOIN khachhang kh ON dh.khachhang_id = kh.id " +
                "ORDER BY dh.ngaylap DESC";


        Cursor cursor = db.rawQuery(sql, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idxId = cursor.getColumnIndex("id");
            int idxNgayLap = cursor.getColumnIndex("ngaylap");
            int idxTongTien = cursor.getColumnIndex("tongtien");
            int idxKhachHangId = cursor.getColumnIndex("khachhang_id");
            int idxGhiChu = cursor.getColumnIndex("ghichu");
            int idxTrangThai = cursor.getColumnIndex("trangthai");
            int idxTenKhachHang = cursor.getColumnIndex("ten_khachhang");

            do {
                int id = cursor.getInt(idxId);
                String ngaylap = cursor.getString(idxNgayLap);
                double tongtien = cursor.getDouble(idxTongTien);
                int khachhang_id = cursor.getInt(idxKhachHangId);
                String ghichu = cursor.getString(idxGhiChu);
                String trangthai = cursor.getString(idxTrangThai);
                String tenKhachHang = cursor.getString(idxTenKhachHang);

                DonHang dh = new DonHang(id, ngaylap, tongtien, khachhang_id, ghichu, trangthai);
                dh.setTenKhachHang(tenKhachHang);  // Gán tên khách hàng
                list.add(dh);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public long insertDonHang(String ngaylap, double tongtien, int khachhang_id, String ghichu) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("ngaylap", ngaylap);
        values.put("tongtien", tongtien);
        values.put("khachhang_id", khachhang_id);
        values.put("ghichu", ghichu);
        values.put("trangthai", "Đang xử lý"); // Mặc định trạng thái

        // Trả về id đơn hàng vừa thêm
        return db.insert("donhang", null, values);
    }

    public void insertChiTietDonHang(int donhang_id, int sanpham_id, int soluong, double dongia) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("donhang_id", donhang_id);
        values.put("sanpham_id", sanpham_id);
        values.put("soluong", soluong);
        values.put("dongia", dongia);

        db.insert("chitiet_donhang", null, values);
    }
    public boolean xoaDonHangVaChiTiet(int donhangId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Xóa chi tiết đơn hàng trước
            int chiTietDeleted = db.delete("chitiet_donhang", "donhang_id=?", new String[]{String.valueOf(donhangId)});

            // Xóa đơn hàng
            int donHangDeleted = db.delete("donhang", "id=?", new String[]{String.valueOf(donhangId)});

            db.setTransactionSuccessful();

            // trả về true nếu đơn hàng được xóa (chi tiết thì xóa hay không không ảnh hưởng logic ở đây)
            return donHangDeleted > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            db.endTransaction();
        }
    }
    public boolean updateTrangThaiDonHang(int donHangId, String trangThaiMoi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("trangthai", trangThaiMoi);
        int rows = db.update("donhang", values, "id = ?", new String[]{String.valueOf(donHangId)});
        return rows > 0;
    }

    public int getSoLuongSanPham(int sanphamId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT soluong FROM sanpham WHERE id = ?", new String[]{String.valueOf(sanphamId)});
        int soluong = 0;
        if (cursor.moveToFirst()) {
            soluong = cursor.getInt(0);
        }
        cursor.close();
        return soluong;
    }

    // Cập nhật số lượng sản phẩm mới
    public void capNhatSoLuongSanPham(int sanphamId, int soluongMoi) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("soluong", soluongMoi);
        db.update("sanpham", values, "id = ?", new String[]{String.valueOf(sanphamId)});
    }

    // Trừ số lượng sản phẩm nếu trạng thái là Đang xử lý hoặc Đã thanh toán
    public void truSoLuongSanPhamTheoTrangThai(int donhangId, String trangThaiMoi) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Chỉ trừ khi trạng thái là "Đang xử lý" hoặc "Đã thanh toán"
        if (trangThaiMoi.equals("Đang xử lý") || trangThaiMoi.equals("Đã thanh toán")) {
            Cursor cursor = db.rawQuery("SELECT sanpham_id, soluong FROM chitiet_donhang WHERE donhang_id = ?", new String[]{String.valueOf(donhangId)});

            if (cursor != null) {
                while (cursor.moveToNext()) {
                    int sanphamId = cursor.getInt(0);
                    int soLuongMua = cursor.getInt(1);

                    // Trừ số lượng sản phẩm
                    db.execSQL("UPDATE sanpham SET soluong = soluong - ? WHERE id = ?", new Object[]{soLuongMua, sanphamId});
                }
                cursor.close();
            }
        }
    }
    public void congSoLuongSanPhamTheoDonHang(int donhangId) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery("SELECT sanpham_id, soluong FROM chitiet_donhang WHERE donhang_id = ?", new String[]{String.valueOf(donhangId)});

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int sanphamId = cursor.getInt(0);
                int soLuongMua = cursor.getInt(1);

                // Cộng lại số lượng sản phẩm
                db.execSQL("UPDATE sanpham SET soluong = soluong + ? WHERE id = ?", new Object[]{soLuongMua, sanphamId});
            }
            cursor.close();
        }
    }

    // Lấy doanh thu theo ngày trong tháng (tạo list có đủ ngày)
    // Lấy doanh thu theo tháng trong năm (trả về List<Float> doanh thu 12 tháng)
    // Hàm lấy doanh thu theo ngày trong tháng, trả về List<Float> với chỉ số i là ngày (1-based)
// Nếu ngày không có doanh thu thì giá trị là 0f
    public List<Float> getDoanhThuTheoNgayTrongThang(int month, int year) {
        List<Float> result = new ArrayList<>();
        // Lấy số ngày của tháng
        int daysInMonth = 31; // mặc định 31
        switch (month) {
            case 2:
                // Kiểm tra năm nhuận
                if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) daysInMonth = 29;
                else daysInMonth = 28;
                break;
            case 4: case 6: case 9: case 11:
                daysInMonth = 30;
                break;
        }
        // Khởi tạo mảng với giá trị 0
        for (int i = 0; i < daysInMonth; i++) result.add(0f);

        String yearStr = String.valueOf(year);
        String monthStr = (month < 10 ? "0" : "") + month;

        String query = "SELECT substr(ngaylap, 9, 2) as day, SUM(tongtien) " +
                "FROM donhang " +
                "WHERE trangthai = 'Đã thanh toán' AND substr(ngaylap, 1, 4) = ? AND substr(ngaylap, 6, 2) = ? " +
                "GROUP BY day";

        Cursor cursor = getReadableDatabase().rawQuery(query, new String[]{yearStr, monthStr});
        if (cursor.moveToFirst()) {
            do {
                int day = Integer.parseInt(cursor.getString(0));
                float sum = cursor.isNull(1) ? 0f : cursor.getFloat(1);
                if (day >= 1 && day <= daysInMonth) {
                    result.set(day - 1, sum);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return result;
    }

    // Hàm lấy doanh thu theo tháng trong năm, trả về List<Float> size 12
// Giá trị doanh thu tháng không có dữ liệu là 0f
    public List<Float> getDoanhThuTheoThangTrongNam(int year) {
        List<Float> result = new ArrayList<>();
        for (int i = 0; i < 12; i++) result.add(0f);

        String yearStr = String.valueOf(year);
        String query = "SELECT substr(ngaylap, 6, 2) as month, SUM(tongtien) " +
                "FROM donhang " +
                "WHERE trangthai = 'Đã thanh toán' AND substr(ngaylap, 1, 4) = ? " +
                "GROUP BY month";

        Cursor cursor = getReadableDatabase().rawQuery(query, new String[]{yearStr});
        if (cursor.moveToFirst()) {
            do {
                int month = Integer.parseInt(cursor.getString(0));
                float sum = cursor.isNull(1) ? 0f : cursor.getFloat(1);
                if (month >= 1 && month <= 12) {
                    result.set(month - 1, sum);
                }
            } while (cursor.moveToNext());
        }
        cursor.close();

        return result;
    }

    // Hàm lấy tổng doanh thu của 1 năm (float)
// Nếu không có dữ liệu thì trả về 0f
    public float getDoanhThuTheoNam(int year) {
        float result = 0f;
        String yearStr = String.valueOf(year);
        String query = "SELECT SUM(tongtien) FROM donhang WHERE trangthai = 'Đã thanh toán' AND substr(ngaylap, 1, 4) = ?";

        Cursor cursor = getReadableDatabase().rawQuery(query, new String[]{yearStr});
        if (cursor.moveToFirst()) {
            result = cursor.isNull(0) ? 0f : cursor.getFloat(0);
        }
        cursor.close();

        return result;
    }
    public String getQuyenNhanVien(String username) {
        String quyen = "";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT quyen FROM nhanvien WHERE username = ?", new String[]{username});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                quyen = cursor.getString(0);
            }
            cursor.close();
        }
        return quyen;
    }

    // Lấy thông tin
    // Lấy thông tin user: tên, địa chỉ, số điện thoại
    // Lấy thông tin user (không có email)
    public String[] getUserInfo(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT ten, sodienthoai, diachi FROM nhanvien WHERE username = ?", new String[]{username});
        if (c.moveToFirst()) {
            String name = c.getString(0);
            String phone = c.getString(1);
            String address = c.getString(2);
            c.close();
            return new String[]{name, phone, address};
        }
        c.close();
        return null;
    }

    // Cập nhật thông tin user (bỏ email)
    public boolean updateUserInfo(String username, String name, String phone, String address) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ten", name);
        values.put("sodienthoai", phone);
        values.put("diachi", address);
        int rows = db.update("nhanvien", values, "username = ?", new String[]{username});
        return rows > 0;
    }

    // Cập nhật mật khẩu
    public boolean updatePassword(String username, String oldPass, String newPass) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT password FROM nhanvien WHERE username = ?", new String[]{username});
        if (cursor.moveToFirst()) {
            String currentPass = cursor.getString(0);
            if (currentPass.equals(oldPass)) {
                cursor.close();
                ContentValues values = new ContentValues();
                values.put("password", newPass);
                int rows = db.update("nhanvien", values, "username = ?", new String[]{username});
                return rows > 0;
            }
        }
        cursor.close();
        return false;
    }

    // Cập nhật ảnh đại diện
    public String getAvatarString(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String avatarString = null;
        Cursor c = null;
        try {
            c = db.rawQuery("SELECT hinhanh FROM nhanvien WHERE username = ?", new String[]{username});
            if (c.moveToFirst()) {
                avatarString = c.getString(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (c != null) c.close();
        }
        return avatarString;
    }

    // Cập nhật avatar dưới dạng String
    public boolean updateAvatarString(String username, String avatarString) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("hinhanh", avatarString);
        int rows = 0;
        try {
            rows = db.update("nhanvien", values, "username = ?", new String[]{username});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rows > 0;
    }

    public List<NhanVien> getAllNhanVien() {
        List<NhanVien> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM nhanvien", null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                NhanVien nv = new NhanVien(
                        cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                        cursor.getString(cursor.getColumnIndexOrThrow("ten")),
                        cursor.getString(cursor.getColumnIndexOrThrow("username")),
                        cursor.getString(cursor.getColumnIndexOrThrow("password")),
                        cursor.getString(cursor.getColumnIndexOrThrow("quyen")),
                        cursor.getString(cursor.getColumnIndexOrThrow("diachi")),
                        cursor.getString(cursor.getColumnIndexOrThrow("sodienthoai")),
                        cursor.getString(cursor.getColumnIndexOrThrow("gioitinh")),
                        cursor.getInt(cursor.getColumnIndexOrThrow("tuoi")),
                        cursor.getString(cursor.getColumnIndexOrThrow("hinhanh"))
                );
                list.add(nv);
            } while (cursor.moveToNext());
            cursor.close();
        }

        return list;
    }
    public boolean insertNhanVien(NhanVien nv) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("ten", nv.getTen());
        values.put("username", nv.getUsername());
        values.put("password", nv.getPassword());
        values.put("quyen", nv.getQuyen());
        values.put("diachi", nv.getDiachi());
        values.put("sodienthoai", nv.getSodienthoai());
        values.put("gioitinh", nv.getGioitinh());
        values.put("tuoi", nv.getTuoi());
        values.put("hinhanh", nv.getHinhanh());  // nếu bạn không dùng, có thể null hoặc bỏ qua

        long result = db.insert("nhanvien", null, values);

        return result != -1; // true nếu insert thành công
    }
}
