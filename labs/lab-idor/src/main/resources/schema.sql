CREATE TABLE payment
(
  id INTEGER AUTO_INCREMENT,
  username VARCHAR(255),
  amount INTEGER,
  description VARCHAR(1023),
  CONSTRAINT pk_payment PRIMARY KEY (id)
);