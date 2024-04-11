package dev.skyherobrine.app.daos.order;

import dev.skyherobrine.app.daos.ConnectDB;
import dev.skyherobrine.app.daos.IDAO;
import dev.skyherobrine.app.daos.product.ChiTietPhienBanSanPhamDAO;
import dev.skyherobrine.app.daos.product.SanPhamDAO;
import dev.skyherobrine.app.entities.order.ChiTietPhieuTraKhachHang;
import dev.skyherobrine.app.entities.order.PhieuTraKhachHang;
import dev.skyherobrine.app.entities.person.KhachHang;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ChiTietPhieuTraKhachHangDAO implements IDAO<ChiTietPhieuTraKhachHang> {
    private ConnectDB connectDB;
    public ChiTietPhieuTraKhachHangDAO() throws Exception {
        connectDB = new ConnectDB();
    }
    @Override
    public boolean them(ChiTietPhieuTraKhachHang chiTietPhieuTraKhachHang) throws Exception {
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
                ("Insert ChiTietPhieuTraKhachHang values(?, ?, ?, ?)");
//        preparedStatement.setString(1, chiTietPhieuTraKhachHang.getPhieuTra().getMaPhieuTraKhachHang());
//        preparedStatement.setString(2, chiTietPhieuTraKhachHang.getChiTietPhienBanSanPham().getMaPhienBanSP());
        preparedStatement.setInt(3, chiTietPhieuTraKhachHang.getSoLuongTra());
        preparedStatement.setString(4, chiTietPhieuTraKhachHang.getNoiDungTra());

        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    public boolean capNhat(ChiTietPhieuTraKhachHang target) throws Exception {
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
    public List<ChiTietPhieuTraKhachHang> timKiem() throws Exception {
        ResultSet resultSet = connectDB.getConnection().createStatement().executeQuery("select * from ChiTietPhieuTraKhachHang");
        List<ChiTietPhieuTraKhachHang> chiTietPhieuTraKhachHangs = new ArrayList<>();
        while(resultSet.next()) {
            ChiTietPhieuTraKhachHang chiTietPhieuTraKhachHang = new ChiTietPhieuTraKhachHang(
//                    new PhieuTraKhachHangDAO().timKiem(resultSet.getString("MaPhieuTraKH")).get(),
//                    new ChiTietPhienBanSanPhamDAO().timKiem(resultSet.getString("MaPhienBanSP")).get(),
//                    resultSet.getInt("SoLuongTra"),
//                    resultSet.getString("NoiDungTra")
            );

            chiTietPhieuTraKhachHangs.add(chiTietPhieuTraKhachHang);
        }
        return chiTietPhieuTraKhachHangs;
    }

    @Override
    public List<ChiTietPhieuTraKhachHang> timKiem(Map<String, Object> conditions) throws Exception {
        AtomicReference<String> query = new AtomicReference<>
                ("select * from ChiTietPhieuTraKhachHang ctptkh where ");
        AtomicBoolean isNeedAnd = new AtomicBoolean(false);
        System.out.println(query.get().toString());
        conditions.forEach((column, value) -> {
            query.set(query.get() + (isNeedAnd.get() ? " and " : "") + ("ctptkh." + column + " like '%" + value + "%'"));
            isNeedAnd.set(true);
        });
        List<ChiTietPhieuTraKhachHang> chiTietPhieuTraKhachHangs = new ArrayList<>();
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query.get());
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            ChiTietPhieuTraKhachHang chiTietPhieuTraKhachHang = new ChiTietPhieuTraKhachHang(
//                    new PhieuTraKhachHangDAO().timKiem(resultSet.getString("MaPhieuTraKH")).get(),
//                    new ChiTietPhienBanSanPhamDAO().timKiem(resultSet.getString("MaPhienBanSP")).get(),
//                    resultSet.getInt("SoLuongTra"),
//                    resultSet.getString("NoiDungTra")
            );

            chiTietPhieuTraKhachHangs.add(chiTietPhieuTraKhachHang);
        }
        return chiTietPhieuTraKhachHangs;
    }

    @Override
    public Optional<ChiTietPhieuTraKhachHang> timKiem(String id) throws Exception {
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
                ("select * from ChiTietPhieuTraKhachHang where MaPhieuTraKH = ?");
        preparedStatement.setString(1, id);

        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            return Optional.of(new ChiTietPhieuTraKhachHang(
//                    new PhieuTraKhachHangDAO().timKiem(resultSet.getString("MaPhieuTraKH")).get(),
//                    new ChiTietPhienBanSanPhamDAO().timKiem(resultSet.getString("MaPhienBanSP")).get(),
//                    resultSet.getInt("SoLuongTra"),
//                    resultSet.getString("NoiDungTra")
            ));
        }
        return Optional.empty();
    }

    @Override
    public List<ChiTietPhieuTraKhachHang> timKiem(String... ids) throws Exception {
        return null;
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

        query.set(query.get() + " from ChiTietPhieuTraKhachHang where ");

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
