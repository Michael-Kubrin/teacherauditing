package org.sibadi.auditing.domain.errors

sealed trait AppError {
  def cast: AppError = this
}

object AppError {

  final case class Unexpected(t: Throwable) extends AppError

  final case class TeacherDoesNotExists(t: Throwable) extends AppError

  final case class ReviewerDoesNotExists(t: Throwable) extends AppError

  final case class TopicDoesNotExists(t: Throwable) extends AppError

  final case class TopicKPIDoesNotExists(t: Throwable) extends AppError

}
