package dev.skyherobrine.app.daos.order;

import dev.skyherobrine.app.daos.PhieuNhapHangDAO;
import dev.skyherobrine.app.entities.order.ChiTietPhieuTraKhachHang;
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
        EntityTransaction tx = em.getTransaction();
        AtomicReference<String> query = new AtomicReference<>
                ("select pnh from PhieuNhapHang pnh where 1 = 1");
        if (conditions != null && !conditions.isEmpty()) {
            for (String key : conditions.keySet()) {
                query.set(query + " AND pnh."+ key +" LIKE :"+ key);
            }
        }
        Query q = em.createQuery(query.get());


        if (conditions != null && !conditions.isEmpty()) {
            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
                q.setParameter(entry.getKey(), entry.getValue());
            }
        }
        List<PhieuNhapHang> phieuNhapHangs = new ArrayList<>();
        try {
            tx.begin();
            phieuNhapHangs = em.createQuery(query.get(), PhieuNhapHang.class).getResultList();
            tx.commit();
            return phieuNhapHangs;
        } catch (Exception e) {
            tx.rollback();
            return null;
        }
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
    }

    @Override
    public List<PhieuNhapHang> timKiem(String... ids) throws RemoteException {
        String query = "select pnh from PhieuNhapHang where 1 = 1 ";
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
    }

    @Override
    public List<Map<String, Object>> timKiem(Map<String, Object> conditions, boolean isDuplicateResult, String... colNames) throws RemoteException {
        AtomicReference<String> query = new AtomicReference<>("select " + (isDuplicateResult ? "distinct " : ""));
        AtomicBoolean canPhay = new AtomicBoolean(false);

        Arrays.stream(colNames).forEach(column -> {
            query.set(query.get() + (canPhay.get() ? "," : "") + column);
            canPhay.set(true);
        });

        query.set(query.get() + " from PhieuNhapHang t where 1 = 1");

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
}
