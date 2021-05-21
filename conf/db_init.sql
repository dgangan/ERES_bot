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