package org.sibadi.auditing

sealed class AuditingError(val code: String)

object AuditingError {

  val error = new AuditingError("error")

}

object Validation {

  val invalidName = new AuditingError("INVALID_NAME")

}