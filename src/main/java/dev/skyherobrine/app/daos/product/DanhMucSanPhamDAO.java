package dev.skyherobrine.app.daos.product;

import dev.skyherobrine.app.daos.ConnectDB;
import dev.skyherobrine.app.daos.IDAO;
import dev.skyherobrine.app.entities.person.NhanVien;
import dev.skyherobrine.app.entities.product.DanhMucSanPham;
import dev.skyherobrine.app.entities.product.ThuongHieu;
import dev.skyherobrine.app.enums.CaLamViec;
import dev.skyherobrine.app.enums.ChucVu;
import dev.skyherobrine.app.enums.TinhTrangNhanVien;
import dev.skyherobrine.app.enums.TinhTrangThuongHieu;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class DanhMucSanPhamDAO implements IDAO<DanhMucSanPham> {
    private ConnectDB connectDB;
    public DanhMucSanPhamDAO() throws Exception{
        connectDB = new ConnectDB();
    }
    @Override
    public boolean them(DanhMucSanPham danhMucSanPham) throws Exception {
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
                ("insert DanhMucSanPham values(?, ?)");
        preparedStatement.setString(1, danhMucSanPham.getMaDM());
        preparedStatement.setString(2, danhMucSanPham.getTenDM());
        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    public boolean capNhat(DanhMucSanPham target) throws Exception {
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
    public List<DanhMucSanPham> timKiem() throws Exception {
        ResultSet resultSet = connectDB.getConnection().createStatement().executeQuery
                ("select * from DanhMucSanPham");
        List<DanhMucSanPham> danhMucSanPhams = new ArrayList<>();
        while (resultSet.next()) {
            DanhMucSanPham danhMucSanPham = new DanhMucSanPham(resultSet.getString("MaDM"), resultSet.getString("TenDM"));
            danhMucSanPhams.add(danhMucSanPham);
        }
        return danhMucSanPhams;
    }

    @Override
    public List<DanhMucSanPham> timKiem(Map<String, Object> conditions) throws Exception {
        AtomicReference<String> query = new AtomicReference<>
                ("select * from DanhMucSanPham dm where ");
        AtomicBoolean isNeedAnd = new AtomicBoolean(false);

        conditions.forEach((column, value) -> {
            query.set(query.get() + (isNeedAnd.get() ? " and " : "") + ("dm." + column + " = N'" + value + "'"));
            isNeedAnd.set(true);
        });

        List<DanhMucSanPham> danhMucSanPhams = new ArrayList<>();
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query.get());
        ResultSet result = preparedStatement.executeQuery();
        while(result.next()) {
            DanhMucSanPham danhMucSanPham = new DanhMucSanPham(
                    result.getString("MaDM"),
                    result.getString("TenDM"));
            danhMucSanPhams.add(danhMucSanPham);
        }
        return danhMucSanPhams;
    }

    @Override
    public Optional<DanhMucSanPham> timKiem(String id) throws Exception {
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
                ("select * from DanhMucSanPham where MaDM = ?");
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            DanhMucSanPham danhMucSanPham = new DanhMucSanPham(resultSet.getString("MaDM"), resultSet.getString("TenDM"));
            return Optional.of(danhMucSanPham);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<DanhMucSanPham> timKiem(String... ids) throws Exception {
        String query = "select * from DanhMucSanPham where ";
        String[] listID = (String[]) Arrays.stream(ids).toArray();
        for(int i = 0; i < listID.length; ++i) {
            query += ("MaDM = '" + listID[i] + "'");
            if((i + 1) >= listID.length) break;
            else query += ", ";
        }

        List<DanhMucSanPham> danhMucSanPhams = new ArrayList<>();
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            DanhMucSanPham danhMucSanPham = new DanhMucSanPham(resultSet.getString("MaDM"), resultSet.getString("TenDM"));
            danhMucSanPhams.add(danhMucSanPham);
        }
        return danhMucSanPhams;
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

        query.set(query.get() + " from DanhMucSanPham where ");

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
