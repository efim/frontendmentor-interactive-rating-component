package industries.sunshine.productpreviewcard

import scala.scalajs.js
import scala.scalajs.js.annotation.*

import org.scalajs.dom

import com.raquo.laminar.api.L.{*, given}

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
        renderVotingComponent(),
      ),
      renderAttribution()
    )
  }

  // full voting component to be usable after adding to dom
  // starts state shared by ui parts and logic for switching between them
  def renderVotingComponent() = {
    val a = 1
    div(
      renderRatingSelectionUI(),
    )
  }

  def renderRatingSelectionUI(): Element = {
    val votes = List(1,2,3,4,5)
    div(
      className := "bg-gradient-to-b rounded-2xl from-blue-dark to-blue-very-dark h-[350px] w-[325px]",
      className := "p-7",
      div(
        className := "flex justify-center items-center w-10 h-10 rounded-full bg-gray-dark",
        img(src := "/images/icon-star.svg", role := "img")
      ),
      p(
        className := "py-4 text-2xl font-bold text-white",
        "How did we do?"),
      p(className := "text-gray-light",
        "Please let us know how we did with your support request. All feedback is appreciated to help us improve our offering!"),
      div(
        className := "flex flex-row justify-between py-6 w-full",
        votes.map(renderRatingSelector(_)),
      ),
      button(
        className := "w-full h-12 text-white rounded-full bg-orange",
        className := "duration-300 hover:bg-white hover:text-orange",
        "SUBMIT")
    )
  }

  def renderRatingSelector(vote: Int) = {
    button(
      className := "flex justify-center items-center w-12 h-12 rounded-full bg-gray-dark",
      className := "text-gray-medium",
      className := "duration-150 hover:text-white hover:bg-orange",
      s"$vote")
  }

  def renderThankYouUI(): Element = {
    div(
      """

  You selected <!-- Add rating here --> out of 5

  Thank you!

  We appreciate you taking the time to give a rating. If you ever need more support,
  don’t hesitate to get in touch!

"""
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
