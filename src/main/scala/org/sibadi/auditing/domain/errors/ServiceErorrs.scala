package org.sibadi.auditing.domain.errors

sealed trait AppError {
  def cast: AppError = this
}

object AppError {

  final case class Unexpected(t: Throwable) extends AppError

  final case class TeacherDoesNotExists(teacherId: String) extends AppError

  final case class GroupDoesNotExists(groupId: String) extends AppError

  final case class KpiDoesNotExists(id: String) extends AppError

  final case class ReviewerDoesNotExists(id: String) extends AppError

  final case class TopicDoesNotExists(t: Throwable) extends AppError

  final case class TopicKPIDoesNotExists(t: Throwable) extends AppError

  final case class GroupByIdDoesNotExists(t: Throwable) extends AppError

  final case class EstimateDoesNotExists(topicId: String, kpiId: String, teacherId: String) extends AppError

  final case class KpiByIdDoesNotExists(t: Throwable) extends AppError

  final case class TeacherByIdDoesNotExists(t: Throwable) extends AppError

  final case class TeacherWithoutGroup(teacherId: String) extends AppError

}
