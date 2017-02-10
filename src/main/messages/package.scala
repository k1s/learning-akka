/**
  *
  */
package object messages {

  case class CreateOrUpdate(key: Int, value: String)
  case class Read(key: Int)
  case class Delete(key: Int)

  case class Value(value: String)
  case class Key(key: Int)

  case object Complete
  case object Error

}
