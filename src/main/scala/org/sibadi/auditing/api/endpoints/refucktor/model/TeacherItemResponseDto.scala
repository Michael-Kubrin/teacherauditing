package org.sibadi.auditing.api.endpoints.refucktor.model

final case class TeacherItemResponseDto(
  id: String,
  name: String,
  surName: String,
  middleName: Option[String]
)
