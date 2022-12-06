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

CREATE TABLE IF NOT EXISTS reviewer(
    id TEXT PRIMARY KEY,
    firstName TEXT NOT NULL,
    lastName TEXT NOT NULL,
    middleName TEXT,
    deleteDt TIMESTAMP
);

CREATE TABLE IF NOT EXISTS reviewer_credentials(
    id TEXT NOT NULL,
    login TEXT NOT NULL,
    passwordHash TEXT NOT NULL,
    bearer TEXT NOT NULL,
    PRIMARY KEY (id, login)
);

CREATE TABLE IF NOT EXISTS "group" (
    id TEXT PRIMARY KEY,
    title TEXT NOT NULL,
    deleteDt TIMESTAMP
);

CREATE TABLE IF NOT EXISTS kpi(
    id TEXT PRIMARY KEY,
    title TEXT NOT NULL,
    deleteDt TIMESTAMP
);

CREATE TABLE IF NOT EXISTS topic(
    id TEXT PRIMARY KEY,
    title TEXT NOT NULL,
    deleteDt TIMESTAMP
);

CREATE TABLE IF NOT EXISTS topic_kpi(
    topicId TEXT,
    kpiId TEXT,
    PRIMARY KEY (topicId, kpiId)
);

CREATE TABLE IF NOT EXISTS kpi_group(
    kpiId TEXT,
    groupId TEXT,
    PRIMARY KEY (kpiId, groupId)
);

CREATE TABLE IF NOT EXISTS teacher_group(
    teacherId TEXT,
    groupId TEXT,
    PRIMARY KEY (teacherId, groupId)
);

CREATE TABLE IF NOT EXISTS estimate(
    topicId TEXT,
    kpiId TEXT,
    groupId TEXT,
    teacherId TEXT,
    status TEXT NOT NULL,
    lastReviewerId TEXT,
    lastChangesDt TIMESTAMP NOT NULL,
    PRIMARY KEY (topicId, kpiId, groupId, teacherId)
);

CREATE TABLE IF NOT EXISTS estimate_files(
    topicId TEXT,
    kpiId TEXT,
    teacherId TEXT,
    fileId TEXT,
    path TEXT NOT NULL,
    PRIMARY KEY (topicId, kpiId, groupId, teacherId, fileId)
);
