#Oracle DB initialization
CREATE TABLE outages (
    outage_id NUMBER,
    outage_date DATE,
    outage_link VARCHAR2(255),
    outage_data BLOB,
    PRIMARY KEY(outage_id)
)