package org.sibadi.auditing

package object domain {

  final case class CreatedTeacher(id: String, password: String)

  final case class CreatedReviewer(id: String, password: String)

  final case class CreatedTopic(groupId: String, title: String)

  final case class CreatedTopicKPI(kpiId: String, topicId: String)

  final case class CreatedEstimated()


}
