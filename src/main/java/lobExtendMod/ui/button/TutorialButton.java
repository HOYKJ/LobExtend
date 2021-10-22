package lobExtendMod.ui.button;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import lobExtendMod.helper.LobExtendImageMaster;
import lobExtendMod.ui.LobExtendFtue;
import lobotomyMod.ui.LobotomyFtue;

import java.util.ArrayList;

/**
 * @author hoykj
 */
public class TutorialButton {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("TutorialButton");
    public static final String[] TEXT = uiStrings.TEXT;
    private static final Color HOVER_BLEND_COLOR = new Color(1.0F, 1.0F, 1.0F, 0.4F);
    private static final float SHOW_X = 1500.0F * Settings.scale, DRAW_Y = 860.0F * Settings.scale;
    private static final float HIDE_X = - 260.0F * Settings.scale;
    private float current_x = HIDE_X;
    private float target_x = this.current_x;
    private boolean isHidden = true;
    private ArrayList<PowerTip> tips = new ArrayList<>();

    public Hitbox hb = new Hitbox(170.0F * Settings.scale, 170.0F * Settings.scale);

    public TutorialButton() {
        this.hb.move(SHOW_X, DRAW_Y);
        this.tips.add(new PowerTip(TEXT[0], TEXT[1]));
    }

    public void update() {
        if (!this.isHidden) {
            this.hb.update();
            if (InputHelper.justClickedLeft && this.hb.hovered) {
                this.hb.clickStarted = true;
                CardCrawlGame.sound.play("UI_CLICK_1");
            }
            if (this.hb.justHovered)
                CardCrawlGame.sound.play("UI_HOVER");
        }
        if (this.current_x != this.target_x) {
            this.current_x = MathUtils.lerp(this.current_x, this.target_x, Gdx.graphics.getDeltaTime() * 9.0F);
            if (Math.abs(this.current_x - this.target_x) < Settings.UI_SNAP_THRESHOLD)
                this.current_x = this.target_x;
        }
    }

    public void hideInstantly() {
        this.current_x = HIDE_X;
        this.target_x = HIDE_X;
        this.isHidden = true;
    }

    public void hide() {
        if (!this.isHidden) {
            this.target_x = HIDE_X;
            this.isHidden = true;
        }
    }

    public void show() {
        if (this.isHidden) {
            this.target_x = SHOW_X;
            this.isHidden = false;
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        renderButton(sb);
        if (this.hb.hovered){
            this.renderTip();
        }
        if (this.hb.hovered && !this.hb.clickStarted) {
            sb.setBlendFunction(770, 1);
            sb.setColor(HOVER_BLEND_COLOR);
            renderButton(sb);
            sb.setBlendFunction(770, 771);
        }
        if (this.hb.clicked) {
            this.hb.clicked = false;
            AbstractDungeon.ftue = new LobExtendFtue();
        }
        if (!this.isHidden)
            this.hb.render(sb);
    }

    private void renderButton(SpriteBatch sb) {
        sb.draw(LobExtendImageMaster.QUESTION_BUTTON, this.current_x - 64.0F, DRAW_Y - 64.0F, 64.0F, 64.0F, 128.0F, 128.0F,
                Settings.scale, Settings.scale, 0.0F, 0, 0, 128, 128, false, false);
    }

    public void renderTip() {
        TipHelper.queuePowerTips(InputHelper.mX + 60.0F * Settings.scale, InputHelper.mY - 30.0F * Settings.scale, this.tips);
    }
}
