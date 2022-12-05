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
    passwordHash: String,
    bearer: String
  )

//  final case class Topics(
//                           title: String,
//                           kpis: String
//  )

  final case class Reviewer(
    id: String,
    firstName: String,
    lastName: String,
    middleName: Option[String],
    deleteDt: Option[LocalDateTime]
  )

}
