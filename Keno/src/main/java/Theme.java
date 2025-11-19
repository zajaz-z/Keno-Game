
import javafx.scene.paint.Color;
//for themes
public class Theme {
    public final Color backgroundColor;
    public final String buttonColor;
    public final String fontFamily;
    public final Color textColor;

    public Theme(Color bg, String btn, String font, Color text) {
        this.backgroundColor = bg;
        this.buttonColor = btn;
        this.fontFamily = font;
        this.textColor = text;
    }
}
