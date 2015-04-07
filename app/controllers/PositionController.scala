package controllers

import db.{Companies, Positions}
import models.PosDetails
import play.api.mvc.Controller
import play.api.data._
import play.api.data.Forms._
import play.api.db.slick._

object PositionController extends Controller {

  implicit val tab = "position"

  val positionForm = Form(
    mapping(
      "id" -> ignored(NOT_IMPLEMENTED),
      "name" -> nonEmptyText,
      "company" -> nonEmptyText
    )(PosDetails.apply)(PosDetails.unapply)
  )

  def list = DBAction { implicit rs =>
    Ok(views.html.position.index(Positions.detailsList))
  }

  val Home = Redirect(routes.PositionController.list)

  def createPosition = DBAction { implicit rs =>
    Ok(views.html.position.createPosition(None, positionForm, Companies.selectAll))
  }

  def insert = DBAction { implicit rs =>
    positionForm.bindFromRequest.fold(
      errors => BadRequest(views.html.position.createPosition(None, errors, Companies.selectAll)),
      position => { Positions.insertWithDetails(position) ; Home }
    )
  }

  def update(id: Int) = DBAction { implicit rs =>
    positionForm.bindFromRequest.fold(
      errors => BadRequest(views.html.position.createPosition(Some(id), errors, Companies.selectAll)),
      position => { Positions.update(id, position) ; Home }
    )
  }

  def delete(id: Int) = DBAction { implicit rs =>
    Positions.delete(id)
    Home
  }

  def details(id: Int) = DBAction { implicit rs =>
    Positions.findById(id).map { position =>
      Ok(
        views.html.position.createPosition(
          Some(id),
          positionForm.fill(position),
          Companies.selectAll
        )
      )
    }.getOrElse { Home }
  }
}
