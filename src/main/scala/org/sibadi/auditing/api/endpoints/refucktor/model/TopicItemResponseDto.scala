package org.sibadi.auditing.api.endpoints.refucktor.model

final case class TopicItemResponseDto(id: String, title: String, kpis: List[KPIItemResponseDto])