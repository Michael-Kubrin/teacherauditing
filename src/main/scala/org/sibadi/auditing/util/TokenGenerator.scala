package org.sibadi.auditing.util

import cats.effect.Sync

import java.security.{KeyPairGenerator, SecureRandom}
import java.util.Base64

class TokenGenerator[F[_]: Sync](keyGenerator: KeyPairGenerator, encoder: Base64.Encoder) {

  def generate: F[String] =
    Sync[F].delay(encoder.encodeToString(keyGenerator.generateKeyPair().getPublic.getEncoded))

}

object TokenGenerator {

  def apply[F[_]: Sync](): TokenGenerator[F] = {
    val generator: KeyPairGenerator = KeyPairGenerator.getInstance("RSA")
    generator.initialize(1024, new SecureRandom())
    new TokenGenerator(generator, Base64.getEncoder)
  }

}
