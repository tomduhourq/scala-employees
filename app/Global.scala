import _root_.db.{Employees, Positions, Companies}
import models.{Employee, Position, Company}
import play.api.GlobalSettings

import play.api._
import play.api.db.slick._
import play.api.Play.current

object Global extends GlobalSettings {

  override def onStart(app: Application) = {
    Logger.info("Opening MySQL connection")

    val startingCompanies = createCompanies
    val startingPositions = createPositions
    val startingEmployees = createEmployees

    Logger.info("Starting data correctly created")

    DB.withSession { implicit session =>

      // Create
      startingCompanies.foreach(Companies.insert)
      startingPositions.foreach(Positions.insert)
      startingEmployees.foreach(Employees.insert)

      Logger.info("Starting data correctly imported to db")
    }
  }

  def createCompanies =
    List(
      Company(1, "Globant"),
      Company(2, "Apple"),
      Company(3, "Google")
    )

  def createPositions =
    List(
      Position(1, "Scala Dev", 1),
      Position(2, "Big Data Dev", 1),
      Position(3, "Java Dev", 2),
      Position(4, "iOS Dev", 2),
      Position(5, "Data Scientist", 3),
      Position(6, "Scala Dev", 2)
    )

  def createEmployees =
    List(
      Employee(1, "Tomas Duhourq", 1),
      Employee(2, "John Doe", 3),
      Employee(3, "Mary Doe", 2),
      Employee(4, "Dick Wall", 6)
    )
}
