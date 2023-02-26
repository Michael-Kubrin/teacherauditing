package org.sibadi.auditing.api.endpoints.model

final case class TeacherItemResponseDto(
  id: String,
  name: String,
  surName: String,
  middleName: Option[String]
)
