/**
  *
  */
package object messages {

  sealed trait IncomeMessage {val key: Int}

  case class Create(value: String)
  case class Read(key: Int) extends IncomeMessage
  case class Update(key: Int, value: String) extends IncomeMessage
  case class Delete(key: Int) extends IncomeMessage

  case class Value(value: String)
  case class Key(key: Int)

  case object Complete
  case object Error

}
