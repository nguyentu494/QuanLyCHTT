package dev.skyherobrine.app.daos;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Đối tượng dùng để kết nối với hệ quản trị CSDL và thực hiện truy vấn thông qua đối tượng Connection
 * , được lấy từ phương thức getConnection().
 *
 * @see Connection
 * @author Trương Dương Minh Nhật
 * @version 1.0
 */
@Getter
public class ConnectDB {
    /**
     * -- GETTER --
     *  Lấy kết nối của CSDL, từ đây thì có thể thực hiện các câu lệnh truy vấn CSDL.
     *
     * @return {@link Connection} trả về kết nối của đối tượng
     */
    private Connection connection;

    /**
     * Thiết lập kết nối CSDL với tên database là QLCHTT
     * @throws Exception báo lỗi nếu như không kết nối được CSDL. Một số lỗi phổ biến: sai đường dẫn,
     * database không tồn tại, tài khoản hoặc mật khẩu sai hoặc không tồn tại nên không kết nối được.
     */
    public ConnectDB() throws Exception {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connection = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=QLCHTT;encrypt=false;trustServerCertificate=true", "sa", "123");

    }

}
