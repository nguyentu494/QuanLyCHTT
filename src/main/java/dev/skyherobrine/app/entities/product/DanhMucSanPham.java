package dev.skyherobrine.app.entities.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * Thực thể "Danh Mục Sản Phẩm", thực thể này sẽ lưu trữ các danh mục bán hàng của cửa hàng, mỗi danh mục
 * sẽ có nhiều loại sản phẩm khác nhau và mỗi sản phẩm sẽ có nhiều phiên bản khác nhau.
 * @author Trương Dương Minh Nhật
 * @version 1.0
 */

@Getter
@Entity
public class DanhMucSanPham implements Serializable {
    /**
	 * 
	 */
	@Serial
    private static final long serialVersionUID = 1L;
	@Id
    @Column(name = "ma_dm", nullable = false)
    private String maDM;
    @Column(name = "ten_dm", nullable = false)
    private String tenDM;

    public DanhMucSanPham(String maDM, String tenDM) {
        this.maDM = maDM;
        this.tenDM = tenDM;
    }

    public DanhMucSanPham() {

    }

    public void setMaDM(String maDM) {
        this.maDM = maDM;
    }

    /**
     * Set tên danh mục không được rỗng <br></br>
     * Nếu tên danh mục rỗng thì sẽ xuất ra exception "Tên danh mục không được rỗng!"
     */
    public void setTenDM(String tenDM) throws Exception {
        if(!tenDM.equalsIgnoreCase(""))
            this.tenDM = tenDM;
        else
            throw new Exception("Tên danh mục không được để trống!");
    }

    @Override
    public String toString() {
        return "DanhMucSanPham{" +
                "maDM='" + maDM + '\'' +
                ", tenDM='" + tenDM + '\'' +
                '}';
    }
}
