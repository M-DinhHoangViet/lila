package views.html.tournament

import play.api.i18n.Lang

import lila.app.templating.Environment.{ given, * }
import lila.app.ui.ScalatagsTemplate.{ *, given }
import lila.rating.PerfType
import play.api.i18n.Lang

import controllers.routes

object leaderboard:

  private def freqWinner(w: lila.tournament.Winner, freq: String)(using Lang) =
    li(
      userIdLink(w.userId.some),
      a(title := w.tourName, href := routes.Tournament.show(w.tourId))(freq)
    )

  private val section = st.section(cls := "tournament-leaderboards__item")

  private def freqWinners(fws: lila.tournament.FreqWinners, perfType: PerfType, name: String)(using
      lang: Lang
  ) =
    section(
      h2(cls := "text", dataIcon := perfType.icon)(name),
      ul(
        fws.yearly.map { w =>
          freqWinner(w, trans.yearly())
        },
        fws.monthly.map { w =>
          freqWinner(w, trans.monthly())
        },
        fws.weekly.map { w =>
          freqWinner(w, trans.weekly())
        },
        fws.daily.map { w =>
          freqWinner(w, trans.daily())
        }
      )
    )

  def apply(winners: lila.tournament.AllWinners)(using Lang) =
    views.html.base.layout(
      title = "Tournament leaderboard",
      moreCss = cssTag("tournament.leaderboard"),
      wrapClass = "full-screen-force"
    ) {
      def eliteWinners =
        section(
          h2(cls := "text", dataIcon := licon.CrownElite)(trans.tourname.eliteArena()),
          ul(
            winners.elite.map { w =>
              li(
                userIdLink(w.userId.some),
                a(title := w.tourName, href := routes.Tournament.show(w.tourId))(showDate(w.date))
              )
            }
          )
        )

      def marathonWinners =
        section(
          h2(cls := "text", dataIcon := licon.Globe)(trans.tourname.marathon()),
          ul(
            winners.marathon.map { w =>
              li(
                userIdLink(w.userId.some),
                a(title := w.tourName, href := routes.Tournament.show(w.tourId))(
                  w.tourName.replace(" Marathon", "")
                )
              )
            }
          )
        )
      main(cls := "page-menu")(
        views.html.user.bits.communityMenu("tournament"),
        div(cls := "page-menu__content box box-pad")(
          h1(cls := "box__top")(trans.tournamentWinners()),
          div(cls := "tournament-leaderboards")(
            eliteWinners,
            freqWinners(winners.hyperbullet, PerfType.Bullet, "HyperBullet"),
            freqWinners(winners.bullet, PerfType.Bullet, "Bullet"),
            freqWinners(winners.superblitz, PerfType.Blitz, "SuperBlitz"),
            freqWinners(winners.blitz, PerfType.Blitz, trans.blitz()),
            freqWinners(winners.rapid, PerfType.Rapid, trans.rapid()),
            marathonWinners,
            lila.tournament.WinnersApi.variants.map { v =>
              PerfType.byVariant(v).map { pt =>
                winners.variants.get(pt.key into chess.variant.Variant.LilaKey).map {
                  freqWinners(_, pt, v.name)
                }
              }
            }
          )
        )
      )
    }
