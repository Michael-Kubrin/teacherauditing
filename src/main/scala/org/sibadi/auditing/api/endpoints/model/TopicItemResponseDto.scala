package org.sibadi.auditing.api.endpoints.model

final case class TopicItemResponseDto(id: String, title: String, kpis: List[KPIItemResponseDto])
