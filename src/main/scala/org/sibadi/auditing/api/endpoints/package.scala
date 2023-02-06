package org.sibadi.auditing.api

import sttp.tapir.{endpoint, header}

package object endpoints {

  val baseEndpoint = endpoint
    .out(header("Access-Control-Allow-Methods", "GET, POST, OPTIONS, PUT, PATCH, DELETE"))
    .out(header("Access-Control-Allow-Headers", "X-Requested-With, content-type"))
    .out(header("Access-Control-Allow-Credentials", "true"))
    .out(header("Access-Control-Allow-Origin", "*"))
}
