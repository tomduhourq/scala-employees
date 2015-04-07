package controllers


import db.{Positions, Companies, Employees}
import models.Details
import play.api.i18n.Messages
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.db.slick._

/**
 * The controllers provide the definition of the endpoints described in
 * the conf/routes file.
 *
 * We can return Action or DBAction (provided by slick-play plugin)
 * depending on what we are doing.
 * DBAction in particular holds in the request an implicit object Session
 * to pass on to any db query, so there is no need to define one.
 */
object Application extends Controller {

  implicit val tab = "index"

  // Define the form of an Employee by his details
  val detailsForm = Form(
    mapping(
      "id" -> ignored(NOT_IMPLEMENTED),
      "name" -> nonEmptyText,
      "position" -> nonEmptyText,
      "company" -> nonEmptyText
    )(Details.apply)(Details.unapply)
  )

  val Home = Redirect(routes.Application.index)

  def index = DBAction { implicit rs =>
    Ok(views.html.index(Employees.detailsList))
  }

  def createEmployee = DBAction { implicit rs =>
    Ok(
      views.html.employee.details(
        None,
        detailsForm,
        Companies.selectAll,
        Positions.unique)
    )
  }

  def insert = DBAction { implicit rs =>
    detailsForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(
          views.html.employee.details(
            None,
            formWithErrors,
            Companies.selectAll,
            Positions.unique
          )
        )
      },
      details => {
        Employees.insertWithDetails(details)
        Home.flashing("success" -> Messages("success.insert", details.name))
      }
    )
  }

  def update(id: Int) = DBAction { implicit rs =>
    detailsForm.bindFromRequest.fold(
      errors =>
        BadRequest(
          views.html.employee.details(
            Some(id),
            errors,
            Companies.selectAll,
            Positions.unique
          )
        )
          .flashing("errors" -> "Fix the errors!"),
      details => {
        Employees.update(id, details)
        Home.flashing("success" -> Messages("success.update", details.name))
      }
    )
  }

  def delete(id: Int) = DBAction { implicit rs =>
    Employees.delete(id)
    Home.flashing("success" -> "Employee has been deleted")
  }

  def details(id: Int) = DBAction { implicit rs =>
    Employees.findById(id).map { employee =>
      Ok(
        views.html.employee.details(
          Some(employee.id),
          detailsForm.fill(employee),
          Companies.selectAll,
          Positions.unique
        )
      )
    }.getOrElse {
      Home.flashing("error" -> Messages("error.notFound", id))
    }
  }
}