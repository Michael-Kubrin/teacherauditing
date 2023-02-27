package org.sibadi.auditing.db.model

import java.time.LocalDateTime

final case class KpiDbModel(
  id: String,
  title: String,
  deleteDt: Option[LocalDateTime]
)
