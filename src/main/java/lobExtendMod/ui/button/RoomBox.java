package lobExtendMod.ui.button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import lobExtendMod.screen.ChooseRoomScreen;

/**
 * @author hoykj
 */
public class RoomBox {
    public Hitbox hb = new Hitbox(256.0F * Settings.scale, 256.0F * Settings.scale);
    private float x, y;
    private int circle;
    private Texture img;
    private boolean active;
    private float duration;
    private float rotation;
    private ChooseRoomScreen screen;

    public RoomBox(float x, float y, Texture img, ChooseRoomScreen screen) {
        this(x, y, img, screen, true);
    }

    public RoomBox(float x, float y, Texture img, ChooseRoomScreen screen, boolean active) {
        this.hb.move(x, y);
        this.x = x;
        this.y = y;
        this.circle = 0;
        this.img = img;
        this.screen = screen;
        this.active = active;
        this.duration = 0;
        this.rotation = MathUtils.random(-10, 10);
    }

    public void update() {
        if (!this.active){
            return;
        }
        this.hb.update();
        if (InputHelper.justClickedLeft && this.hb.hovered) {
            this.hb.clickStarted = true;
//            CardCrawlGame.sound.play("UI_CLICK_1");
        }
        if (this.hb.justHovered) {
            int roll = MathUtils.random(3);
            switch (roll) {
                case 0:
                    CardCrawlGame.sound.play("MAP_SELECT_1");
                    return;
                case 1:
                    CardCrawlGame.sound.play("MAP_SELECT_2");
                    return;
                case 2:
                    CardCrawlGame.sound.play("MAP_SELECT_3");
                    return;
            }
            CardCrawlGame.sound.play("MAP_SELECT_4");
        }

        if (this.hb.hovered){
            if (this.circle < 5){
                this.duration += Gdx.graphics.getDeltaTime();
                if (this.duration > 0.05F) {
                    this.duration -= 0.05F;
                    this.circle ++;
                }
            }
        }
        else {
            this.circle = 0;
            this.duration = 0;
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(Color.GRAY);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, this.x - 98 + this.screen.right, this.y - 98, 98, 98, 196, 196, Settings.scale, Settings.scale, this.rotation, 0, 0, ImageMaster.WHITE_SQUARE_IMG.getWidth(), ImageMaster.WHITE_SQUARE_IMG.getHeight(), false, false);
        sb.setColor(Color.WHITE);
        if (this.img != null){
            sb.draw(this.img, this.x - 96 + this.screen.right, this.y - 96, 96, 96, 192, 192, Settings.scale, Settings.scale, this.rotation, 0, 0, this.img.getWidth(), this.img.getHeight(), false, false);
        }
        if (this.circle > 0){
            Texture tmp = ImageMaster.MAP_CIRCLE_1;
            switch (this.circle){
                case 2:
                    tmp = ImageMaster.MAP_CIRCLE_2;
                    break;
                case 3:
                    tmp = ImageMaster.MAP_CIRCLE_3;
                    break;
                case 4:
                    tmp = ImageMaster.MAP_CIRCLE_4;
                    break;
                case 5:
                    tmp = ImageMaster.MAP_CIRCLE_5;
                    break;
            }
            sb.setColor(new Color(123 / 255.0F, 81 / 255.0F, 50 / 255.0F, 1.0F));
            sb.draw(tmp, this.x - 96.0F + this.screen.right, this.y - 96.0F, 96.0F, 96.0F, 192.0F, 192.0F, 2.8F * Settings.scale, 2.8F * Settings.scale, 0, 0, 0, 192, 192, false, false);
        }
        if (!this.active){
            sb.setColor(new Color(183 / 255.0F, 167 / 255.0F, 156 / 255.0F, 1.0F));
            sb.draw(ImageMaster.MAP_CIRCLE_5, this.x - 96.0F + this.screen.right, this.y - 96.0F, 96.0F, 96.0F, 192.0F, 192.0F, 2.8F * Settings.scale, 2.8F * Settings.scale, 0, 0, 0, 192, 192, false, false);
        }

        this.hb.render(sb);
    }
}
