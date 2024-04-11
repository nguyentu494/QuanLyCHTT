package dev.skyherobrine.app.daos.product;

import dev.skyherobrine.app.daos.ConnectDB;
import dev.skyherobrine.app.daos.IDAO;
import dev.skyherobrine.app.entities.product.DanhMucSanPham;
import dev.skyherobrine.app.entities.product.LoaiSanPham;
import dev.skyherobrine.app.entities.product.ThuongHieu;
import dev.skyherobrine.app.enums.TinhTrangThuongHieu;

import javax.swing.text.html.Option;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class LoaiSanPhamDAO implements IDAO<LoaiSanPham> {
    private ConnectDB connectDB;

    public LoaiSanPhamDAO() throws Exception {
        connectDB = new ConnectDB();
    }

    @Override
    public boolean them(LoaiSanPham loaiSanPham) throws Exception {
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
                ("Insert LoaiSanPham values(?, ?, ?)");
        preparedStatement.setString(1, loaiSanPham.getMaLoai());
        preparedStatement.setString(2, loaiSanPham.getTenLoai());
        preparedStatement.setString(3, loaiSanPham.getDanhMucSanPham().getMaDM());

        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    public boolean capNhat(LoaiSanPham target) throws Exception {
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
                ("Update LoaiSanPham set TenLoai = ?, MaDM = ? where MaLoai = ?");
        preparedStatement.setString(1, target.getTenLoai());
        preparedStatement.setString(2, target.getDanhMucSanPham().getMaDM());
        preparedStatement.setString(3, target.getMaLoai());

        return preparedStatement.executeUpdate() > 0;
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
    public List<LoaiSanPham> timKiem() throws Exception {
        ResultSet resultSet = connectDB.getConnection().createStatement().executeQuery
                ("select * from LoaiSanPham");
        List<LoaiSanPham> loaiSanPhams = new ArrayList<>();
        while (resultSet.next()) {
            LoaiSanPham loaiSanPham = new LoaiSanPham(resultSet.getString("MaLoai"),
                    resultSet.getString("TenLoai"),
                    new DanhMucSanPhamDAO().timKiem(resultSet.getString("MaDM")).get());

            loaiSanPhams.add(loaiSanPham);
        }
        return loaiSanPhams;
    }

    @Override
    public List<LoaiSanPham> timKiem(Map<String, Object> conditions) throws Exception {
        AtomicReference<String> query = new AtomicReference<>
                ("select * from LoaiSanPham lsp where ");
        AtomicBoolean isNeedAnd = new AtomicBoolean(false);

        conditions.forEach((column, value) -> {
            query.set(query.get() + (isNeedAnd.get() ? " and " : "") + ("lsp." + column + " = N'" + value + "'"));
            isNeedAnd.set(true);
        });

        List<LoaiSanPham> loaiSanPhams = new ArrayList<>();
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query.get());
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            LoaiSanPham loaiSanPham = new LoaiSanPham(resultSet.getString("MaLoai"),
                    resultSet.getString("TenLoai"),
                    new DanhMucSanPhamDAO().timKiem(resultSet.getString("MaDM")).get());

            loaiSanPhams.add(loaiSanPham);
        }
        return loaiSanPhams;
    }

    @Override
    public Optional<LoaiSanPham> timKiem(String id) throws Exception {
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
                ("select * from LoaiSanPham where MaLoai = ?");
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            return Optional.of(new LoaiSanPham(resultSet.getString("MaLoai"), resultSet.getString("TenLoai"),
                    new DanhMucSanPhamDAO().timKiem(resultSet.getString("MaDM")).get()));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<LoaiSanPham> timKiem(String... ids) throws Exception {
        String query = "select * from LoaiSanPham where ";
        String[] listID = (String[]) Arrays.stream(ids).toArray();
        for (int i = 0; i < listID.length; ++i) {
            query += ("MaLoai = '" + listID[i] + "'");
            if ((i + 1) >= listID.length) break;
            else query += ", ";
        }

        List<LoaiSanPham> loaiSanPhams = new ArrayList<>();
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            LoaiSanPham loaiSanPham = new LoaiSanPham(resultSet.getString("MaLoai"),
                    resultSet.getString("TenLoai"),
                    new DanhMucSanPhamDAO().timKiem(resultSet.getString("MaDM")).get());

            loaiSanPhams.add(loaiSanPham);
        }
        return loaiSanPhams;
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

        query.set(query.get() + " from LoaiSanPham where ");

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
