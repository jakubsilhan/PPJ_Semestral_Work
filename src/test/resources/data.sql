INSERT INTO COUNTRY(id, name)
VALUES (1, 'CZ'),
       (2, 'GB'),
       (3, 'DE');

INSERT INTO CITY(id, name, longitude, latitude, country_id)
VALUES (1, 'Prague', 14.42076, 50.088039, 1),
       (2, 'Liberec', 15.05619, 50.767109, 1),
       (3, 'Berlin', 13.41053, 52.524368, 3),
       (4, 'London', -0.12574, 51.50853, 2),
       (5, 'Zittau', 14.83333, 50.900002, 3);