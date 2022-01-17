package TreeHugger.Schema

/***
A case object is defined by a name without any argument
A case class is defined by a name and some arguments
Arguments are defined as @ArgumentTemplate, that contains argName and argType
 ***/

case class CaseClassTemplate(caseClassName: String, caseClassArgs: Option[Seq[ArgumentTemplate]])
