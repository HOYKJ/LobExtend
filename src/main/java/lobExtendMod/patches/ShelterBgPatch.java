package lobExtendMod.patches;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rooms.EventRoom;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import lobExtendMod.event.Shelter;
import lobExtendMod.helper.LobExtendImageMaster;

import java.util.Set;

/**
 * @author hoykj
 */
public class ShelterBgPatch {
    @SpireEnum
    public static AbstractDungeon.RenderScene SHELTER;

    @SpirePatch(
            clz= AbstractDungeon.class,
            method="render",
            paramtypez = {
                    SpriteBatch.class
            }
    )
    public static class render {
        @SpirePrefixPatch
        public static void prefix(AbstractDungeon _inst, SpriteBatch sb){
            if (AbstractDungeon.getCurrRoom() instanceof EventRoom && AbstractDungeon.getCurrRoom().event instanceof Shelter){
                AbstractDungeon.rs = SHELTER;
                Texture img = LobExtendImageMaster.SHELTER_BG;
                if (Settings.WIDTH / img.getWidth() >= Settings.HEIGHT / img.getHeight()) {
                    float height = img.getHeight() * ((float)Settings.WIDTH / img.getWidth());
                    sb.setColor(Color.WHITE.cpy());
                    sb.draw(img, 0.0F, 0.0F, Settings.WIDTH, height);
                }
                else {
                    float width = img.getWidth() * ((float)Settings.HEIGHT / img.getHeight());
                    float x = width - Settings.WIDTH;
                    sb.setColor(Color.WHITE.cpy());
                    sb.draw(img, -x / 2, 0.0F, width, Settings.HEIGHT);
                }
            }
        }
    }

//    @SpirePatch(
//            clz= AbstractDungeon.class,
//            method="render"
//    )
//    public static class render2 {
//        @SpireInsertPatch(rloc=20)
//        public static void insert(SpriteBatch sb){
//            if (AbstractDungeon.rs == SHELTER){
//
//            }
//        }
//    }
}
