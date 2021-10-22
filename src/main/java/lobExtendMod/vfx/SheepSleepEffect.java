package lobExtendMod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

/**
 * @author hoykj
 */
public class SheepSleepEffect extends AbstractGameEffect {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("CampfireSleepEffect");
    public static final String[] TEXT = uiStrings.TEXT;
    private static final float HEAL_AMOUNT = 0.3F;
    private static final float DUR = 3.0F;
    private static final float FAST_MODE_DUR = 1.5F;
    private boolean hasHealed = false;
    private int healAmount;
    private Color screenColor = AbstractDungeon.fadeColor.cpy();

    public SheepSleepEffect() {
        if (Settings.FAST_MODE) {
            this.startingDuration = 1.5F;
        } else {
            this.startingDuration = 3.0F;
        }
        this.duration = this.startingDuration;
        this.screenColor.a = 0.0F;
        AbstractDungeon.overlayMenu.proceedButton.hide();
        if (ModHelper.isModEnabled("Night Terrors")) {
            this.healAmount = (int)(AbstractDungeon.player.maxHealth * 1.0F);
            AbstractDungeon.player.decreaseMaxHealth(5);
        } else {
            this.healAmount = (int)(AbstractDungeon.player.maxHealth * 0.3F);
        }
        if (AbstractDungeon.player.hasRelic("Regal Pillow"))
            this.healAmount += 15;
    }

    public void update() {
        this.duration -= Gdx.graphics.getDeltaTime();
        updateBlackScreenColor();
        if (this.duration < this.startingDuration - 0.5F && !this.hasHealed) {
            playSleepJingle();
            this.hasHealed = true;
            if (AbstractDungeon.player.hasRelic("Regal Pillow"))
                AbstractDungeon.player.getRelic("Regal Pillow").flash();
            AbstractDungeon.player.heal(this.healAmount, false);
            for (AbstractRelic r : AbstractDungeon.player.relics)
                r.onRest();
        }
        if (this.duration < this.startingDuration / 2.0F) {
            this.isDone = true;
        }
    }

    private void playSleepJingle() {
        int roll = MathUtils.random(0, 2);
        switch (AbstractDungeon.id) {
            case "Exordium":
                if (roll == 0) {
                    CardCrawlGame.sound.play("SLEEP_1-1");
                    break;
                }
                if (roll == 1) {
                    CardCrawlGame.sound.play("SLEEP_1-2");
                    break;
                }
                CardCrawlGame.sound.play("SLEEP_1-3");
                break;
            case "TheCity":
                if (roll == 0) {
                    CardCrawlGame.sound.play("SLEEP_2-1");
                    break;
                }
                if (roll == 1) {
                    CardCrawlGame.sound.play("SLEEP_2-2");
                    break;
                }
                CardCrawlGame.sound.play("SLEEP_2-3");
                break;
            case "TheBeyond":
                if (roll == 0) {
                    CardCrawlGame.sound.play("SLEEP_3-1");
                    break;
                }
                if (roll == 1) {
                    CardCrawlGame.sound.play("SLEEP_3-2");
                    break;
                }
                CardCrawlGame.sound.play("SLEEP_3-3");
                break;
        }
    }

    private void updateBlackScreenColor() {
        if (this.duration > this.startingDuration - 0.5F) {
            this.screenColor.a = Interpolation.fade.apply(1.0F, 0.0F, (this.duration - this.startingDuration - 0.5F) * 2.0F);
        } else if (this.duration < 1.0F) {
            this.screenColor.a = Interpolation.fade.apply(0.0F, 1.0F, this.duration);
        } else {
            this.screenColor.a = 1.0F;
        }
    }

    public void render(SpriteBatch sb) {
        sb.setColor(this.screenColor);
        sb.draw(ImageMaster.WHITE_SQUARE_IMG, 0.0F, 0.0F, Settings.WIDTH, Settings.HEIGHT);
    }

    public void dispose() {}
}
