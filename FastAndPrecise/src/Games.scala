import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.{Gdx, InputAdapter}
import com.badlogic.gdx.graphics.Color
import java.util.{Timer, TimerTask}
import scala.collection.mutable.ArrayBuffer
import scala.util.Random


class Games extends PortableApplication  {

  val words: ArrayBuffer[String] = Words.getWords()
  var wordIndex = 0

  val fallingWords = new ArrayBuffer[WordPosition]()
  val timer = new Timer()

  var img: BitmapImage = _


   // Rastgele koordinatlar için yardımcı fonksiyon
  def getRandomPosition(maxWidth: Float, maxHeight: Float): (Float, Float) = {
    val x = Random.nextFloat() * maxWidth
    val y = maxHeight
    (x, y)
  }


  override def onKeyDown(keycode: Int): Unit = {
    val chr = (keycode + 68).toChar

    for (i <- 0 until words.length) {
      if (chr == words(i)) {
        fallingWords(0).word = fallingWords(0).word.substring(i + 1)
        println(words)
        return
      }
    }
  }


  def levelOne(): ArrayBuffer[String] = {
    words.take(1)
  }

  // onInit method
  override def onInit(): Unit = {
    println("Game initialized")
    setTitle("Random Words Display")

    img = new BitmapImage("data/rocket.png")


    timer.scheduleAtFixedRate(new TimerTask {
      override def run(): Unit = {

        var wordsLevelOne = levelOne()

        if (wordIndex < wordsLevelOne.length) {
          val (x, y) = getRandomPosition(getWindowWidth.toFloat, getWindowHeight.toFloat)
          fallingWords += WordPosition(words(wordIndex), x, y)
          wordIndex += 1
        } else {
          timer.cancel()
        }

        Gdx.app.postRunnable(new Runnable {
          override def run(): Unit = {
            forceRedraw()
          }
        })
      }
    }, 0, 1000)
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


  def forceRedraw(): Unit = {
    Gdx.graphics.requestRendering()
  }


} // End of the Games Class