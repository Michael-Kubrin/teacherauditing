package org.sibadi.auditing.api.endpoints

import sttp.tapir._

object YamlDocAPI {

  def yamlDocApi = List(yamlDocAPIEndpoint)

  def yamlDocAPIEndpoint: Endpoint[Unit, Unit, Unit, String, Any] =
    endpoint.get
      .tag("Docs")
      .in("api" / "docs.yaml")
      .out(stringBody)
      .out(header("Content-Type", "application/yaml"))

}
