package dev.skyherobrine.app.daos.order;

import dev.skyherobrine.app.daos.ChiTietPhieuNhapHangDAO;
import dev.skyherobrine.app.entities.order.ChiTietPhieuNhapHang;
import dev.skyherobrine.app.entities.product.ChiTietPhienBanSanPham;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChiTietPhieuNhapHangImp extends UnicastRemoteObject implements ChiTietPhieuNhapHangDAO<ChiTietPhieuNhapHang> {
    private EntityManager em;
    public ChiTietPhieuNhapHangImp() throws RemoteException {
        em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
    }

    @Override
    public boolean them(ChiTietPhieuNhapHang chiTietPhieuNhapHang) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(chiTietPhieuNhapHang);
            et.commit();
            return true;
        } catch (Exception e) {
            et.rollback();
            return false;
        }
    }

    @Override
    public boolean capNhat(ChiTietPhieuNhapHang target) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.merge(target);
            et.commit();
            return true;
        } catch (Exception e) {
            et.rollback();
            return false;
        }
    }

    @Override
    public boolean xoa(String id) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.remove(em.find(ChiTietPhieuNhapHang.class, id));
            et.commit();
            return true;
        } catch (Exception e) {
            et.rollback();
            return false;
        }
    }

    @Override
    public int xoa(String... ids) throws RemoteException {
        return 0;
    }

    @Override
    public List<ChiTietPhieuNhapHang> timKiem() throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            List<ChiTietPhieuNhapHang> chiTietPhieuNhapHangs = em.createNamedQuery("CTPNH.findAll", ChiTietPhieuNhapHang.class).getResultList();
            et.commit();
            return chiTietPhieuNhapHangs;
        } catch (Exception e) {
            et.rollback();
            return null;
        }
    }

    @Override
    public List<ChiTietPhieuNhapHang> timKiem(Map<String, Object> conditions) throws RemoteException {
        EntityTransaction tx = em.getTransaction();

        AtomicReference<String> query = new AtomicReference<>
                ("select ctpnh from ChiTietPhieuNhapHang ctpnh where 1 = 1 ");
        AtomicBoolean isNeedAnd = new AtomicBoolean(false);
        if (conditions != null && !conditions.isEmpty()) {
            for (String key : conditions.keySet()) {
                if(key.contains(".")){
                    String ex = key.substring(key.lastIndexOf(".")+1);
                    query.set(query + " AND ctpnh."+ key +" LIKE :"+ ex);
                }else{
                    query.set(query + " AND ctpnh."+ key +" LIKE :"+ key);
                }
            }
        }

        Query q = em.createQuery(query.get(), ChiTietPhieuNhapHang.class);

        if (conditions != null && !conditions.isEmpty()) {
            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
                if(entry.getKey().contains(".")){
                    String ex = entry.getKey().substring(entry.getKey().indexOf(".") + 1);
                    q.setParameter(ex, entry.getValue());
                }else{
                    q.setParameter(entry.getKey(), entry.getValue());
                }
            }
        }

        List<ChiTietPhieuNhapHang> chiTietPhieuNhapHangs = new ArrayList<>();
        try {
            tx.begin();
            chiTietPhieuNhapHangs = q.getResultList();
            tx.commit();
            return chiTietPhieuNhapHangs;
        } catch (Exception e) {
            tx.rollback();
            return null;
        }
    }

    @Override
    public List<ChiTietPhieuNhapHang> timKiemHaiBang(Map<String, Object> conditions) throws RemoteException {
        return em.createNamedQuery("CTPNH.findByDayAndMaSP", ChiTietPhieuNhapHang.class)
                .setParameter("ngayNhap", conditions.get("ngay_lap"))
                .setParameter("maSP", conditions.get("ma_sp")).getResultList();
    }

    @Override
    public ChiTietPhieuNhapHang timKiem(String id) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            ChiTietPhieuNhapHang chiTietPhieuNhapHang = em.createNamedQuery("CTPNH.findByID", ChiTietPhieuNhapHang.class)
                    .setParameter("id", id).getSingleResult();
            et.commit();
            return Optional.of(chiTietPhieuNhapHang).get();
        } catch (Exception e) {
            et.rollback();
            return (ChiTietPhieuNhapHang) Optional.empty().get();
        }
    }

    @Override
    public List<ChiTietPhieuNhapHang> timKiem(String... ids) throws RemoteException {
        return null;
    }
    @Override
    public List<Map<String, Object>> timKiem(Map<String, Object> conditions, boolean isDuplicateResult, String... colNames) throws RemoteException {
        AtomicReference<String> query = new AtomicReference<>("select " + (isDuplicateResult ? "distinct " : ""));
        AtomicBoolean canPhay = new AtomicBoolean(false);

        Arrays.stream(colNames).forEach(column -> {
            query.set(query.get() + (canPhay.get() ? "," : "") + column);
            canPhay.set(true);
        });

        query.set(query.get() + " from ChiTietPhieuNhapHang t where 1 = 1");

        if (conditions != null && !conditions.isEmpty()) {
            for (String key : conditions.keySet()) {
                query.set(query + " AND t."+ key +" LIKE :"+ key);
            }
        }
        Query q = em.createQuery(query.get());

        if (conditions != null && !conditions.isEmpty()) {
            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
                q.setParameter(entry.getKey(), entry.getValue());
            }
        }

        List<Map<String, Object>> listResult = new ArrayList<>();
//        System.out.println(query.get());
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

    //Đổi Optional thành List
    @Override
    public List<ChiTietPhieuNhapHang> timKiem(String maPhieuNhap, String maSP) throws RemoteException{
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            List<ChiTietPhieuNhapHang> chiTietPhieuNhapHangs = em.createNamedQuery("CTPNH.findByMaPhieuNhapAndMaSP", ChiTietPhieuNhapHang.class)
                    .setParameter("maPhieuNhap", maPhieuNhap)
                    .setParameter("maSP", maSP)
                    .getResultList();
            et.commit();
            return chiTietPhieuNhapHangs;
        } catch (Exception e) {
            et.rollback();
            return null;
        }
    }
}
