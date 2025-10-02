CREATE TABLE IF NOT EXISTS restaurant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    kitchen_type VARCHAR(100),
    start_operation TIME,
    end_operation TIME,
    owner_id BIGINT NOT NULL,

    CONSTRAINT fk_restaurant_owner FOREIGN KEY (owner_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS restaurant_addresses (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  street VARCHAR(255) NOT NULL,
  city VARCHAR(100) NOT NULL,
  postal_code VARCHAR(20) NOT NULL,
  number VARCHAR(20) NOT NULL,
  restaurant_id BIGINT NOT NULL UNIQUE,

  CONSTRAINT fk_restaurant_address_restaurant
      FOREIGN KEY (restaurant_id) REFERENCES restaurant(id)
);

CREATE TABLE IF NOT EXISTS menu_item (
   id BIGINT AUTO_INCREMENT PRIMARY KEY,
   name VARCHAR(255) NOT NULL,
   description TEXT,
   price DECIMAL(10,2) NOT NULL,
   available_on_site_only BOOLEAN NOT NULL,
   photo_path VARCHAR(500),
   restaurant_id BIGINT NOT NULL,

   CONSTRAINT fk_menu_item_restaurant
       FOREIGN KEY (restaurant_id) REFERENCES restaurant(id)
);

INSERT INTO tb_role (name) VALUES ('RESTAURANT_OWNER');
