//package org.sibadi.auditing.configs
//
//import cats.effect.{Resource, Sync}
//import pureconfig.ConfigSource
//
//import scala.util.Try
//
//object ConfigKeeper {
//
//  def getFullResource[F[_]: Sync]: Resource[F, TeacherConfig] =
//    Resource.eval(Sync[F].fromTry(Try(ConfigSource.default.loadOrThrow[TeacherConfig])))
//
//}
