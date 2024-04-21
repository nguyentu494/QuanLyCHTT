package dev.skyherobrine.app.daos.person;

import dev.skyherobrine.app.daos.ConnectDB;
import dev.skyherobrine.app.daos.NhaCungCapDAO;
import dev.skyherobrine.app.entities.person.KhachHang;
import dev.skyherobrine.app.entities.person.NhaCungCap;
import dev.skyherobrine.app.entities.product.ChiTietPhienBanSanPham;
import dev.skyherobrine.app.enums.TinhTrangNhaCungCap;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class NhaCungCapImp extends UnicastRemoteObject implements NhaCungCapDAO<NhaCungCap> {
    private EntityManager em;

    public NhaCungCapImp() throws RemoteException {
        em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
    }

    @Override
    public boolean them(NhaCungCap nhaCungCap) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(nhaCungCap);
            et.commit();
            return true;
        } catch (Exception e) {
            et.rollback();
            return false;
        }
    }

    @Override
    public boolean capNhat(NhaCungCap target) throws RemoteException {
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
            NhaCungCap nhaCungCap = em.find(NhaCungCap.class, id);
            nhaCungCap.setTinhTrang(TinhTrangNhaCungCap.CHAM_DUT);
            em.merge(nhaCungCap);
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
    public List<NhaCungCap> timKiem() throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            List<NhaCungCap> nhaCungCaps = em.createNamedQuery("NhaCungCap.findAll", NhaCungCap.class).getResultList();
            et.commit();
            return nhaCungCaps;
        } catch (Exception e) {
            et.rollback();
            return new ArrayList<>();
        }
    }

    @Override
    public List<NhaCungCap> timKiem(Map<String, Object> conditions) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        AtomicReference<String> query = new AtomicReference<>("select ncc from NhaCungCap ncc where 1 = 1");
        if (conditions != null && !conditions.isEmpty()) {
            for (String key : conditions.keySet()) {
                query.set(query + " AND ncc."+ key +" LIKE :"+ key);
            }
        }
        Query q = em.createQuery(query.get());


        if (conditions != null && !conditions.isEmpty()) {
            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
                q.setParameter(entry.getKey(), entry.getValue());
            }
        }
        List<NhaCungCap> nhaCungCaps = new ArrayList<>();
        try {
            tx.begin();
            nhaCungCaps = em.createQuery(query.get(), NhaCungCap.class).getResultList();
            tx.commit();
            return nhaCungCaps;
        } catch (Exception e) {
            tx.rollback();
            return null;
        }
    }

    @Override
    public NhaCungCap timKiem(String id) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            NhaCungCap nhaCungCap = em.createNamedQuery("NhaCungCap.findByID", NhaCungCap.class).setParameter("maNCC", id).getSingleResult();
            et.commit();
            return nhaCungCap;
        } catch (Exception e) {
            et.rollback();
            return null;
        }
    }

    @Override
    public List<NhaCungCap> timKiem(String... ids) throws RemoteException {
        String query = "select ncc from NhaCungCap ncc where 1 = 1";
        for (String id : ids) {
            query += " AND ncc.maNCC = '" + id + "'";
        }
        Query q = em.createQuery(query);

        List<NhaCungCap> nhaCungCaps = new ArrayList<>();
        try {
            nhaCungCaps = q.getResultList();
            return nhaCungCaps;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> timKiem(Map<String, Object> conditions, boolean isDuplicateResult, String... colNames) throws RemoteException {
        AtomicReference<String> query = new AtomicReference<>("select " + (isDuplicateResult ? "distinct " : ""));
        AtomicBoolean canPhay = new AtomicBoolean(false);

        Arrays.stream(colNames).forEach(column -> {
            query.set(query.get() + (canPhay.get() ? "," : "") + "t."+column);
            canPhay.set(true);
        });

        query.set(query.get() + " from NhaCungCap t where 1 = 1");

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
