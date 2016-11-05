package model

case class Incident(val date: String,
                    val error: String,
                    val account: String) extends Serializable {

  override def toString = s"$date|$error|$account"

}