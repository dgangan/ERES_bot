package com.eres;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Properties;

import oracle.ucp.jdbc.PoolDataSourceFactory;
import oracle.ucp.jdbc.PoolDataSource;

public class DBHelper {

    String TABLE_NAME = "outages";
    Connection conn;

    public DBHelper(String propertiesFile) throws SQLException, IOException {
        Properties dbProperties = PropertiesLoader.loadPropertiesFile(propertiesFile);
        final String DB_URL = dbProperties.getProperty("jdbc_url");
        System.out.println(DB_URL);
        final String DB_USER = dbProperties.getProperty("db_user");
        final String DB_PASSWORD = dbProperties.getProperty("db_pass");
        final String CONN_FACTORY_CLASS_NAME="oracle.jdbc.pool.OracleDataSource";

        PoolDataSource pds = PoolDataSourceFactory.getPoolDataSource();

        // Set the connection factory first before all other properties
        pds.setConnectionFactoryClassName(CONN_FACTORY_CLASS_NAME);
        pds.setURL(DB_URL);
        pds.setUser(DB_USER);
        pds.setPassword(DB_PASSWORD);
        pds.setConnectionPoolName("JDBC_UCP_POOL");

        pds.setInitialPoolSize(5);
        pds.setMinPoolSize(5);
        pds.setMaxPoolSize(20);
        pds.getConnection();
        conn = pds.getConnection();
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
                String JsonAddresses = blobToString(BlobJsonAddresses);
                OutageDay addresses = new OutageDay(JsonAddresses, dt, id, link);
                if(addresses.getAddresses().size() > 0 && link.length() >0)
                    optionalAddresses = Optional.of(addresses);
            }
        }
        return optionalAddresses;
    }

    public int getLastId() throws SQLException{
        final String queryStatement = "SELECT MAX(outage_id) FROM " + TABLE_NAME;
        try(PreparedStatement ps = conn.prepareStatement(queryStatement)){
            ResultSet rs = ps.executeQuery(queryStatement);
            if(rs.next()){
                return rs.getInt(1);
            }
            return 0;
        }
    }

    private String blobToString(Blob blob) throws IOException, SQLException{
        BufferedReader bufReader = new BufferedReader(new InputStreamReader(blob.getBinaryStream()));
        StringBuilder sb = new StringBuilder();
        String s;
        while((s = bufReader.readLine()) != null)
            sb.append(s);
        return sb.substring(3);
    }
}