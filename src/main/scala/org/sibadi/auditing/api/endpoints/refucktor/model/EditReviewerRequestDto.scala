package org.sibadi.auditing.api.endpoints.refucktor.model

final case class EditReviewerRequestDto(name: String, surName: String, middleName: Option[String])