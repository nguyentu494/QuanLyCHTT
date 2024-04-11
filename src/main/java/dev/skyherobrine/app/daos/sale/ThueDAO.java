package dev.skyherobrine.app.daos.sale;

import dev.skyherobrine.app.daos.ConnectDB;
import dev.skyherobrine.app.daos.IDAO;
import dev.skyherobrine.app.daos.order.HoaDonDAO;
import dev.skyherobrine.app.daos.person.KhachHangDAO;
import dev.skyherobrine.app.daos.person.NhanVienDAO;
import dev.skyherobrine.app.daos.product.ChiTietPhienBanSanPhamDAO;
import dev.skyherobrine.app.entities.order.ChiTietHoaDon;
import dev.skyherobrine.app.entities.order.HoaDon;
import dev.skyherobrine.app.entities.sale.Thue;

import javax.xml.transform.Result;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ThueDAO implements IDAO<Thue> {

    private ConnectDB connectDB;

    public ThueDAO() throws Exception{
        connectDB = new ConnectDB();
    }

    @Override
    public boolean them(Thue thue) throws Exception {
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
                ("Insert Thue values(?, ?, ?, ?)");
        preparedStatement.setString(1, thue.getMaThue());
        preparedStatement.setDouble(2, thue.getGiaTri());
        preparedStatement.setTimestamp(3, Timestamp.valueOf(thue.getNgayApDung()));
        preparedStatement.setBoolean(4, thue.isHieuLuc());

        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    public boolean capNhat(Thue target) throws Exception {
        return false;
    }
    public boolean update(String HieuLuc){
        try {
            PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
                    ("update Thue set HieuLuc = ?");
            preparedStatement.setString(1, HieuLuc);
            return preparedStatement.executeUpdate() > 0;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public boolean xoa(String id) throws Exception {
        return false;
    }

    @Override
    public int xoa(String... ids) throws Exception {
        return 0;
    }

    @Override
    public List<Thue> timKiem() throws Exception {
        ResultSet resultSet = connectDB.getConnection().createStatement().executeQuery("select * from Thue");
        List<Thue> thues = new ArrayList<>();

        while(resultSet.next()) {
            Thue thue = new Thue(
                    resultSet.getString("MaThue"),
                    resultSet.getDouble("GiaTri"),
                    resultSet.getTimestamp("NgayApDung").toLocalDateTime(),
                    resultSet.getBoolean("HieuLuc")
            );

            thues.add(thue);
        }

        return thues;
    }

    @Override
    public List<Map<String, Object>> timKiem(Map<String, Object> conditions, boolean isDuplicateResult, String... colNames) throws Exception {
        AtomicReference<String> query = new AtomicReference<>("select " + (isDuplicateResult ? "distinct " : ""));
        AtomicBoolean canPhay = new AtomicBoolean(false);
        AtomicBoolean canAnd = new AtomicBoolean(false);

        Arrays.stream(colNames).forEach(column -> {
            query.set(query.get() + (canPhay.get() ? "," : "") + column);
            canPhay.set(true);
        });

        query.set(query.get() + " from Thue where ");

        conditions.forEach((column, value) -> {
            query.set(query.get() + (canAnd.get() ? " AND " : "") + column + " like '%" + value + "%'");
            canAnd.set(true);
        });

        ResultSet resultSet = connectDB.getConnection().createStatement().executeQuery(query.get());

        List<Map<String, Object>> listResult = new ArrayList<>();
        while(resultSet.next()){
            Map<String, Object> rowDatas = new HashMap<>();
            for(String column : Arrays.stream(colNames).toList()) {
                rowDatas.put(column, resultSet.getString(column));
            }
            listResult.add(rowDatas);
        }
        return listResult;
    }

    @Override
    public List<Thue> timKiem(Map<String, Object> conditions) throws Exception {
        AtomicReference<String> query = new AtomicReference<>
                ("select * from Thue cthd where ");
        AtomicBoolean isNeedAnd = new AtomicBoolean(false);

        conditions.forEach((column, value) -> {
            query.set(query.get() + (isNeedAnd.get() ? " and " : "") + ("cthd." + column + " like '%" + value + "%'"));
            isNeedAnd.set(true);
        });

        List<Thue> thues = new ArrayList<>();
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query.get());
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            Thue thue = new Thue(
                    resultSet.getString("MaThue"),
                    resultSet.getDouble("GiaTri"),
                    resultSet.getTimestamp("NgayApDung").toLocalDateTime(),
                    resultSet.getBoolean("HieuLuc")
            );

            thues.add(thue);
        }
        return thues;
    }

    @Override
    public Optional<Thue> timKiem(String id) throws Exception {
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement("select * from Thue where MaThue = ?");
        preparedStatement.setString(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            return Optional.of(new Thue(
                    resultSet.getString("MaThue"),
                    resultSet.getDouble("GiaTri"),
                    resultSet.getTimestamp("NgayApDung").toLocalDateTime(),
                    resultSet.getBoolean("HieuLuc")
            ));
        } else {
            return null;
        }
    }

    @Override
    public List<Thue> timKiem(String... ids) throws Exception {
        String query = "select * from Thue where ";
        String[] listID = (String[]) Arrays.stream(ids).toArray();
        for(int i = 0; i < listID.length; ++i) {
            query += ("MaThue = '" + listID[i] + "'");
            if((i + 1) >= listID.length) break;
            else query += ", ";
        }

        List<Thue> thues = new ArrayList<>();
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            Thue thue = new Thue(
                    resultSet.getString("MaThue"),
                    resultSet.getDouble("GiaTri"),
                    resultSet.getTimestamp("NgayApDung").toLocalDateTime(),
                    resultSet.getBoolean("HieuLuc")
            );

            thues.add(thue);
        }
        return thues;
    }
}
