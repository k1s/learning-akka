package model

object Messages {

  case class Value(value: String)
  case class Key(key: Int)

  case object Complete
  case object Error

}
