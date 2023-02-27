package org.sibadi.auditing.db.model

final case class TeacherCredentialsDbModel(
  id: String,
  login: String,
  passwordHash: String,
  bearer: String
)
