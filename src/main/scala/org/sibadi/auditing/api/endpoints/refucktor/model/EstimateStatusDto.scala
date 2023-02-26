package org.sibadi.auditing.api.endpoints.refucktor.model

import enumeratum._
import sttp.tapir.Codec.PlainCodec

sealed trait EstimateStatusDto extends EnumEntry
object EstimateStatusDto extends Enum[EstimateStatusDto] {
  val values = findValues
  implicit val codec: PlainCodec[EstimateStatusDto] = sttp.tapir.codec.enumeratum.plainCodecEnumEntryDecodeCaseInsensitive
  case object WaitingForFill extends EstimateStatusDto
  case object Filled         extends EstimateStatusDto
  case object Declined       extends EstimateStatusDto
  case object Corrected      extends EstimateStatusDto
  case object Accepted       extends EstimateStatusDto
}