package controllers

import db.Companies
import models.Company
import play.api.mvc.{Action, Controller}
import play.api.data._
import play.api.data.Forms._
import play.api.db.slick._

object CompanyController extends Controller  {

  implicit val tab = "company"

  val companyForm = Form(
    mapping(
      "id" -> ignored(NOT_IMPLEMENTED),
      "name" -> nonEmptyText
    )(Company.apply)(Company.unapply)
  )

  def list = DBAction { implicit rs =>
    Ok(views.html.company.index(Companies.selectAll))
  }

  val Home = Redirect(routes.CompanyController.list)

  def createCompany = Action { implicit request =>
    Ok(views.html.company.createCompany(None, companyForm))
  }

  def insert = DBAction { implicit rs =>
    companyForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.company.createCompany(None, formWithErrors)),
      company => { Companies.insert(company) ; Home }
    )
  }

  def update(id: Int) = DBAction { implicit rs =>
    companyForm.bindFromRequest.fold(
      errors => BadRequest(views.html.company.createCompany(Some(id), errors)),
      company => { Companies.update(id, company) ; Home }
    )
  }

  def details(id: Int) = DBAction { implicit rs =>
    Companies.findById(id).map { company =>
      Ok(views.html.company.createCompany(Some(id), companyForm.fill(company)))
    }.getOrElse {
      Home
    }
  }

  def delete(id: Int) = DBAction { implicit rs =>
    Companies.delete(id)
    Home
  }
}
