CREATE TABLE IF NOT EXISTS teacher(
    id TEXT PRIMARY KEY,
    firstName TEXT NOT NULL,
    lastName TEXT NOT NULL,
    middleName TEXT,
    deleteDt TIMESTAMP
);

CREATE TABLE IF NOT EXISTS teacher_credentials(
    id TEXT NOT NULL,
    login TEXT NOT NULL,
    passwordHash TEXT NOT NULL,
    bearer TEXT NOT NULL,
    PRIMARY KEY (id, login)
);
