package NebulaScala3.Schema

case class MethodSchema(methodName: String,
                        methodReturnType: String,
                        methodArgs: Seq[ArgumentSchema],
                        caseList : Seq[CaseSchema],
                        customObject: Seq[CustomObjectSchema])
