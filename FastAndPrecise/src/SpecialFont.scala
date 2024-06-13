import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator
import com.badlogic.gdx.graphics.g2d.BitmapFont

object SpecialFont {
  // FONT
  private val starjedi = Gdx.files.internal("data/font/Starjedi.ttf")
  private val parameter = new FreeTypeFontGenerator.FreeTypeFontParameter
  private val generator = new FreeTypeFontGenerator(starjedi)

  def create(outside_color: String, inside_color: String, size: Int): BitmapFont = {
    parameter.size = generator.scaleForPixelHeight(size)
    parameter.color = Color.valueOf(inside_color)
    parameter.borderColor = Color.valueOf(outside_color)
    parameter.borderWidth = 3
    return generator.generateFont(parameter)
  }

  def generate(): Unit = {
    generator.dispose()
  }

}
