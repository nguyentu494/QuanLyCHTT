package dev.skyherobrine.app.entities.product;

import dev.skyherobrine.app.enums.MauSac;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class ChiTietPhienBanSanPham {
    @Id
    @Column(name = "ma_phien_ban_sp", nullable = false)
    private String maPhienBanSP;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ma_sp", nullable = false)
    private SanPham sanPham;
    @Enumerated(EnumType.STRING)
    @Column(name = "mau_sac", nullable = false)
    private MauSac mauSac;
    @Column(name = "kich_thuoc", nullable = false)
    private String kichThuoc;
    @Column(name = "so_luong", nullable = false)
    private int soLuong;
    @Column(name = "hinh_anh", nullable = false)
    private String hinhAnh;

    public ChiTietPhienBanSanPham(String maPhienBanSP, SanPham sanPham, MauSac mauSac, String kichThuoc, int soLuong, String hinhAnh) {
        setMaPhienBanSP(maPhienBanSP);
        setSanPham(sanPham);
        setMauSac(mauSac);
        setKichThuoc(kichThuoc);
        setSoLuong(soLuong);
        setHinhAnh(hinhAnh);
    }

    public ChiTietPhienBanSanPham() {

    }

    public void setMaPhienBanSP(String maPhienBanSP) {
        this.maPhienBanSP = maPhienBanSP;
    }

    public void setSanPham(SanPham sanPham) {
        this.sanPham = sanPham;
    }

    public void setMauSac(MauSac mauSac) {
        this.mauSac = mauSac;
    }

    public void setKichThuoc(String kichThuoc) {
        this.kichThuoc = kichThuoc;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    @Override
    public String toString() {
        return "ChiTietPhienBanSanPham{" +
                "maPhienBanSP='" + maPhienBanSP + '\'' +
                ", sanPham=" + sanPham +
                ", mauSac=" + mauSac +
                ", kichThuoc='" + kichThuoc + '\'' +
                ", soLuong=" + soLuong +
                ", hinhAnh='" + hinhAnh + '\'' +
                '}';
    }
}
