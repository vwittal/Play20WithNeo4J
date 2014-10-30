package models

import play.api.libs.json._
import play.api.libs.functional.syntax._
import utils.persistence.graph
import reflect.ClassManifest


/**
 * User: andy
 * Date: 1/02/12
 */

case class User(id: Int, firstName: String) extends Model[User] {

  override def save(implicit m: ClassManifest[User], f:Format[User]):User = {
    val user = super.save
    user.index("models", "keyword", firstName)
    user
  }

  def addToGroup(group:Group) {
    graph.createRelationship(this, User.IS_IN, group)
  }

  def iKnow(user:User) {
    graph.createRelationship(this, User.KNOWS, user)
  }

  def groups : List[Group] = {
    graph.relationTargets[Group](this, User.IS_IN)
  }

  def known : List[User] = {
    graph.relationTargets[User](this, User.KNOWS)
  }

}

object User {

  val IS_IN:String = "IS_IN"
  val KNOWS:String = "KNOWS"

  val userReads: Reads[User] = (
    (JsPath \ "id").read[Int].orElse( Reads.pure(null.asInstanceOf[Int]) ) and
    (JsPath \ "firstName").read[String]
  )(User.apply _)

  val userWrites: Writes[User] = (
    (JsPath \ "id").write[Int] and
    (JsPath \ "firstName").write[String]
  )(unlift(User.unapply))

  implicit val userFormat = Format(userReads, userWrites)

//  implicit object UserFormat extends Format[User] {
//    def reads(json: JsValue): JsResult[User] = User(
//      (json \ "id").asOpt[Int].getOrElse(null.asInstanceOf[Int]),
//      (json \ "firstName").as[String]
//    )
//
//    def writes(u: User): JsValue =
//      JsObject(List(
//        "_class_" -> JsString(User.getClass.getName),
//        "firstName" -> JsString(u.firstName)
//      ) ::: (if (u.id != null.asInstanceOf[Int]) {
//        List("id" -> JsNumber(u.id))
//      } else {
//        Nil
//      }))
//  }
}