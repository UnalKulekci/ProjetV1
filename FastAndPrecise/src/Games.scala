import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import org.lwjgl.opengl.Display.setTitle
import com.badlogic.gdx.Input.Keys

import java.util.{Timer, TimerTask}
import scala.collection.mutable.ArrayBuffer
import scala.util.Random

case class WordPosition(word: String, var x: Float, var y: Float)

class Games extends PortableApplication {

  val words: ArrayBuffer[String] = Words.getWords
  var wordIndex = 0
  val fallingWords = scala.collection.mutable.ArrayBuffer[WordPosition]()
  val timer = new Timer()

  var img: BitmapImage = _

  // Rastgele koordinatlar için yardımcı fonksiyon
  def getRandomPosition(maxWidth: Float, maxHeight: Float): (Float, Float) = {
    val x = Random.nextFloat() * maxWidth
    val y = maxHeight
    (x, y)
  }

  // onInit method
  override def onInit(): Unit = {
    println("Game initialized")
    setTitle("Random Words Display")

    img = new BitmapImage("data/rocket.png")

    timer.scheduleAtFixedRate(new TimerTask {
      override def run(): Unit = {
        if (wordIndex < words.length) {
          val (x, y) = getRandomPosition(getWindowWidth.toFloat, getWindowHeight.toFloat)
          fallingWords += WordPosition(words(wordIndex), x, y)
          wordIndex += 1
        } else {
          wordIndex = 0
        }
        // Redraw the screen after updating the wordIndex
        Gdx.app.postRunnable(new Runnable {
          override def run(): Unit = {
            forceRedraw()
          }
        })
      }
    }, 0, 1000) // Her saniyede bir kelime eklemek için
  }

  // onGraphicRender method
  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()
    g.setBackgroundColor(Color.WHITE)
    g.drawPicture(100f, 100f, img)

    val screenWidth = getWindowWidth.toFloat
    val screenHeight = getWindowHeight.toFloat
    g.setColor(Color.BLACK)

    // Update and draw falling words
    for (wordPos <- fallingWords) {
      wordPos.y -= 1 // Her çerçevede y pozisyonunu azalt
      g.drawString(wordPos.x, wordPos.y, wordPos.word)
    }

    // Remove words that have fallen off the screen
    fallingWords --= fallingWords.filter(_.y < 0)
  }

  def checkWords(): Boolean = ???

  override def onKeyDown(keycode: Int): Unit = ???

  // Bu metot ekranda yeniden çizimi zorlamak için kullanılabilir.
  def forceRedraw(): Unit = {
    // Ekranda bir yeniden çizim işlemi başlatır.
    Gdx.graphics.requestRendering()
  }
}