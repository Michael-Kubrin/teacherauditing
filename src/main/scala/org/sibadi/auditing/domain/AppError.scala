package org.sibadi.auditing.domain

trait AppError {

  sealed class TeacherErrors(val code: String)

  object TeacherErrors {

    val teacherNotFound = new TeacherErrors("TEACHER_NOT_FOUND")

  }

  object Validation {

    val invalidTeacherName = new TeacherErrors("TEACHER_INVALID_NAME")
  }
}
