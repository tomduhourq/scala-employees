package views

import models.{Details, Position, Company}
import play.api.data.Form
import views.html.helper.{form, FieldConstructor}

object ViewHelpers {

  implicit val fields = FieldConstructor(views.html.fieldConstructor.f)

  def tuplify(seq: Seq[{def id: Int; def name: String}]) = {
    seq.map(i => (i.id.toString, i.name))
  }

  private def extractSearchName[T <: {def company: String}](form: Form[T]) =
     form.value match {
       case Some(_) => form.get.company
       case _ => ""
     }

  private def extractSearchPosition(form: Form[Details]) =
    form.value match {
      case Some(_) => form.get.position
      case _ => ""
    }

  def extractCompanyName[T <: {def company: String}](companies: Seq[Company], form: Form[T]) = {
    companies.filter(_.name == extractSearchName(form)).headOption match {
      case Some(c) => (s"${c.id}", c.name)
      case _ => ("0", "-- Select an option --")
    }
  }

  def extractPositionName(positions: Seq[Position], employee: Form[Details]) = {
    positions.filter(_.name == extractSearchPosition(employee)).headOption match {
      case Some(p) => p.name
      case _ => "-- Select an option --"
    }
  }
}