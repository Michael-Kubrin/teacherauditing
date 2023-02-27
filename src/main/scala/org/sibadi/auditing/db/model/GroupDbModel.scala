package org.sibadi.auditing.db.model

import java.time.LocalDateTime

final case class GroupDbModel(
  id: String,
  title: String,
  deleteDt: Option[LocalDateTime]
)
