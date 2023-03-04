package org.sibadi.auditing.service.refucktor

import cats.data.EitherT
import cats.effect.MonadCancel
import cats.effect.std.UUIDGen
import cats.syntax.functor._
import doobie.syntax.all._
import doobie.util.transactor.Transactor
import org.sibadi.auditing.api.endpoints.model._
import org.sibadi.auditing.db.model.ReviewerDbModel
import org.sibadi.auditing.syntax.sql._
import org.typelevel.log4cats.Logger
import doobie.postgres.implicits.JavaTimeLocalDateTimeMeta

class ReviewerService[F[_]: UUIDGen: Logger](
  transactor: Transactor[F]
)(implicit M: MonadCancel[F, Throwable]) {

  def getAll: EitherT[F, ApiError, List[ReviewerItemResponseDto]] =
    sql"""SELECT id, firstName, lastName, middleName, deleteDt FROM reviewer WHERE deleteDt IS NOT null"""
      .query[ReviewerDbModel]
      .to[List]
      .eitherT(transactor)
      .map(_.map(t => ReviewerItemResponseDto(id = t.id, firstName = t.firstName, lastName = t.lastName, middleName = t.middleName)))

  def delete(id: String): EitherT[F, ApiError, Unit] =
    sql"""DELETE FROM reviewer WHERE id = $id""".update.run.eitherT(transactor).void

  def edit(id: String, body: EditReviewerRequestDto): EitherT[F, ApiError, Unit] =
    sql"""
       UPDATE reviewer
       SET
        firstName = ${body.firstName},
        lastName = ${body.lastName},
        middleName = ${body.middleName}
       WHERE id = $id;
       """.update.run.eitherT(transactor).void

}
