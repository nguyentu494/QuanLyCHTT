package dev.skyherobrine.app.daos.order;

import dev.skyherobrine.app.daos.ChiTietPhieuNhapHangPhienBanSPDAO;
import dev.skyherobrine.app.entities.order.ChiTietPhieuNhapHangPhienBanSP;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ChiTietPhieuNhapHangPhienBanSPImp extends UnicastRemoteObject implements ChiTietPhieuNhapHangPhienBanSPDAO<ChiTietPhieuNhapHangPhienBanSP> {
    private EntityManager em;
    public ChiTietPhieuNhapHangPhienBanSPImp() throws RemoteException{
        em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
    }

    @Override
    public boolean them(ChiTietPhieuNhapHangPhienBanSP chiTietPhieuNhapHangPhienBanSP) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(chiTietPhieuNhapHangPhienBanSP);
            et.commit();
            return true;
        }catch (Exception e){
            et.rollback();
            return false;
        }
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("Insert ChiTietPhieuNhapHangPhienBanSP values(?, ?, ?)");
//        preparedStatement.setString(1, chiTietPhieuNhapHangPhienBanSP.getChiTietPhieuNhapHang().getMaChiTietPhieuNhap());
//        preparedStatement.setString(2, chiTietPhieuNhapHangPhienBanSP.getChiTietPhienBanSanPham().getMaPhienBanSP());
//        preparedStatement.setInt(3, chiTietPhieuNhapHangPhienBanSP.getSoLuongNhap());
//        return preparedStatement.executeUpdate() > 0;
//        return false;
    }

    @Override
    public boolean capNhat(ChiTietPhieuNhapHangPhienBanSP target) throws RemoteException {
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
//        return false;
    }

    @Override
    public boolean xoa(String id) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            ChiTietPhieuNhapHangPhienBanSP chiTietPhieuNhapHangPhienBanSP = em.find(ChiTietPhieuNhapHangPhienBanSP.class, id);
            em.remove(chiTietPhieuNhapHangPhienBanSP);
            et.commit();
            return true;
        }catch (Exception e) {
            et.rollback();
            return false;
        }
    }

    @Override
    public int xoa(String... ids) throws RemoteException {
        return 0;
    }

    @Override
    public List<ChiTietPhieuNhapHangPhienBanSP> timKiem() throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try{
            et.begin();
            List<ChiTietPhieuNhapHangPhienBanSP> chiTietPhieuNhapHangPhienBanSPs = em.createNamedQuery("CTPNHPBSP.findAll",
                    ChiTietPhieuNhapHangPhienBanSP.class).getResultList();
            et.commit();
            return chiTietPhieuNhapHangPhienBanSPs;
        }catch (Exception e){
            et.rollback();
            return null;
        }

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

        query.set(query.get() + " from ChiTietPhieuNhapHangPhienBanSP where ");
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

    @Override
    public List<ChiTietPhieuNhapHangPhienBanSP> timKiem(Map<String, Object> conditions) throws RemoteException {
        AtomicReference<String> query = new AtomicReference<>
                ("select * from ChiTietPhieuNhapHangPhienBanSP ctpnhpbsp where ");
        AtomicBoolean isNeedAnd = new AtomicBoolean(false);
        conditions.forEach((column, value) -> {
            query.set(query.get() + (isNeedAnd.get() ? " and " : "") + ("ctpnhpbsp." + column + " like '" + value + "'"));
            isNeedAnd.set(true);
        });

        List<ChiTietPhieuNhapHangPhienBanSP> chiTietPhieuNhapHangPhienBanSPS = new ArrayList<>();
        try {
            chiTietPhieuNhapHangPhienBanSPS = em.createNativeQuery(query.get(), ChiTietPhieuNhapHangPhienBanSP.class).getResultList();
            return chiTietPhieuNhapHangPhienBanSPS;
        } catch (Exception e) {
            return chiTietPhieuNhapHangPhienBanSPS;
        }
    }

    @Override
    public ChiTietPhieuNhapHangPhienBanSP timKiem(String id) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            ChiTietPhieuNhapHangPhienBanSP chiTietPhieuNhapHangPhienBanSP = em.find(ChiTietPhieuNhapHangPhienBanSP.class, id);
            et.commit();
            return chiTietPhieuNhapHangPhienBanSP;
        }catch (Exception e){
            et.rollback();
            return null;
        }
    }

    @Override
    public List<ChiTietPhieuNhapHangPhienBanSP> timKiem(String... ids) throws RemoteException {
        return null;
    }
}
