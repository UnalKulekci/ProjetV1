import ch.hevs.gdx2d.components.bitmaps.BitmapImage
import ch.hevs.gdx2d.desktop.PortableApplication
import ch.hevs.gdx2d.lib.GdxGraphics
import com.badlogic.gdx.graphics.g2d.{Batch, BitmapFont, SpriteBatch}
import java.awt.geom.Point2D
import scala.collection.mutable.ArrayBuffer
import scala.util.Random
import com.badlogic.gdx.Input

class Game extends PortableApplication(1920, 1080) {

  private var w: ArrayBuffer[ArrayBuffer[String]] = ArrayBuffer.empty[ArrayBuffer[String]]
  private var arrRoundWords: ArrayBuffer[String] = ArrayBuffer.empty[String]
  private val arrRoundWordsLength: ArrayBuffer[Int] = ArrayBuffer.empty[Int]
  private val arrPositions: ArrayBuffer[Point2D] = ArrayBuffer.empty[Point2D]
  private val fallingWords = new ArrayBuffer[WordPosition]()
  private var currentWordIndex: Int = -1
  private var idxTimer: Int = 0
  private var secondTimer: Int = 0
  private var roundCounter: Int = 0
  private var scoreCounter: Int = 0
  private var wallpaper: BitmapImage = _
  private var gameover: BitmapImage = _
  private var isc_logo: BitmapImage = _ // d41367
  private var font_blue: BitmapFont = _ // 165baa
  private var font_top1: BitmapFont = _
  private var font_isc: BitmapFont = _
  private var font_isc_50: BitmapFont = _
  private var font_black: BitmapFont = _ // Color.BLACK
  private var font_in_use: BitmapFont = _
  private var isGameOver = false // Game over state
  private var recordAdded: Boolean = false
  private var game_started: Boolean = false
  private var chr: Char = ' '

  // Used to replay the game
  private def initGame(): Unit = {
    isGameOver = false
    w = Words.createRoundArray(Words.getWords().toArray)
    arrRoundWords.clear()
    arrPositions.clear()
    fallingWords.clear()
    arrRoundWordsLength.clear()
    currentWordIndex = -1
    roundCounter = 0
    scoreCounter = 0
    idxTimer = 0
    secondTimer = 0
    recordAdded = false
  }

  // Word select after pressing a key depending on the y position as well as the order of letters
  private def returnCurrentIdx(a: ArrayBuffer[WordPosition], c: Char): Int = {
    var min: Int = 0
    var x: ArrayBuffer[WordPosition] = ArrayBuffer.empty[WordPosition]
    for (i <- a.indices) {
      if (a(i).word.startsWith(c.toString)) {
        x.append(a(i))
      }
    }
    x = x.sortBy(_.word)
    if (x.length == 1) {
      return a.indexOf(x(0))
    } else if (x.isEmpty) {
      return -1
    } else {
      min = 0
      for (i <- x.indices) {
        if (x(min).y > x(i).y) {
          min = i
        }
      }
      return a.indexOf(x(min))
    }
    -1
  }

  // Function used to display words in a random way
  private def getRandomPosition(maxWidth: Float, maxHeight: Float): (Float, Float) = {
    val x = Random.between(200, maxWidth - 200)
    val y = maxHeight + Random.between(50, 200)
    (x, y)
  }

  // Function called only ones
  override def onInit(): Unit = {
    println(Console.MAGENTA + "FAST & PRECISE" + Console.RESET + " 2024 " + Console.RESET + "by " + Console.BLUE + "ÜNAL" + Console.RESET + " and" + Console.YELLOW + " FILIP" + Console.RESET)
    setTitle("FAST & PRECISE")
    // Set imags path
    wallpaper = new BitmapImage("data/wallpaper.png")
    gameover = new BitmapImage("data/gameover.png")
    isc_logo = new BitmapImage("data/logo_isc.png")
    // Creating fonts
    font_top1 = SpecialFont.create("000000", "d41367", 40)
    font_isc = SpecialFont.create("d41367", "ffffff", 30)
    font_isc_50 = SpecialFont.create("d41367", "ffffff", 50)
    font_blue = SpecialFont.create("165baa", "ffffff", 30)
    font_black = SpecialFont.create("000000", "ffffff", 30)
    font_in_use = font_blue
    SpecialFont.generate()
  }

  // Graphic function, counter
  override def onGraphicRender(g: GdxGraphics): Unit = {
    g.clear()
    g.drawPicture(getWindowWidth / 2, getWindowHeight / 2, wallpaper)
    g.drawTransformedPicture(g.getScreenWidth - 200, 60f, 0, 200f, 50.92f, isc_logo)
    if (!isGameOver) {
      // Print the score
      g.drawString(20f, 70f, s" Total Scores :", font_black, -1)
      g.drawString(230f, 70f, s" ${scoreCounter}", font_isc)
      // Print Round & Timer
      g.drawString(20f, 50f, s"Round : ${roundCounter + 1}          Time :", font_black, -1)
      g.drawString(330f, 50f, s"${secondTimer} s", font_isc)
      if (game_started) {
        idxTimer += 1
        if (idxTimer % 60 == 0) {
          secondTimer += 1
          idxTimer = 0
        }
      } else {
        g.drawStringCentered(g.getScreenHeight / 2, "press space to start", font_isc_50)
      }
    }


    // Filling the array with words for the actual round
    if (roundCounter < w.length) {
      // Adding the words for one round
      arrRoundWords = w(roundCounter)
      // Filling fallingWords array used to make the words fall on the window
      if (fallingWords.length < arrRoundWords.length) {
        for (i <- arrRoundWords.indices) {
          var (x, y) = getRandomPosition(getWindowWidth.toFloat, getWindowHeight.toFloat)
          var attempts: Int = 0
          val wordLength: Int = arrRoundWords(i).length
          var collision: Boolean = true

          while (collision && attempts < 5) {
            collision = false
            for (pos: Point2D <- arrPositions) {
              val posX = pos.getX.toFloat
              val posY = pos.getY.toFloat
              if ((x < posX + wordLength * 17.5 && x + wordLength * 17.5 > posX) && (y < posY + 20 && y + 20 > posY)) {
                collision = true
              }
            }
            // If there's a collision, redo the random x,y pair
            if (collision) {
              val newPos = getRandomPosition(getWindowWidth.toFloat, getWindowHeight.toFloat)
              x = newPos._1
              y = newPos._2
              attempts += 1
            }
          }
          // If after 5 attemps, there's still the collision, add 50 pixels to the y
          if (collision && attempts >= 5) {
            y += 50
          }
          arrPositions.append(new Point2D.Float(x, y))
        }
        // Appending the x,y for each word before drawing it on the screen
        for (i <- arrRoundWords.indices) {
          fallingWords.append(WordPosition(arrRoundWords(i), arrPositions(i).getX.toFloat, arrPositions(i).getY.toFloat))
        }
      }
      // arrRoundWordsLength used to change the color of a word which is being typed
      if (arrRoundWordsLength.length < arrRoundWords.length) {
        for (i <- arrRoundWords.indices) {
          arrRoundWordsLength.append(arrRoundWords(i).length)
        }
      }
    }

    // Update and draw falling words
    for (i: Int <- fallingWords.indices) {
      fallingWords(i).y -= 1 // -60 pixels/s

      // Changing the color of the falling word when it's been typed on
      if (arrRoundWordsLength(i) != fallingWords(i).word.length) {
        font_in_use = font_isc
      } else {
        font_in_use = font_blue
      }
      // Drawing falling words on the screen
      if (!isGameOver) {
        g.drawString(fallingWords(i).x, fallingWords(i).y, fallingWords(i).word, font_in_use, 0)
      }
      font_in_use = font_blue

      // Game over
      if (fallingWords(i).y < 0) {
        isGameOver = true
        g.drawPicture(g.getScreenWidth / 2, g.getScreenHeight / 2, gameover)
        g.drawStringCentered(g.getScreenHeight / 2 - 100, s"Achieved round : ${roundCounter}", font_blue)
        g.drawStringCentered(g.getScreenHeight / 2 - 150, s"Score : ${scoreCounter}", font_blue)
        g.drawStringCentered(g.getScreenHeight / 2 - 250, "PRESS y to replay", font_isc)
        g.drawStringCentered(g.getScreenHeight / 2 - 300, "PRESS q to quit", font_black)
        g.drawString(240, getWindowHeight * 0.375f, "top 10", font_isc_50, 0)
        for (i <- Record.getFirst10("data/records.txt").indices) {
          if (i == 0) {
            g.drawString(90, getWindowHeight * 0.35f - 30, f"${i + 1}%02d.   " + Record.getFirst10("data/records.txt")(i), font_top1)
          } else {
            g.drawString(100, getWindowHeight * 0.35f - 50 - 20 * (i), f"${i + 1}%02d.   " + Record.getFirst10("data/records.txt")(i), font_blue)
          }
        }
      }
    }
    // Add new score bigger than 0 only once
    if (isGameOver && !recordAdded && scoreCounter > 0) {
      Record.addNew("data/records.txt", scoreCounter)
      recordAdded = true
    }
  }

  // This function deletes the letters from the word
  override def onKeyDown(keycode: Int): Unit = {

    chr = (keycode + 68).toChar

    if (keycode == Input.Keys.SPACE && !game_started) {
      game_started = true
      initGame()
    }

    if (currentWordIndex == -1) {
      // Getting the word index to work on
      currentWordIndex = returnCurrentIdx(fallingWords, chr)
    }

    // Removing word from arrays after deleting every typing every letter of the word
    if (currentWordIndex != -1 && fallingWords(currentWordIndex).word.startsWith(chr.toString)) {
      fallingWords(currentWordIndex).word = fallingWords(currentWordIndex).word.substring(1)
      if (fallingWords(currentWordIndex).word.isEmpty) {
        scoreCounter += (roundCounter + 1) * arrRoundWordsLength(currentWordIndex)
        arrRoundWords.remove(currentWordIndex)
        fallingWords.remove(currentWordIndex)
        arrRoundWordsLength.remove(currentWordIndex)
        currentWordIndex = -1
        if (fallingWords.isEmpty) {
          roundCounter += 1
          arrPositions.clear()
        }
      }
    }
    if (isGameOver) {
      if (chr == 'y') {
        initGame()
      } else if (chr == 'q') {
        println("Thank you for playing")
        println(s"Achieved round : ${Console.MAGENTA + roundCounter + Console.RESET}")
        println(s"Score : ${Console.MAGENTA + scoreCounter + Console.RESET} in ${Console.YELLOW + secondTimer + Console.RESET} seconds")
        System.exit(1337)
      } else {
        return
      }
    }
  }
}

