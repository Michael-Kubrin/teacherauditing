CREATE TABLE IF NOT EXISTS teacher(
    id TEXT PRIMARY KEY,
    firstName TEXT NOT NULL,
    lastName TEXT NOT NULL,
    middleName TEXT,
    deleteDt TIMESTAMP
);
