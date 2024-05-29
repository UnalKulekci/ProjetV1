import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color
import scala.collection.mutable.ArrayBuffer


class GamesDevPart extends PortableApplication() {

  override def onInit(): Unit = {
    println("Game initialized")
    setTitle("Games Test")
  }

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()
    g.setBackgroundColor(Color.WHITE)
    g.setColor(Color.BLACK)
    //Gdx.input.isKeyPressed()
    var posx: Float = 100f
    var posy: Float = 100f

    for( i <- arrSorted.indices){
      g.drawString(posx, posy, arrSorted(i))
      posx += 50
      posy += 100
    }
  }

  val w : ArrayBuffer[ArrayBuffer[String]] = Words.createRoundArray(Words.getWords().toArray)
  var arrSorted: ArrayBuffer[String] = w(0).sortWith(_ < _)

  var currentWordIndex = -1
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
        currentWordIndex = -1
      }
    }
  }

} // End of the class

object gtest extends App {
  val game: GamesDevPart = new GamesDevPart()
}

