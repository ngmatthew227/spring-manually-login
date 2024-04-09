CREATE TABLE app_user (
  id INT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(50) NOT NULL,
  password VARCHAR(100) NOT NULL,
  role VARCHAR(50) NOT NULL
);

-- create a user
INSERT INTO app_user (username, password, role) VALUES ('user', 'pass1', 'USER');
INSERT INTO app_user (username, password, role) VALUES ('app_admin', 'pass2', 'ADMIN');
