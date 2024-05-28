import Main.g
import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.{Gdx, InputAdapter}
import com.badlogic.gdx.graphics.Color

import java.util.{Timer, TimerTask}
import scala.collection.mutable.ArrayBuffer
import scala.util.Random


class Games extends PortableApplication  {

  val words: ArrayBuffer[String] = new Words().getWords()
  var wordIndex = 0

  val fallingWords = new ArrayBuffer[WordPosition]()
  val timer = new Timer()

  var img: BitmapImage = _

  var key = new Key()

   // Rastgele koordinatlar için yardımcı fonksiyon
  def getRandomPosition(maxWidth: Float, maxHeight: Float): (Float, Float) = {
    val x = Random.nextFloat() * maxWidth
    val y = maxHeight
    (x, y)
  }


  def checkWords(g: GdxGraphics, key: Char): Unit = {

    if (fallingWords.nonEmpty) {
      val wordInDisplay = fallingWords(0).word

      if (wordInDisplay.nonEmpty && wordInDisplay(0) == key) {
        g.drawCircle(50f, 50f, 60f)
      }
    }
  }

  def levelOne(): ArrayBuffer[String] = {
    words.take(2)
  }

  def levelTwo(): ArrayBuffer[String] = {
    words.take(11)
  }

  def levelThree(): ArrayBuffer[String] =
    words.take(21)


  def checkWords(g:GdxGraphics): Unit = {
   var wordInDisplay = fallingWords(0).word
    if(wordInDisplay(0) == key) {
      g.drawCircle(50f,50f,60f)
    }
  }

  // onInit method
  override def onInit(): Unit = {
    println("Game initialized")
    setTitle("Random Words Display")

    img = new BitmapImage("data/rocket.png")

    // Her saniye yeni bir kelime eklemek için timer ayarı
    timer.scheduleAtFixedRate(new TimerTask {
      override def run(): Unit = {

        var wordsLevelOne = levelOne()
        var wordsLevelTwo = levelTwo()
        var wordsLevelThere = levelThree()

        if (wordIndex < wordsLevelOne.length) {
          val (x, y) = getRandomPosition(getWindowWidth.toFloat, getWindowHeight.toFloat)
          fallingWords += WordPosition(words(wordIndex), x, y) // Yeni kelimeyi ekler
          wordIndex += 1
        } else {
          timer.cancel() // Tüm kelimeler eklendiğinde timer'ı durdur
        }
        // Ekranı yeniden çizdir
        Gdx.app.postRunnable(new Runnable {
          override def run(): Unit = {
            forceRedraw()
          }
        })
      }
    }, 0, 1000) // 1 saniyelik aralıklarla kelime ekler
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

  // Bu metot ekranda yeniden çizimi zorlamak için kullanılabilir.
  def forceRedraw(): Unit = {
    // Ekranda bir yeniden çizim işlemi başlatır.
    Gdx.graphics.requestRendering()
  }
}