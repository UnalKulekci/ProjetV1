import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color
import scala.collection.mutable.ArrayBuffer


class GamesDevPart extends PortableApplication(1920,1080) {
  val w: ArrayBuffer[ArrayBuffer[String]] = Words.createRoundArray(Words.getWords().toArray)
  var arrSorted: ArrayBuffer[String] = ArrayBuffer.empty[String]
  val arrSortedLength: ArrayBuffer[Int] = ArrayBuffer.empty[Int]
  var currentWordIndex = -1
  var roundCounter: Int = 0

  override def onInit(): Unit = {
    println("Game initialized")
    setTitle("Games Test")
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()
    g.setBackgroundColor(Color.WHITE)
    g.setColor(Color.BLACK)
    var posx: Float = 100f
    var posy: Float = 100f

    if (roundCounter < w.length) {
      arrSorted = w(roundCounter)
      if (arrSortedLength.length < arrSorted.length) {
        for (i <- arrSorted.indices) {
          arrSortedLength.append(arrSorted(i).length)
        }
      }
    }

    for (i <- (arrSorted.length - 1) to 0 by -1) {
      if (arrSortedLength(i) != arrSorted(i).length) {
        g.setColor(new Color(212, 0, 103, 255))
      } else {
        g.setColor(Color.BLACK)
      }
      g.drawString(posx, posy, arrSorted(i))
      g.setColor(Color.BLACK)
      posx += 15
      posy += 15
    }

    g.drawSchoolLogo()
  }

  override def onKeyDown(keycode: Int): Unit = {
    val chr = (keycode + 68).toChar
    if (currentWordIndex == -1) {
      for (idx <- arrSorted.indices) {
        if (arrSorted(idx).startsWith(chr.toString)) {
          currentWordIndex = idx
        }
      }
    }


    if (currentWordIndex != -1 && arrSorted(currentWordIndex).startsWith(chr.toString)) {
      arrSorted(currentWordIndex) = arrSorted(currentWordIndex).substring(1)
      if (arrSorted(currentWordIndex).isEmpty) {
        arrSorted.remove(currentWordIndex)
        arrSortedLength.remove(currentWordIndex)
        currentWordIndex = -1
        if (arrSorted.isEmpty) {
          roundCounter += 1
          println("Round is over you can pass the other...")
        }
      }
    }

  }
} // End of the class

object gtest extends App {
  val game: GamesDevPart = new GamesDevPart()
}


