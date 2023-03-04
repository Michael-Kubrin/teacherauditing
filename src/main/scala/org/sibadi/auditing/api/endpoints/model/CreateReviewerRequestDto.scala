package org.sibadi.auditing.api.endpoints.model

final case class CreateReviewerRequestDto(firstName: String, lastName: String, middleName: Option[String], login: String)
