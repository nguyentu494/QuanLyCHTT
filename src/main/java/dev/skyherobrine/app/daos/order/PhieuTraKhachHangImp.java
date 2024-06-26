package dev.skyherobrine.app.daos.order;

import dev.skyherobrine.app.daos.ConnectDB;
import dev.skyherobrine.app.daos.PhieuTraKhachHangDAO;
import dev.skyherobrine.app.entities.order.ChiTietPhieuNhapHang;
import dev.skyherobrine.app.entities.order.PhieuNhapHang;
import dev.skyherobrine.app.entities.order.PhieuTraKhachHang;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import jakarta.persistence.Query;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;

import java.io.File;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class PhieuTraKhachHangImp extends UnicastRemoteObject implements PhieuTraKhachHangDAO<PhieuTraKhachHang> {
    private EntityManager em;

    public PhieuTraKhachHangImp() throws RemoteException {
        em = Persistence.createEntityManagerFactory("JPA_Shop").createEntityManager();
    }
    @Override
    public boolean them(PhieuTraKhachHang phieuTraKhachHang) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            em.persist(phieuTraKhachHang);
            et.commit();
            return true;
        }catch (Exception e){
            et.rollback();
            return false;
        }
    }

    @Override
    public boolean capNhat(PhieuTraKhachHang target) throws RemoteException {
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
    public List<PhieuTraKhachHang> timKiem() throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            List<PhieuTraKhachHang> phieuTraKhachHangs = em.createNamedQuery("PhieuTraKhachHang.findAll", PhieuTraKhachHang.class).getResultList();
            et.commit();
            return phieuTraKhachHangs;
        } catch (Exception e) {
            et.rollback();
            return null;
        }
//        ResultSet resultSet = connectDB.getConnection().createStatement().executeQuery
//                ("select * from PhieuTraKhachHang");
//        List<PhieuTraKhachHang> phieuTraKhachHangs = new ArrayList<>();
//        while(resultSet.next()) {
//            PhieuTraKhachHang phieuTraKhachHang = new PhieuTraKhachHang(
//                    resultSet.getString("MaPhieuTraKH"),
//                    resultSet.getTimestamp("NgayLap").toLocalDateTime(),
//                    new HoaDonImp().timKiem(resultSet.getString("MaHD"))
//            );
//
//            phieuTraKhachHangs.add(phieuTraKhachHang);
//        }
//        return phieuTraKhachHangs;
    }

    @Override
    public List<PhieuTraKhachHang> timKiem(Map<String, Object> conditions) throws RemoteException {
        EntityTransaction tx = em.getTransaction();
        AtomicReference<String> query = new AtomicReference<>
                ("select ptkh from PhieuTraKhachHang ptkh where 1 = 1");
        if (conditions != null && !conditions.isEmpty()) {
            for (String key : conditions.keySet()) {
                if(key.contains(".")){
                    String ex = key.substring(key.lastIndexOf(".")+1);
                    query.set(query + " AND ptkh."+ key +" LIKE :"+ ex);
                }else{
                    query.set(query + " AND ptkh."+ key +" LIKE :"+ key);
                }
            }
        }
        System.out.println(query.get());

        Query q = em.createQuery(query.get(), PhieuTraKhachHang.class);

        if (conditions != null && !conditions.isEmpty()) {
            for (Map.Entry<String, Object> entry : conditions.entrySet()) {
                if(entry.getKey().contains(".")){
                    System.out.println(entry.getKey() + " asss" + entry.getValue());
                    String ex = entry.getKey().substring(entry.getKey().indexOf(".") + 1);
                    q.setParameter(ex, entry.getValue());
                }else{
                    q.setParameter(entry.getKey(), entry.getValue());
                }
            }
        }
        List<PhieuTraKhachHang> phieuTraKhachHangs = new ArrayList<>();
        try {
            tx.begin();
            phieuTraKhachHangs = q.getResultList();
            tx.commit();
            return phieuTraKhachHangs;
        } catch (Exception e) {
            tx.rollback();
            return null;
        }
    }

    @Override
    public PhieuTraKhachHang timKiem(String id) throws RemoteException {
        EntityTransaction et = em.getTransaction();
        try {
            et.begin();
            PhieuTraKhachHang phieuTraKhachHang = em.find(PhieuTraKhachHang.class, id);
            et.commit();
            return phieuTraKhachHang;
        }catch (Exception e) {
            et.rollback();
            return null;
        }
    }

    @Override
    public List<PhieuTraKhachHang> timKiem(String... ids) throws RemoteException {
        String query = "select * from PhieuTraKhachHang where ";
        String[] listID = (String[]) Arrays.stream(ids).toArray();
        for(int i = 0; i < listID.length; ++i) {
            query += ("ma_phieu_tra_khach_hang = '" + listID[i] + "'");
            if((i + 1) >= listID.length) break;
            else query += ", ";
        }

        List<PhieuTraKhachHang> phieuTraKhachHangs = new ArrayList<>();
        try {
            phieuTraKhachHangs = em.createNativeQuery(query, PhieuTraKhachHang.class).getResultList();
            return phieuTraKhachHangs;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
//        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query);
//        ResultSet resultSet = preparedStatement.executeQuery();
//        while(resultSet.next()) {
//            PhieuTraKhachHang phieuTraKhachHang = new PhieuTraKhachHang(
//                    resultSet.getString("MaPhieuTraKH"),
//                    resultSet.getTimestamp("NgayLap").toLocalDateTime(),
//                    new HoaDonImp().timKiem(resultSet.getString("MaHD"))
//            );
//
//            phieuTraKhachHangs.add(phieuTraKhachHang);
//        }
//        return phieuTraKhachHangs;
    }

    @Override
    public List<Map<String, Object>> timKiem(Map<String, Object> conditions, boolean isDuplicateResult, String... colNames) throws RemoteException {
        AtomicReference<String> query = new AtomicReference<>("select " + (isDuplicateResult ? "distinct " : ""));
        AtomicBoolean canPhay = new AtomicBoolean(false);

        Arrays.stream(colNames).forEach(column -> {
            query.set(query.get() + (canPhay.get() ? "," : "") + column);
            canPhay.set(true);
        });

        query.set(query.get() + " from PhieuTraKhachHang t where 1 = 1");

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
    public JasperPrint xuatHoaDon(Map<String, Object> data) throws JRException, RemoteException, SQLException {
        JasperDesign jasperDesign = JRXmlLoader.load(new File("src/main/resources/HoaDon/phieutra.jrxml"));
        JasperReport jreport = JasperCompileManager.compileReport(jasperDesign);
        Connection con = DriverManager.getConnection("jdbc:sqlserver://localhost:1433;databaseName=QLCHTT1;encrypt=false;trustServerCertificate=true", "sa", "123");
        JasperPrint jprint = JasperFillManager.fillReport(jreport, data, con);
        new PhieuTraKhachHangImp();
        return jprint;
    }
}
