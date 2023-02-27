package org.sibadi.auditing.db.model

import java.time.LocalDateTime

final case class EstimateDbModel(
                           topicId: String,
                           kpiId: String,
                           groupId: String,
                           teacherId: String,
                           status: String,
                           score: Long,
                           lastReviewerId: Option[String],
                           lastChangesDt: LocalDateTime
                         )