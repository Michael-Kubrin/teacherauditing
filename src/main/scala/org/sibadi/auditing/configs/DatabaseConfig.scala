package org.sibadi.auditing.configs

final case class DatabaseConfig(
  host: String,
  port: Int,
  name: String,
  username: String,
  password: String,
  driver: String,
  maxPoolSize: Option[Int] = None,
  schemaName: Option[String] = None
) {

  val jdbcUrl: String = s"jdbc:postgresql://$host:$port/$name"

}
