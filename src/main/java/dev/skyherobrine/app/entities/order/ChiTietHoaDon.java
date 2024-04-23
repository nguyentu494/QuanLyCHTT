package dev.skyherobrine.app.entities.order;

import dev.skyherobrine.app.entities.Key.ChiTietHoaDonId;
import dev.skyherobrine.app.entities.product.ChiTietPhienBanSanPham;
import dev.skyherobrine.app.entities.product.SanPham;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * Thực thể "Chi Tiết Hoá Đơn", thực thể này dùng để chứa thông tin chi tiết về sản phẩm mà khách hàng
 * mua và cũng như giá tiền tương ứng.
 * @author Trương Dương Minh Nhật
 * @version 1.0
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@NamedQueries(
        {
                @NamedQuery(name = "CTHD.findAll", query = "SELECT cthd FROM ChiTietHoaDon cthd"),
                @NamedQuery(name = "CTHD.findByID", query = "SELECT cthd FROM ChiTietHoaDon cthd WHERE cthd.chiTietHoaDonId = :id"),
                @NamedQuery(name = "CTHD.findByMaHD", query = "SELECT cthd FROM ChiTietHoaDon cthd WHERE cthd.chiTietHoaDonId.hoaDon.maHD = :maHD")
        }
)
public class ChiTietHoaDon implements Serializable {
    /**
	 * 
	 */
	@Serial
    private static final long serialVersionUID = 1L;
	@EmbeddedId
    private ChiTietHoaDonId chiTietHoaDonId;
    @Column(name = "so_luong_mua", nullable = false)
    private int soLuongMua;
    /**
     * Set số lượng mua phải lớn hơn 0 <br></br>
     * Nếu số lượng mua bằng 0 thì sẽ xuất ra exception "Số lượng mua lớn hơn 0"
     */
    public void setSoLuongMua(int soLuongMua) throws Exception {
        if(soLuongMua>0)
            this.soLuongMua = soLuongMua;
        else
            throw new Exception("Số lượng mua phải lớn hơn 0!");
    }

    public double giaTienSanPham() {
        return soLuongMua * chiTietHoaDonId.getPhienBanSanPham().getSanPham().giaBan();
    }

}
