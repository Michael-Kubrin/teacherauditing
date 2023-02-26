package org.sibadi.auditing.api.endpoints.model

final case class CreateTeacherRequestDto(name: String, surName: String, middleName: Option[String], login: String)
