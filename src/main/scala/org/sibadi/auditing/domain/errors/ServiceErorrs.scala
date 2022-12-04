package org.sibadi.auditing.domain.errors

sealed trait AppError {
  def cast: AppError = this
}

object AppError {

  final case class Unexpected(t: Throwable) extends AppError

}
