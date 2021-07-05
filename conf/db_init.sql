#Oracle DB initialization
CREATE TABLE outages (
    outage_id NUMBER,
    outage_date DATE,
    outage_link VARCHAR2(255),
    outage_data BLOB,
    PRIMARY KEY(outage_id)
)

CREATE TABLE users (
    user_id NUMBER(19),
    user_name VARCHAR(255),
    user_first_name VARCHAR(255),
    user_last_name VARCHAR(255),
    user_role VARCHAR(255),
    chat_id NUMBER(19),
    register_date DATE,
    PRIMARY KEY(user_id)
)

CREATE TABLE user_settings (
    user_id NUMBER(19),
    address_region VARCHAR(255),
    address_city VARCHAR(255),
    address_street VARCHAR(255),
    enabled VARCHAR(255) DEFAULT 't',
    repeat VARCHAR(255) DEFAULT 't',
    PRIMARY KEY(user_id)
)

    MERGE INTO users d USING (SELECT 1 user_id, 'yyy' user_name from dual) s ON ( d.USER_ID=s.USER_ID )
      WHEN MATCHED THEN UPDATE SET d.user_name=s.user_name
      WHEN NOT MATCHED THEN INSERT (user_id,user_name)
        VALUES ( s.user_id, s.user_name);