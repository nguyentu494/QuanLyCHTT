package dev.skyherobrine.app.daos.order;

import dev.skyherobrine.app.daos.ChiTietPhieuNhapHangPhienBanSPDAO;
import dev.skyherobrine.app.entities.order.ChiTietPhieuNhapHangPhienBanSP;
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

        Arrays.stream(colNames).forEach(column -> {
            query.set(query.get() + (canPhay.get() ? "," : "") + column);
            canPhay.set(true);
        });

        query.set(query.get() + " from ChiTietPhieuNhapHangPhienBanSP t where 1 = 1");

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
        System.out.println(query.get());
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
        EntityTransaction tx = em.getTransaction();
        AtomicReference<String> query = new AtomicReference<>
                ("select ctpnhpbsp from ChiTietPhieuNhapHangPhienBanSP ctpnhpbsp where 1 = 1");
        if (conditions != null && !conditions.isEmpty()) {
            for (String key : conditions.keySet()) {
                query.set(query + " AND pbsp."+ key +" LIKE :"+ key);
            }
        }
        Query q = em.createQuery(query.get());


        if (conditions != null && !conditions.isEmpty()) {
            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
                q.setParameter(entry.getKey(), entry.getValue());
            }
        }
        List<ChiTietPhieuNhapHangPhienBanSP> chiTietPhieuNhapHangPhienBanSPS = new ArrayList<>();
        try {
            tx.begin();
            chiTietPhieuNhapHangPhienBanSPS = em.createQuery(query.get(), ChiTietPhieuNhapHangPhienBanSP.class).getResultList();
            tx.commit();
            return chiTietPhieuNhapHangPhienBanSPS;
        } catch (Exception e) {
            tx.rollback();
            return null;
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
