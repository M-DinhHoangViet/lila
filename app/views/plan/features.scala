package views
package html.plan

import play.api.i18n.Lang

import lila.app.templating.Environment.{ given, * }
import lila.app.ui.ScalatagsTemplate.{ *, given }

import controllers.routes

object features:

  val engineFullName = "Stockfish 16 NNUE"

  def apply()(using PageContext) =
    views.html.base.layout(
      title = title,
      moreCss = cssTag("feature"),
      openGraph = lila.app.ui
        .OpenGraph(
          title = title,
          url = s"$netBaseUrl${routes.Plan.features.url}",
          description = "All of Lichess features are free for all and forever. We do it for the chess!"
        )
        .some
    ) {
      main(cls := "box box-pad features")(
        table(
          header(h1(dataIcon := licon.ScreenDesktop)(trans.website())),
          tbody(
            tr(check)(
              strong(trans.zeroAdsAndNoTracking())
            ),
            tr(unlimited)(
              trans.bulletBlitzClassical()
            ),
            tr(unlimited)(
              trans.correspondenceChess()
            ),
            tr(unlimited)(
              trans.playAndCreateTournaments(
              a(href := routes.Tournament.home)(trans.tournaments())
				)
            ),
            tr(unlimited)(
              trans.playAndCreateSimul(
              a(href := routes.Simul.home)(trans.simultaneousExhibitions())
				)
            ),
            tr(check)(
              trans.standardAndEightVariants(
              a(href := routes.ContentPage.variantHome)(trans.faq.eightVariants())
				)
            ),
            tr(custom(s"${lila.fishnet.FishnetLimiter.maxPerDay} per day"))(
			  trans.deepEngineServerAnalysis(engineFullName)
			  )
            ),
            tr(unlimited)(
              trans.boardEditorAndAnalysisBoardWithSF()
            ),
            tr(unlimited)(
              a(href := "https://lichess.org/blog/WN-gLzAAAKlI89Xn/thousands-of-stockfish-analysers")(
                trans.cloudEngineAnalysis()
              )
            ),
            tr(unlimited)(
              a(href := "https://lichess.org/blog/WFvLpiQAACMA8e9D/learn-from-your-mistakes")(
                trans.learnFromYourMistakes()
              )
            ),
            tr(unlimited)(
              a(href := "https://lichess.org/blog/V0KrLSkAAMo3hsi4/study-chess-the-lichess-way")(
                trans.studiesSharedAndPersistentAnalysis()
              )
            ),
            tr(unlimited)(
              a(href := "https://lichess.org/blog/VmZbaigAABACtXQC/chess-insights")(
                trans.chessInsightsDetailedAnalysisOfYourPlay()
              )
            ),
            tr(check)(
              a(href := routes.Learn.index)(trans.allChessBasicsLessons())
            ),
            tr(unlimited)(
              a(href := routes.Puzzle.home)(trans.tacticalPuzzlesFromUserGames())
            ),
            tr(unlimited)(
              a(href := routes.Puzzle.home)(trans.puzzles()),
              ", ",
              a(href := routes.Puzzle.streak)("Puzzle Streak"),
              ", ",
              a(href := routes.Storm.home)("Puzzle Storm"),
              ", ",
              a(href := routes.Racer.home)("Puzzle Racer")
            ),
            tr(check)(
			  trans.globalOpeningExplorer0(
              a(href := s"${routes.UserAnalysis.index}#explorer")(trans.globalOpeningExplorer())
				)
            ),
            tr(check)(
              trans.personalOpeningExplorer0(
              a(href := s"${routes.UserAnalysis.index}#explorer/me")(trans.personalOpeningExplorer()),
              a(href := s"${routes.UserAnalysis.index}#explorer/M_DinhHoangViet")(trans.otherPlayers())
              )
            ),
            tr(unlimited)(
              a(href := s"${routes.UserAnalysis.parseArg("QN4n1/6r1/3k4/8/b2K4/8/8/8_b_-_-")}#explorer")(
                trans.sevenPieceEndgameTablebase()
              )
            ),
            tr(check)(
              trans.downloadOrUploadAnyGameAsPgn()
            ),
            tr(unlimited)(
			  trans.advancedSearchThroughOverNbLichessGames(
              a(href := routes.Search.index(1))(trans.search.advancedSearch()),
				)
            ),
            tr(unlimited)(
              a(href := routes.Video.index)(trans.videoLibrary())
            ),
            tr(check)(
              trans.forumTeamsMessagingFriendsChallenges()
            ),
            tr(check)(
              trans.availableInNbLanguage(
              a(href := "https://crowdin.com/project/lichess")(trans.nbLanguages())
			  )
            ),
            tr(check)(
              trans.themeBoardsPiecesBackground()
            ),
            tr(check)(
              strong(trans.allFeaturesToComeForever())
            )
          ),
          header(h1(dataIcon := licon.PhoneMobile)(trans.mobile())),
          tbody(
            tr(check)(
              strong(trans.zeroAdsAndNoTracking())
            ),
            tr(unlimited)(
              trans.onlineAndOfflinePlay()
            ),
            tr(unlimited)(
              trans.bulletBlitzClassical()
            ),
            tr(unlimited)(
              trans.correspondenceChess()
            ),
            tr(unlimited)(
              a(href := routes.Tournament.home)(trans.arena.arenaTournaments())
            ),
            tr(check)(
              trans.boardEditorAndAnalysisBoardWithSF()
            ),
            tr(unlimited)(
              a(href := routes.Puzzle.home)(trans.puzzles())
            ),
            tr(check)(
              trans.availableInNbLanguage(
              a(href := "https://crowdin.com/project/lichess")(trans.nbLanguages())
			  )
            ),
            tr(check)(
              trans.themeBoardsPiecesBackground()
            ),
            tr(check)(
              trans.xLandscapeSupport()
            ),
            tr(check)(
              strong(trans.allFeaturesToComeForever())
            )
          ),
          header(h1(trans.supportLichess())),
          tbody(cls := "support")(
            st.tr(
              th(
                trans.contributeToLichessAndX()
              ),
              td("-"),
              td(span(dataIcon := patronIconChar, cls := "is is-green text check")(trans.yes()))
            ),
            st.tr(cls := "price")(
              th,
              td(cls := "green")("$0"),
              td(a(href := routes.Plan.index, cls := "green button")("$5/month"))
            )
          )
        ),
        p(cls := "explanation")(
          strong(trans.yesBothAccountsHaveTheSameFeatures()),
          br,
		  trans.reasonFreeAndPatronAccountSame()
          br,
          strong(trans.allFeaturesAreFree()),
          br,
          trans.ifYouLoveLichess(),
          a(cls := "button", href := routes.Plan.index)(trans.supportUsWithAPatronAccount())
        )
      )
    }

  private def header(name: Frag)(using Lang) =
    thead(
      st.tr(th(name), th(trans.patron.freeAccount()), th(trans.patron.lichessPatron()))
    )

  private val unlimited = span(dataIcon := licon.Checkmark, cls := "is is-green text unlimited")(trans.unlimited())

  private val check = span(dataIcon := licon.Checkmark, cls := "is is-green text check")(trans.yes())

  private def custom(str: String) = span(dataIcon := licon.Checkmark, cls := "is is-green text check")(str)

  private def all(content: Frag) = frag(td(content), td(content))

  private def tr(value: Frag)(text: Frag*) = st.tr(th(text), all(value))

  private val title = "Lichess features"
