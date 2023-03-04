package org.sibadi.auditing.api

import org.sibadi.auditing.api.endpoints.model.ApiError

object ApiErrors {
  val emptyNameError: ApiError       = ApiError.BadRequest("empty name")
  val sameNameError: ApiError        = ApiError.BadRequest("same name")
  val sqlError: ApiError             = ApiError.InternalError("sql error")
  val noGroupWithGivenId: ApiError   = ApiError.NotFound("no group with given id")
  val noKpiWithGivenId: ApiError     = ApiError.NotFound("no kpi with given id")
  val noTeacherWithGivenId: ApiError = ApiError.NotFound("no teacher with given id")
  val unauthorized: ApiError         = ApiError.Unauthorized("unauthorized")
  val loginExists: ApiError          = ApiError.BadRequest("login exists")
}
