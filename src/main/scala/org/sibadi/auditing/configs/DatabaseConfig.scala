package org.sibadi.auditing.configs

final case class DatabaseConfig(
  host: String,
  port: Int,
  name: String,
  username: String,
  password: String,
  maxPoolSize: Int
) {

  val jdbcUrl: String = s"jdbc:postgresql://$host:$port/$name"

}
