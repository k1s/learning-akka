package services

import com.typesafe.config.ConfigFactory

trait ConfigService {
  private val config = ConfigFactory.load()

  private val httpConfig = config.getConfig("http")
  val httpHost = httpConfig.getString("interface")
  val httpPort = httpConfig.getInt("port")

  private val databaseConfig = config.getConfig("postgres")
  val jdbcUrl = databaseConfig.getString("db.url")
  val dbUser = databaseConfig.getString("db.user")
  val dbPassword = databaseConfig.getString("db.password")

  private val storageConfig = config.getConfig("storage")
  val timeoutSeconds = storageConfig.getInt("timeoutSeconds")
  val shardsNum = storageConfig.getInt("shardsNum")
}

