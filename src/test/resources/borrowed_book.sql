DROP TABLE borrowed_book;
CREATE VIEW borrowed_book AS SELECT * FROM book WHERE borrowed = TRUE;
