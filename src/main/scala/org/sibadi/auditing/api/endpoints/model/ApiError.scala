package org.sibadi.auditing.api.endpoints.model

sealed abstract class ApiError(val code: String) {
  def cast: ApiError = this
}

object ApiError {

  final case class BadRequest(override val code: String) extends ApiError(code)

  object BadRequest {
    implicit val codec: io.circe.Codec[BadRequest] = io.circe.generic.semiauto.deriveCodec[BadRequest]
  }

  final case class Unauthorized(override val code: String) extends ApiError(code)

  object Unauthorized {
    implicit val codec: io.circe.Codec[Unauthorized] = io.circe.generic.semiauto.deriveCodec[Unauthorized]
  }

  final case class Forbidden(override val code: String) extends ApiError(code)

  object Forbidden {
    implicit val codec: io.circe.Codec[Forbidden] = io.circe.generic.semiauto.deriveCodec[Forbidden]
  }

  final case class NotFound(override val code: String) extends ApiError(code)

  object NotFound {
    implicit val codec: io.circe.Codec[NotFound] = io.circe.generic.semiauto.deriveCodec[NotFound]
  }

  final case class Conflict(override val code: String) extends ApiError(code)

  object Conflict {
    implicit val codec: io.circe.Codec[Conflict] = io.circe.generic.semiauto.deriveCodec[Conflict]
  }

  final case class InternalError(override val code: String) extends ApiError(code)

  object InternalError {
    implicit val codec: io.circe.Codec[InternalError] = io.circe.generic.semiauto.deriveCodec[InternalError]
  }

  final case class Stub(override val code: String) extends ApiError(code)

  object Stub {
    implicit val codec: io.circe.Codec[Stub] = io.circe.generic.semiauto.deriveCodec[Stub]
  }

}
