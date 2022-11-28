package org.sibadi.auditing.domain

import java.time.ZonedDateTime

object response {

  object UserActionType extends Enumeration {
    type UserActionType = Value

    val UserCreated: UserActionType = Value("created")
    val UserUpdated: UserActionType = Value("updated")
    val UserDeleted: UserActionType = Value("deleted")
  }

  case class TeacherResponse(
    id: String,
    name: String,
    createdAt: ZonedDateTime,
    deletedAt: Option[ZonedDateTime] = None
  )
}
