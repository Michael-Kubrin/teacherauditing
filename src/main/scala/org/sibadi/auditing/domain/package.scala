package org.sibadi.auditing

import org.sibadi.auditing.api.endpoints.GeneratedEndpoints.CreateTopicRequestDto

package object domain {

  final case class CreatedTeacher(id: String, password: String)

  final case class CreatedReviewer(id: String, password: String)

  final case class EditedTeacher(id: String, password: String)

//  final case class CreateTopics(topics: List[CreateTopicRequestDto])

  final case class UpdatedTeacher() // TODO: What should be here, if out is empty?

}
