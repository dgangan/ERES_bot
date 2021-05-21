package com.eres.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import com.eres.common.PropertiesLoader;
import com.eres.parser.OutageDay;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import oracle.ucp.jdbc.PoolDataSource;

public class OracleDBCommon {

    private static Connection conn;
    private static String propFile = "./conf/oracle_db.properties";

    public static void createConnection(String propertiesFile) throws SQLException, IOException {
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
        conn.setAutoCommit(false);
    }

    public static Connection getConnection() throws SQLException, IOException {
        if (conn==null) createConnection(propFile);
        return conn;
    }

    public static String blobToString(Blob blob) throws IOException, SQLException{
        BufferedReader bufReader = new BufferedReader(new InputStreamReader(blob.getBinaryStream()));
        StringBuilder sb = new StringBuilder();
        String s;
        while((s = bufReader.readLine()) != null)
            sb.append(s);
        return sb.substring(3);
    }
}