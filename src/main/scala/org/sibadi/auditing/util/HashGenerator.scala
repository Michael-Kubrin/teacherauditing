//package org.sibadi.auditing.util
//
//import cats.effect.Sync
//import cats.syntax.all._
//import cats.effect.std.SecureRandom
//
//import java.util.Base64
//import javax.crypto.SecretKeyFactory
//import javax.crypto.spec.PBEKeySpec
//
//class HashGenerator[F[_]: Sync] {
//
//  private def pbkdf2(password: String, salt: Array[Byte], iterations: Int): F[Array[Byte]] = Sync[F].delay {
//    val keySpec = new PBEKeySpec(password.toCharArray, salt, iterations, 256)
//    val keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
//    keyFactory.generateSecret(keySpec).getEncoded
//  }
//
//  def hashPassword(password: String): F[String] = {
//    for {
//      random <- SecureRandom.javaSecuritySecureRandom
//      salt   <- random.nextBytes(16)
//      hash   <- pbkdf2(password, salt, 10000)
//      salt64 = Base64.getEncoder.encodeToString(salt)
//      hash64 = Base64.getEncoder.encodeToString(hash)
//    } yield s"10000:$hash64:$salt64"
//  }
//
//  def checkPassword(password: String, passwordHash: String): F[Boolean] = {
//    passwordHash.split(":") match {
//      case Array(it, hash64, salt64) if it.forall(_.isDigit) =>
//        val hash = Base64.getDecoder.decode(hash64)
//        val salt = Base64.getDecoder.decode(salt64)
//        pbkdf2(password, salt, it.toInt).map(_.sameElements(hash))
//      case _ => false.pure
//    }
//  }
//
//}
