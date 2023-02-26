package org.sibadi.auditing.api

package object model {

  final case class IdResponseDto(id: String)

  final case class CredentialsResponseDto(id: String, password: String)

  final case class CreateKPIRequestDto(title: String)

  final case class TopicItemResponseDto(id: String, title: String, kpis: List[KPIItemResponseDto])

  final case class KPIItemResponseDto(id: String, title: String)

  final case class EditTopicRequestDto(name: String)

  final case class EditGroupRequestDto(name: String)

  final case class CreateTopicRequestDto(name: String)

  final case class TopicKpiItemResponseDto(id: String, title: String)

  final case class EditKpiRequestDto(name: String)

  final case class CreateTeacherRequestDto(name: String, surName: String, middleName: Option[String], login: String)

  final case class TeacherItemResponseDto(
    id: String,
    name: String,
    surName: String,
    middleName: Option[String]
  )

  final case class EditTeacherRequestDto(name: String, surName: String, middleName: Option[String])

  final case class CreateReviewerRequestDto(name: String, surName: String, middleName: Option[String], login: String)

  final case class ReviewerItemResponseDto(id: String, name: String, surName: String, middleName: Option[String])

  final case class EditReviewerRequestDto(name: String, surName: String, middleName: Option[String])

  final case class CreateGroupRequestDto(name: String)

  final case class GroupItemResponseDto(
    id: String,
    name: String,
    kpis: List[KpiInGroupItemDto],
    teachers: List[TeacherInGroupItemDto]
  )

  final case class TeacherInGroupItemDto(
    id: String,
    firstName: String,
    lastName: String,
    middleName: Option[String]
  )

  final case class KpiInGroupItemDto(id: String, title: String)

}
