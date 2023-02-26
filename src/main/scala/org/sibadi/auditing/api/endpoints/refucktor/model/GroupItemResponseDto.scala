package org.sibadi.auditing.api.endpoints.refucktor.model

final case class GroupItemResponseDto(
  id: String,
  name: String,
  kpis: List[KpiInGroupItemDto],
  teachers: List[TeacherInGroupItemDto]
)
