import NebulaScala2.Compiler.ToolboxGenerator
import org.scalatest.flatspec.AnyFlatSpec

class MessageCodeCompilerTest extends AnyFlatSpec {

  "The toolbox" should "compile and instantiate an Actor" in {
    val toolbox = ToolboxGenerator.generateToolbox()

    val source =
      """
        |case class Authentication(
        |                      email : String,
        |                      password : String
        |                    ) {}
        |
        |scala.reflect.classTag[Authentication].runtimeClass
        |""".stripMargin

    val clazz = (toolbox.compile(toolbox.parse(source))()).asInstanceOf[Class[_]]
    val ctor = clazz.getDeclaredConstructors()(0)
    val instance = ctor.newInstance("andrea@gmail.com", "password")
    println(ctor.getName)
    println(instance)

  }

/*  "The toolbox" should "compile and instantiate an Actor" in {
    case class Authentication(email: String)
    val test = Authentication("ciao")
    println(test.email)
  }
*/
}
