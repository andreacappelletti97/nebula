import com.google.protobuf.DynamicMessage

object SomeExternalImport {
  def ciao(msg: Any) : String = {
    val aaa: Option[DynamicMessage] = None
    if (msg.isInstanceOf[DynamicMessage]) {
      println("THERE WE GO!!!")
      msg.toString
    } else {
      println(msg.toString)
      msg.toString
    }
  }
}
