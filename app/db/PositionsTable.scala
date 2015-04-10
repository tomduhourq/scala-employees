package db

import common.PositionNotFoundException
import models.{PosDetails, Position}

import scala.slick.driver.MySQLDriver.simple._

class PositionsTable(tag: Tag) extends Table[Position](tag, "POSITIONS") {

  def id = column[Int]("ID", O.AutoInc, O.PrimaryKey)
  def name = column[String]("NAME")
  def companyId = column[Int]("COMPANY")

  def * = (id, name, companyId) <> (Position.tupled, Position.unapply)

  def supplier =
    foreignKey(
      "COM_FK",
      companyId,
      TableQuery[CompaniesTable]
    )(
        _.id,
        onUpdate=ForeignKeyAction.Cascade,
        onDelete=ForeignKeyAction.Cascade
      )
}

object Positions extends DAO {

  def insert(position: Position)(implicit s: Session): Int =
    (Positions returning Positions.map(_.id)) += position

  def insertWithDetails(position: PosDetails)(implicit s: Session) =
    insert(
      Position(
        position.id,
        position.name,
        db.Companies.findById(position.company.toInt).orNull.id
      )
    )

  def selectAll(implicit s: Session) =
    Positions.list

  // Group by acts as DISTINCT
  def unique(implicit s: Session) =
    selectAll.groupBy(_.name).map(_._2(0)).toSeq

  def update(id: Int, position: PosDetails)(implicit s: Session) =
    Positions
      .filter(_.id === id)
      .update(
        Position(
          id,
          position.name,
          position.company.toInt
        )
      )

  def delete(id: Int)(implicit s: Session) =
    Positions.filter(_.id === id).delete

  def findIdByName(name: String)(implicit s: Session) =
    Positions
      .filter(_.name === name).list.headOption match {
      case Some(Position(id, _, _)) => id
      case _ => throw new PositionNotFoundException(s"Company with name: $name was not found")
    }

  private lazy val selectAllFormedQuery =
    for {
      pos  <- Positions
      comp <- Companies if comp.id === pos.companyId
    } yield (pos.id, pos.name, comp.name)

  def findById(id: Int)(implicit s: Session) =
    selectAllFormedQuery.filter(_._1 === id).list.headOption match {
      case Some(d) => Some(toCaseClass(d))
      case _ => None
    }

  def detailsList(implicit s: Session) = selectAllFormedQuery.list.map(toCaseClass _)
  private[this] def toCaseClass(details: (Int, String, String)) = PosDetails.tupled(details)
}
