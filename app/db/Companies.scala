package db

import scala.slick.lifted.ProvenShape
import scala.slick.driver.MySQLDriver.simple._


class Companies(tag: Tag) extends Table[(Int, String)](tag, "COMPANIES") {

  def id = column[Int]("ID", O.PrimaryKey)
  def name = column[String]("NAME")

  def * : ProvenShape[(Int,String)] = (id, name)
}
