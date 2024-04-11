package dev.skyherobrine.app.entities.product;

import dev.skyherobrine.app.entities.order.ChiTietPhieuNhapHang;
import dev.skyherobrine.app.entities.sale.Thue;
import dev.skyherobrine.app.enums.DoTuoi;
import dev.skyherobrine.app.enums.MauSac;
import dev.skyherobrine.app.enums.PhongCachMac;
import dev.skyherobrine.app.enums.TinhTrangSanPham;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Thực thể "Sản Phẩm", thực thể này dùng để lưu trữ thông tin sản phẩm trong cửa hàng, thông tin sản phẩm
 * sẽ được hiển thị thông tin chi tiết trên ứng dụng để nhân viên bán hàng có thể đọc được và quản lý.
 * @author Trương Dương Minh Nhật
 * @version 1.1
 */
@Getter
@Entity
@NamedQueries({
        @NamedQuery(name = "SanPham.findAll", query = "SELECT sp FROM SanPham sp"),
        @NamedQuery(name = "SanPham.findByID", query = "SELECT sp FROM SanPham sp WHERE sp.maSP = :maSP")
})
public class SanPham {

    @Id
    @Column(name = "ma_sp", nullable = false)
    private String maSP;
    @Column(name = "ten_sp", nullable = false)
    private String tenSP;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_loai", nullable = false)
    private LoaiSanPham loaiSanPham;
    @Column(name = "phong_cach_mac", nullable = false)
    @Enumerated(EnumType.STRING)
    private PhongCachMac phongCachMac;
    @Column(name = "do_tuoi", nullable = false)
    @Enumerated(EnumType.STRING)
    private DoTuoi doTuoi;
    @Column(name = "xuat_xu", nullable = false)
    private String xuatXu;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_th", nullable = false)
    private ThuongHieu thuongHieu;
    @Column(name = "phan_tram_loi", nullable = false)
    private float phanTramLoi;
    @Column(name = "ngay_san_xuat", nullable = false)
    private LocalDate ngaySanXuat;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_thue", nullable = false)
    private Thue thue;
    @Enumerated(EnumType.STRING)
    @Column(name = "tinh_trang", nullable = false)
    private TinhTrangSanPham tinhTrang;
    @OneToMany(mappedBy = "sanPham")
    @ToString.Exclude
    private List<ChiTietPhieuNhapHang> chiTietPhieuNhapHangs;

    public SanPham(String maSP, String tenSP, LoaiSanPham loaiSanPham, PhongCachMac phongCachMac, DoTuoi doTuoi, String xuatXu, ThuongHieu thuongHieu, float phanTramLoi, LocalDate ngaySanXuat, Thue thue, TinhTrangSanPham tinhTrang) throws Exception{
        this.setMaSP(maSP);
        this.setTenSP(tenSP);
        this.setLoaiSanPham(loaiSanPham);
        this.setPhongCachMac(phongCachMac);
        this.setDoTuoi(doTuoi);
        this.setXuatXu(xuatXu);
        this.setThuongHieu(thuongHieu);
        this.setPhanTramLoi(phanTramLoi);
        this.setNgaySanXuat(ngaySanXuat);
        this.thue = thue;
        this.setTinhTrang(tinhTrang);
    }

    public SanPham() {

    }

    public void setMaSP(String maSP) {
        this.maSP = maSP;
    }

    /**
     * Set tên sản phẩm không được rỗng <br></br>
     * Nếu tên sản phẩm rỗng thì sẽ xuất ra exception "Tên sản phẩm không được để trống!"
     */
    public void setTenSP(String tenSP) throws Exception {
        if(!tenSP.equalsIgnoreCase(""))
            this.tenSP = tenSP;
        else
            throw new Exception("Tên sản phẩm không được để trống!");
    }

    public void setLoaiSanPham(LoaiSanPham loaiSanPham) {
        this.loaiSanPham = loaiSanPham;
    }

    public void setPhongCachMac(PhongCachMac phongCachMac) {
        this.phongCachMac = phongCachMac;
    }

    public void setDoTuoi(DoTuoi doTuoi) {
        this.doTuoi = doTuoi;
    }

    public void setXuatXu(String xuatXu) throws Exception {
        if(!xuatXu.equalsIgnoreCase(""))
            this.xuatXu = xuatXu;
        else
            throw new Exception("Xuất xứ không được để trống!");
    }

    public void setThuongHieu(ThuongHieu thuongHieu) {
        this.thuongHieu = thuongHieu;
    }

    public void setPhanTramLoi(float phanTramLoi) {
        this.phanTramLoi = phanTramLoi;
    }

    public void setNgaySanXuat(LocalDate ngaySanXuat) {
        this.ngaySanXuat = ngaySanXuat;
    }

    public void setThue(Thue thue) {
        this.thue = thue;
    }

    public void setTinhTrang(TinhTrangSanPham tinhTrang) {
        this.tinhTrang = tinhTrang;
    }

    public void setChiTietPhieuNhapHangs(List<ChiTietPhieuNhapHang> chiTietPhieuNhapHangs) {
        this.chiTietPhieuNhapHangs = chiTietPhieuNhapHangs;
    }

    public double giaBan() {
        return giaNhapGanNhat() * (1 + phanTramLoi / 100) * (1 + thue.getGiaTri() / 100);
    }

    public double giaNhapGanNhat() {
        double giaBan = 0;
        LocalDateTime ngayNhap = null;
        for(ChiTietPhieuNhapHang chiTietPhieuNhapHang : chiTietPhieuNhapHangs) {
            if(ngayNhap == null) {
                ngayNhap = chiTietPhieuNhapHang.getPhieuNhapHang().getNgayLapPhieu();
                giaBan = chiTietPhieuNhapHang.getGiaNhap();
            } else if(ngayNhap.isBefore(chiTietPhieuNhapHang.getPhieuNhapHang().getNgayLapPhieu())) {
                ngayNhap = chiTietPhieuNhapHang.getPhieuNhapHang().getNgayLapPhieu();
                giaBan = chiTietPhieuNhapHang.getGiaNhap();
            }
        }
        return giaBan;
    }


    @Override
    public String toString() {
        return "SanPham{" +
                "maSP='" + maSP + '\'' +
                ", tenSP='" + tenSP + '\'' +
                ", loaiSanPham=" + loaiSanPham +
                ", phongCachMac=" + phongCachMac +
                ", doTuoi=" + doTuoi +
                ", xuatXu='" + xuatXu + '\'' +
                ", thuongHieu=" + thuongHieu +
                ", phanTramLoi=" + phanTramLoi +
                '}';
    }
}
