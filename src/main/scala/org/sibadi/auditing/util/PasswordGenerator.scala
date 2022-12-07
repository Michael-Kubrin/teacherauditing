package org.sibadi.auditing.util

object PasswordGenerator {

  def randomPassword(len: Int): String = {
    val randomize = new scala.util.Random(System.nanoTime)
    val stringBuilder = new StringBuilder(len)
    val password = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
    for (i <- 0 until len)
      stringBuilder.append(password(randomize.nextInt(password.length)))
    stringBuilder.toString
  }

}
