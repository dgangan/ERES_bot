package com.eres.parser;

import com.eres.common.OracleDBCommon;
import com.eres.common.PropertiesLoader;
import oracle.ucp.jdbc.PoolDataSource;
import oracle.ucp.jdbc.PoolDataSourceFactory;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class OracleDBUtil {
    Connection conn;
    String TABLE_NAME = "outages";

    public OracleDBUtil(Connection conn){
        this.conn = conn;
    }

    public void writeOutageDay(Integer id, LocalDate date, String link, String jsonAddresses) throws SQLException {
        final String queryStatement = "INSERT INTO " + TABLE_NAME + " (outage_id, outage_date, outage_link, outage_data) VALUES (?,?,?,?)";

        System.out.println("\n Query is " + queryStatement);

        conn.setAutoCommit(false);
        try (PreparedStatement ps = conn.prepareStatement(queryStatement)){
            ps.setInt(1,id);
            ps.setDate(2, java.sql.Date.valueOf(date));
            ps.setString(3,link);
            Blob blob = conn.createBlob();
            blob.setBytes(4, jsonAddresses.getBytes());
            ps.setBlob(4,blob);
            ps.executeUpdate();
            conn.commit();

            System.out.println("DB was successfully updated. id -> " + id);
        }
    }

    public Optional<OutageDay> getOutageDayById(int id) throws IOException,SQLException{
        final String queryStatement = "SELECT outage_id, outage_date, outage_link, outage_data FROM " + TABLE_NAME + " WHERE outage_id = " + id;
        Optional<OutageDay> optionalAddresses = Optional.empty();
        try(PreparedStatement ps = conn.prepareStatement(queryStatement)){
            ResultSet rs = ps.executeQuery(queryStatement);
            if(rs.next()){
                LocalDate dt = rs.getDate(2).toLocalDate();
                String link = rs.getString(3);
                Blob BlobJsonAddresses = rs.getBlob(4);
                String JsonAddresses = OracleDBCommon.blobToString(BlobJsonAddresses);
                OutageDay addresses = new OutageDay(JsonAddresses, dt, id, link);
                if(addresses.getAddresses().size() > 0 && link.length() >0)
                    optionalAddresses = Optional.of(addresses);
            }
        }
        return optionalAddresses;
    }

    public List<OutageDay> getOutageDaysByRange(LocalDate startDate, LocalDate endDate) throws IOException,SQLException{
        final String queryStatement = "SELECT outage_id, outage_date, outage_link, outage_data FROM "
                + TABLE_NAME
                + " WHERE outage_date BETWEEN (?) AND (?)"
                + " ORDER BY outage_date ASC";
        List<OutageDay> outagesByRange = new ArrayList<>();

        try (PreparedStatement ps = conn.prepareStatement(queryStatement)){
            ps.setDate(1, java.sql.Date.valueOf(startDate));
            ps.setDate(2, java.sql.Date.valueOf(endDate));
            ResultSet rs = ps.executeQuery();
            conn.commit();
            while(rs.next()){
                int id = rs.getInt(1);
                LocalDate dt = rs.getDate(2).toLocalDate();
                String link = rs.getString(3);
                Blob BlobJsonAddresses = rs.getBlob(4);
                String JsonAddresses = OracleDBCommon.blobToString(BlobJsonAddresses);
                OutageDay addresses = new OutageDay(JsonAddresses, dt, id, link);
                if(addresses.getAddresses().size() > 0 && link.length() >0)
                    outagesByRange.add(addresses);
            }
        }
        return outagesByRange;
    }

    public List<OutageDay> getOutageDaysFromDate(LocalDate startDate) throws IOException,SQLException{
        return this.getOutageDaysByRange(startDate, this.getLastDate());
    }

    public List<OutageDay> getOutageDaysToDate(LocalDate endDate) throws IOException,SQLException{
        return this.getOutageDaysByRange(LocalDate.of(2000,01,01),endDate);
    }

    public int getLastId() throws SQLException{
        final String queryStatement = "SELECT MAX(outage_id) FROM " + TABLE_NAME;
        try(PreparedStatement ps = conn.prepareStatement(queryStatement)){
            ResultSet rs = ps.executeQuery(queryStatement);
            conn.commit();
            if(rs.next()){
                return rs.getInt(1);
            }
            return 0;
        }
    }

    public LocalDate getLastDate() throws SQLException{
        final String queryStatement = "SELECT MAX(outage_date) FROM " + TABLE_NAME;
        try(PreparedStatement ps = conn.prepareStatement(queryStatement)){
            ResultSet rs = ps.executeQuery();
            conn.commit();
            if(rs.next()){
                return rs.getDate(1).toLocalDate();
            }
        }
        return null;
    }
}
