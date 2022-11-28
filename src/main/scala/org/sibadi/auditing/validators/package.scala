package org.sibadi.auditing

import cats.syntax.option._
import cats.data.{NonEmptyList, ValidatedNel}
import cats.syntax.{ApplySyntax, ValidatedSyntax}
import scala.util.matching.Regex

package object validators extends ValidatedSyntax with ApplySyntax {

  type ValidationErrors    = NonEmptyList[Error]
  type ValidationResult[T] = ValidatedNel[Error, T]

  def skipProperty[T](data: T): ValidationResult[T] = data.validNel

  def notEmpty(source: String)(error: Error): ValidationResult[String] =
    if (source.isEmpty) error.invalidNel else source.validNel

  def checkIsPositive(source: BigDecimal)(error: Error): ValidationResult[BigDecimal] =
    if (source < 0) error.invalidNel else source.validNel

  def checkRegex(regex: Regex)(error: Error)(source: String): ValidationResult[String] =
    if (regex.findAllIn(source).isEmpty) error.invalidNel else source.validNel

  def optionalValidator[T](data: Option[T])(f: T => ValidationResult[T]): ValidationResult[Option[T]] =
    data match {
      case Some(value) => f(value).map(_.some)
      case None        => data.validNel
    }

}
