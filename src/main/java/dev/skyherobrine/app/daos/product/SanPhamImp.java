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
        StringBuilder jpqlBuilder = new StringBuilder("select t from SanPham t where 1 = 1");

        for (String key : conditions.keySet()) {
            jpqlBuilder.append(" and t.").append(key).append(" = :").append(key);
        }

        Query query = em.createQuery(jpqlBuilder.toString(), LoaiSanPham.class);

        for (Map.Entry<String, Object> entry : conditions.entrySet()) {
            query.setParameter(entry.getKey(), entry.getValue());
        }

        List<SanPham> resultList = query.getResultList();

        return resultList.isEmpty() ? null : resultList;
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
        StringBuilder jpqlBuilder = new StringBuilder("SELECT " + (isDuplicateResult ? "distinct " : ""));

        for (int i = 0; i < colNames.length; i++) {
            jpqlBuilder.append("t.").append(colNames[i]);
            if (i < colNames.length - 1) {
                jpqlBuilder.append(", ");
            }
        }

        jpqlBuilder.append(" FROM SanPham t WHERE 1 = 1");

        if (conditions != null && !conditions.isEmpty()) {
            for (String key : conditions.keySet()) {
                jpqlBuilder.append(" AND t.").append(key).append(" = :").append(key);
            }
        }

        Query query = em.createQuery(jpqlBuilder.toString());

        if (conditions != null && !conditions.isEmpty()) {
            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
                query.setParameter(entry.getKey(), entry.getValue());
            }
        }

        List<Object[]> results = query.getResultList();

        List<Map<String, Object>> formattedResults = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> rowMap = new HashMap<>();
            for (int i = 0; i < colNames.length; i++) {
                rowMap.put(colNames[i], row[i]);
            }
            formattedResults.add(rowMap);
        }

        if (isDuplicateResult || !formattedResults.isEmpty()) {
            return formattedResults;
        } else {
            return null;
        }
    }
}
