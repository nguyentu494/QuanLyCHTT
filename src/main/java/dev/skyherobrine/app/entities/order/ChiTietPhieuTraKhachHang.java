package dev.skyherobrine.app.entities.order;

import dev.skyherobrine.app.entities.Key.ChiTietPhieuTraKhachHangId;
import dev.skyherobrine.app.entities.product.ChiTietPhienBanSanPham;
import dev.skyherobrine.app.entities.product.SanPham;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.*;
import java.io.Serial;
import java.io.Serializable;

/**
 * Thực thể "Chi Tiết Phiếu Trả Khách Hàng", thực thể này dùng để lưu thông tin chi tiết về sản phẩm
 * mà khách hàng đến trả và cũng như số lượng khách hàng muốn trả.
 *
 * @author Trương Dương Minh Nhật
 * @version 1.0
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@NamedQueries(
        {
                @NamedQuery(
                        name = "ChiTietPhieuTraKhachHang.findAll",
                        query = "SELECT c FROM ChiTietPhieuTraKhachHang c")
        }
)
public class ChiTietPhieuTraKhachHang implements Serializable {
    /**
     *
     */
    @Serial
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    private ChiTietPhieuTraKhachHangId chiTietPhieuTraKhachHangId;
    @Column(name = "so_luong_tra", nullable = false)
    private int soLuongTra;
    @Column(name = "noi_dung_tra", nullable = false)
    private String noiDungTra;

    /**
     * Set số lượng trả phải lớn hơn 0 <br></br>
     * Nếu số lượng trả lớn hơn thì sẽ xuất ra exception "Số lượng trả lớn hơn 0"
     */
    public void setSoLuongTra(int soLuongTra) throws Exception {
        if (soLuongTra > 0)
            this.soLuongTra = soLuongTra;
        else {
            JOptionPane.showMessageDialog(null, "Số lượng trả phải lớn hơn 0!");
            throw new Exception("Số lượng mặt hàng trả phải lớn hơn 0!");
        }
    }


    public void setNoiDungTra(String noiDungTra) {
        this.noiDungTra = noiDungTra;
    }


}
