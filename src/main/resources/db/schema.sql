CREATE TABLE person (
	id INTEGER NOT NULL,
	name VARCHAR(20),
	nationality VARCHAR(2)
);

CREATE TABLE address (
	person_id INTEGER NOT NULL,
	address_1 VARCHAR(20),
	postcode VARCHAR(8)
);
