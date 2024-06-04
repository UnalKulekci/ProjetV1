import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

class Mixed extends PortableApplication(1920, 1080) {

  val w: ArrayBuffer[ArrayBuffer[String]] = Words.createRoundArray(Words.getWords().toArray)
  var arrSorted: ArrayBuffer[String] = ArrayBuffer.empty[String]
  val arrSortedLength: ArrayBuffer[Int] = ArrayBuffer.empty[Int]
  var currentWordIndex = -1
  var roundCounter: Int = 0
  var fallingWords = new ArrayBuffer[WordPosition]()

  override def onInit(): Unit = {
    println("Game initialized")
    setTitle("MIXED")
  }

  def getRandomPosition(maxWidth: Float, maxHeight: Float): (Float, Float) = {
    val x = Random.nextFloat() * maxWidth
    val y = maxHeight
    (x, y)
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()
    g.setBackgroundColor(Color.WHITE)

    if (roundCounter < w.length) {
      arrSorted = w(roundCounter)
      if (fallingWords.length < arrSorted.length) {
        for (i <- 0 until arrSorted.length) {
          val (x, y) = getRandomPosition(getWindowWidth.toFloat, getWindowHeight.toFloat)
          fallingWords.append(WordPosition(arrSorted(i), x, y))
        }
      }
      if (arrSortedLength.length < arrSorted.length) {
        for (i <- arrSorted.indices) {
          arrSortedLength.append(arrSorted(i).length)
        }
      }
    }

    // Update and draw falling words
    for (i: Int <- 0 until fallingWords.length) {
      fallingWords(i).y -= 1

      if (arrSortedLength(i) != fallingWords(i).word.length) {
        g.setColor(new Color(212, 0, 103, 255))
      } else {
        g.setColor(Color.BLACK)
      }

      g.drawString(fallingWords(i).x, fallingWords(i).y, fallingWords(i).word)
      g.setColor(Color.BLACK)
    }

    g.drawSchoolLogo()
  }

  override def onKeyDown(keycode: Int): Unit = {
    val chr = (keycode + 68).toChar
    if (currentWordIndex == -1) {
      for (idx <- arrSorted.indices) {
        if (fallingWords(idx).word.startsWith(chr.toString)) {
          currentWordIndex = idx
        }
      }
    }

    if (currentWordIndex != -1 && fallingWords(currentWordIndex).word.startsWith(chr.toString)) {
      //      arrSorted(currentWordIndex) = arrSorted(currentWordIndex).substring(1)
      fallingWords(currentWordIndex).word = fallingWords(currentWordIndex).word.substring(1)
      if (fallingWords(currentWordIndex).word.isEmpty) {
        arrSorted.remove(currentWordIndex)
        fallingWords.remove(currentWordIndex)
        arrSortedLength.remove(currentWordIndex)
        currentWordIndex = -1
        if (fallingWords.isEmpty) {
          roundCounter += 1
          println("Round is over you can pass the other...")
        }
      }
    }
  }
}

object test extends App {
  val g1: Mixed = new Mixed()
}
