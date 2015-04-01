package controllers

import db.Employees
import models.Details
import play.api.data.Form
import play.api.data.Forms._
import play.api.db.slick.DBAction
import play.api.i18n.Messages
import play.api.mvc.Controller
import play.api.Play.current



object Application extends Controller {

  // Define the form of an Employee by his details
  val detailsForm = Form(
    mapping(
      "id" -> number,
      "name" -> nonEmptyText,
      "position" -> nonEmptyText,
      "company" -> nonEmptyText
    )(Details.apply)(Details.unapply)
  )

  val Home = Redirect(routes.Application.index)

  def index = DBAction { implicit rs =>
    Ok(views.html.index(Employees.detailsList(rs.dbSession)))
  }

  def insert = DBAction { implicit rs =>
    detailsForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(views.html.employee.details(None, formWithErrors))
      },
      details => {
        Employees.insertWithDetails(details)(rs.dbSession)
        Home.flashing("success" -> Messages("success.insert", details.name))
      }
    )
  }

  def update(id: Int) = DBAction { implicit rs =>
    detailsForm.bindFromRequest.fold(
      errors =>
        Ok(views.html.employee.details(Some(id), errors)).flashing("errors" -> "Fix the errors!"),
      details => {
        Employees.update(id, details)(rs.dbSession)
        Home.flashing("success" -> Messages("success.update", details.name))
      }
    )
  }

  def details(id: Int) = DBAction { implicit rs =>
    Employees.findById(id)(rs.dbSession).map { employee =>
      Ok(views.html.employee.details(Some(employee.id), detailsForm.fill(employee)))
    }.getOrElse {
      Home.flashing("error" -> Messages("error.notFound", id))
    }
  }

}