package org.sibadi.auditing.service

import cats.effect.{MonadCancel, Resource}
import org.sibadi.auditing.db._
import org.sibadi.auditing.util.Filer

class PublicService[F[_]](
  estimateDAO: EstimateDAO[F],
  teacherGroupDAO: TeacherGroupDAO[F],
  estimateFilesDAO: EstimateFilesDAO[F],
  filer: Filer[F]
)(implicit M: MonadCancel[F, Throwable]) {

//  def createLogin(): EitherT[F, AppError, Unit] =

}

object PublicService {
  def apply[F[_]](estimateDAO: EstimateDAO[F], teacherGroupDAO: TeacherGroupDAO[F], estimateFilesDAO: EstimateFilesDAO[F], filer: Filer[F])(implicit
    M: MonadCancel[F, Throwable]
  ): Resource[F, PublicService[F]] =
    Resource.pure(new PublicService(estimateDAO, teacherGroupDAO, estimateFilesDAO, filer))
}
