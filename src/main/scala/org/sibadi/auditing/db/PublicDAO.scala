package org.sibadi.auditing.db

import cats.effect.MonadCancel
//import cats.syntax.functor._
import doobie._
//import doobie.postgres.implicits._
//import doobie.syntax.all._

class PublicDAO[F[_]](transactor: Transactor[F])(implicit M: MonadCancel[F, Throwable]) {

//  def createLogin(login: String): F[Unit] =
//    sql"""
//         INSERT INTO
//       """

}
