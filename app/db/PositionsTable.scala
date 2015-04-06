package db

import common.PositionNotFoundException
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

  // Group by acts as DISTINCT
  def unique(implicit s: Session) =
    selectAll.groupBy(_.name).map(_._2(0)).toSeq

  def findIdByName(name: String)(implicit s: Session) =
    Positions
      .filter(_.name === name).list.headOption match {
      case Some(Position(id, _, _)) => id
      case _ => throw new PositionNotFoundException(s"Company with name: $name was not found")
    }

}
