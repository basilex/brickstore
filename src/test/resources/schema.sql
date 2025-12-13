-- H2 Test Schema - Compatible with application schema

-- Create COUNTRY table
CREATE TABLE IF NOT EXISTS country (
    id          VARCHAR(64)  NOT NULL PRIMARY KEY,
    pid         VARCHAR(64)  NOT NULL UNIQUE,
    name        VARCHAR(255) NOT NULL UNIQUE,
    iso2        VARCHAR(2)   NOT NULL UNIQUE,
    iso3        VARCHAR(3)   NOT NULL UNIQUE,
    code        SMALLINT     NOT NULL UNIQUE,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT '1000-01-01'
);

-- Create CURRENCY table
CREATE TABLE IF NOT EXISTS currency (
    id          VARCHAR(64)  NOT NULL PRIMARY KEY,
    pid         VARCHAR(64)  NOT NULL UNIQUE,
    name        VARCHAR(255) NOT NULL UNIQUE,
    iso3        VARCHAR(3)   NOT NULL UNIQUE,
    code        SMALLINT     NOT NULL UNIQUE,
    symbol      VARCHAR(10)  NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP    NOT NULL DEFAULT '1000-01-01'
);

-- Create COUNTRY_CURRENCY table
CREATE TABLE IF NOT EXISTS country_currency (
    id          VARCHAR(64)  NOT NULL PRIMARY KEY,
    country_id  VARCHAR(64)  NOT NULL,
    currency_id VARCHAR(64)  NOT NULL,
    created_at  TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (country_id, currency_id),
    FOREIGN KEY (country_id) REFERENCES country(id) ON DELETE CASCADE,
    FOREIGN KEY (currency_id) REFERENCES currency(id) ON DELETE CASCADE
);

-- Create indexes
CREATE INDEX IF NOT EXISTS country_currency_country_id_ix ON country_currency (country_id);
CREATE INDEX IF NOT EXISTS country_currency_currency_id_ix ON country_currency (currency_id);
