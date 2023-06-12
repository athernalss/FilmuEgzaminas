Aplikacijos naudojimui reikia pasijungti XAMPP Apache bei MySQL modules.

MySQL databazėje sukurkite naują DB pavadinimu "filmai".

Paspaudus import, įklijuokitę šį kodą:
-- ----------------------------------
CREATE DATABASE IF NOT EXISTS filmai;

USE filmai;

CREATE TABLE IF NOT EXISTS categories (
  category_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS movies (
  movie_id INT AUTO_INCREMENT PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  rating DOUBLE,
  category_id INT,
  FOREIGN KEY (category_id) REFERENCES categories(category_id)
);
CREATE TABLE IF NOT EXISTS users (
  user_id INT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(255) NOT NULL,
  password VARCHAR(255) NOT NULL,
  isAdmin TINYINT(1) DEFAULT 0
);
-- ----------------------------------
