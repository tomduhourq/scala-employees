package models

// These are abstractions which suit tables
case class Company(id: Int, name: String)
case class Position(id: Int, name: String, companyId: Int)
case class Employee(id: Int, name: String, positionId: Int)

// Employee and position general abstractions
case class Details(id: Int, name: String, position: String, company: String)
case class PosDetails(id: Int, name: String, company: String)