CREATE TABLE events (
    id UUID NOT NULL,
    title VARCHAR(200) NOT NULL,
    description VARCHAR(2000),
    category VARCHAR(30) NOT NULL,
    venue_name VARCHAR(200) NOT NULL,
    city VARCHAR(100) NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    total_seats INTEGER NOT NULL,
    available_seats INTEGER NOT NULL,
    price_in_paise BIGINT NOT NULL,
    status VARCHAR(20) NOT NULL,
    organizer_id UUID NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    CONSTRAINT pk_events PRIMARY KEY (id)
  )