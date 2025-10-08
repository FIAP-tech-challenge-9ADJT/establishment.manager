CREATE TABLE IF NOT EXISTS menu_item (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         name VARCHAR(255) NOT NULL,
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    available_in_place_only BOOLEAN NOT NULL DEFAULT FALSE,
    photo_path VARCHAR(500),
    restaurant_id BIGINT NOT NULL,

    CONSTRAINT fk_menu_item_restaurant FOREIGN KEY (restaurant_id)
    REFERENCES restaurant(id)
    ON DELETE CASCADE
    );
