package dev.skyherobrine.app.entities.order;

import dev.skyherobrine.app.entities.order.HoaDon;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Thực thể "Phiếu Trả Khách Hàng", thực thể này dùng để chứa dữ liệu liên quan đến việc khách hàng
 * đến cửa hàng trả món hàng lại nếu như món hàng mà khách hàng mua có bị hỏng hay muốn trả để đổi
 * , mua cái khác.
 * @author Trương Dương Minh Nhật
 * @version 1.0
 */
@Getter
@Entity
public class PhieuTraKhachHang {
    @Id
    @Column(name = "ma_phieu_tra_khach_hang", nullable = false)
    private String maPhieuTraKhachHang;
    @Column(name = "ngay_lap", nullable = false)
    private LocalDateTime ngayLap;
    @ManyToOne
    @JoinColumn(name = "ma_hd", nullable = false)
    private HoaDon hoaDon;

    public PhieuTraKhachHang(String maPhieuTraKhachHang, LocalDateTime ngayLap, HoaDon hoaDon) throws Exception{
        this.setMaPhieuTraKhachHang(maPhieuTraKhachHang);
        this.setNgayLap(ngayLap);
        this.setHoaDon(hoaDon);
    }

    public PhieuTraKhachHang() {

    }

    public void setMaPhieuTraKhachHang(String maPhieuTraKhachHang) {
        this.maPhieuTraKhachHang = maPhieuTraKhachHang;
    }

    public void setNgayLap(LocalDateTime ngayLap) {
        this.ngayLap = ngayLap;
    }

    public void setHoaDon(HoaDon hoaDon) {
        this.hoaDon = hoaDon;
    }

    @Override
    public String toString() {
        return "PhieuTraKhachHang{" +
                "maPhieuTraKhachHang='" + maPhieuTraKhachHang + '\'' +
                ", ngayLap=" + ngayLap +
                ", hoaDon=" + hoaDon +
                '}';
    }
}
