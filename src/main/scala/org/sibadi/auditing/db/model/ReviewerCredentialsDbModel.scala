package org.sibadi.auditing.db.model

final case class ReviewerCredentialsDbModel(
  id: String,
  login: String,
  passwordHash: String,
  bearer: String
)
