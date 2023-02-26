package org.sibadi.auditing.api.endpoints.refucktor.model

final case class KpiReviewerDataResponseDto(
  description: Option[String],
  score: Option[Double],
  files: List[AttachmentDto],
  status: EstimateStatusDto
)
