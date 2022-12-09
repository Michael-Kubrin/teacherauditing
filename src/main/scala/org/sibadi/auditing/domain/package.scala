package org.sibadi.auditing

package object domain {

  final case class CreatedTeacher(id: String, password: String)

  final case class CreatedReviewer(id: String, password: String)

  final case class CreatedTopicKPI(kpiId: String, topicId: String)


  object EstimateStatus extends Enumeration() {
    type EstimateStatus = Value
    val Waiting = Value("waiting")
    val Accepted = Value("accepted")
    val Declined = Value("declined")
    val Reviewed = Value("reviewed")
  }

}