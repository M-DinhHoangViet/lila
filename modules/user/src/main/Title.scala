package lila.user

import play.api.i18n.Lang

object Title:
  import lila.i18n.I18nKeys
  val LM  = UserTitle("LM")
  val BOT = UserTitle("BOT")

  // important: names are as stated on FIDE profile pages
  val all (using Lang): String = Seq[(UserTitle, String)](
    UserTitle("GM")  -> trans.grandmaster(),
    UserTitle("WGM") -> trans.womanGrandmaster(),
    UserTitle("IM")  -> trans.internationalMaster(),
    UserTitle("WIM") -> trans.womanInternationalMaster(),
    UserTitle("FM")  -> trans.fideMaster(),
    UserTitle("WFM") -> trans.womanFideMaster(),
    UserTitle("NM")  -> trans.nationalMaster(),
    UserTitle("CM")  -> trans.candidateMaster(),
    UserTitle("WCM") -> trans.womanCandidateMaster(),
    UserTitle("WNM") -> trans.womanNationalMaster(),
    LM               -> trans.lichessMaster(),
    BOT              -> trans.chessRobot()
  )

  val names          = all.toMap
  lazy val fromNames = all.map(_.swap).toMap

  val acronyms = all.map(_._1)

  def titleName(title: UserTitle): String = names.getOrElse(title, title.value)

  def get(str: String): Option[UserTitle]      = UserTitle(str.toUpperCase).some filter names.contains
  def get(strs: List[String]): List[UserTitle] = strs flatMap { get(_) }

  object fromUrl:

    // https://ratings.fide.com/card.phtml?event=740411
    private val FideProfileUrlRegex = """(?:https?://)?ratings\.fide\.com/card\.phtml\?event=(\d+)""".r
    // >&nbsp;FIDE title</td><td colspan=3 bgcolor=#efefef>&nbsp;Grandmaster</td>
    private val FideProfileTitleRegex =
      s"""<div class="profile-top-info__block__row__data">(${names.values mkString "|"})</div>""".r.unanchored

    // https://ratings.fide.com/profile/740411
    private val NewFideProfileUrlRegex = """(?:https?://)?ratings\.fide\.com/profile/(\d+)""".r

    import play.api.libs.ws.StandaloneWSClient

    def toFideId(url: String): Option[Int] =
      url.trim match
        case FideProfileUrlRegex(id)    => id.toIntOption
        case NewFideProfileUrlRegex(id) => id.toIntOption
        case _                          => none

    def apply(url: String)(using ws: StandaloneWSClient): Fu[Option[UserTitle]] =
      toFideId(url) so fromFideProfile

    private def fromFideProfile(id: Int)(using ws: StandaloneWSClient): Fu[Option[UserTitle]] =
      ws.url(s"""https://ratings.fide.com/profile/$id""").get().dmap(_.body) dmap {
        case FideProfileTitleRegex(name) => fromNames get name
        case _                           => none
      }
