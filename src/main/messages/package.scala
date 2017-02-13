/**
  *
  */
package object messages {

  case class Create(value: String)
  case class Read(key: Int)
  case class Update(key: Int, value: String)
  case class Delete(key: Int)

  case class Put(key: Int, value: String)
  case class Value(value: String)
  case class Key(key: Int)

  case object Complete
  case object Error

}
