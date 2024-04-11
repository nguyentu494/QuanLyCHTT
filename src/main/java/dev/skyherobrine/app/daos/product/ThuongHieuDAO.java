package dev.skyherobrine.app.daos.product;

import dev.skyherobrine.app.daos.ConnectDB;
import dev.skyherobrine.app.daos.IDAO;
import dev.skyherobrine.app.daos.person.NhanVienDAO;
import dev.skyherobrine.app.entities.product.LoaiSanPham;
import dev.skyherobrine.app.entities.product.ThuongHieu;
import dev.skyherobrine.app.enums.TinhTrangThuongHieu;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ThuongHieuDAO implements IDAO<ThuongHieu> {
    private ConnectDB connectDB;
    public ThuongHieuDAO() throws Exception{
        connectDB = new ConnectDB();
    }
    @Override
    public boolean them(ThuongHieu thuongHieu) throws Exception {
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
                ("Insert ThuongHieu values(?, ?, ?)");
        preparedStatement.setString(1, thuongHieu.getMaTH());
        preparedStatement.setString(2, thuongHieu.getTenTH());
        preparedStatement.setString(3, thuongHieu.getTinhTrang().toString());
        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    public boolean capNhat(ThuongHieu target) throws Exception {
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
    public List<ThuongHieu> timKiem() throws Exception {
        ResultSet resultSet = connectDB.getConnection().createStatement().executeQuery
                ("select * from ThuongHieu");
        List<ThuongHieu> thuongHieus = new ArrayList<>();
        while (resultSet.next()) {
            ThuongHieu thuongHieu = new ThuongHieu(resultSet.getString("MaTH"), resultSet.getString("TenTH"),
                    TinhTrangThuongHieu.layGiaTri(resultSet.getString("TinhTrang")));
            thuongHieus.add(thuongHieu);
        }
        return thuongHieus;
    }

    @Override
    public List<ThuongHieu> timKiem(Map<String, Object> conditions) throws Exception {
        AtomicReference<String> query = new AtomicReference<>
                ("select * from ThuongHieu th where ");
        AtomicBoolean isNeedAnd = new AtomicBoolean(false);

        conditions.forEach((column, value) -> {
            query.set(query.get() + (isNeedAnd.get() ? " and " : "") + ("th." + column + " = N'" + value + "'"));
            isNeedAnd.set(true);
        });

        List<ThuongHieu> thuongHieus = new ArrayList<>();
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query.get());
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            ThuongHieu thuongHieu = new ThuongHieu(
                    resultSet.getString("MaTH"),
                    resultSet.getString("TenTH"),
                    TinhTrangThuongHieu.layGiaTri(resultSet.getString("TinhTrang"))
            );
            thuongHieus.add(thuongHieu);
        }
        return thuongHieus;
    }

    @Override
    public Optional<ThuongHieu> timKiem(String id) throws Exception {
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
                ("select * from ThuongHieu where MaTH = ?");
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            return Optional.of(new ThuongHieu(resultSet.getString("MaTH"), resultSet.getString("TenTH"),
                    TinhTrangThuongHieu.layGiaTri(resultSet.getString("TinhTrang"))));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<ThuongHieu> timKiem(String... ids) throws Exception {
        String query = "select * from ThuongHieu where ";
        String[] listID = (String[]) Arrays.stream(ids).toArray();
        for(int i = 0; i < listID.length; ++i) {
            query += ("MaHoatDong = '" + listID[i] + "'");
            if((i + 1) >= listID.length) break;
            else query += ", ";
        }

        List<ThuongHieu> thuongHieus = new ArrayList<>();
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            ThuongHieu thuongHieu = new ThuongHieu(resultSet.getString("MaTH"),
                    resultSet.getString("TenTH"),
                    TinhTrangThuongHieu.layGiaTri(resultSet.getString("TinhTrang")));
            thuongHieus.add(thuongHieu);
        }
        return thuongHieus;
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

        query.set(query.get() + " from ThuongHieu where ");

        conditions.forEach((column, value) -> {
            query.set(query.get() + (canAnd.get() ? " AND " : "") + column + " like '%" + value + "%'");
            canAnd.set(true);
        });

        System.out.println(query);
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
}
