package dev.skyherobrine.app.entities.person;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * Thực thể khách hàng, khách hàng sẽ là người thực hiên mua hàng tại cửa hàng, hệ thống cửa hàng sẽ lưu trữ
 * thông tin của khách hàng khi khách đến mua hàng tại cửa hàng và thông tin của khách cũng sẽ chứng minh khách
 * có mua món hàng ở cửa hàng và khi có trả hàng thì cửa hàng sẽ dựa vào thông tin hoá đơn mà khách hàng có
 * được khi mua sẽ tiến hành lập phiếu trả hàng.
 * @author Trương Dương Minh Nhật
 * @version 1.0
 */
@Getter
@Entity
public class KhachHang implements Serializable {
    /**
	 * 
	 */
	@Serial
    private static final long serialVersionUID = 1L;
	@Id
    @Column(name = "ma_kh", nullable = false)
    private String maKH;
    @Column(name = "ho_ten", nullable = false, columnDefinition = "nvarchar(255)")
    private String hoTen;
    @Column(name = "so_dien_thoai", nullable = false)
    private String soDienThoai;
    @Column(name = "gioi_tinh", nullable = false)
    private boolean gioiTinh;
    @Column(name = "ngay_sinh", nullable = false)
    private LocalDate ngaySinh;
    @Column(name = "diem_tich_luy", nullable = false)
    private float diemTichLuy;

    public KhachHang(String maKH, String hoTen, String soDienThoai, boolean gioiTinh, LocalDate ngaySinh, float diemTichLuy) throws Exception{
        this.setMaKH(maKH);
        this.setHoTen(hoTen);
        this.setSoDienThoai(soDienThoai);
        this.setGioiTinh(gioiTinh);
        this.setNgaySinh(ngaySinh);
        this.setDiemTichLuy(diemTichLuy);
    }

    public KhachHang() {

    }

    public void setMaKH(String maKH) {
        this.maKH = maKH;
    }

    /**
     * Set họ và tên không được rỗng, nếu họ và tên rỗng thì sẽ xuất ra exception "Họ tên không được rỗng!" <br></br>
     * Họ tên chỉ được chứa những chữ cái <br></br>
     * Nếu họ và tên có ký tự khác chữ cái sẽ xuất ra exception "Họ và tên không được chứa ký tự số và ký tự đặc biệt!"
     */
    public void setHoTen(String hoTen) throws Exception {
        if(!(hoTen.equalsIgnoreCase("")))
            if(!(hoTen.matches(".+\\D[ ]{1,}.+\\D$")))
                throw new Exception("Họ và tên không được chứa ký tự số và ký tự đặc biệt!");
            else
                this.hoTen = hoTen;
        else
            throw new Exception("Họ tên không được rỗng!");
    }

    /**
     * Set số điện thoại không được rỗng, nếu số điện thoại rỗng thì sẽ xuất ra exception "Số điện thoại không được rỗng!" <br></br>
     * Số điện thoại phải bắt đầu bằng số 0 hoặc +84 và kèm theo 9 số <br></br>
     * Nếu số điện thoại có ký tự khác số sẽ xuất ra exception "Số điện thoại phải bắt đầu bằng (0) hoặc (+84) và kèm theo 9 số!"
     */
    public void setSoDienThoai(String soDienThoai) throws Exception {
        if(!(soDienThoai.equalsIgnoreCase(""))) {
            if(!(soDienThoai.matches("(^0\\d{9}|^(\\+84)\\d{9})")))
                throw new Exception("Số điện thoại phải bắt đầu bằng (0) hoặc (+84) và kèm theo 9 số!");
            else
                this.soDienThoai = soDienThoai;
        }
        else
            throw new Exception("Số điện thoại không được rỗng!");
    }

    public void setGioiTinh(boolean gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    /**
     * Set ngày sinh phải lớn hơn ngày hiện tại <br></br>
     * Nếu ngày sinh trễ hơn ngày hiện tại thì sẽ xuất ra exception "Ngày sinh phải bé hơn ngày hiện tại!"
     */
    public void setNgaySinh(LocalDate ngaySinh) throws Exception {
        if(!ngaySinh.isAfter(LocalDate.now()))
            this.ngaySinh = ngaySinh;
        else
            throw new Exception("Ngày sinh phải bé hơn ngày hiện tại!");
    }

    public void setDiemTichLuy(float diemTichLuy) {
        this.diemTichLuy = diemTichLuy;
    }

    public String layBaSoCuoiDienThoai() {
        int getLength = soDienThoai.toCharArray().length;
        return soDienThoai.toCharArray()[getLength - 3] + "" + soDienThoai.toCharArray()[getLength - 2] + soDienThoai.toCharArray()[getLength - 1];
    }
}
