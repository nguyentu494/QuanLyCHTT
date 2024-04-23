package dev.skyherobrine.app.daos.sale;

import dev.skyherobrine.app.daos.ThueDAO;
import dev.skyherobrine.app.entities.product.SanPham;
import dev.skyherobrine.app.entities.product.ThuongHieu;
import dev.skyherobrine.app.entities.sale.Thue;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ThueImp extends UnicastRemoteObject implements ThueDAO<Thue> {

    private EntityManager em;

    public ThueImp() throws RemoteException {
        em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
    }

    @Override
    public boolean them(Thue thue) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(thue);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean capNhat(Thue target) throws RemoteException {
        return false;
    }

    public boolean update(String HieuLuc) {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.merge(HieuLuc);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            return false;
        }
    }

    @Override
    public boolean xoa(String id) throws RemoteException {
        return false;
    }

    @Override
    public int xoa(String... ids) throws RemoteException {
        return 0;
    }

    @Override
    public List<Thue> timKiem() throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            List<Thue> thues = em.createNamedQuery("Thue.findAll", Thue.class).getResultList();
            tx.commit();
            return thues;
        } catch (Exception e) {
            tx.rollback();
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

        query.set(query.get() + " from Thue t  where 1 = 1");

        if (conditions != null && !conditions.isEmpty()) {
            for (String key : conditions.keySet()) {
                if(key.contains(".")){
                    String ex = key.substring(key.lastIndexOf(".")+1);
                    query.set(query + " AND t."+ key +" LIKE :"+ ex);
                }else{
                    query.set(query + " AND t."+ key +" LIKE :"+ key);
                }
            }
        }
        Query q = em.createQuery(query.get());

        List<Map<String, Object>> listResult = new ArrayList<>();
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
    public List<Thue> timKiem(Map<String, Object> conditions) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        AtomicReference<String> query = new AtomicReference<>("select th from Thue th where 1 = 1");
        if (conditions != null && !conditions.isEmpty()) {
            for (String key : conditions.keySet()) {
                if(key.contains(".")){
                    String ex = key.substring(key.lastIndexOf(".")+1);
                    query.set(query + " AND th."+ key +" LIKE :"+ ex);
                }else{
                    query.set(query + " AND cast( th."+ key +" as String ) LIKE :"+ key);
                }
            }
        }
        Query q = em.createQuery(query.get(), Thue.class);


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
        List<Thue> thues = new ArrayList<>();
        try {
            tx.begin();
            thues = q.getResultList();
            tx.commit();
            return thues;
        } catch (Exception e) {
            tx.rollback();
            return null;
        }
    }

    @Override
    public Thue timKiem(String id) throws Exception {
        return Optional.of(em.find(Thue.class, id)).get();
    }

    @Override
    public List<Thue> timKiem(String... ids) throws Exception {
        List<Thue> thues = new ArrayList<>();
        for (String id : ids) {
            thues.add(em.find(Thue.class, id));
        }
        return thues;
    }
}
