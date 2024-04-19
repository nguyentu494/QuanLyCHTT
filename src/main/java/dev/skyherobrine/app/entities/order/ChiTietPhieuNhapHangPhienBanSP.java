package dev.skyherobrine.app.entities.order;

import dev.skyherobrine.app.entities.Key.ChiTietPhieuNhapHangPhienBanSPId;
import dev.skyherobrine.app.entities.product.ChiTietPhienBanSanPham;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

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
public class ChiTietPhieuNhapHangPhienBanSP implements Serializable {
    /**
	 * 
	 */
	@Serial
    private static final long serialVersionUID = 1L;
	@EmbeddedId
    private ChiTietPhieuNhapHangPhienBanSPId chiTietPhieuNhapHangPhienBanSPId;
    @Column(name = "so_luong", nullable = false)
    private int soLuongNhap;

}
