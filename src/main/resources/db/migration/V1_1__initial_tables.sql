CREATE TABLE location
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    address         VARCHAR(255) NOT NULL,
    street_number   VARCHAR(255) NOT NULL,
    reference_point VARCHAR(255),
    maps_url        VARCHAR(255),
    neighborhood    VARCHAR(255) NOT NULL,
    city            VARCHAR(255) NOT NULL
);

CREATE TABLE category
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    age_group   VARCHAR(255) NOT NULL
);

CREATE TABLE artist
(
    id     BIGINT AUTO_INCREMENT PRIMARY KEY,
    name   VARCHAR(255) NOT NULL,
    type   VARCHAR(255) NOT NULL,
    gender VARCHAR(255) NOT NULL
);

CREATE TABLE event_organizer
(
    id      BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id VARCHAR(255) NOT NULL,
    name    VARCHAR(255) NOT NULL,
    title   VARCHAR(255) NOT NULL
);

CREATE TABLE event_type
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL
);

CREATE TABLE event
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    location_id BIGINT       NOT NULL,
    description VARCHAR(255) NOT NULL,
    date_time   DATETIME     NOT NULL,
    image_path  VARCHAR(255),
    category_id BIGINT       NOT NULL,
    details     VARCHAR(255),
    FOREIGN KEY (location_id) REFERENCES location (id),
    FOREIGN KEY (category_id) REFERENCES category (id)
);

CREATE TABLE artist_event
(
    artist_id BIGINT,
    event_id  BIGINT,
    PRIMARY KEY (artist_id, event_id),
    FOREIGN KEY (artist_id) REFERENCES artist (id),
    FOREIGN KEY (event_id) REFERENCES event (id)
);

CREATE TABLE event_organizer_event
(
    event_organizer_id BIGINT,
    event_id           BIGINT,
    PRIMARY KEY (event_organizer_id, event_id),
    FOREIGN KEY (event_organizer_id) REFERENCES event_organizer (id),
    FOREIGN KEY (event_id) REFERENCES event (id)
);

CREATE TABLE review
(
    id        BIGINT AUTO_INCREMENT PRIMARY KEY,
    event_id  BIGINT       NOT NULL,
    user_id   VARCHAR(255) NOT NULL,
    rating    FLOAT        NOT NULL,
    comment   VARCHAR(255),
    timestamp DATETIME     NOT NULL,
    FOREIGN KEY (event_id) REFERENCES event (id)
);
