package dev.skyherobrine.app.daos.order;

import dev.skyherobrine.app.daos.ConnectDB;
import dev.skyherobrine.app.daos.IDAO;
import dev.skyherobrine.app.daos.product.SanPhamDAO;
import dev.skyherobrine.app.entities.order.ChiTietPhieuNhapHang;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ChiTietPhieuNhapHangDAO implements IDAO<ChiTietPhieuNhapHang> {
    private EntityManager em;
    public ChiTietPhieuNhapHangDAO() throws Exception{
        em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
    }

    @Override
    public boolean them(ChiTietPhieuNhapHang chiTietPhieuNhapHang) throws Exception {
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("Insert ChiTietPhieuNhap values(?, ?, ?, ?)");
//        preparedStatement.setString(1, chiTietPhieuNhapHang.getMaChiTietPhieuNhap());
//        preparedStatement.setString(2, chiTietPhieuNhapHang.getSanPham().getMaSP());
//        preparedStatement.setString(3, chiTietPhieuNhapHang.getPhieuNhapHang().getMaPhieuNhap());
//        preparedStatement.setDouble(4, chiTietPhieuNhapHang.getGiaNhap());
//
//        return preparedStatement.executeUpdate() > 0;
        return true;
    }

    @Override
    public boolean capNhat(ChiTietPhieuNhapHang target) throws Exception {
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("Update ChiTietPhieuNhap set MaChiTietPhieuNhap = ?, MaSP = ?, MaPhieuNhap = ?, GiaNhap = ? where MaChiTietPhieuNhap = ? AND MaSP = ?");
//        preparedStatement.setString(1, target.getMaChiTietPhieuNhap());
//        preparedStatement.setString(2, target.getSanPham().getMaSP());
//        preparedStatement.setString(3, target.getPhieuNhapHang().getMaPhieuNhap());
//        preparedStatement.setDouble(4, target.getGiaNhap());
//        preparedStatement.setString(5, target.getMaChiTietPhieuNhap());
//        preparedStatement.setString(6, target.getSanPham().getMaSP());
//
//        return preparedStatement.executeUpdate() > 0;
        return false;
    }

    @Override
    public boolean xoa(String id) throws Exception {
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("Delete from ChiTietPhieuNhap where MaPhieuNhap = ?");
//        preparedStatement.setString(1, id);
//
//        return preparedStatement.executeUpdate() > 0;
        return false;
    }

    @Override
    public int xoa(String... ids) throws Exception {
        return 0;
    }

    @Override
    public List<ChiTietPhieuNhapHang> timKiem() throws Exception {
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
            List<ChiTietPhieuNhapHang> chiTietPhieuNhapHangs = em.createNamedQuery("CTPNH.findAll", ChiTietPhieuNhapHang.class).getResultList();
            et.commit();
            return chiTietPhieuNhapHangs;
        }catch (Exception e){
            et.rollback();
            return null;
        }
    }

    @Override
    public List<ChiTietPhieuNhapHang> timKiem(Map<String, Object> conditions) throws Exception {
//        AtomicReference<String> query = new AtomicReference<>
//                ("select * from ChiTietPhieuNhap ctpn where ");
//        AtomicBoolean isNeedAnd = new AtomicBoolean(false);
//        conditions.forEach((column, value) -> {
//            query.set(query.get() + (isNeedAnd.get() ? " and " : "") + ("ctpn." + column + " like '" + value +"'"));
//            isNeedAnd.set(true);
//        });

        List<ChiTietPhieuNhapHang> chiTietPhieuNhapHangs = new ArrayList<>();
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query.get());
//        ResultSet resultSet = preparedStatement.executeQuery();
//        while (resultSet.next()) {
//            ChiTietPhieuNhapHang chiTietPhieuNhapHang = new ChiTietPhieuNhapHang(
//                    resultSet.getString("MaChiTietPhieuNhap"),
//                    new PhieuNhapHangDAO().timKiem(resultSet.getString("MaPhieuNhap")).get(),
//                    new SanPhamDAO().timKiem(resultSet.getString("MaSP")).get(),
//                    resultSet.getDouble("GiaNhap"));
//
//            chiTietPhieuNhapHangs.add(chiTietPhieuNhapHang);
//        }
        return chiTietPhieuNhapHangs;
    }

    @Override
    public Optional<ChiTietPhieuNhapHang> timKiem(String id) throws Exception {
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("select * from ChiTietPhieuNhap where MaChiTietPhieuNhap = ?");
//        preparedStatement.setString(1, id);
//        ResultSet resultSet = preparedStatement.executeQuery();
//        if (resultSet.next()) {
//            return Optional.of(new ChiTietPhieuNhapHang(
//                    resultSet.getString("MaChiTietPhieuNhap"),
//                    new PhieuNhapHangDAO().timKiem(resultSet.getString("MaPhieuNhap")).get(),
//                    new SanPhamDAO().timKiem(resultSet.getString("MaSP")).get(),
//                    resultSet.getDouble("GiaNhap")
//            ));
//        }
        return Optional.empty();
    }

    @Override
    public List<ChiTietPhieuNhapHang> timKiem(String... ids) throws Exception {
        return null;
    }

    public Optional<ChiTietPhieuNhapHang> timKiem(String maPhieuNhap, String maSP) throws Exception{
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("select * from ChiTietPhieuNhap where MaPhieuNhap = ? and MaSP = ?");
//        preparedStatement.setString(1, maPhieuNhap);
//        preparedStatement.setString(2, maSP);
//        ResultSet resultSet = preparedStatement.executeQuery();
//        if(resultSet.next()) {
//            return Optional.of(new ChiTietPhieuNhapHang(resultSet.getString("MaChiTietPhieuNhap"),
//                    new PhieuNhapHangDAO().timKiem(resultSet.getString("MaPhieuNhap")).get(),
//                    new SanPhamDAO().timKiem(resultSet.getString("MaSP")).get(),
//                    resultSet.getDouble("GiaNhap")));
//        } else {
            return Optional.empty();
//        }
    }

    @Override
    public List<Map<String, Object>> timKiem(Map<String, Object> conditions, boolean isDuplicateResult, String... colNames) throws Exception {
//        AtomicReference<String> query = new AtomicReference<>("select " + (isDuplicateResult ? "distinct " : ""));
//        AtomicBoolean canPhay = new AtomicBoolean(false);
//        AtomicBoolean canAnd = new AtomicBoolean(false);
//
//        Arrays.stream(colNames).forEach(column -> {
//            query.set(query.get() + (canPhay.get() ? "," : "") + column);
//            canPhay.set(true);
//        });
//
//        query.set(query.get() + " from ChiTietPhieuNhap where ");
//
//        conditions.forEach((column, value) -> {
//            query.set(query.get() + (canAnd.get() ? " AND " : "") + column + " like '%" + value + "%'");
//            canAnd.set(true);
//        });
//
//        ResultSet resultSet = connectDB.getConnection().createStatement().executeQuery(query.get());

        List<Map<String, Object>> listResult = new ArrayList<>();
//        while(resultSet.next()){
//            Map<String, Object> rowDatas = new HashMap<>();
//            for(String column : Arrays.stream(colNames).toList()) {
//                rowDatas.put(column, resultSet.getString(column));
//            }
//            listResult.add(rowDatas);
//        }
        return listResult;
    }
}
