package org.sibadi.auditing.api.endpoints

import org.sibadi.auditing.api.endpoints.model._

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
  val exampleCreateGroupRequestDto = CreateGroupRequestDto("some group name")

  val exampleGroupResponseItemDtoList = List(
    GroupItemResponseDto(
      id = "fjsa7u8ftw24tg4yfigi",
      name = "Директор",
      kpis = List(
        KpiInGroupItemDto(
          id = "fjasklf89y3ui4",
          title = "Средний балл ЕГЭ поступивших на обучение по очной форме по специальностям и направлениям, закрепленным за институтом"
        ),
        KpiInGroupItemDto(
          id = "fjfdsafdsfsui4",
          title = "Процент приема в пределах контрольных цифр приема по специальностям и направлениям, закрепленным за институтом"
        )
      ),
      teachers = List(
        TeacherInGroupItemDto(
          id = "fajsklfjklasdd",
          firstName = "Имя",
          lastName = "Фамильев",
          middleName = Some("Отчествович")
        ),
        TeacherInGroupItemDto(
          id = "fajsklfjklasdd",
          firstName = "Имя",
          lastName = "Фамильева",
          middleName = Some("Отчествовна")
        )
      )
    ),
    GroupItemResponseDto(
      id = "fasd89yq3nqfwhejkgas",
      name = "Зав. кафедрой",
      kpis = List(
        KpiInGroupItemDto(
          id = "fjasklf89y3ui4",
          title = "Средний балл ЕГЭ поступивших на обучение по образовательной программе, закрепленной за кафедрой"
        ),
        KpiInGroupItemDto(
          id = "fjfdsafdsfsui4",
          title = "Процент приема в пределах контрольных цифр приема по образовательной программе, закрепленной за кафедрой"
        )
      ),
      teachers = List(
        TeacherInGroupItemDto(
          id = "fajsklfjklasdd",
          firstName = "Имя",
          lastName = "Фамильев",
          middleName = Some("Отчествович")
        ),
        TeacherInGroupItemDto(
          id = "fajsklfjklasdd",
          firstName = "Имя",
          lastName = "Фамильева",
          middleName = Some("Отчествовна")
        )
      )
    ),
    GroupItemResponseDto(
      id = "fbasdnmbfewtyruitwei",
      name = "НПР",
      kpis = List(
        KpiInGroupItemDto(
          id = "fjasklf89y3ui4",
          title =
            "Участие в организации и проведении руководителем/испол нителем профориентационного мероприятия (при количестве участников не менее 25 человек), включенного в план работы университета/институ та (Подтверждающий документ с указанием количества участников)"
        ),
        KpiInGroupItemDto(
          id = "fjfdsafdsfsui4",
          title = "Количество привлеченных и зачисленных абитуриентов с баллом 200 и выше"
        )
      ),
      teachers = List(
        TeacherInGroupItemDto(
          id = "fajsklfjklasdd",
          firstName = "Имя",
          lastName = "Фамильев",
          middleName = Some("Отчествович")
        ),
        TeacherInGroupItemDto(
          id = "fajsklfjklasdd",
          firstName = "Имя",
          lastName = "Фамильева",
          middleName = Some("Отчествовна")
        )
      )
    )
  )
}
