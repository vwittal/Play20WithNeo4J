import dispatch.classic.HandlerVerbs
import play.api.libs.json._
import play.api.libs.json.Json._

class PlayJsonHandlers(subject: HandlerVerbs) {

  /**Process response as JsValue in block */
  def >![T](block: (JsValue) => T) = subject >- {
    (str) =>
      block(parse(str))
  }
}