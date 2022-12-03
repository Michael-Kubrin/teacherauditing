package org.sibadi.auditing.errors

sealed class ServiceErrors(val code: String)

object ServiceErrors {

  val teacherNotFound = new ServiceErrors("TEACHER_NOT_FOUND")

  object Validation {

    val invalidAuth = new ServiceErrors("Invalid Authentication")

    val invalidTeacherName = new ServiceErrors("TEACHER_INVALID_NAME")
  }
}
