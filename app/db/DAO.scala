package db

import play.api.db.slick.Config.driver.simple._

/**
 * Definition of TableQueries of the models.
 *
 * These will be the responsible of interacting with the db.
 */
private[db] trait DAO {
  lazy val Companies = TableQuery[CompaniesTable]
  lazy val Positions = TableQuery[PositionsTable]
  lazy val Employees = TableQuery[EmployeesTable]
}

