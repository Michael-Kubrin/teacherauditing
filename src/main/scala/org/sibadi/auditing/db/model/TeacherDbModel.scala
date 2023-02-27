package org.sibadi.auditing.db.model

import java.time.LocalDateTime

final case class TeacherDbModel(
  id: String,
  firstName: String,
  lastName: String,
  middleName: Option[String],
  deleteDt: Option[LocalDateTime]
)
