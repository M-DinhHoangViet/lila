package lila.user

import play.api.libs.json.*

object Title:
  import play.api.i18n.Lang
  import lila.i18n.I18nKeys
  val LM  = UserTitle("LM")
  val BOT = UserTitle("BOT")

  // important: names are as stated on FIDE profile pages
  val all = Seq[(UserTitle, String)](
    UserTitle("GM")  -> lila.i18n.I18nKeys.grandmaster.txt(),
    UserTitle("WGM") -> lila.i18n.I18nKeys.womanGrandmaster.txt(),
    UserTitle("IM")  -> lila.i18n.I18nKeys.internationalMaster.txt(),
    UserTitle("WIM") -> lila.i18n.I18nKeys.womanInternationalMaster.txt(),
    UserTitle("FM")  -> lila.i18n.I18nKeys.fideMaster.txt(),
    UserTitle("WFM") -> lila.i18n.I18nKeys.womanFideMaster.txt(),
    UserTitle("NM")  -> lila.i18n.I18nKeys.nationalMaster.txt(),
    UserTitle("CM")  -> lila.i18n.I18nKeys.candidateMaster.txt(),
    UserTitle("WCM") -> lila.i18n.I18nKeys.womanCandidateMaster.txt(),
    UserTitle("WNM") -> lila.i18n.I18nKeys.womanNationalMaster.txt(),
    LM               -> lila.i18n.I18nKeys.lichessMaster.txt(),
    BOT              -> lila.i18n.I18nKeys.chessRobot.txt()
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
