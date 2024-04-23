package dev.skyherobrine.app.daos.product;

import dev.skyherobrine.app.daos.ConnectDB;
import dev.skyherobrine.app.daos.DanhMucSanPhamDAO;
import dev.skyherobrine.app.daos.IDAO;
import dev.skyherobrine.app.entities.product.ChiTietPhienBanSanPham;
import dev.skyherobrine.app.entities.product.DanhMucSanPham;
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

public class DanhMucSanPhamImp extends UnicastRemoteObject implements DanhMucSanPhamDAO<DanhMucSanPham> {
    private EntityManager em;

    public DanhMucSanPhamImp() throws RemoteException {
        em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
    }

    @Override
    public boolean them(DanhMucSanPham danhMucSanPham) throws Exception {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            em.persist(danhMucSanPham);
            tx.commit();
            return true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean capNhat(DanhMucSanPham target) throws Exception {
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
    public List<DanhMucSanPham> timKiem() throws Exception {
        return em.createQuery("select d from DanhMucSanPham d", DanhMucSanPham.class).getResultList();
    }

    @Override
    public List<DanhMucSanPham> timKiem(Map<String, Object> conditions) throws Exception {
        EntityTransaction tx = em.getTransaction();
        AtomicReference<String> query = new AtomicReference<>("select dm from DanhMucSanPham dm where 1 = 1");
        if (conditions != null && !conditions.isEmpty()) {
            for (String key : conditions.keySet()) {
                if(key.contains(".")){
                    String ex = key.substring(key.lastIndexOf(".")+1);
                    query.set(query + " AND dm."+ key +" LIKE :"+ ex);
                }else{
                    query.set(query + " AND dm."+ key +" LIKE :"+ key);
                }
            }
        }
        Query q = em.createQuery(query.get(), DanhMucSanPham.class);


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
        List<DanhMucSanPham> danhMucSanPhams = new ArrayList<>();
        try {
            tx.begin();
            danhMucSanPhams = q.getResultList();
            tx.commit();
            return danhMucSanPhams;
        } catch (Exception e) {
            tx.rollback();
            return null;
        }
    }

    @Override
    public DanhMucSanPham timKiem(String id) throws Exception {
        return Optional.of(em.find(DanhMucSanPham.class, id)).get();
    }

    @Override
    public List<DanhMucSanPham> timKiem(String... ids) throws Exception {
        List<DanhMucSanPham> dmsps = new ArrayList<>();
        for (String id : ids) {
            dmsps.add(em.find(DanhMucSanPham.class, id));
        }
        return dmsps;

    }

    @Override
    public List<Map<String, Object>> timKiem(Map<String, Object> conditions, boolean isDuplicateResult, String... colNames) throws Exception {

        AtomicReference<String> query = new AtomicReference<>("select " + (isDuplicateResult ? "distinct " : ""));
        AtomicBoolean canPhay = new AtomicBoolean(false);

        Arrays.stream(colNames).forEach(column -> {
            query.set(query.get() + (canPhay.get() ? "," : "") + column);
            canPhay.set(true);
        });

        query.set(query.get() + " from DanhMucSanPham t where 1 = 1");

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
