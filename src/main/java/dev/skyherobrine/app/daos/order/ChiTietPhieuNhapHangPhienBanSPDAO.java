package dev.skyherobrine.app.daos.order;

import dev.skyherobrine.app.daos.ConnectDB;
import dev.skyherobrine.app.daos.IDAO;
import dev.skyherobrine.app.daos.product.ChiTietPhienBanSanPhamDAO;
import dev.skyherobrine.app.daos.product.SanPhamDAO;
import dev.skyherobrine.app.entities.order.ChiTietPhieuNhapHangPhienBanSP;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ChiTietPhieuNhapHangPhienBanSPDAO implements IDAO<ChiTietPhieuNhapHangPhienBanSP> {
    private EntityManager em;
    public ChiTietPhieuNhapHangPhienBanSPDAO() throws Exception{
        em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
    }

    @Override
    public boolean them(ChiTietPhieuNhapHangPhienBanSP chiTietPhieuNhapHangPhienBanSP) throws Exception {
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("Insert ChiTietPhieuNhapHangPhienBanSP values(?, ?, ?)");
//        preparedStatement.setString(1, chiTietPhieuNhapHangPhienBanSP.getChiTietPhieuNhapHang().getMaChiTietPhieuNhap());
//        preparedStatement.setString(2, chiTietPhieuNhapHangPhienBanSP.getChiTietPhienBanSanPham().getMaPhienBanSP());
//        preparedStatement.setInt(3, chiTietPhieuNhapHangPhienBanSP.getSoLuongNhap());
//        return preparedStatement.executeUpdate() > 0;
        return false;
    }

    @Override
    public boolean capNhat(ChiTietPhieuNhapHangPhienBanSP target) throws Exception {
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
    public List<ChiTietPhieuNhapHangPhienBanSP> timKiem() throws Exception {
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
            List<ChiTietPhieuNhapHangPhienBanSP> chiTietPhieuNhapHangPhienBanSPs = em.createNamedQuery("CTPNHPBSP.findAll", ChiTietPhieuNhapHangPhienBanSP.class).getResultList();
            et.commit();
            return chiTietPhieuNhapHangPhienBanSPs;
        }catch (Exception e){
            et.rollback();
            return null;
        }

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
//        query.set(query.get() + " from ChiTietPhieuNhapHangPhienBanSP where ");
//
//        conditions.forEach((column, value) -> {
//            query.set(query.get() + (canAnd.get() ? " AND " : "") + column + " like '%" + value + "%'");
//            canAnd.set(true);
//        });
//
//        System.out.println(query);
//        ResultSet resultSet = connectDB.getConnection().createStatement().executeQuery(query.get());
//
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

    @Override
    public List<ChiTietPhieuNhapHangPhienBanSP> timKiem(Map<String, Object> conditions) throws Exception {
        return null;
    }

    @Override
    public Optional<ChiTietPhieuNhapHangPhienBanSP> timKiem(String id) throws Exception {
        return Optional.empty();
    }

    @Override
    public List<ChiTietPhieuNhapHangPhienBanSP> timKiem(String... ids) throws Exception {
        return null;
    }
}
