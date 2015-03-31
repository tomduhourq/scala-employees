package controllers

import _root_.db.{Employees, Positions, Companies}
import play.api._
import play.api.db.slick.DBAction
import play.api.i18n.Messages
import play.api.mvc._

object Application extends Controller {

  val Home = Redirect(routes.Application.index)

  def index = DBAction { implicit rs =>
    Ok(views.html.index(Employees.detailsList(rs.dbSession)))
  }



}