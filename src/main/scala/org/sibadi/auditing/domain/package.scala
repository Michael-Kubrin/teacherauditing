package org.sibadi.auditing

package object domain {

  final case class CreatedTeacher(id: String, password: String)

  final case class CreatedReviewer(id: String, password: String)

  final case class CreatedTopicKPI(kpiId: String, topicId: String)

  final case class FullTopic(
    id: String,
    title: String,
    kpis: List[FullKpi]
  )

  final case class FullKpi(
    id: String,
    title: String
  )

  final case class FullGroup(
    id: String,
    title: String,
    kpis: List[FullKpi]
  )

  object EstimateStatus extends Enumeration() {
    type EstimateStatus = Value
    val Waiting  = Value("waiting")
    val Accepted = Value("accepted")
    val Declined = Value("declined")
    val Reviewed = Value("reviewed")
  }

}
