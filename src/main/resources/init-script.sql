CREATE SEQUENCE seq_1 INCREMENT BY 1 START WITH 1 NOCYCLE CACHE 10 ORDER;
CREATE TABLE CUSTOMER (CUSTOMER_ID bigint AUTO_INCREMENT PRIMARY KEY, USER_NAME VARCHAR2(40) UNIQUE, FIRST_NAME VARCHAR2(40), LAST_NAME VARCHAR2(40));
CREATE TABLE TRANSACTION (TRANSACTION_ID bigint AUTO_INCREMENT PRIMARY KEY, CUSTOMER_ID bigint ,TRAN_WHEN TIMESTAMP WITH TIME ZONE, TRAN_AMOUNT NUMERIC);

INSERT INTO CUSTOMER(CUSTOMER_ID, USER_NAME, FIRST_NAME,LAST_NAME) values (1, 'rsfw2003', 'Tony','Baek');
INSERT INTO CUSTOMER(CUSTOMER_ID, USER_NAME, FIRST_NAME,LAST_NAME) values (2, 'rtww2003', 'Dan', 'Doppermin');

-- Customer 1, time zone is in EST
INSERT INTO TRANSACTION(TRANSACTION_ID, CUSTOMER_ID, TRAN_WHEN, TRAN_AMOUNT) VALUES (1, 1,'2024-04-05 09:00:00 -05:00',110);
INSERT INTO TRANSACTION(TRANSACTION_ID, CUSTOMER_ID, TRAN_WHEN, TRAN_AMOUNT) VALUES (2, 1,'2024-03-04 09:00:00 -05:00',47);
INSERT INTO TRANSACTION(TRANSACTION_ID, CUSTOMER_ID, TRAN_WHEN, TRAN_AMOUNT) VALUES (3, 1,'2024-02-03 09:00:00 -05:00',357);
INSERT INTO TRANSACTION(TRANSACTION_ID, CUSTOMER_ID, TRAN_WHEN, TRAN_AMOUNT) VALUES (4, 1,'2024-01-05 09:00:00 -05:00',4000);
INSERT INTO TRANSACTION(TRANSACTION_ID, CUSTOMER_ID, TRAN_WHEN, TRAN_AMOUNT) VALUES (5, 1,'2024-01-05 09:00:00 -04:00',32);

-- Customer 2 time zone is in EST -
INSERT INTO TRANSACTION(TRANSACTION_ID, CUSTOMER_ID, TRAN_WHEN, TRAN_AMOUNT) VALUES (6, 2,'2024-04-05 09:00:00 -05:00',532);
INSERT INTO TRANSACTION(TRANSACTION_ID, CUSTOMER_ID, TRAN_WHEN, TRAN_AMOUNT) VALUES (7, 2,'2024-03-04 09:00:00 -05:00',74);
INSERT INTO TRANSACTION(TRANSACTION_ID, CUSTOMER_ID, TRAN_WHEN, TRAN_AMOUNT) VALUES (8, 2,'2024-03-05 09:00:00 -05:00',684);
INSERT INTO TRANSACTION(TRANSACTION_ID, CUSTOMER_ID, TRAN_WHEN, TRAN_AMOUNT) VALUES (9, 2,'2024-02-05 09:00:00 -04:00',145);
INSERT INTO TRANSACTION(TRANSACTION_ID, CUSTOMER_ID, TRAN_WHEN, TRAN_AMOUNT) VALUES (10, 2,'2024-02-05 09:00:00 -04:00',12);

-- Old records
INSERT INTO TRANSACTION(TRANSACTION_ID, CUSTOMER_ID, TRAN_WHEN, TRAN_AMOUNT) VALUES (11, 1,'2023-04-05 09:00:00 -05:00',532);
INSERT INTO TRANSACTION(TRANSACTION_ID, CUSTOMER_ID, TRAN_WHEN, TRAN_AMOUNT) VALUES (12, 1,'2023-03-04 09:00:00 -05:00',74);
INSERT INTO TRANSACTION(TRANSACTION_ID, CUSTOMER_ID, TRAN_WHEN, TRAN_AMOUNT) VALUES (13, 2,'2023-03-05 09:00:00 -05:00',684);
INSERT INTO TRANSACTION(TRANSACTION_ID, CUSTOMER_ID, TRAN_WHEN, TRAN_AMOUNT) VALUES (14, 2,'2023-02-05 09:00:00 -05:00',45);
INSERT INTO TRANSACTION(TRANSACTION_ID, CUSTOMER_ID, TRAN_WHEN, TRAN_AMOUNT) VALUES (15, 2,'2023-02-05 09:00:00 -05:00',12);