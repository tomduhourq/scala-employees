package db

import scala.slick.driver.MySQLDriver.simple._

class Positions(tag: Tag) extends Table[(Int, String, Int)](tag, "POSITIONS") {

  def id = column[Int]("ID", O.PrimaryKey)
  def name = column[String]("NAME")
  def companyId = column[Int]("COMPANY")

  def * = (id, name, companyId)

  def supplier = foreignKey("COM_FK", companyId, TableQuery[Companies])(_.id)
}
