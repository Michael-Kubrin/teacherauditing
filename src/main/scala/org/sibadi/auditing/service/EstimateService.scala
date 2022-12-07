package org.sibadi.auditing.service

import cats.data.EitherT
import cats.effect.{MonadCancel, Resource}
import cats.syntax.all._
import org.sibadi.auditing.db
import org.sibadi.auditing.db.{Estimate, EstimateDAO, EstimateFiles, EstimateFilesDAO, TeacherGroupDAO}
import org.sibadi.auditing.domain._
import org.sibadi.auditing.domain.errors.AppError

import java.time.{LocalDateTime, ZoneId}
import java.util.UUID

class EstimateService[F[_]](
  estimateDAO: EstimateDAO[F],
  teacherGroupDAO: TeacherGroupDAO[F],
  estimateFilesDAO: EstimateFilesDAO[F]
)(implicit M: MonadCancel[F, Throwable]) {

  def createEstimate(topicId: String, kpiId: String, teacherId: String): EitherT[F, AppError, Unit] =
    for {
      group <- EitherT.fromOptionF(teacherGroupDAO.getByTeacherId(teacherId), AppError.TeacherWithoutGroup(teacherId).cast)
      _ <- EitherT(
        estimateDAO
          .insert(db.Estimate(topicId, kpiId, group.groupId, teacherId, EstimateStatus.Waiting.toString, None, LocalDateTime.now(ZoneId.of("UTC"))))
          .map(_.asRight[AppError])
          .handleError(throwable => AppError.Unexpected(throwable).asLeft[Unit])
      )
    } yield ()

  // TODO implement as changing estimate state
  def updateEstimate(topicId: String, kpiId: String, teacherId: String): EitherT[F, AppError, Unit] =
    for {
      group <- EitherT.fromOptionF(teacherGroupDAO.getByTeacherId(teacherId), AppError.TeacherWithoutGroup(teacherId).cast)
      _ <- EitherT(
        estimateDAO
          .update(db.Estimate(topicId, kpiId, group.groupId, teacherId, EstimateStatus.Waiting.toString, None, LocalDateTime.now(ZoneId.of("UTC"))))
          .map(_.asRight[AppError])
          .handleError(throwable => AppError.Unexpected(throwable).asLeft[Unit])
      )
    } yield ()

  def getEstimate(topicId: String, kpiId: String, teacherId: String): EitherT[F, AppError, Option[Estimate]] =
    for {
      _ <- EitherT(
        estimateDAO
          .get(topicId, kpiId, teacherId)
          .map(_.asRight[AppError])
          .handleError(throwable => AppError.Unexpected(throwable).asLeft[Option[Estimate]])
      )
    } yield ()

  //TODO: EstimateFilesService:

  //TODO: Which type of estimateFile: EstimateFile has to be need written?
  def createEstimateFiles(estimateFiles: ???): EitherT[F, AppError, Unit] =
    EitherT(
      estimateFilesDAO
        .insert(
          db.EstimateFiles(
            estimateFiles,
            estimateFiles,
            estimateFiles,
            estimateFiles,
            estimateFiles
          )
        )
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.Unexpected(throwable).asLeft[Unit])
    )

  def getEstimateFiles(topicId: String, kpiId: String, teacherId: String): EitherT[F, AppError, Option[EstimateFiles]] =
    EitherT(
      estimateFilesDAO
        .get(topicId, kpiId, teacherId)
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.Unexpected(throwable).asLeft[Option[EstimateFiles]])
    )

  def deleteEstimateFiles(topicId: String, kpiId: String, teacherId: String): EitherT[F, AppError, Option[EstimateFiles]] =
    EitherT(
      estimateFilesDAO
        .delete(topicId, kpiId, teacherId)
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.Unexpected(throwable).asLeft[Option[EstimateFiles]])
    )

}

object EstimateService {
  def apply[F[_]](estimateDAO: EstimateDAO[F], teacherGroupDAO: TeacherGroupDAO[F], estimateFilesDAO: EstimateFilesDAO[F])(implicit
    M: MonadCancel[F, Throwable]
  ): Resource[F, EstimateService[F]] =
    Resource.pure(new EstimateService(estimateDAO, teacherGroupDAO, estimateFilesDAO))
}
