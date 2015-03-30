package db

import scala.slick.driver.MySQLDriver.simple._

class Employees(tag: Tag) extends Table[(Int, String, Int)](tag, "EMPLOYEES") {

  def id = column[Int]("ID", O.PrimaryKey)
  def name = column[String]("NAME")
  def positionId = column[Int]("POSITION")

  def * = (id, name, positionId)

  def position = foreignKey("POS_FK", positionId, TableQuery[Positions])(_.id)
}
