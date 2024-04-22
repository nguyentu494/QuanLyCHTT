package dev.skyherobrine.app.entities.product;

import jakarta.persistence.*;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;

/**
 * Thực thể "Loại sản phẩm", thực thể này sẽ lưu trữ tất cả các sản phẩm tương ứng theo loại.
 * @author Trương Dương Minh Nhật
 * @version 1.0
 */
@Getter
@Entity
public class LoaiSanPham implements Serializable {
    /**
	 * 
	 */
	@Serial
    private static final long serialVersionUID = 1L;
	@Id
    @Column(name = "ma_loai", nullable = false)
    private String maLoai;
    @Column(name = "ten_loai", nullable = false)
    private String tenLoai;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "ma_dm", nullable = false)
    private DanhMucSanPham danhMucSanPham;

    public LoaiSanPham(String maLoai, String tenLoai, DanhMucSanPham danhMucSanPham) {
        this.maLoai = maLoai;
        this.tenLoai = tenLoai;
        this.danhMucSanPham = danhMucSanPham;
    }

    public LoaiSanPham() {

    }

    public void setMaLoai(String maLoai) {
        this.maLoai = maLoai;
    }

    /**
     * set loại danh mục không được rỗng <br></br>
     * nếu loại danh mục rỗng thì sẽ xuất ra exception "Loại danh mục không được rỗng!"
     */
    public void setTenLoai(String tenLoai) throws Exception {
        if(!tenLoai.equalsIgnoreCase(""))
            this.tenLoai = tenLoai;
        else
            throw new Exception("Tên loại không được để trống!");
    }

    public void setDanhMucSanPham(DanhMucSanPham danhMucSanPham) {
        this.danhMucSanPham = danhMucSanPham;
    }

    @Override
    public String toString() {
        return "LoaiSanPham{" +
                "maLoai='" + maLoai + '\'' +
                ", tenLoai='" + tenLoai + '\'' +
                '}';
    }
}
