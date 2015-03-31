package db

import play.api.db.slick.Config.driver.simple._

private[db] trait DAO {
  lazy val Companies = TableQuery[CompaniesTable]
  lazy val Positions = TableQuery[PositionsTable]
  lazy val Employees = TableQuery[EmployeesTable]
}

