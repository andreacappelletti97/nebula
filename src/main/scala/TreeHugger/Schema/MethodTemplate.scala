package TreeHugger.Schema


case class MethodTemplate(methodName: String,
                          methodReturnType: String,
                          methodArgs: Option[Seq[ArgumentTemplate]],
                          caseList : Option[Seq[CaseTemplate]])
