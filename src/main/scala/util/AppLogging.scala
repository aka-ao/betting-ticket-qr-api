package util

import org.slf4j.LoggerFactory

trait AppLogging {
  lazy val logging = LoggerFactory.getLogger(this.getClass())
}
