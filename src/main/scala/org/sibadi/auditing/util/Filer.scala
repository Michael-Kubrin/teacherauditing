package org.sibadi.auditing.util

import cats.data.EitherT
import cats.syntax.applicativeError._
import cats.syntax.functor._
import cats.syntax.either._
import cats.effect.{Resource, Sync}
import org.sibadi.auditing.domain.errors.AppError

import java.io.{File, FileInputStream, FileOutputStream}

class Filer[F[_]: Sync] {

  def saveToF(src: File, path: String): EitherT[F, AppError, Unit] =
    EitherT {
      saveTo(src, path)
        .map(_.asRight[AppError])
        .handleError(throwable => AppError.Unexpected(throwable).asLeft)
    }

  private def saveTo(src: File, path: String): F[Unit] =
    (for {
      destinationFile <- Resource.eval(createFile(path))
      in <- inputStream(src)
      out <- outputStream(destinationFile)
    } yield (in, out)).use { case (in, out) =>
      Sync[F].blocking {
        out.getChannel.transferFrom(in.getChannel, 0, Long.MaxValue)
      }
    }

  private def inputStream(file: File): Resource[F, FileInputStream] =
    Resource.fromAutoCloseable(Sync[F].blocking(new FileInputStream(file)))

  private def outputStream(file: File): Resource[F, FileOutputStream] =
    Resource.fromAutoCloseable(Sync[F].blocking(new FileOutputStream(file)))

  private def createFile(path: String): F[File] =
    Sync[F].blocking(new File(path))

}
