package db

import models.Employee

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
}
