package dev.skyherobrine.app.entities.order;

import dev.skyherobrine.app.entities.person.KhachHang;
import dev.skyherobrine.app.entities.person.NhanVien;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;
import java.util.Set;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Thực thể "Hoá Đơn", dùng để lưu trữ thông tin về lập hoá đơn của khách hàng đã mua hàng tại cửa hàng.
 * @author Trương Dương Minh Nhật
 * @version 1.0
 */
@Getter
@Entity
@NamedQueries({
        @NamedQuery(name = "HD.findAll", query = "SELECT hd FROM HoaDon hd"),
        @NamedQuery(name = "HD.findByID", query = "SELECT hd FROM HoaDon hd WHERE hd.maHD = :id"),
        @NamedQuery(name = "HD.findTop30", query = "SELECT hd FROM HoaDon hd ORDER BY hd.ngayLap ASC LIMIT 30"),
        @NamedQuery(name = "HD.findByDate", query = "SELECT hd FROM HoaDon hd WHERE (current_date - hd.ngayLap) >= 7"),
})
@NamedNativeQueries({
})
public class HoaDon implements Serializable {
    /**
	 * 
	 */
	@Serial
    private static final long serialVersionUID = 1L;
	@Id
    @Column(name = "ma_hd", nullable = false)
    private String maHD;
    @Column(name = "ngay_lap", nullable = false)
    private LocalDateTime ngayLap;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "ma_nv", nullable = false)
    private NhanVien nhanVienLap;
    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "ma_kh", nullable = false)
    private KhachHang khachHang;
    @Column(name = "so_tien_kh_tra", nullable = false)
    private BigDecimal soTienKHTra;
    @Column(name = "ghi_chu")
    private String ghiChu;
    @OneToMany(mappedBy = "chiTietHoaDonId.hoaDon", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude
    private Set<ChiTietHoaDon> chiTietHoaDons;

    public HoaDon(String maHD, LocalDateTime ngayLap, NhanVien nhanVienLap, KhachHang khachHang, BigDecimal soTienKHTra, String ghiChu) throws Exception {
        this.setMaHD(maHD);
        this.setNgayLap(ngayLap);
        this.setNhanVienLap(nhanVienLap);
        this.setKhachHang(khachHang);
        this.setSoTienKHTra(soTienKHTra);
        this.setGhiChu(ghiChu);
    }

    public HoaDon() {

    }

    public void setMaHD(String maHD) {
        this.maHD = maHD;
    }

    public void setNgayLap(LocalDateTime ngayLap) {
        this.ngayLap = ngayLap;
    }

    public void setNhanVienLap(NhanVien nhanVienLap) {
        this.nhanVienLap = nhanVienLap;
    }

    public void setKhachHang(KhachHang khachHang) {
        this.khachHang = khachHang;
    }

    public void setSoTienKHTra(BigDecimal soTienKHTra) {
        this.soTienKHTra = soTienKHTra;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public void setChiTietHoaDons(Set<ChiTietHoaDon> chiTietHoaDons) {
        this.chiTietHoaDons = chiTietHoaDons;
    }

    public double tongTien() {
        double tongTien = 0;
        for (ChiTietHoaDon chiTietHoaDon : chiTietHoaDons) {
            tongTien += chiTietHoaDon.giaTienSanPham();
        }
        return tongTien;

    }

    @Override
    public String toString() {
        return "HoaDon{" +
                "maHD='" + maHD + '\'' +
                ", ngayLap=" + ngayLap +
                ", nhanVienLap=" + nhanVienLap +
                ", khachHang=" + khachHang +
                ", soTienKHTra=" + soTienKHTra +
                ", ghiChu='" + ghiChu + '\'' +
                ", chiTietHoaDons=" + chiTietHoaDons +
                '}';
    }
}
