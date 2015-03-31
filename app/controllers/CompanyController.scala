package controllers

import db.Companies
import models.Company
import play.api.mvc.{Action, Controller}
import play.api.data._
import play.api.data.Forms._

object CompanyController extends Controller  {

  val companyForm = Form(
    mapping(
      "id" -> ignored(NOT_IMPLEMENTED),
      "name" -> nonEmptyText
    )(Company.apply)(Company.unapply)
  )

  def createCompany = Action { implicit request =>
    Ok(views.html.company.createCompany(companyForm))
  }

  def saveCompany = Action { implicit request =>
//    companyForm.bindFromRequest.fold(
//      formWithErrors =>
//        BadRequest(views.html.company.createCompany(formWithErrors))
//      company => {
//        Companies.insert(company)
//        Application.Home.flashing("success" -> "Company %s has been created".format(company.name))
//      }
//    )
    Ok("sarlompa")
  }
}
