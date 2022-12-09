package org.sibadi.auditing

import java.time.LocalDateTime

package object db {

  final case class Teacher(
    id: String,
    firstName: String,
    lastName: String,
    middleName: Option[String],
    deleteDt: Option[LocalDateTime]
  )

  final case class TeacherCredentials(
    id: String,
    login: String,
    passwordHash: String,
    bearer: String
  )

  final case class Reviewer(
    id: String,
    firstName: String,
    lastName: String,
    middleName: Option[String],
    deleteDt: Option[LocalDateTime]
  )

  final case class ReviewerCredentials(
    id: String,
    login: String,
    passwordHash: String,
    bearer: String
  )

  final case class Group(
    id: String,
    title: String,
    deleteDt: Option[LocalDateTime]
  )

  final case class Kpi(
    id: String,
    title: String,
    deleteDt: Option[LocalDateTime]
  )

  final case class Topic(
    id: String,
    title: String,
    deleteDt: Option[LocalDateTime]
  )

  final case class TopicKpi(
    topicId: String,
    kpiId: String
  )

  final case class KpiGroup(
    kpiId: String,
    groupId: String
  )

  final case class TeacherGroup(
    teacherId: String,
    groupId: String
  )

  final case class Estimate(
    topicId: String,
    kpiId: String,
    groupId: String,
    teacherId: String,
    status: String,
    score: Long,
    lastReviewerId: Option[String],
    lastChangesDt: LocalDateTime
  )

  final case class EstimateFiles(
    topicId: String,
    kpiId: String,
    teacherId: String,
    fileId: String,
    path: String
  )

}
