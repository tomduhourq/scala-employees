package views

import views.html.helper.FieldConstructor

object ViewHelpers {

  implicit val fields = FieldConstructor(views.html.fieldConstructor.f)

  def tuplify(seq: Seq[{def id: Int; def name: String}]) = {
    seq.map(i => (i.id.toString, i.name))
  }
}