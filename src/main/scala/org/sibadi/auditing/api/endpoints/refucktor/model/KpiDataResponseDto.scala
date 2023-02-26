package org.sibadi.auditing.api.endpoints.refucktor.model

final case class KpiDataResponseDto(
  description: Option[String],
  score: Option[Double],
  files: List[AttachmentDto],
  status: EstimateStatusDto
)
