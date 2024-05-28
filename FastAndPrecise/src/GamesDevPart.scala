import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.Color

class GamesDevPart extends PortableApplication() {

  override def onInit(): Unit = {
    println("Game initialized")
    setTitle("Games Test")
  }

  var posx: Float = 100f
  var posy: Float = 100f

  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()
    g.setBackgroundColor(Color.WHITE)
    g.setColor(Color.BLACK)
    //Gdx.input.isKeyPressed()

    g.drawString(posx, posy, words)
  }

  var words : String = "sion"
  override def onKeyDown(keycode: Int): Unit = {
    val chr = (keycode + 68).toChar

    for(i <- 0 until words.length) {
      if(chr == words(i)) {
        words = words.substring(i+1)
        println(words)
        return
      }
    }
  }

}

object gtest extends App {
  val game: GamesDevPart = new GamesDevPart()
}

