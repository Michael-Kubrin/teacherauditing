package org.sibadi.auditing.api.endpoints.model

final case class CreateTeacherRequestDto(firstName: String, lastName: String, middleName: Option[String], login: String)
