package dev.skyherobrine.app.entities.order;

import dev.skyherobrine.app.entities.Key.ChiTietPhieuNhapHangPhienBanSPId;
import dev.skyherobrine.app.entities.product.ChiTietPhienBanSanPham;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@NamedQueries({
        @NamedQuery(name = "CTPNHPBSP.findAll", query = "SELECT ctpnhpbsp FROM ChiTietPhieuNhapHangPhienBanSP ctpnhpbsp"),
        @NamedQuery(name = "CTPNHPBSP.findByID", query = "SELECT ctpnhpbsp FROM ChiTietPhieuNhapHangPhienBanSP ctpnhpbsp WHERE ctpnhpbsp.id = :id")
})
public class ChiTietPhieuNhapHangPhienBanSP {
    @EmbeddedId
    private ChiTietPhieuNhapHangPhienBanSPId chiTietPhieuNhapHangPhienBanSPId;
    @Column(name = "so_luong_nhap", nullable = false)
    private int soLuongNhap;

}
