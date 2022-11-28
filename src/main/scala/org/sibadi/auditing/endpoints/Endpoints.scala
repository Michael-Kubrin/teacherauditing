package org.sibadi.auditing.endpoints

import cats.effect.Async
import cats.syntax.semigroupk._
import org.http4s.HttpRoutes
import org.sibadi.auditing.routes.RouterOps
import org.sibadi.auditing.service.TeacherService
import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}

class Endpoints[F[_]: Async](teacherService: TeacherService[F])(implicit
  serverOptions: Http4sServerOptions[F]
) extends RouterOps[F] {

//  def routes: HttpRoutes[F] =
//    createTeacher
//    updateTeacher
//    deleteTeacher

//    private def createTeacher: HttpRoutes[F] =
//      Http4sServerInterpreter(serverOptions).toRoutes(???.serverLogic{
//        case (id, request) =>
//
//      }

  //    private def updateTeacher: HttpRoutes[F] =
  //      Http4sServerInterpreter(serverOptions).toRoutes(???.serverLogic{
  //        case (id, request) =>
  //
  //      }

  //    private def deleteTeacher: HttpRoutes[F] =
  //      Http4sServerInterpreter(serverOptions).toRoutes(???.serverLogic{
  //        case (id, request) =>
  //
  //      }
}
