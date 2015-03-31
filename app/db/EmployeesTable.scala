package db

import common.EmployeeNotFoundException
import models.{Details, Employee}

import scala.slick.driver.MySQLDriver.simple._

class EmployeesTable(tag: Tag) extends Table[Employee](tag, "EMPLOYEES") {

  def id = column[Int]("ID", O.AutoInc, O.PrimaryKey)
  def name = column[String]("NAME")
  def positionId = column[Int]("POSITION")

  def * = (id, name, positionId) <> (Employee.tupled, Employee.unapply)

  def position = foreignKey("POS_FK", positionId, TableQuery[PositionsTable])(_.id)
}

object Employees extends DAO {

  def insert(employee: Employee)(implicit s: Session): Int =
    (Employees returning Employees.map(_.id)) += employee

  def selectAll(implicit s: Session) =
    Employees.list

  private def selectAllFormedQuery =
    for {
      emp  <- Employees
      pos  <- Positions if pos.id  === emp.positionId
      comp <- Companies if comp.id === pos.companyId
    } yield (emp.id, emp.name, pos.name, comp.name)

  def detailsById(id: Int)(implicit s: Session) = {
    selectAllFormedQuery.filter(_._1 === id).firstOption match {
      case Some((id, empName, posName, compName)) =>
        Details(id, empName, posName, compName)
      case _ => throw new EmployeeNotFoundException(s"Employee with id: $id not found")
    }
  }

  def detailsList(implicit s: Session) = selectAllFormedQuery.list.map(toCaseClass _)
  private[this] def toCaseClass(details: (Int, String, String, String)) = Details.tupled(details)
}
