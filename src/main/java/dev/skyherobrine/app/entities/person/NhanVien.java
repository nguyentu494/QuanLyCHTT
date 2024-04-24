package dev.skyherobrine.app.entities.person;

import dev.skyherobrine.app.enums.CaLamViec;
import dev.skyherobrine.app.enums.ChucVu;
import dev.skyherobrine.app.enums.TinhTrangNhanVien;
import jakarta.persistence.*;
import lombok.Getter;

import javax.swing.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

import static java.time.Period.*;

/**
 * Thực thể nhân viên, nhân viên có trách nhiệm làm các công việc của cửa hàng từ quản lý bán hàng đến các quản
 * lý sản phẩm, hoá đơn,...</br>
 * Nhân viên có 2 dạng: <b>Nhân viên bán hàng</b> và <b>nhân viên quản lý nhân sự</b>.</br>
 * <ul>
 *     <li>Nhân viên bán hàng sẽ làm các công việc liên quan đến cửa hàng.</li>
 *     <li>Nhân viên quản lý nhân sự sẽ làm các công việc liên quan đến quản lý nhân viên, mọi sự hoạt động của nhân viên
 *         trong cửa hàng.</li>
 * </ul>
 * @author Trương Dương Minh Nhật
 * @version 1.0
 */
@Getter
@Entity
@NamedQueries({
        @NamedQuery(name = "NV.findAll", query = "SELECT nv FROM NhanVien nv"),
        @NamedQuery(name = "NV.findByID", query = "SELECT nv FROM NhanVien nv WHERE nv.maNV = :id"),
})
public class NhanVien implements Serializable {

    /**
	 * 
	 */
	@Serial
    private static final long serialVersionUID = 1L;
	@Id
    @Column(name = "ma_nv", nullable = false)
    private String maNV;
    @Column(name = "ho_ten", nullable = false, columnDefinition = "nvarchar(255)")
    private String hoTen;
    @Column(name = "so_dien_thoai", nullable = false)
    private String soDienThoai;
    @Column(name = "gioi_tinh", nullable = false)
    private boolean gioiTinh;
    @Column(name = "ngay_sinh", nullable = false)
    private LocalDate ngaySinh;
    @Column(name = "email", nullable = false)
    private String email;
    @Column(name = "dia_chi", nullable = false, columnDefinition = "nvarchar(255)")
    private String diaChi;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "chuc_vu", nullable = false)
    private ChucVu chucVu;
    @Enumerated(value = EnumType.ORDINAL)
    @Column(name = "ca_lam_viec", nullable = false)
    private CaLamViec caLamViec;
    @Column(name = "ten_tai_khoan", nullable = false)
    private String tenTaiKhoan;
    @Column(name = "mat_khau", nullable = false)
    private String matKhau;
    @Column(name = "hinh_anh", nullable = false)
    private String hinhAnh;
    @Enumerated(value = EnumType.STRING)
    @Column(name = "tinh_trang", nullable = false)
    private TinhTrangNhanVien tinhTrang;

    public NhanVien(String maNV, String hoTen, String soDienThoai, boolean gioiTinh, LocalDate ngaySinh, String email, String diaChi, ChucVu chucVu, CaLamViec caLamViec, String tenTaiKhoan, String matKhau, String hinhAnh, TinhTrangNhanVien tinhTrang) throws Exception {
        this.setMaNV(maNV);
        this.setHoTen(hoTen);
        this.setSoDienThoai(soDienThoai);
        this.setGioiTinh(gioiTinh);
        this.setNgaySinh(ngaySinh);
        this.setEmail(email);
        this.setDiaChi(diaChi);
        this.setChucVu(chucVu);
        this.setCaLamViec(caLamViec);
        this.setTenTaiKhoan(tenTaiKhoan);
        this.setMatKhau(matKhau);
        this.setHinhAnh(hinhAnh);
        this.setTinhTrang(tinhTrang);
    }

    public NhanVien() {

    }

    public void setMaNV(String maNV) {
        this.maNV = maNV;
    }

    /**
     * Set họ và tên không được rỗng, nếu họ và tên rỗng thì sẽ xuất ra exception "Họ tên không được rỗng!"
     * Họ tên chỉ được chứa những chữ cái
     * Nếu họ và tên có ký tự khác chữ cái sẽ xuất ra exception "Họ và tên không được chứa ký tự số và ký tự đặc biệt!"
     */
    public void setHoTen(String hoTen) throws Exception {
        System.out.println(hoTen);
        if(!(hoTen.equalsIgnoreCase(""))){
            if(!(hoTen.matches(".+\\D[ ]{1,}.+\\D$")))
                throw new Exception("Họ và tên không được chứa ký tự số và ký tự đặc biệt");
            else
                this.hoTen = hoTen;
        }

        else{
            JOptionPane.showMessageDialog(null, "Họ tên không được rỗng!");
            throw new Exception("Họ tên không được rỗng!");
        }
    }

    /**
     * Set số điện thoại không được rỗng, nếu số điện thoại rỗng thì sẽ xuất ra exception "Số điện thoại không được rỗng!" <br></br>
     * Số điện thoại phải bắt đầu bằng số 0 hoặc +84 và kèm theo 9 số <br></br>
     * Nếu số điện thoại có ký tự khác số sẽ xuất ra exception "Số điện thoại phải bắt đầu bằng (0) hoặc (+84) và kèm theo 9 số!"
     */
    public void setSoDienThoai(String soDienThoai) throws Exception {
        if(!(soDienThoai.equalsIgnoreCase(""))) {
            if(!(soDienThoai.matches("(^0\\d{9}|^(\\+84)\\d{9})"))) {
                JOptionPane.showMessageDialog(null, "Số điện thoại phải bắt đầu bằng (0) hoặc (+84) và kèm theo 9 số!");
                throw new Exception("Số điện thoại phải bắt đầu bằng (0) hoặc (+84) và kèm theo 9 số!");
            }else
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
     * Nếu ngày sinh - ngày hiện tại <18 thì xuất ra exception "Số tuổi của nhân viên phải lớn hơn 18!"
     */
    public void setNgaySinh(LocalDate ngaySinh) throws Exception {
        try{
            if(Period.between(ngaySinh, LocalDate.now()).getYears()>=18)
                this.ngaySinh = ngaySinh;
            else {
                JOptionPane.showMessageDialog(null, "Số tuổi của nhân viên phải lớn hơn 18!");
                throw new Exception("Số tuổi của nhân viên phải lớn hơn 18!");
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null, "Ngày sinh không được rỗng!");
            throw new Exception("Ngày sinh không được rỗng!");
        }

    }

    /**
     * Set email nhà cung cấp không được rỗng <br></br>
     * Nếu email nhà cung cấp rỗng thì sẽ xuất ra exception "Email không được rỗng!" <br></br>
     * Kiểm tra email có đúng định dạng hay không <br></br>
     * Nếu email sai định dạng thì sẽ xuất ra exception "Email phải có định dạng [kí tự]@gmail.com. Ví dụ: 'abc@gmail.com' !"
     */
    public void setEmail(String email) throws Exception {
        if(!(email.equalsIgnoreCase(""))){
            if(!(email.matches("([\\w{1,}][.][\\w{1,}]|[\\w{1,}])+@gmail\\.com$"))){
                JOptionPane.showMessageDialog(null, "Email phải có định dạng [kí tự]@gmail.com. Ví dụ: 'abc@gmail.com'");
                throw new Exception("Email phải có định dạng [kí tự]@gmail.com. Ví dụ: 'abc@gmail.com' !");
            } else
                this.email = email;
        }
        else{
            JOptionPane.showMessageDialog(null, "Email không được rỗng!");
            throw new Exception("Email không được rỗng!");
        }
    }

    public void setDiaChi(String diaChi) throws Exception {
        if(!(diaChi.equalsIgnoreCase("")))
            this.diaChi = diaChi;
        else{
            JOptionPane.showMessageDialog(null, "Địa chỉ không được rỗng!");
            throw new Exception("Địa chỉ không được rỗng!");
        }
    }

    public void setChucVu(ChucVu chucVu) {
        this.chucVu = chucVu;
    }

    public void setCaLamViec(CaLamViec caLamViec) {
        this.caLamViec = caLamViec;
    }

    public void setTenTaiKhoan(String tenTaiKhoan) {
        this.tenTaiKhoan = tenTaiKhoan;
    }

    /**
     * set mật khẩu không được rỗng, nếu mật khẩu rỗng thì sẽ xuất ra exception "Mật khẩu không được rỗng!" <br></br>
     * mật khẩu bao gỗm chữ và số, phải có trên 8 ký tự <br></br>
     * nếu mật khẩu ít hơn 8 ký tự sẽ xuất ra exception "Mật khẩu phải có từ 8 ký tự trở lên!"
     */
    public void setMatKhau(String matKhau) throws Exception {
        if(!(matKhau.equalsIgnoreCase(""))){
            if(!(matKhau.matches("[\\w]{8,}"))) {
                JOptionPane.showMessageDialog(null, "Mật khẩu phải có từ 8 ký tự trở lên!");
                throw new Exception("Mật khẩu phải có từ 8 ký tự trở lên!");
            }else
                this.matKhau = matKhau;
        }
        else {
            JOptionPane.showMessageDialog(null, "Mật khẩu không được rỗng!");
            throw new Exception("Mật khẩu không được rỗng!");
        }
    }

    public void setHinhAnh(String hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public void setTinhTrang(TinhTrangNhanVien tinhTrang) {
        this.tinhTrang = tinhTrang;
    }
}
