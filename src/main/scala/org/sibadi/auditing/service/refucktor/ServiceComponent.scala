package org.sibadi.auditing.service.refucktor

import cats.effect.Sync
import cats.effect.std.UUIDGen
import org.sibadi.auditing.configs.AdminConfig
import org.sibadi.auditing.db.DbComponent
import org.sibadi.auditing.service.AllService
import org.sibadi.auditing.util.{HashGenerator, TokenGenerator}
import org.typelevel.log4cats.Logger

class ServiceComponent[F[_]](
  val all: AllService[F],
  val group: GroupService[F],
  val register: RegisterService[F],
  val teacher: TeacherService[F],
  val reviewer: ReviewerService[F],
  val kpi: KpiService[F],
  val topic: TopicService[F],
  val auth: AuthenticatorService[F]
)

object ServiceComponent {

  def apply[F[_]: Logger: Sync: UUIDGen](
    db: DbComponent[F],
    adminConfig: AdminConfig
  ): ServiceComponent[F] = {
    val tokenGenerator       = TokenGenerator()
    val hashGenerator        = new HashGenerator()
    val allService           = new AllService[F](db.transactor)
    val groupService         = new GroupService[F](db.transactor)
    val registerService      = new RegisterService[F](db.transactor, tokenGenerator, hashGenerator)
    val teacherService       = new TeacherService[F](db.transactor)
    val reviewerService      = new ReviewerService[F](db.transactor)
    val kpiService           = new KpiService[F](db.transactor)
    val topicService         = new TopicService[F](db.transactor)
    val authenticatorService = new AuthenticatorService[F](db, adminConfig, tokenGenerator, hashGenerator)
    new ServiceComponent[F](
      all = allService,
      group = groupService,
      register = registerService,
      teacher = teacherService,
      reviewer = reviewerService,
      kpi = kpiService,
      topic = topicService,
      auth = authenticatorService
    )
  }

}
