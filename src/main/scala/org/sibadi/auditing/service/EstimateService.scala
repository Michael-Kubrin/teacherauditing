package org.sibadi.auditing.service

import cats.data.EitherT
import cats.effect.{MonadCancel, Resource}
import cats.syntax.all._
import org.sibadi.auditing.db
import org.sibadi.auditing.db._
import org.sibadi.auditing.domain._
import org.sibadi.auditing.domain.errors.AppError
import org.sibadi.auditing.util.Filer

import java.io.File
import java.time.{LocalDateTime, ZoneId}
import java.util.UUID

class EstimateService[F[_]](
  estimateDAO: EstimateDAO[F],
  teacherGroupDAO: TeacherGroupDAO[F],
  estimateFilesDAO: EstimateFilesDAO[F],
  filer: Filer[F]
)(implicit M: MonadCancel[F, Throwable]) {

  def createEstimate(topicId: String, kpiId: String, teacherId: String, score: Long): EitherT[F, AppError, Unit] =
    for {
      group <- EitherT.fromOptionF(teacherGroupDAO.getByTeacherId(teacherId), AppError.TeacherWithoutGroup(teacherId).cast)
      _ <- EitherT(
        estimateDAO
          .insert(db.Estimate(topicId, kpiId, group.groupId, teacherId, EstimateStatus.Waiting.toString, score, None, LocalDateTime.now(ZoneId.of("UTC"))))
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

  def createEstimateFiles(topicId: String, kpiId: String, teacherId: String, file: File): EitherT[F, AppError, Unit] = {
    val fileId   = Option(file.getName).filterNot(_.isBlank).getOrElse(UUID.randomUUID().toString)
    val filePath = s"/$topicId/$kpiId/$teacherId/$fileId"
    for {
      _ <- filer.saveToF(file, filePath)
      dbItem = db.EstimateFiles(topicId, kpiId, teacherId, fileId, filePath)
      _ <- EitherT.liftF(
        estimateFilesDAO
          .insert(dbItem)
          .map(_.asRight[AppError])
          .handleError(throwable => AppError.Unexpected(throwable).asLeft[Unit])
      )
    } yield ()
  }

  def getEstimateFiles(topicId: String, kpiId: String, teacherId: String): EitherT[F, AppError, Option[EstimateFiles]] =
    EitherT(
      estimateFilesDAO
        .get(topicId, kpiId, teacherId)
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.Unexpected(throwable).asLeft[Option[EstimateFiles]])
    )

  def deleteEstimateFiles(topicId: String, kpiId: String, teacherId: String): EitherT[F, AppError, Unit] =
    EitherT(
      estimateFilesDAO
        .delete(topicId, kpiId, teacherId)
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.Unexpected(throwable).cast.asLeft[Unit])
    )

}

object EstimateService {
  def apply[F[_]](estimateDAO: EstimateDAO[F], teacherGroupDAO: TeacherGroupDAO[F], estimateFilesDAO: EstimateFilesDAO[F], filer: Filer[F])(implicit
    M: MonadCancel[F, Throwable]
  ): Resource[F, EstimateService[F]] =
    Resource.pure(new EstimateService(estimateDAO, teacherGroupDAO, estimateFilesDAO, filer))
}
