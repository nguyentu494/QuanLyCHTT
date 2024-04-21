package dev.skyherobrine.app.entities.order;

import dev.skyherobrine.app.entities.product.SanPham;
import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * Thực thể "Chi Tiết Phiếu Nhập", thực thể này dùng để lưu trữ thông tin chi tiết về nhập hàng, sản phẩm
 * nhập với số lượng là bao nhiêu? Số tiền nhập hàng ứng với sản phẩm đó là bao nhiêu?
 * @author Trương Dương Minh Nhật
 * @version 1.0
 */
@Getter
@Entity
@NamedQueries({
        @NamedQuery(name = "CTPNH.findAll", query = "SELECT ctpnh FROM ChiTietPhieuNhapHang ctpnh"),
        @NamedQuery(name = "CTPNH.findByID", query = "SELECT ctpnh FROM ChiTietPhieuNhapHang ctpnh WHERE ctpnh.id = :id"),
        @NamedQuery(name = "CTPNH.findByDayAndMaSP", query = "SELECT ctpnh FROM ChiTietPhieuNhapHang ctpnh " +
                "inner join PhieuNhapHang pnh on ctpnh.phieuNhapHang.maPhieuNhap = pnh.maPhieuNhap " +
                "where pnh.ngayLapPhieu < :ngayNhap and ctpnh.sanPham.maSP = :maSP"),
        @NamedQuery(name = "CTPNH.findByMaPhieuNhapAndMaSP", query = "SELECT ctpnh FROM ChiTietPhieuNhapHang ctpnh " +
                "WHERE ctpnh.phieuNhapHang.maPhieuNhap = :maPhieuNhap and ctpnh.sanPham.maSP = :maSP")
})
public class ChiTietPhieuNhapHang implements Serializable {
    /**
	 * 
	 */
	@Serial
    private static final long serialVersionUID = 1L;
	@Id
    @Column(name = "ma_chi_tiet_phieu_nhap", nullable = false)
    private String maChiTietPhieuNhap;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_phieu_nhap", nullable = false)
    private PhieuNhapHang phieuNhapHang;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_sp", nullable = false)
    private SanPham sanPham;
    @Column(name = "gia_nhap", nullable = false)
    private double giaNhap;

    public ChiTietPhieuNhapHang(String maChiTietPhieuNhap, PhieuNhapHang phieuNhapHang, SanPham sanPham, double giaNhap) throws Exception{
        this.setMaChiTietPhieuNhap(maChiTietPhieuNhap);
        this.setPhieuNhapHang(phieuNhapHang);
        this.setSanPham(sanPham);
        this.setGiaNhap(giaNhap);
    }

    public ChiTietPhieuNhapHang() {

    }

    public void setPhieuNhapHang(PhieuNhapHang phieuNhapHang) {
        this.phieuNhapHang = phieuNhapHang;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }


    /**
     * set số giá nhập phải lớn hơn 0
     * Nếu số giá nhập bằng 0 thì sẽ xuất ra exception "Giá nhập lớn hơn 0"
     */
    public void setGiaNhap(double giaNhap) throws Exception {
        if(giaNhap>0)
            this.giaNhap = giaNhap;
        else
            throw new Exception("Giá nhập phải lớn hơn 0!");
    }

    public void setMaChiTietPhieuNhap(String maChiTietPhieuNhap) {
        this.maChiTietPhieuNhap = maChiTietPhieuNhap;
    }

    /**
     * Tính tiền nhập hàng. Công thức tính: <b>Giá Nhập * Số Lượng Nhập = Giá Tiền Trả</b>
     * @return {@link double} trả về số thực là số tiền nhập ứng với sản phẩm này.
     */

    @Override
    public String toString() {
        return "ChiTietPhieuNhapHang{" +
                "maChiTietPhieuNhap='" + maChiTietPhieuNhap +
                ", phieuNhapHang=" + phieuNhapHang +
                ", sanPham=" + sanPham +
                ", giaNhap=" + giaNhap +
                '}';
    }

}
