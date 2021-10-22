package lobExtendMod.screen;

import basemod.helpers.SuperclassFinder;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import lobExtendMod.LobExtendMod;
import lobExtendMod.event.LobotomyEvent;
import lobExtendMod.event.isolate.AbstractIsolate;
import lobExtendMod.event.isolate.MainRoom;
import lobExtendMod.helper.LobExtendImageMaster;
import lobExtendMod.ui.button.CancelButton;
import lobExtendMod.ui.button.RoomBox;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * @author hoykj
 */
public class ChooseRoomScreen extends AbstractGameEffect {
    private ArrayList<RoomBox> box = new ArrayList<>();
    private LobotomyEvent root;
    private CancelButton cancel;
    public float right;
    public boolean end;
    private boolean playSFX;

    public ChooseRoomScreen(LobotomyEvent root){
        CardCrawlGame.isPopupOpen = true;
        this.root = root;
        this.color = Color.BLACK.cpy();
        this.color.a = 0;
        int i = 0;
        ArrayList<AbstractIsolate> list = this.root.getIsolates();
        for (AbstractIsolate isolate : list){
            if (isolate instanceof MainRoom){
                continue;
            }
            i ++;
            float x = 0, y = 0;
            switch (i){
                case 1:
                    x = Settings.WIDTH / 2.0F - 400 * Settings.scale;
                    y = Settings.HEIGHT / 2.0F + 160 * Settings.scale;
                    break;
                case 2:
                    x = Settings.WIDTH / 2.0F;
                    y = Settings.HEIGHT / 2.0F + 140 * Settings.scale;
                    break;
                case 3:
                    x = Settings.WIDTH / 2.0F + 400 * Settings.scale;
                    y = Settings.HEIGHT / 2.0F + 140 * Settings.scale;
                    break;
                case 4:
                    x = Settings.WIDTH / 2.0F - 240 * Settings.scale;
                    y = Settings.HEIGHT / 2.0F - 160 * Settings.scale;
                    break;
                case 5:
                    x = Settings.WIDTH / 2.0F + 160 * Settings.scale;
                    y = Settings.HEIGHT / 2.0F - 160 * Settings.scale;
                    break;
            }
            this.box.add(new RoomBox(x, y, isolate.getImg(), this, !isolate.entered));
        }
        this.cancel = new CancelButton(this);
        this.cancel.show();
        this.right = Settings.WIDTH;
        this.end = false;
        this.playSFX = true;
    }

    public void update(){
        if (this.playSFX){
            this.playSFX = false;
            if (MathUtils.randomBoolean()) {
                CardCrawlGame.sound.play("MAP_OPEN", 0.1F);
            } else {
                CardCrawlGame.sound.play("MAP_OPEN_2", 0.1F);
            }
        }

        if (this.end){
            if (this.right < Settings.WIDTH){
                this.right += (this.right / Settings.WIDTH * 4000 + 3000) * Gdx.graphics.getDeltaTime();
            }
            else {
                CardCrawlGame.isPopupOpen = false;
                this.isDone = true;
            }
        }
        else {
            if (this.right > 0){
                this.right -= (this.right / Settings.WIDTH * 4000 + 3000) * Gdx.graphics.getDeltaTime();
            }
            else {
                this.right = 0;
            }
        }
        this.color.a = 0.8F - this.right / Settings.WIDTH * 0.6F;

        for (RoomBox aBox : this.box) {
            aBox.update();
        }
        this.cancel.update();
        this.updateInput();
    }

    private void updateInput() {
        if (this.end){
            return;
        }
        for (int i = 0; i < this.box.size(); i ++){
            if (this.box.get(i).hb.clicked){
                this.end = true;
                this.playSFX = true;
                this.root.setIsolate(i + 1);
            }
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        sb.setColor(this.color);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, Settings.WIDTH, Settings.HEIGHT);

        sb.setColor(new Color(1, 1, 1, 0.9F));
        Texture img = LobExtendImageMaster.INFO_BG;
        sb.draw(img, (Settings.WIDTH - img.getWidth() * Settings.scale * 1.6F) / 2.0F + this.right, (Settings.HEIGHT - img.getHeight() * Settings.scale * 1.6F) / 2.0F,
                img.getWidth() * Settings.scale * 1.6F, img.getHeight() * Settings.scale * 1.6F);

        sb.setColor(Color.WHITE.cpy());
        for (RoomBox aBox : this.box) {
            aBox.render(sb);
        }
        this.cancel.render(sb);
    }

    public void dispose(){}

    @SpirePatch(
            clz= CardCrawlGame.class,
            method = "render"
    )
    public static class render {
        @SpireInsertPatch(rloc=58)
        public static void Insert(CardCrawlGame _inst) throws NoSuchFieldException, IllegalAccessException {
            if(LobExtendMod.chooseRoomScreen != null && !LobExtendMod.chooseRoomScreen.isDone){
                Field sb = SuperclassFinder.getSuperclassField(_inst.getClass(), "sb");
                sb.setAccessible(true);
                LobExtendMod.chooseRoomScreen.render((SpriteBatch) sb.get(_inst));
            }
        }
    }

    @SpirePatch(
            clz= CardCrawlGame.class,
            method = "update"
    )
    public static class update {
        @SpireInsertPatch(rloc=26)
        public static void Insert(CardCrawlGame _inst) {
            if(LobExtendMod.chooseRoomScreen != null && !LobExtendMod.chooseRoomScreen.isDone){
                LobExtendMod.chooseRoomScreen.update();
            }
        }
    }
}
