package dev.skyherobrine.app.entities.order;

import dev.skyherobrine.app.entities.person.NhaCungCap;
import dev.skyherobrine.app.enums.TinhTrangNhapHang;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Thực thể "Phiếu Nhập Hàng", thực thể này dùng để lưu trữ thông tin phiếu nhập hàng của cửa hàng trong
 * việc nhập các sản phẩm từ nhà cung cấp đến.
 * @author Trương Dương Minh Nhật
 * @version 1.0
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "PNH.findAll", query = "SELECT pnh FROM PhieuNhapHang pnh"),
        @NamedQuery(name = "PNH.findByID", query = "SELECT pnh FROM PhieuNhapHang pnh WHERE pnh.maPhieuNhap = :id")
})
public class PhieuNhapHang {
    @Id
    @Column(name = "ma_phieu_nhap", nullable = false)
    private String maPhieuNhap;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ma_ncc", nullable = false)
    private NhaCungCap nhaCungCap;
    @Column(name = "ngay_lap_phieu", nullable = false)
    private LocalDateTime ngayLapPhieu;
    @Column(name = "ngay_hen_giao", nullable = false)
    private LocalDateTime ngayHenGiao;
    @Column(name = "ghi_chu")
    private String ghiChu;
    @Enumerated(EnumType.STRING)
    @Column(name = "tinh_trang", nullable = false)
    private TinhTrangNhapHang tinhTrang;

    public PhieuNhapHang(String maPhieuNhap, NhaCungCap nhaCungCap, LocalDateTime ngayLapPhieu, LocalDateTime ngayHenGiao, String ghiChu, TinhTrangNhapHang tinhTrang) {
        this.maPhieuNhap = maPhieuNhap;
        this.nhaCungCap = nhaCungCap;
        this.ngayLapPhieu = ngayLapPhieu;
        this.ngayHenGiao = ngayHenGiao;
        this.ghiChu = ghiChu;
        this.tinhTrang = tinhTrang;
    }

    public PhieuNhapHang() {

    }

    public String getMaPhieuNhap() {
        return maPhieuNhap;
    }

    public void setMaPhieuNhap(String maPhieuNhap) {
        this.maPhieuNhap = maPhieuNhap;
    }

    public NhaCungCap getNhaCungCap() {
        return nhaCungCap;
    }

    public void setNhaCungCap(NhaCungCap nhaCungCap) {
        this.nhaCungCap = nhaCungCap;
    }

    public LocalDateTime getNgayLapPhieu() {
        return ngayLapPhieu;
    }

    public void setNgayLapPhieu(LocalDateTime ngayLapPhieu) {
        this.ngayLapPhieu = ngayLapPhieu;
    }

    public LocalDateTime getNgayHenGiao() {
        return ngayHenGiao;
    }
    /**
     * Set ngày hẹn giao phải lớn hơn ngày lập phiếu nhập <br></br>
     * Nếu ngày hẹn giao nhỏ hơn ngày lập phiếu thì sẽ xuất ra exception "Ngày hẹn giao không được sớm hơn ngày lập phiếu!"
     */
    public void setNgayHenGiao(LocalDateTime ngayHenGiao) throws Exception {
        if(!ngayHenGiao.isBefore(this.ngayLapPhieu))
            this.ngayHenGiao = ngayHenGiao;
        else
            throw new Exception("Ngày hẹn giao không được sớm hơn ngày lập phiếu!");
    }

    public String getGhiChu() {
        return ghiChu;
    }

    public void setGhiChu(String ghiChu) {
        this.ghiChu = ghiChu;
    }

    public TinhTrangNhapHang getTinhTrang() {
        return tinhTrang;
    }

    public void setTinhTrang(TinhTrangNhapHang tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    @Override
    public String toString() {
        return "PhieuNhapHang{" +
                "maPhieuNhap='" + maPhieuNhap + '\'' +
                ", nhaCungCap=" + nhaCungCap +
                ", ngayLapPhieu=" + ngayLapPhieu +
                ", ngayHenGiao=" + ngayHenGiao +
                ", ghiChu='" + ghiChu + '\'' +
                ", tinhTrang=" + tinhTrang +
                '}';
    }
}
