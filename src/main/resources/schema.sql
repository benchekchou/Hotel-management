CREATE TABLE rooms (
                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                       type TEXT NOT NULL,
                       price REAL NOT NULL,
                       available BOOLEAN NOT NULL
);
CREATE TABLE clients (
                         id INTEGER PRIMARY KEY AUTOINCREMENT,
                         name TEXT NOT NULL,
                         contact_info TEXT NOT NULL,
                         password TEXT NOT NULL,
                         phone_number TEXT NOT NULL
);


CREATE TABLE reservations (
                              id INTEGER PRIMARY KEY AUTOINCREMENT,
                              room_id INTEGER NOT NULL,
                              client_id INTEGER NOT NULL,
                              check_in_date DATE NOT NULL,
                              check_out_date DATE NOT NULL,
                              confirmed BOOLEAN NOT NULL,
                              FOREIGN KEY (room_id) REFERENCES rooms(id),
                              FOREIGN KEY (client_id) REFERENCES clients(id)
);

CREATE TABLE payments (
                          id INTEGER PRIMARY KEY AUTOINCREMENT,
                          client_id INTEGER NOT NULL,
                          amount REAL NOT NULL,
                          date TEXT NOT NULL,
                          FOREIGN KEY (client_id) REFERENCES clients(id)
);

CREATE TABLE users (
                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                       username TEXT NOT NULL UNIQUE,
                       password TEXT NOT NULL
);