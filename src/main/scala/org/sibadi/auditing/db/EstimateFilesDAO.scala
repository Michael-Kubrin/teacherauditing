package org.sibadi.auditing.db

import cats.effect.MonadCancel
import cats.syntax.functor._
import doobie._
import doobie.syntax.all._

import doobie.implicits.javasql._
import doobie.postgres.implicits._

class EstimateFilesDAO[F[_]](transactor: Transactor[F])(implicit M: MonadCancel[F, Throwable]) {

  def insert(estimateFiles: EstimateFiles): F[Unit] =
    sql"""
       INSERT INTO estimateFiles (topicId, kpiId, teacherId, fileId, path)
       VALUES (${estimateFiles.topicId}, ${estimateFiles.kpiId}, ${estimateFiles.teacherId}, ${estimateFiles.fileId}, ${estimateFiles.path})
       """
      .update
      .run
      .void
      .transact(transactor)
      
  def get(topicId: String, kpiId: String, teacherId: String): F[Option[EstimateFiles]] =
    sql"""
       SELECT topicId, kpiId, teacherId, fileId, path
       FROM estimateFiles
       WHERE topicId = $topicId
         AND kpiId = $kpiId
         AND teacherId = $teacherId
       """
      .query[EstimateFiles]
      .option
      .transact(transactor)

  def delete(topicId: String, kpiId: String, teacherId: String): F[Unit] =
    sql"""
       DELETE FROM estimate_files
       WHERE topicId = $topicId
         AND kpiId = $kpiId
         AND teacherId = $teacherId
       """
      .update
      .run
      .void
      .transact(transactor)
  
}
