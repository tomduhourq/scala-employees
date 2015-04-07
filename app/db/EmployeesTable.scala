package db

import common.EmployeeNotFoundException
import models.{ Details, Employee}

import scala.slick.driver.MySQLDriver.simple._

class EmployeesTable(tag: Tag) extends Table[Employee](tag, "EMPLOYEES") {

  // Definition of columns using Lifted Embedding. column[T] are wrappers of Scala's type T.
  def id = column[Int]("ID", O.AutoInc, O.PrimaryKey)
  def name = column[String]("NAME")
  def positionId = column[Int]("POSITION")

  // Every table needs to define this function as it represents the projection
  // of the table when coming from the db.
  def * = (id, name, positionId) <> (Employee.tupled, Employee.unapply)

  // Definition of FK, with configurations of what to do onUpdate and onDelete
  def position =
    foreignKey(
      "POS_FK",
      positionId,
      TableQuery[PositionsTable]
    )(
        _.id,
        onUpdate=ForeignKeyAction.Cascade,
        onDelete=ForeignKeyAction.Cascade
      )
}

/**
 * This is the table's companion object.
 *
 * Companion objects are the singleton class of a proper class and
 * have the responsibility of adding behavior to the concrete class.
 * When working with Slick, it is a common use to put the functions
 * referring queries here.
 */
object Employees extends DAO {

  def insert(employee: Employee)(implicit s: Session): Int =
    (Employees returning Employees.map(_.id)) += employee

  def insertWithDetails(details: Details)(implicit s: Session) =
    insert(Employee(details.id, details.name, db.Positions.findIdByName(details.position)))

  def selectAll(implicit s: Session) =
    Employees.list

  def find(id: Int)(implicit s: Session) =
    Employees.filter(_.id === id).list.headOption

  def findById(id: Int)(implicit s: Session) =
    selectAllFormedQuery.filter(_._1 === id).list.headOption match {
      case Some(d) => Some(toCaseClass(d))
      case _ => None
    }

  // This for expression represents a query with a JOIN statement for every if we add.
  // and the yield represents the mapping to do.
  private lazy val selectAllFormedQuery =
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
            details.position.toInt
          )
        )

  def delete(id: Int)(implicit s: Session) =
    Employees.filter(_.id === id).delete

  def detailsList(implicit s: Session) = selectAllFormedQuery.list.map(toCaseClass _)
  private[this] def toCaseClass(details: (Int, String, String, String)) = Details.tupled(details)
}
