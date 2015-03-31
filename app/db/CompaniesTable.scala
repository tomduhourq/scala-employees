package db

import models.Company

import scala.slick.driver.MySQLDriver.simple._


class CompaniesTable(tag: Tag) extends Table[Company](tag, "COMPANIES") {

  def id = column[Int]("ID", O.AutoInc, O.PrimaryKey)
  def name = column[String]("NAME")

  def * = (id, name) <> (Company.tupled, Company.unapply)
}

// Table's companion object
object Companies extends DAO {

  def insert(company: Company)(implicit session: Session): Int =
    (Companies returning Companies.map(_.id)) += company
}