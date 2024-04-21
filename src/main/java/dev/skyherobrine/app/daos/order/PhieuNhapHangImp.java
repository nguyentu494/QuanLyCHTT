package dev.skyherobrine.app.daos.order;

import dev.skyherobrine.app.daos.PhieuNhapHangDAO;
import dev.skyherobrine.app.entities.order.PhieuNhapHang;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class PhieuNhapHangImp extends UnicastRemoteObject implements PhieuNhapHangDAO<PhieuNhapHang> {
    private EntityManager em;
    public PhieuNhapHangImp() throws RemoteException {
        em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
    }
    @Override
    public boolean them(PhieuNhapHang phieuNhapHang) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(phieuNhapHang);
            et.commit();
            return true;
        }catch (Exception e){
            et.rollback();
            return false;
        }
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("Insert PhieuNhapHang values(?, ?, ?, ?, ?, ?)");
//        preparedStatement.setString(1, phieuNhapHang.getMaPhieuNhap());
//        preparedStatement.setString(2, phieuNhapHang.getNhaCungCap().getMaNCC());
//        preparedStatement.setTimestamp(3, Timestamp.valueOf(phieuNhapHang.getNgayHenGiao()));
//        preparedStatement.setTimestamp(4, Timestamp.valueOf(phieuNhapHang.getNgayLapPhieu()));
//        preparedStatement.setString(5, phieuNhapHang.getGhiChu());
//        preparedStatement.setString(6, phieuNhapHang.getTinhTrang().toString());
//
//        return preparedStatement.executeUpdate() > 0;
//        return false;
    }

    @Override
    public boolean capNhat(PhieuNhapHang target) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.merge(target);
            et.commit();
            return true;
        }catch (Exception e){
            et.rollback();
            return false;
        }
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("Update PhieuNhapHang set MaPhieuNhap = ?, MaNCC = ?, NgayHenGiao = ?, NgayLapPhieu = ?, GhiChu = ?, TinhTrang = ? where MaPhieuNhap = ?");
//        preparedStatement.setString(1, target.getMaPhieuNhap());
//        preparedStatement.setString(2, target.getNhaCungCap().getMaNCC());
//        preparedStatement.setTimestamp(3, Timestamp.valueOf(target.getNgayHenGiao()));
//        preparedStatement.setTimestamp(4, Timestamp.valueOf(target.getNgayLapPhieu()));
//        preparedStatement.setString(5, target.getGhiChu());
//        preparedStatement.setString(6, target.getTinhTrang().toString());
//        preparedStatement.setString(7, target.getMaPhieuNhap());
//        return preparedStatement.executeUpdate() > 0;
//        return false;
    }

    @Override
    public boolean xoa(String id) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            PhieuNhapHang phieuNhapHang = em.find(PhieuNhapHang.class, id);
            em.remove(phieuNhapHang);
            et.commit();
            return true;
        }catch (Exception e) {
            et.rollback();
            return false;
        }
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("Delete from PhieuNhapHang where MaPhieuNhap = ?");
//        preparedStatement.setString(1, id);
//
//        return preparedStatement.executeUpdate() > 0;
//        return false;
    }

    @Override
    public int xoa(String... ids) throws RemoteException {
        return 0;
    }

    @Override
    public List<PhieuNhapHang> timKiem() throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
            List<PhieuNhapHang> phieuNhapHangs = em.createNamedQuery("PNH.findAll", PhieuNhapHang.class).getResultList();
            et.commit();
            return phieuNhapHangs;
        }catch (Exception e){
            et.rollback();
            return null;
        }
    }

    @Override
    public List<PhieuNhapHang> timKiem(Map<String, Object> conditions) throws RemoteException {
        AtomicReference<String> query = new AtomicReference<>
                ("select * from PhieuNhapHang pnh where ");
        AtomicBoolean isNeedAnd = new AtomicBoolean(false);

        conditions.forEach((column, value) -> {
            query.set(query.get() + (isNeedAnd.get() ? " and " : "") + ("pnh." + column + " like '" + value + "'"));
            isNeedAnd.set(true);
        });
        List<PhieuNhapHang> phieuNhapHangs = new ArrayList<>();
        try {
            phieuNhapHangs = em.createNativeQuery(query.get(), PhieuNhapHang.class).getResultList();
            return phieuNhapHangs;
        }catch (Exception e){
            return null;
        }
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query.get());
//        ResultSet result = preparedStatement.executeQuery();
//        while(result.next()) {
//            PhieuNhapHang phieuNhapHang = new PhieuNhapHang(result.getString("MaPhieuNhap"),
//                    new NhaCungCapDAO().timKiem(result.getString("MaNCC")).get(),
//                    result.getTimestamp("NgayLapPhieu").toLocalDateTime(),
//                    result.getTimestamp("NgayHenGiao").toLocalDateTime(),
//                    result.getString("GhiChu"),
//                    TinhTrangNhapHang.layGiaTri(result.getString("TinhTrang")));
//
//            phieuNhapHangs.add(phieuNhapHang);
//        }
//        return phieuNhapHangs;
    }

    @Override
    public PhieuNhapHang timKiem(String id) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
            PhieuNhapHang phieuNhapHang = em.createNamedQuery("PNH.findByID", PhieuNhapHang.class).setParameter("id", id).getSingleResult();
            et.commit();
            return Optional.of(phieuNhapHang).get();
        }catch (Exception e){
            et.rollback();
            return (PhieuNhapHang) Optional.empty().get();
        }
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("select * from PhieuNhapHang where MaPhieuNhap = ?");
//        preparedStatement.setString(1, id);
//        ResultSet resultSet = preparedStatement.executeQuery();
//        if(resultSet.next()) {
//            return Optional.of(new PhieuNhapHang(resultSet.getString("MaPhieuNhap"),
//                    new NhaCungCapDAO().timKiem(resultSet.getString("MaNCC")).get(),
//                    resultSet.getTimestamp("NgayLapPhieu").toLocalDateTime(),
//                    resultSet.getTimestamp("NgayHenGiao").toLocalDateTime(),
//                    resultSet.getString("GhiChu"),
//                    TinhTrangNhapHang.layGiaTri(resultSet.getString("TinhTrang"))));
//        } else {
//            return Optional.empty();
//        }
    }

    @Override
    public List<PhieuNhapHang> timKiem(String... ids) throws RemoteException {
        String query = "select * from PhieuNhapHang where ";
        String[] listID = (String[]) Arrays.stream(ids).toArray();
        for(int i = 0; i < listID.length; ++i) {
            query += ("ma_phieu_nhap = '" + listID[i] + "'");
            if((i + 1) >= listID.length) break;
            else query += ", ";
        }

        List<PhieuNhapHang> phieuNhapHangs = new ArrayList<>();
        try {
            phieuNhapHangs = em.createNativeQuery(query, PhieuNhapHang.class).getResultList();
            return phieuNhapHangs;
        }catch (Exception e){
            return null;
        }
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query);
//        ResultSet resultSet = preparedStatement.executeQuery();
//        while(resultSet.next()) {
//            PhieuNhapHang phieuNhapHang = new PhieuNhapHang(resultSet.getString("MaPhieuNhap"),
//                    new NhaCungCapDAO().timKiem(resultSet.getString("MaNCC")).get(),
//                    resultSet.getTimestamp("NgayLapPhieu").toLocalDateTime(),
//                    resultSet.getTimestamp("NgayHenGiao").toLocalDateTime(),
//                    resultSet.getString("GhiChu"),
//                    TinhTrangNhapHang.layGiaTri(resultSet.getString("TinhTrang")));
//
//            phieuNhapHangs.add(phieuNhapHang);
//        }
//        return phieuNhapHangs;
    }

    @Override
    public List<Map<String, Object>> timKiem(Map<String, Object> conditions, boolean isDuplicateResult, String... colNames) throws RemoteException {
        AtomicReference<String> query = new AtomicReference<>("select " + (isDuplicateResult ? "distinct " : ""));
        AtomicBoolean canPhay = new AtomicBoolean(false);
        AtomicBoolean canAnd = new AtomicBoolean(false);

        Arrays.stream(colNames).forEach(column -> {
            query.set(query.get() + (canPhay.get() ? "," : "") + column);
            canPhay.set(true);
        });

        query.set(query.get() + " from PhieuNhapHang where ");
        conditions.forEach((column, value) -> {
            query.set(query.get() + (canAnd.get() ? " AND " : "") + column + " like '%" + value + "%'");
            canAnd.set(true);
        });

        List<Map<String, Object>> listResult = new ArrayList<>();
        Query q = em.createNativeQuery(query.get());
        List<Object[]> results = q.getResultList();
        for (Object[] result : results) {
            Map<String, Object> rowDatas = new HashMap<>();
            for (int i = 0; i < colNames.length; i++) {
                rowDatas.put(colNames[i], result[i]);
            }
            listResult.add(rowDatas);
        }
        return listResult;
    }
}
