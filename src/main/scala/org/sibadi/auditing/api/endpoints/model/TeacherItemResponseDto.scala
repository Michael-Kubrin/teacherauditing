package org.sibadi.auditing.api.endpoints.model

final case class TeacherItemResponseDto(
  id: String,
  firstName: String,
  lastName: String,
  middleName: Option[String]
)
