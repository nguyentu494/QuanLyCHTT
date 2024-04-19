package dev.skyherobrine.app.daos.person;

import dev.skyherobrine.app.daos.ConnectDB;
import dev.skyherobrine.app.daos.NhaCungCapDAO;
import dev.skyherobrine.app.entities.person.NhaCungCap;
import dev.skyherobrine.app.enums.TinhTrangNhaCungCap;

import java.rmi.server.UnicastRemoteObject;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class NhaCungCapImp extends UnicastRemoteObject implements NhaCungCapDAO<NhaCungCap> {
    private ConnectDB connectDB;
    public NhaCungCapImp() throws Exception{
        connectDB = new ConnectDB();
    }

    @Override
    public boolean them(NhaCungCap nhaCungCap) throws Exception {
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
                ("Insert NhaCungCap values(?, ?, ?, ?, ?)");
        preparedStatement.setString(1, nhaCungCap.getMaNCC());
        preparedStatement.setString(2, nhaCungCap.getTenNCC());
        preparedStatement.setString(3, nhaCungCap.getDiaChiNCC());
        preparedStatement.setString(4, nhaCungCap.getEmail());
        preparedStatement.setString(5, nhaCungCap.getTinhTrang().toString());

        int result = preparedStatement.executeUpdate();
        return result > 0;
    }

    @Override
    public boolean capNhat(NhaCungCap target) throws Exception {
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
                ("Update NhaCungCap set TenNCC = ?, DiaChiNCC = ?, Email = ?, TinhTrang = ? where MaNCC = ?");
        preparedStatement.setString(1, target.getMaNCC());
        preparedStatement.setString(2, target.getDiaChiNCC());
        preparedStatement.setString(3, target.getEmail());
        preparedStatement.setString(4, target.getTinhTrang().toString());
        preparedStatement.setString(5, target.getMaNCC());

        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    public boolean xoa(String id) throws Exception {
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
                ("Update NhaCungCap set TinhTrang = ? where MaNCC = ?");
        preparedStatement.setString(1, "CHAM_DUT");
        preparedStatement.setString(2, id);

        return preparedStatement.executeUpdate() > 0;
    }

    @Override
    public int xoa(String... ids) throws Exception {
        return 0;
    }

    @Override
    public List<NhaCungCap> timKiem() throws Exception {
        List<NhaCungCap> nhaCungCaps = new ArrayList<>();
        ResultSet result = connectDB.getConnection().createStatement().executeQuery("select * from NhaCungCap");
        while(result.next()) {
            NhaCungCap nhaCungCap = new NhaCungCap(result.getString("MaNCC"),
                    result.getString("TenNCC"), result.getString("DiaChiNCC"),
                    result.getString("Email"), TinhTrangNhaCungCap.layGiaTri(result.getString("TinhTrang")));

            nhaCungCaps.add(nhaCungCap);
        }
        return nhaCungCaps;
    }

    @Override
    public List<NhaCungCap> timKiem(Map<String, Object> conditions) throws Exception {
        AtomicReference<String> query = new AtomicReference<>
                ("select * from NhaCungCap t where ");
        AtomicBoolean isNeedAnd = new AtomicBoolean(false);

        conditions.forEach((column, value) -> {
            query.set(query.get() + (isNeedAnd.get() ? " and " : "") + ("t." + column + " like N'%" + value + "%'"));
            isNeedAnd.set(true);
        });
        List<NhaCungCap> nhaCungCaps = new ArrayList<>();
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query.get());
        ResultSet result = preparedStatement.executeQuery();
        while(result.next()) {
            NhaCungCap nhaCungCap = new NhaCungCap(
                    result.getString("MaNCC"),
                    result.getString("TenNCC"),
                    result.getString("DiaChiNCC"),
                    result.getString("Email"),
                    TinhTrangNhaCungCap.layGiaTri(result.getString("TinhTrang"))
            );
            nhaCungCaps.add(nhaCungCap);
        }
        return nhaCungCaps;
    }

    @Override
    public NhaCungCap timKiem(String id) throws Exception {
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement
                ("select * from NhaCungCap NCC where NCC.MaNCC = ?");
        preparedStatement.setString(1, id);

        ResultSet result = preparedStatement.executeQuery();
        if (result.next()) {
            return Optional.of(new NhaCungCap(result.getString("MaNCC"),
                    result.getString("TenNCC"), result.getString("DiaChiNCC"),
                    result.getString("Email"), TinhTrangNhaCungCap.layGiaTri(result.getString("TinhTrang")))).get();
        } else {
            return (NhaCungCap) Optional.empty().get();
        }
    }

    @Override
    public List<NhaCungCap> timKiem(String... ids) throws Exception {
        String query = "select * from NhaCungCap NCC where ";
        String[] listID = (String[]) Arrays.stream(ids).toArray();
        for(int i = 0; i < listID.length; ++i) {
            query += ("NCC.MaNCC like '%" + listID[i] + "%'");
            if((i + 1) >= listID.length) break;
            else query += ", ";
        }

        List<NhaCungCap> nhaCungCaps = new ArrayList<>();
        PreparedStatement preparedStatement = connectDB.getConnection().prepareStatement(query);
        ResultSet result = preparedStatement.executeQuery();
        while(result.next()) {
            NhaCungCap nhaCungCap = new NhaCungCap(result.getString("MaNCC"),
                    result.getString("TenNCC"), result.getString("DiaChiNCC"),
                    result.getString("Email"), TinhTrangNhaCungCap.layGiaTri(result.getString("TinhTrang")));
            nhaCungCaps.add(nhaCungCap);
        }
        return nhaCungCaps;
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

        query.set(query.get() + " from NhaCungCap where ");
        conditions.forEach((column, value) -> {
            query.set(query.get() + (canAnd.get() ? " AND " : "") + column + " like '%" + value + "%'");
            canAnd.set(true);
        });
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
