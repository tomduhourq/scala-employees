package db

import common.EmployeeNotFoundException
import models.{ Details, Employee}

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

  def insertWithDetails(details: Details)(implicit s: Session) =
    insert(Employee(details.id, details.name, db.Positions.findIdByName(details.position)))

  def selectAll(implicit s: Session) =
    Employees.list

  def findById(id: Int)(implicit s: Session) =
    selectAllFormedQuery.filter(_._1 === id).list.headOption match {
      case Some(d) => Some(toCaseClass(d))
      case _ => None
    }

  private def selectAllFormedQuery =
    for {
      emp  <- Employees
      pos  <- Positions if pos.id  === emp.positionId
      comp <- Companies if comp.id === pos.companyId
    } yield (emp.id, emp.name, pos.name, comp.name)

  def detailsById(id: Int)(implicit s: Session) = {
    selectAllFormedQuery.filter(_._1 === id).firstOption match {
      case Some(d) => toCaseClass(d)
      case _ => throw new EmployeeNotFoundException(s"Employee with id: $id not found")
    }
  }

  def update(id: Int, details: Details)(implicit s: Session) =
      Employees
        .filter(_.id === id)
        .update(
          Employee(
            id,
            details.name,
            db.Positions.findIdByName(details.position)
          )
        )

  def detailsList(implicit s: Session) = selectAllFormedQuery.list.map(toCaseClass _)
  private[this] def toCaseClass(details: (Int, String, String, String)) = Details.tupled(details)
}
