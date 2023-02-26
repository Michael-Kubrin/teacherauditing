package org.sibadi.auditing.api.endpoints.refucktor

import org.sibadi.auditing.api.endpoints.refucktor.model._

object TapirExamples {
  val attachmentExample = AttachmentDto("фотки_с_корпоратива.zip", "jsaldh77t3giahkjfdndasmc")
  val kpiDataExample = KpiDataResponseDto(
    Some("Фотки, которые попросил декан, прикрепил"),
    Some(0.89),
    List(attachmentExample),
    EstimateStatusDto.Accepted
  )
  val kpiReviewerDataResponseDtoExample = KpiReviewerDataResponseDto(
    Some("Фотки, которые попросил декан, прикрепил"),
    None,
    List(attachmentExample),
    EstimateStatusDto.Filled
  )
  val loginExample = LoginRequestDto("aboba", "verysecret")
  val bearerExample = BearerResponseDto(
    "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCc7szXqXKa+0hndaYFfSPNM2UKFvnSWj5bu85wNa3T+7Lb1BmddIt2hZNk0lcNb14J362FT5N27PabP4L8eZN7P9MJXicsQa9mmpbzQERKPorKtLmbDvTe0sNaX6eRBQ7yl87n+8CsYuH6M7Nqkw2/LghQWteoiosq5YTLK3dYKQIDAQAB"
  )
}