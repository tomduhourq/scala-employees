package db

import models.Position

import scala.slick.driver.MySQLDriver.simple._

class PositionsTable(tag: Tag) extends Table[Position](tag, "POSITIONS") {

  def id = column[Int]("ID", O.AutoInc, O.PrimaryKey)
  def name = column[String]("NAME")
  def companyId = column[Int]("COMPANY")

  def * = (id, name, companyId) <> (Position.tupled, Position.unapply)

  def supplier = foreignKey("COM_FK", companyId, TableQuery[CompaniesTable])(_.id)
}

object Positions extends DAO {

  def insert(position: Position)(implicit s: Session): Int =
    (Positions returning Positions.map(_.id)) += position

  def selectAll(implicit s: Session) =
    Positions.list
}
