package dev.skyherobrine.app.daos.order;

import dev.skyherobrine.app.daos.ChiTietHoaDonDAO;
import dev.skyherobrine.app.daos.ConnectDB;
import dev.skyherobrine.app.daos.HoaDonDAO;
import dev.skyherobrine.app.daos.product.ChiTietPhienBanSanPhamImp;
import dev.skyherobrine.app.entities.Key.ChiTietHoaDonId;
import dev.skyherobrine.app.entities.order.ChiTietHoaDon;
import dev.skyherobrine.app.entities.order.HoaDon;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class ChiTietHoaDonImp extends UnicastRemoteObject implements ChiTietHoaDonDAO<ChiTietHoaDon> {
    private ConnectDB connectDB;
    private EntityManager em;
    public ChiTietHoaDonImp() throws Exception {
        em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
    }
    @Override
    public boolean them(ChiTietHoaDon chiTietHoaDon) throws Exception {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(chiTietHoaDon);
            et.commit();
            new HoaDonImp();
            return true;
        } catch (Exception e) {
            et.rollback();
            return false;
        }
    }

    @Override
    public boolean capNhat(ChiTietHoaDon target) throws Exception {
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
    public List<ChiTietHoaDon> timKiem() throws Exception {
        return em.createNamedQuery("CTHD.findAll", ChiTietHoaDon.class).getResultList();
    }

    @Override
    public List<ChiTietHoaDon> timKiem(Map<String, Object> conditions) throws Exception {
        EntityTransaction tx = em.getTransaction();
        AtomicReference<String> query = new AtomicReference<>("select cthd from ChiTietHoaDon cthd where 1 = 1");
        if (conditions != null && !conditions.isEmpty()) {
            for (String key : conditions.keySet()) {
                if(key.contains(".")){
                    String ex = key.substring(key.lastIndexOf(".")+1);
                    query.set(query + " AND cthd."+ key +" LIKE :"+ ex);
                }else{
                    query.set(query + " AND cthd."+ key +" LIKE :"+ key);
                }
            }
        }
        Query q = em.createQuery(query.get(), ChiTietHoaDon.class);


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
        List<ChiTietHoaDon> chiTietHoaDons = new ArrayList<>();
        try {
            tx.begin();
            chiTietHoaDons = q.getResultList();
            tx.commit();
            return chiTietHoaDons;
        } catch (Exception e) {
            tx.rollback();
            return null;
        }
    }

    @Override
    public ChiTietHoaDon timKiem(String id) throws Exception {
        return null;
    }

    @Override
    public List<ChiTietHoaDon> timKiem(String... ids) throws Exception {
        return null;
    }

    @Override
    public List<Map<String, Integer>> timKiem(String cols, String join, String query) throws SQLException, RemoteException {
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("select " + cols + " from  ChiTietHoaDon " + join + " where " + query);
//
//        ResultSet resultSet = preparedStatement.executeQuery();
        List<Map<String, Integer>> listResult = new ArrayList<>();
        String sql = ("select " + cols + " from  ChiTietHoaDon " + join + " where " + query);
        listResult = em.createNativeQuery(sql).getResultList().stream().map(row -> {
            Object[] o = (Object[]) row;
            Map<String, Integer> rowDatas = new HashMap<>();
            rowDatas.put((String) o[0], (Integer) o[1]);
            return rowDatas;
        }).toList();
        return listResult;
    }
    @Override
    public List<Map<String, Object>> timKiemHD(String cols, String join, String query) throws Exception {
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
//                ("select " + cols + " from  ChiTietHoaDon " + join + " where " + query);
//
//        ResultSet resultSet = preparedStatement.executeQuery();

        String sql = ("select " + cols + " from  ChiTietHoaDon " + join + " where " + query);


        List<Map<String, Object>> listResult = new ArrayList<>();
        listResult = em.createNativeQuery(sql).getResultList().stream().map(row -> {
            Object[] o = (Object[]) row;
            Map<String, Integer> rowDatas = new HashMap<>();
            rowDatas.put((String) o[2], (Integer) o[1]);
            Map<String, Object> rowDatas1 = new HashMap<>();
            rowDatas1.put((String) o[0], rowDatas);
            return rowDatas1;
        }).toList();
        System.out.println("SSSS"+sql);
        System.out.println(listResult);
//        while (resultSet.next()) {
//            Map<String, Integer> rowDatas = new HashMap<>();
//            rowDatas.put(resultSet.getString("NgayLap"), resultSet.getInt("soLuongBan"));
//            Map<String, Object> rowDatas1 = new HashMap<>();
//            rowDatas1.put(resultSet.getString("MaSP"), rowDatas);
//            listResult.add(rowDatas1);
//        }
        return listResult;
    }
    @Override
    public ChiTietHoaDon timKiem(String maHD, String maPhienBanSP) throws Exception{
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
                ("select * from ChiTietHoaDon where MaPhienBanSP = ? and MaHD = ?");
        preparedStatement.setString(1, maPhienBanSP);
        preparedStatement.setString(2, maHD);

        ResultSet resultSet = preparedStatement.executeQuery();

        ChiTietHoaDon chiTietHoaDon = em.find(ChiTietHoaDon.class, new ChiTietHoaDonId(new HoaDonImp().timKiem(maHD),
                new ChiTietPhienBanSanPhamImp().timKiem(maPhienBanSP)));
        return chiTietHoaDon;
    }
    @Override
    public List<Map<String, Object>> timKiem(Map<String, Object> conditions, boolean isDuplicateResult, String... colNames) throws Exception {
        AtomicReference<String> query = new AtomicReference<>("select " + (isDuplicateResult ? "distinct " : ""));
        AtomicBoolean canPhay = new AtomicBoolean(false);
        AtomicBoolean canAnd = new AtomicBoolean(false);

        Arrays.stream(colNames).forEach(column -> {
            query.set(query.get() + (canPhay.get() ? "," : "") + column);
            canPhay.set(true);
        });

        query.set(query.get() + " from ChiTietHoaDon where ");

        conditions.forEach((column, value) -> {
            query.set(query.get() + (canAnd.get() ? " AND " : "") + column + " like '%" + value + "%'");
            canAnd.set(true);
        });

        System.out.println(query);
        ResultSet resultSet = connectDB.getConnection().createStatement().executeQuery(query.get());

        List<Map<String, Object>> listResult = new ArrayList<>();
        while(resultSet.next()){
            Map<String, Object> rowDatas = new HashMap<>();
            for(String column : Arrays.stream(colNames).toList()) {
                rowDatas.put(column, resultSet.getString(column));
            }
            listResult.add(rowDatas);
        }
        return listResult;
    }


}
