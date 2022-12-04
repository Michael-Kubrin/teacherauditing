package org.sibadi.auditing

package object domain {

  final case class CreatedTeacher(id: String, password: String)

  final case class UpdatedTeacher() // TODO: What should be here, if out is empty?

}
