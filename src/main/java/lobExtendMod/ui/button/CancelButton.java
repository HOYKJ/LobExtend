package lobExtendMod.ui.button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import lobExtendMod.screen.ChooseRoomScreen;

/**
 * @author hoykj
 */
public class CancelButton {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("CancelButton");
    public static final String[] TEXT = uiStrings.TEXT;
    public static final float TAKE_Y = 240.0F * Settings.scale;
    private static final float SHOW_X = 1460.0F * Settings.scale;
    private static final float HIDE_X = Settings.WIDTH / 2.0F;
    private float current_x = HIDE_X;
    private float target_x = this.current_x;
    private boolean isHidden = true;
    private Color textColor = Color.WHITE.cpy();
    private Color btnColor = Color.WHITE.cpy();
    public boolean screenDisabled = false;
    private static final float HITBOX_W = 260.0F * Settings.scale;
    private static final float HITBOX_H = 80.0F * Settings.scale;
    public Hitbox hb = new Hitbox(0.0F, 0.0F, HITBOX_W, HITBOX_H);
    private float controllerImgTextWidth = 0.0F;
    private ChooseRoomScreen screen;
    private int circle;
    private float duration;

    public CancelButton(ChooseRoomScreen screen) {
        this.hb.move(Settings.WIDTH / 2.0F, TAKE_Y);
        this.screen = screen;
        this.circle = 0;
        this.duration = 0;
    }

    public void update() {
        if (this.isHidden)
            return;
        this.hb.update();
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
        if (this.hb.hovered && InputHelper.justClickedLeft) {
            this.hb.clickStarted = true;
            //CardCrawlGame.sound.play("UI_CLICK_1");
        }
        if ((this.hb.clicked || InputActionSet.cancel.isJustPressed() || CInputActionSet.cancel.isJustPressed()) && !this.screenDisabled) {
            this.hb.clicked = false;
            this.screen.end = true;
            if (MathUtils.randomBoolean()) {
                CardCrawlGame.sound.play("MAP_OPEN", 0.1F);
            } else {
                CardCrawlGame.sound.play("MAP_OPEN_2", 0.1F);
            }
            CardCrawlGame.isPopupOpen = false;
        }
        this.screenDisabled = false;
        if (this.current_x != this.target_x) {
            this.current_x = MathUtils.lerp(this.current_x, this.target_x, Gdx.graphics.getDeltaTime() * 9.0F);
            if (Math.abs(this.current_x - this.target_x) < Settings.UI_SNAP_THRESHOLD) {
                this.current_x = this.target_x;
                this.hb.move(this.current_x, TAKE_Y);
            }
        }
        this.textColor.a = MathHelper.fadeLerpSnap(this.textColor.a, 1.0F);
        this.btnColor.a = this.textColor.a;
    }

    public void hideInstantly() {
        this.current_x = HIDE_X;
        this.target_x = HIDE_X;
        this.isHidden = true;
        this.textColor.a = 0.0F;
        this.btnColor.a = 0.0F;
    }

    public void hide() {
        this.isHidden = true;
    }

    public void show() {
        this.isHidden = false;
        this.textColor.a = 0.0F;
        this.btnColor.a = 0.0F;
        this.current_x = HIDE_X;
        this.target_x = SHOW_X;
        this.hb.move(SHOW_X, TAKE_Y);
    }

    public void render(SpriteBatch sb) {
        if (this.isHidden)
            return;
        //renderButton(sb);
        if (FontHelper.getSmartWidth(FontHelper.buttonLabelFont, TEXT[0], 9999.0F, 0.0F) > 200.0F * Settings.scale) {
            FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, TEXT[0], this.current_x + this.screen.right, TAKE_Y, this.textColor, 0.8F);
        } else {
            FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, TEXT[0], this.current_x + this.screen.right, TAKE_Y, this.textColor);
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
            sb.draw(tmp, this.current_x - 96.0F + this.screen.right, TAKE_Y - 96.0F, 96.0F, 96.0F, 192.0F, 192.0F, 1.5F * Settings.scale, 1.5F * Settings.scale, 0, 0, 0, 192, 192, false, false);
        }
    }

    private void renderButton(SpriteBatch sb) {
        sb.setColor(this.btnColor);
        sb.draw(ImageMaster.REWARD_SCREEN_TAKE_BUTTON, this.current_x - 256.0F, TAKE_Y - 128.0F, 256.0F, 128.0F, 512.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 512, 256, false, false);
        if (this.hb.hovered && !this.hb.clickStarted) {
            sb.setBlendFunction(770, 1);
            sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.3F));
            sb.draw(ImageMaster.REWARD_SCREEN_TAKE_BUTTON, this.current_x - 256.0F, TAKE_Y - 128.0F, 256.0F, 128.0F, 512.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 512, 256, false, false);
            sb.setBlendFunction(770, 771);
        }
        if (Settings.isControllerMode) {
            if (this.controllerImgTextWidth == 0.0F)
                this.controllerImgTextWidth = FontHelper.getSmartWidth(FontHelper.buttonLabelFont, TEXT[0], 99999.0F, 0.0F) / 2.0F;
            sb.setColor(Color.WHITE);
            sb.draw(CInputActionSet.cancel
                    .getKeyImg(), this.current_x - 32.0F - this.controllerImgTextWidth - 38.0F * Settings.scale, TAKE_Y - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 64, 64, false, false);
        }
        this.hb.render(sb);
    }
}
