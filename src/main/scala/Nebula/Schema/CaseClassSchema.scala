package Nebula.Schema

/***
A case object is defined by a name without any argument
A case class is defined by a name and some arguments
Arguments are defined as @ArgumentSchema, that contains argName and argType
 ***/
case class CaseClassSchema(caseClassName: String, caseClassArgs: Option[Seq[ArgumentSchema]])
