package db

import common.CompanyNotFoundException
import models.Company

import scala.slick.driver.MySQLDriver.simple._


class CompaniesTable(tag: Tag) extends Table[Company](tag, "COMPANIES") {

  def id = column[Int]("ID", O.AutoInc, O.PrimaryKey)
  def name = column[String]("NAME", O.NotNull)

  def * = (id, name) <> (Company.tupled, Company.unapply)
}

object Companies extends DAO {

  def insert(company: Company)(implicit s: Session): Int =
    (Companies returning Companies.map(_.id)) += company

  def selectAll(implicit s: Session) =
    Companies.list

  def findById(id: Int)(implicit s: Session) =
    Companies.filter(_.id === id).list.headOption match {
      case Some(c) => Some(c)
      case _ => None
    }

  def findIdByName(name: String)(implicit s: Session) =
    Companies.filter(_.name === name).list.headOption match {
      case Some(Company(id, _)) => id
      case _ => throw new CompanyNotFoundException(s"Company with name: $name was not found")
    }

  def update(id: Int, company: Company)(implicit s: Session) =
    Companies.filter(_.id === id).update(company)

  def delete(id: Int)(implicit s: Session) = {
    Companies.filter(_.id === id).delete
  }
}