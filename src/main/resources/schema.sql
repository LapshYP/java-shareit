CREATE TABLE IF NOT EXISTS users (
                                           id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                           name VARCHAR(255) NOT NULL,
                                           email VARCHAR(512) NOT NULL,
                                           CONSTRAINT pk_user PRIMARY KEY (id),
                                           CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
      );
CREATE TABLE IF NOT EXISTS items (
                                     id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                     name VARCHAR(255) NOT NULL,
                                     description VARCHAR(512) NOT NULL,
                                     owner_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
                                     available BOOLEAN NOT NULL,
                                     request BIGINT,
                                     CONSTRAINT pk_item PRIMARY KEY (id),
                                     CONSTRAINT UQ_ITEM_NAME UNIQUE (name)
);
CREATE TABLE IF NOT EXISTS bookings (
                                        id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
                                        start timestamp,
                                        end_time timestamp,
                                        item_id BIGINT REFERENCES items(id) ON DELETE CASCADE,
                                        booker_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
                                        status VARCHAR(255),
                                        CONSTRAINT pk_booking PRIMARY KEY (id),
                                        CONSTRAINT UQ_BOOKING_START_END_ITEM UNIQUE (start, end_time, item_id)
);