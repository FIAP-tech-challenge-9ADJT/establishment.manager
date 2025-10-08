CREATE TABLE IF NOT EXISTS menu_items (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    price DOUBLE NOT NULL,
    photo_url VARCHAR(255) NOT NULL,
    restaurant_id BIGINT NOT NULL,

    CONSTRAINT uq_menu_item_name_restaurant UNIQUE (name, restaurant_id),

    CONSTRAINT fk_menu_item_restaurant FOREIGN KEY (restaurant_id)
        REFERENCES restaurant(id)
        ON DELETE CASCADE
);