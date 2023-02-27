package org.sibadi.auditing.db.model

final case class EstimateFilesDbModel(
  topicId: String,
  kpiId: String,
  teacherId: String,
  fileId: String,
  path: String
)
