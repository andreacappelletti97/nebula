package Nebula.Schema

case class MethodSchema(methodName: String,
                        methodReturnType: String,
                        methodArgs: Option[Seq[ArgumentSchema]],
                        caseList : Option[Seq[CaseSchema]])