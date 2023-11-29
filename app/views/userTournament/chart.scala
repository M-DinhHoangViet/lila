package views.html
package userTournament

import lila.app.templating.Environment.{ given, * }
import lila.app.ui.ScalatagsTemplate.*
import lila.user.User

object chart:

  def apply(u: User, data: lila.tournament.LeaderboardApi.ChartData)(using PageContext) =
    bits.layout(
      u,
      title = s"${u.username} tournaments",
      path = "chart"
    ) {
      div(cls := "tournament-stats")(
        boxTop(h1(userLink(u, withOnline = true), " tournament stats")),
        p(cls := "box__pad")(trans.rankAvgHelp()),
        p(cls := "box__pad")(
          trans.allAvgOnThisPageAreMedians(
          a(href := "https://www.dictionary.com/e/average-vs-mean-vs-median-vs-mode")(trans.medians())
          )
        ),
        table(cls := "slist slist-pad perf-results")(
          thead(
            tr(
              th,
              th(trans.tournaments()),
              th(trans.arena.pointsAvg()),
              th(trans.arena.pointsSum()),
              th(trans.arena.rankAvg())
            )
          ),
          tbody(
            data.perfResults.map { case (pt, res) =>
              tr(
                th(iconTag(pt.icon, pt.trans)),
                td(res.nb.localize),
                td(res.points.median.map(_.toInt)),
                td(res.points.sum.localize),
                td(res.rankPercentMedian, "%")
              )
            },
            tr(
              th(trans.total()),
              td(data.allPerfResults.nb.localize),
              td(data.allPerfResults.points.median.map(_.toInt)),
              td(data.allPerfResults.points.sum.localize),
              td(data.allPerfResults.rankPercentMedian, "%")
            )
          )
        )
      )
    }
