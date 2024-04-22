package dev.skyherobrine.app.daos.product;

import dev.skyherobrine.app.daos.SanPhamDAO;
import dev.skyherobrine.app.entities.product.LoaiSanPham;
import dev.skyherobrine.app.entities.product.SanPham;
import dev.skyherobrine.app.enums.TinhTrangSanPham;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class SanPhamImp extends UnicastRemoteObject implements SanPhamDAO<SanPham> {
    private static EntityManager em;

    public SanPhamImp() throws Exception {
        em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
    }

    @Override
    public boolean them(SanPham sanPham) throws Exception {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(sanPham);
            et.commit();
            return true;
        } catch (Exception e) {
            et.rollback();
            return false;
        }
    }

    @Override
    public boolean capNhat(SanPham target) throws Exception {
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
    public boolean xoa(String id) throws Exception {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            SanPham sanPham = em.find(SanPham.class, id);
            sanPham.setTinhTrang(TinhTrangSanPham.KHONG_CON_BAN);
            em.merge(sanPham);
            et.commit();
            return true;
        } catch (Exception e) {
            et.rollback();
            return false;
        }
    }

    @Override
    public int xoa(String... ids) throws Exception {
        return 0;
    }

    @Override
    public List<SanPham> timKiem() throws RemoteException {
        try {
            return em.createNamedQuery("SanPham.findAll", SanPham.class).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<SanPham> timKiem(Map<String, Object> conditions) throws Exception {
        EntityTransaction tx = em.getTransaction();
        AtomicReference<String> query = new AtomicReference<>("select sp from SanPham sp where 1 = 1");
        if (conditions != null && !conditions.isEmpty()) {
            for (String key : conditions.keySet()) {
                if(key.contains(".")){
                    String ex = key.substring(key.lastIndexOf(".")+1);
                    query.set(query + " AND sp."+ key +" LIKE :"+ ex);
                }else{
                    query.set(query + " AND sp."+ key +" LIKE :"+ key);
                }
            }
        }
        Query q = em.createQuery(query.get(), SanPham.class);


        if (conditions != null && !conditions.isEmpty()) {
            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
                if(entry.getKey().contains(".")) {
                    String ex = entry.getKey().substring(entry.getKey().lastIndexOf(".") + 1);
                    q.setParameter(ex, entry.getValue());
                }else{
                    q.setParameter(entry.getKey(), entry.getValue());
                }
            }
        }
        List<SanPham> sanPhams = new ArrayList<>();
        try {
            tx.begin();
            sanPhams = q.getResultList();
            tx.commit();
            return sanPhams;
        } catch (Exception e) {
            tx.rollback();
            return null;
        }
    }

    @Override
    public SanPham timKiem(String id) throws Exception {
        SanPham sp = em.createNamedQuery("SanPham.findByID", SanPham.class).setParameter("maSP", id).getSingleResult();
        return Optional.of(sp).get();
    }

    @Override
    public List<SanPham> timKiem(String... ids) throws Exception {
        String query = "select sp from SanPham sp where 1 = 1 ";
        for (String id : ids) {
            query += " and sp.maSP = '" + id + "'";
        }
        return em.createQuery(query, SanPham.class).getResultList();
    }

    @Override
    public List<Map<String, Object>> timKiem(Map<String, Object> conditions, boolean isDuplicateResult, String... colNames) throws Exception {
        AtomicReference<String> query = new AtomicReference<>("select " + (isDuplicateResult ? "distinct " : ""));
        AtomicBoolean canPhay = new AtomicBoolean(false);

        Arrays.stream(colNames).forEach(column -> {
            query.set(query.get() + (canPhay.get() ? "," : "") + "t."+column);
            canPhay.set(true);
        });

        query.set(query.get() + " from SanPham t where 1 = 1");

        if (conditions != null && !conditions.isEmpty()) {
            for (String key : conditions.keySet()) {
                query.set(query + " AND t."+key+" LIKE :"+key);
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
