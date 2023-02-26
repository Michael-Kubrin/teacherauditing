package org.sibadi.auditing.api.endpoints.refucktor.model

final case class TeacherInGroupItemDto(
  id: String,
  firstName: String,
  lastName: String,
  middleName: Option[String]
)
