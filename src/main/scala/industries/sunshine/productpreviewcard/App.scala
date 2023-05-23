package industries.sunshine.productpreviewcard

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom

import com.raquo.laminar.api.L.{*, given}
import com.raquo.airstream.state.Var

@main
def App(): Unit =
  renderOnDomContentLoaded(
    dom.document.getElementById("app"),
    Main.appElement()
  )

object Main {
  def appElement(): Element = {
    div(
      className := "flex flex-col w-screen h-screen bg-[#121417]",
      div(
        className := "flex flex-grow justify-center items-center",
        renderVotingComponent()
      ),
      renderAttribution()
    )
  }

  /* full voting component to be usable after adding to dom
   * starts state shared by ui parts and logic for switching between them
   */
  def renderVotingComponent() = {
    val selectedVote = Var[Option[Int]](None)
    var isSubmitted = Var[Boolean](false)

    // for simpler components it's good enough to pass Var into subcomponent
    // for more complex might be good to pass in limited Signal \ update function
    // that way it could be easier to reason about possible interactions
    def markSubmitted(unit: Unit): Unit = {
      // in application with backend we could set another boolean variable in case submission was successful
      selectedVote.signal.now() match {
        case None    => () // do nothing, no value selected
        case Some(_) => isSubmitted.writer.onNext(true)
      }
    }
    def isSelectedVoteSignal(forVoteButton: Int) =
      selectedVote.signal.map {
        case Some(`forVoteButton`) => true
        case _                     => false
      }
    def setVote(vote: Int) = {
      selectedVote.writer.onNext(Some(vote))
      println(s"voting $vote. current vote is ${selectedVote.signal.now()}")
    }

    div(
      child <-- isSubmitted.signal.map(
        if (_)
          renderThankYouUI(selectedVote.signal.now())
        else
          renderRatingSelectionUI(setVote, isSelectedVoteSignal, markSubmitted)
      )
    )
  }

  def renderRatingSelectionUI(
      setVote: Int => Unit,
      isSelectedVoteSignal: Int => Signal[Boolean],
      markSubmitted: Unit => Unit
  ): Element = {
    val votes = List(1, 2, 3, 4, 5)

    div(
      className := "bg-gradient-to-b rounded-2xl from-blue-dark to-blue-very-dark h-[350px] w-[325px]",
      className := "p-7",
      div(
        className := "flex justify-center items-center w-10 h-10 rounded-full bg-gray-dark",
        img(src := "/images/icon-star.svg", role := "img")
      ),
      p(className := "py-4 text-2xl font-bold text-white", "How did we do?"),
      p(
        className := "text-gray-light",
        "Please let us know how we did with your support request. All feedback is appreciated to help us improve our offering!"
      ),
      div(
        className := "flex flex-row justify-between py-6 w-full",
        votes.map(vote =>
          renderRatingSelector(vote, setVote, isSelectedVoteSignal(vote))
        )
      ),
      button(
        className := "w-full h-12 text-white rounded-full bg-orange",
        className := "duration-300 hover:bg-white hover:text-orange",
        onClick --> Observer(_ => {
          // use Fetch to send results to server here
          markSubmitted(())
        }),
        "SUBMIT"
      )
    )
  }

  def renderRatingSelector(
      vote: Int,
      setVote: Int => Unit,
      isSelectedSignal: Signal[Boolean]
  ) = {
    button(
      className := "flex justify-center items-center w-12 h-12 rounded-full",
      className <-- isSelectedSignal.map {
        case true  => "bg-gray-medium text-white"
        case false => "bg-gray-dark text-gray-medium"
      },
      className := "duration-150 hover:text-white hover:bg-orange",
      onClick --> Observer.apply(_ => setVote(vote)),
      s"$vote"
    )
  }

  def renderThankYouUI(finalRatingOpt: Option[Int]): Element = {
    val finalRating = finalRatingOpt.getOrElse({
      println("Error, no was rating selected.")
      1
    })
    div(
      className := "flex flex-col items-center py-10 px-8",
      className := "text-white bg-gradient-to-b rounded-2xl from-blue-dark to-blue-very-dark h-[350px] w-[325px]",
      img(src := "/images/illustration-thank-you.svg", role := "img"),
      div(
        className := "my-5 w-auto rounded-full bg-[#282F39] text-[#A76A34]",
        p(
          className := "py-2 px-3 text-[#D58755]",
          s"You selected $finalRating out of 5 "
        )
      ),
      p(
        className := "pb-3 text-2xl font-bold",
        "Thank you!"
      ),
      p(
        className := "leading-relaxed text-center text-gray-400 text-[.9rem]",
        "We appreciate you taking the time to give a rating. If you ever need more support, don’t hesitate to get in touch! "
      )
    )
  }

  def renderAttribution(): Element = {
    div(
      className := "h-4 attribution",
      "Challenge by ",
      a(
        href := "https://www.frontendmentor.io?ref=challenge",
        target := "_blank",
        "Frontend Mentor"
      ),
      " Coded by ",
      a(href := "#", "Your Name Here")
    )
  }

}
