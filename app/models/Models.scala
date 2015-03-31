package models

case class Company(id: Int, name: String)
case class Position(id: Int, name: String, companyId: Int)
case class Employee(id: Int, name: String, positionId: Int)
