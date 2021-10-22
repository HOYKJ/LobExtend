package lobExtendMod.helper;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.megacrit.cardcrawl.helpers.FontHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author hoykj
 */
public class LobExtendFontHelper {
    public static BitmapFont FearFont;
    public static BitmapFont WantedFont;
    public static BitmapFont WantedFont_2;

    public static void initialize() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Method prepFont = FontHelper.class.getDeclaredMethod("prepFont", float.class, boolean.class);
        Field fontFile = FontHelper.class.getDeclaredField("fontFile");
        Field param = FontHelper.class.getDeclaredField("param");
        prepFont.setAccessible(true);
        fontFile.setAccessible(true);
        param.setAccessible(true);

        ((FreeTypeFontGenerator.FreeTypeFontParameter)param.get(FontHelper.class)).borderWidth = 0.0F;
        ((FreeTypeFontGenerator.FreeTypeFontParameter)param.get(FontHelper.class)).shadowOffsetX = 1;
        ((FreeTypeFontGenerator.FreeTypeFontParameter)param.get(FontHelper.class)).shadowOffsetY = 1;
        ((FreeTypeFontGenerator.FreeTypeFontParameter)param.get(FontHelper.class)).spaceX = 0;

        fontFile.set(FontHelper.class, Gdx.files.internal("font/zhs/NotoSansMonoCJKsc-Regular.otf"));
        FearFont = (BitmapFont) prepFont.invoke(FontHelper.class, 28.0F, false);

        fontFile.set(FontHelper.class, Gdx.files.internal("lobotomyMod/font/norwester.otf"));
        WantedFont = (BitmapFont) prepFont.invoke(FontHelper.class, 31.0F, false);
        WantedFont_2 = (BitmapFont) prepFont.invoke(FontHelper.class, 28.0F, false);
    }
}
