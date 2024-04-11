package dev.skyherobrine.app.daos.product;

import dev.skyherobrine.app.daos.ConnectDB;
import dev.skyherobrine.app.daos.IDAO;
import dev.skyherobrine.app.entities.product.ChiTietPhienBanSanPham;
import dev.skyherobrine.app.enums.MauSac;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ChiTietPhienBanSanPhamDAO implements IDAO<ChiTietPhienBanSanPham> {
    private ConnectDB connectDB;
    public ChiTietPhienBanSanPhamDAO() throws Exception {
        connectDB = new ConnectDB();
    }

    @Override
    public boolean them(ChiTietPhienBanSanPham chiTietPhienBanSanPham) throws Exception {
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
                ("Insert into PhienBanSanPham(MaPhienBanSP, MaSP, MauSac, KichThuoc, SoLuong, HinhAnh) values(?, ?, ?, ?, ?, ?)");
        preparedStatement.setString(1, chiTietPhienBanSanPham.getMaPhienBanSP());
        preparedStatement.setString(2, chiTietPhienBanSanPham.getSanPham().getMaSP());
        preparedStatement.setString(3, chiTietPhienBanSanPham.getMauSac().toString());
        preparedStatement.setString(4, chiTietPhienBanSanPham.getKichThuoc());
        preparedStatement.setInt(5, chiTietPhienBanSanPham.getSoLuong());
        preparedStatement.setString(6, chiTietPhienBanSanPham.getHinhAnh());

        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    public boolean capNhat(ChiTietPhienBanSanPham target) throws Exception {
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
                ("Update PhienBanSanPham set MaPhienBanSP = ?, MaSP = ?, MauSac = ?, KichThuoc = ?, SoLuong = ?, HinhAnh = ? where MaPhienBanSP = ?");
        preparedStatement.setString(1, target.getMaPhienBanSP());
        preparedStatement.setString(2, target.getSanPham().getMaSP());
        preparedStatement.setString(3, target.getMauSac().toString());
        preparedStatement.setString(4, target.getKichThuoc());
        preparedStatement.setInt(5, target.getSoLuong());
        preparedStatement.setString(6, target.getHinhAnh());
        preparedStatement.setString(7, target.getMaPhienBanSP());

        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    public boolean xoa(String id) throws Exception {
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
                ("Delete from PhienBanSanPham where MaPhienBanSP = ?");
        preparedStatement.setString(1, id);

        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    public int xoa(String... ids) throws Exception {
        return 0;
    }

    @Override
    public List<ChiTietPhienBanSanPham> timKiem() throws Exception {
        ResultSet resultSet = connectDB.getConnection().createStatement().executeQuery("select * from PhienBanSanPham");
        List<ChiTietPhienBanSanPham> chiTietPhienBanSanPhams = new ArrayList<>();
        while(resultSet.next()) {
            ChiTietPhienBanSanPham chiTietPhienBanSanPham = new ChiTietPhienBanSanPham(
                    resultSet.getString("MaPhienBanSP"),
                    new SanPhamDAO().timKiem(resultSet.getString("MaSP")).get(),
                    MauSac.layGiaTri(resultSet.getString("MauSac")),
                    resultSet.getString("KichThuoc"),
                    resultSet.getInt("SoLuong"),
                    resultSet.getString("HinhAnh")
            );

            chiTietPhienBanSanPhams.add(chiTietPhienBanSanPham);
        }
        return chiTietPhienBanSanPhams;
    }

    @Override
    public List<ChiTietPhienBanSanPham> timKiem(Map<String, Object> conditions) throws Exception {
        AtomicReference<String> query = new AtomicReference<>
                ("select * from PhienBanSanPham t where ");
        AtomicBoolean isNeedAnd = new AtomicBoolean(false);

        conditions.forEach((column, value) -> {
            query.set(query.get() + (isNeedAnd.get() ? " and " : "") + ("t." + column + " like '%" + value + "%'"));
            isNeedAnd.set(true);
        });

        List<ChiTietPhienBanSanPham> chiTietPhienBanSanPhams = new ArrayList<>();
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query.get());
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            ChiTietPhienBanSanPham chiTietPhienBanSanPham = new ChiTietPhienBanSanPham(
                    resultSet.getString("MaPhienBanSP"),
                    new SanPhamDAO().timKiem(resultSet.getString("MaSP")).get(),
                    MauSac.layGiaTri(resultSet.getString("MauSac")),
                    resultSet.getString("KichThuoc"),
                    resultSet.getInt("SoLuong"),
                    resultSet.getString("HinhAnh")
            );

            chiTietPhienBanSanPhams.add(chiTietPhienBanSanPham);
        }
        return chiTietPhienBanSanPhams;
    }

    @Override
    public Optional<ChiTietPhienBanSanPham> timKiem(String id) throws Exception {
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
                ("select * from PhienBanSanPham where MaPhienBanSP = ?");
        preparedStatement.setString(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            return Optional.of(new ChiTietPhienBanSanPham(resultSet.getString("MaPhienBanSP"), new SanPhamDAO().timKiem(resultSet.getString("MaSP")).get(),
                    MauSac.layGiaTri(resultSet.getString("MauSac")), resultSet.getString("KichThuoc"), resultSet.getInt("SoLuong"), resultSet.getString("HinhAnh")));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<ChiTietPhienBanSanPham> timKiem(String... ids) throws Exception {
        return null;
    }

    public Optional<ChiTietPhienBanSanPham> timKiem(String maSP, MauSac mauSac, String kichThuoc) throws Exception{
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
                ("select * from PhienBanSanPham where MaSP = ? and MauSac = ? and KichThuoc = ?");
        preparedStatement.setString(1, maSP);
        preparedStatement.setString(2, mauSac.toString());
        preparedStatement.setString(3, kichThuoc);
        ResultSet resultSet = preparedStatement.executeQuery();
        if(resultSet.next()) {
            return Optional.of(new ChiTietPhienBanSanPham(resultSet.getString("MaPhienBanSP"), new SanPhamDAO().timKiem(resultSet.getString("MaSP")).get(),
                    MauSac.layGiaTri(resultSet.getString("MauSac")), resultSet.getString("KichThuoc"), resultSet.getInt("SoLuong"), resultSet.getString("HinhAnh")));
        } else {
            return Optional.empty();
        }
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

        query.set(query.get() + " from PhienBanSanPham where ");

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
}
