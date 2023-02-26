package org.sibadi.auditing.api.endpoints.refucktor.model

final case class CreateReviewerRequestDto(name: String, surName: String, middleName: Option[String], login: String)