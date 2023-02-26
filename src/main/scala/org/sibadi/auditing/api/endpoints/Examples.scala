package org.sibadi.auditing.api.endpoints

import org.sibadi.auditing.api.endpoints.refucktor.model._

object Examples {

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
          title = "Участие в организации и проведении руководителем/испол нителем профориентационного мероприятия (при количестве участников не менее 25 человек), включенного в план работы университета/институ та (Подтверждающий документ с указанием количества участников)"
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
