package TreeHugger.Schema

/**
 * Represents a fully qualified type name.
 */
case class TypeName(fullName: String) {

  private def lastDot = fullName.lastIndexOf('.')

  def packageName: String = fullName.take(lastDot)

  def shortName: String = fullName.drop(lastDot + 1)

}
