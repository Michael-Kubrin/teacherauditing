package org.sibadi.auditing.api.endpoints.model

final case class EditTeacherRequestDto(name: String, surName: String, middleName: Option[String])