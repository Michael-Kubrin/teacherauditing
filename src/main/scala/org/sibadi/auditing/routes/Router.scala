//package org.sibadi.auditing.routes
//
//import cats.effect.Async
//import org.http4s.HttpRoutes
//import org.sibadi.auditing.service.TeacherService
//import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}
//
//
//class Router[F[_]: Async](service: TeacherService[F])(implicit serverOptions: Http4sServerOptions[F, F]) extends RouterOps[F] {
//
//  def routes: HttpRoutes[F] =
//
//}
//
//object Router {
//  def apply[F[_]: Async](service: Router[F])(implicit serverOptions: Http4sServerOptions[F, F]): Router[F] =
//    new Router(service)
//}