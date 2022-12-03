package org.sibadi.auditing.validators

import org.sibadi.auditing.errors.ServiceErrors
import org.sibadi.auditing.service.Authenticator.UserType

object UserTypeValidator {

  def validateUserType(userType: UserType): ValidationResult[UserType] =
//    notEmpty(userType)(ServiceErrors.Validation.invalidAuth)
    ???
}
