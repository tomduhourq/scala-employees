/**
 * Created by tomas on 31/03/15.
 */
package object common {
  class EmployeeNotFoundException(message: String) extends Exception
  class CompanyNotFoundException(message: String) extends Exception
  class PositionNotFoundException(message: String) extends Exception
}
