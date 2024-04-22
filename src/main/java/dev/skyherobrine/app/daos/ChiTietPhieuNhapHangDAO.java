package dev.skyherobrine.app.daos;

import dev.skyherobrine.app.entities.order.ChiTietPhieuNhapHang;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Interface này được dùng để hiện thực và xử lý các phương thức liên quan đến vấn đề truy vấn trong
 * CSDL để lấy dữ liệu.<br>
 * Interface này cung cấp đầy đủ các loại tương tác dữ liệu: Insert - chèn dữ liệu, Update - cập nhật
 * , Delete - xoá (nhưng bản chất là cập nhật tình trạng), Select - lấy và xem dữ liệu.
 * @author Trương Dương Minh Nhật
 * @param <T>
 * @see T
 * @version 3.0
 */
public interface ChiTietPhieuNhapHangDAO<T> extends Remote {
    /**
     * Chèn thêm dữ liệu vào trong CSDL.
     * @param t
     * @return <b>true</b> nếu chèn dữ liệu thành công, <b>false</b> nếu không thể nào chèn được dữ
     * liệu.
     * @throws Exception phương thức này sẽ ném ngoại lệ nếu rơi một trong những trường hợp: <u>sai
     * cú pháp</u>, <u>không kết nối SQL được</u>, <u>trùng dữ liệu khoá chính</u>
     * @since 1.0
     */
    boolean them(T t) throws Exception;

    /**
     * Cập nhật dữ liệu của một hoặc nhiều dữ liệu khác nhau tuỳ thuộc vào đối tượng đó có chứa mã hay
     * không? Nếu đối tượng truyền vào không có chứa mã, tức là mã null (nếu mã là con số mã truyền số
     * âm) thì khi cập nhật sẽ cập nhật cho nhiều dữ liệu khác nhau, nếu có mã thì chỉ cập nhật theo mã.
     * @param target
     * @return {@link Integer} trả về số nguyên, chứa số lượng dòng bị ảnh hưởng.
     * @throws Exception phương thức này sẽ ném ngoại lệ nếu rơi một trong những trường hợp: <u>sai
     * cú pháp</u>, <u>không kết nối SQL được</u>, <u>không tìm thấy dữ liệu để cập nhật</u>
     * @since 1.0
     */
    boolean capNhat(T target) throws Exception;

    /**
     * Xoá dữ liệu của một đối tượng thông qua mã ID của đối tượng đó.
     * @param id
     * @return <b>true</b> - nếu tìm được đối tượng và đã cập nhật hay xoá (tuỳ tình huống) dữ liệu,
     * hoặc <b>false</b> - nếu không tìm được đối tượng
     * @throws Exception phương thức này sẽ ném ngoại lệ nếu rơi một trong những trường hợp: <u>sai
     * cú pháp</u>, <u>không kết nối SQL được</u>, <u>không tìm thấy dữ liệu để xoá</u>
     * @since 1.0
     */
    boolean xoa(String id) throws Exception;

    /**
     * Xoá dữ liệu của nhiều đối tượng không qua nhiều ID khác nhau. Phương thức sẽ thực hiện truy vấn
     * xoá các dòng dữ liệu của bảng trong CSDL theo mảng chứa các ID của tham số.
     * @param ids chứa danh sách tất cả các mã ID của dữ liệu.
     * @return {@link Integer} - trả về số nguyên, số lượng dòng bị xoá.
     * @throws Exception phương thức này sẽ ném ngoại lệ nếu rơi một trong những trường hợp: <u>sai
     * cú pháp</u>, <u>không kết nối SQL được</u>
     * @since 1.0
     */
    int xoa(String...ids) throws Exception;

    /**
     * Lấy danh sách các dữ liệu của bảng {@link T} trong CSDL. Phương thức sẽ thực hiện truy vấn lấy
     * danh sách các dữ liệu tồn tại mà không đi kèm bất kì điều kiện truy vấn nào.
     * @return {@link List} - trả về danh sách các phần từ, ứng với mỗi phần tử là một đối tượng {@link T}
     * @throws Exception phương thức này sẽ ném ngoại lệ nếu rơi một trong những trường hợp: <u>sai
     * cú pháp</u>, <u>không kết nối SQL được</u>
     * @since 1.0
     */
    List<T> timKiem() throws Exception;

    /**
     * Lấy danh sách các dữ liệu của bảng {@link T} trong CSDL và lấy một số giá trị của thuộc tình trong
     * bảng. Phương thức này sẽ thực hiện truy vấn lấy danh sách các dữ liệu ứng với số thuộc tính cần thiết
     * để lấy. Ngoài ra có thể lọc các dữ liệu theo điều kiện.<br><br>
     * <i>(!) -> Lưu ý rằng các tên cột trong tham số thứ 2 phải trùng với các tên cột trong <b>Key</b> của {@link Map}</i>
     * @param conditions là <b>{@link Map}</b> thuộc <b>{@link java.util.Collection}</b>, dùng để chứa danh sách
     * các tên cột {@link String} và ứng với giá trị {@link Object}, các phần tử này sẽ là những điều kiện để có
     * thể lọc dữ liệu.<br>
     * @param isDuplicateResult là tham số cho phép kết quả của dữ liệu truy vấn trả về có được phép trùng lặp hay không?<br>
     * @param colNames là danh sách các phần tử theo kiểu {@link String} dùng để chứa các tên cột, khi lấy dữ liệu
     * sẽ lấy dữ liệu theo các tên cột tương ứng.
     * @return {@link List} chứ danh sách và mỗi phần tử là {@link Map}, mỗi phần tử trong danh sách sẽ có 2
     * trường <b>Key</b> và <b>Value</b>. Trường <b>Key</b> sẽ giữ vai trò chứa tên cột và trường <b>Value</b>
     * sẽ giữ vai trò là giá trị lưu trữ ứng với tên cột đó.
     * @throws Exception phương thức sẽ ném ngoại lệ nếu rơi một trong những trường hợp: <u>sai cú pháp</u>,
     * <u>không thể kết nối SQL được</u>
     * @since 3.0
     */
    List<Map<String, Object>> timKiem(Map<String, Object> conditions, boolean isDuplicateResult, String...colNames) throws Exception;

    /**
     * Lấy danh sách các dữ liệu của bảng {@link T} trong CSDL theo điều kiện trong tham số. Phương thức
     * này sẽ thực hiện truy vấn lấy danh sách các dữ liệu thoả mãn điều kiện trong câu truy vấn.
     * @param conditions là một {@link Map} thuộc {@link java.util.Collection} chứa danh sách các phần tử
     * tuân theo quy tắc <b>Key - Value</b>.
     * @return {@link List} - danh sách các phần tử khớp với câu lệnh truy vấn CSDL. Mỗi một phần tử trong
     * danh sách ứng với đối tượng {@link T}
     * @throws Exception phương thức này sẽ ném ngoại lệ nếu rơi một trong những trường hợp: <u>sai
     * cú pháp</u>, <u>không kết nối SQL được</u>
     * @since 1.0
     */
    List<T> timKiem(Map<String, Object> conditions) throws Exception;

    /**
     * Tìm kiếm một đối tượng theo mã id trong CSDL.
     *
     * @param id
     * @return {@link Optional} - một đối tượng container chứa một giá trị mang {@link T}, đối tượng container
     * sẽ khử việc kết quả trả về bị null, để tránh tình trạng chương trình bị exception về {@link NullPointerException}
     * @throws Exception phương thức này sẽ ném ngoại lệ nếu rơi một trong những trường hợp: <u>sai
     *                   cú pháp</u>, <u>không kết nối SQL được</u>
     * @since 1.0
     */
    T timKiem(String id) throws Exception;

    /**
     * Lấy danh sách các dữ liệu của bảng {@link T} trong CSDL theo các mã ID đã được liệt kê trong tham
     * số. Phương thức này sẽ truy vấn, tìm kiếm các dữ liệu khớp với một trong danh sách các mã trong
     * truy vấn.
     * @param ids
     * @return {@link List} - danh sách các phần tử, mỗi phần tử ứng với đối tượng {@link T}
     * @throws Exception phương thức này sẽ ném ngoại lệ nếu rơi một trong những trường hợp: <u>sai
     * cú pháp</u>, <u>không kết nối SQL được</u>
     * @since 1.0
     */
    List<T> timKiem(String...ids) throws Exception;
    List<T> timKiemHaiBang(Map<String, Object> conditions) throws RemoteException;
    List<ChiTietPhieuNhapHang> timKiem(String maPhieuNhap, String maSP) throws RemoteException;
}
