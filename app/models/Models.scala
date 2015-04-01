package models

case class Company(id: Int, name: String)
case class Position(id: Int, name: String, companyId: Int)
case class Employee(id: Int, name: String, positionId: Int)

// Employee general abstraction
case class Details(id: Int, name: String, position: String, company: String)

