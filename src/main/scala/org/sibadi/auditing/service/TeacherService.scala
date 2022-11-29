package org.sibadi.auditing.service

import cats.data.EitherT
import cats.effect.Sync
import org.sibadi.auditing.domain.response.TeacherResponse
import org.slf4j.LoggerFactory

trait TeacherService[F[_]] {

  def create(id: String): EitherT[F, TeacherService[F], TeacherResponse]
  def update(id: String): EitherT[F, TeacherService[F], TeacherResponse]
  def delete(id: String): EitherT[F, TeacherService[F], TeacherResponse]
}

class TeacherServiceImpl[F[_]: Sync]() extends TeacherService[F] {
  private val log = LoggerFactory.getLogger(this.getClass)

  override def create(id: String): EitherT[F, TeacherService[F], TeacherResponse] =
    ???

  override def update(id: String): EitherT[F, TeacherService[F], TeacherResponse] =
    ???

  override def delete(id: String): EitherT[F, TeacherService[F], TeacherResponse] =
    ???
}
