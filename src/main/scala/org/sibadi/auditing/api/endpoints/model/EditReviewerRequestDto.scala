package org.sibadi.auditing.api.endpoints.model

final case class EditReviewerRequestDto(name: String, surName: String, middleName: Option[String])