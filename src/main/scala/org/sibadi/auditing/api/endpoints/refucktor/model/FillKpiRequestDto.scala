package org.sibadi.auditing.api.endpoints.refucktor.model

import cats.syntax.eq._
import sttp.model.Part
import sttp.tapir.{DecodeResult, MultipartCodec}

final case class FillKpiRequestDto(
  description: Option[Part[String]],
  files: List[FileDataDto]
)

object FillKpiRequestDto {

  implicit val codec: MultipartCodec[FillKpiRequestDto] =
    MultipartCodec.Default.mapDecode(decodeFillKpiRequestDto)(encodeFillKpiRequestDtoPlug)

  private def decodeFillKpiRequestDto(rawParts: Seq[Part[Array[Byte]]]): DecodeResult[FillKpiRequestDto] = {
    val description = rawParts.find(_.name === "description").map(a => a.copy(body = new String(a.body)))
    val files       = rawParts.filterNot(_.name === "description").map(part => FileDataDto(part.name, part.body)).toList
    DecodeResult.Value(FillKpiRequestDto(description, files))
  }

  private def encodeFillKpiRequestDtoPlug(fillKpiRequestDto: FillKpiRequestDto): Seq[Part[Array[Byte]]] = Seq.empty

}
