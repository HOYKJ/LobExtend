package lobExtendMod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import lobExtendMod.helper.LobExtendFontHelper;
import lobotomyMod.action.common.LatterAction;
import lobotomyMod.helper.LobotomyImageMaster;

/**
 * @author hoykj
 */
public class CensoredFearEffect extends AbstractGameEffect {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("FearLevel");
    public static final String[] TEXT = uiStrings.TEXT;
    private AbstractCreature target;
    private int level;
    private Texture img;
    private String word;
    private GlyphLayout gl = new GlyphLayout();

    public CensoredFearEffect(AbstractCreature target){
        this.target = target;
        this.level = 5;
        if (target instanceof AbstractPlayer || (target instanceof AbstractMonster && ((AbstractMonster) target).type == AbstractMonster.EnemyType.BOSS)){
            this.level = 4;
        }
        this.img = LobotomyImageMaster.FEAR_LEVEL[level];
        this.color = LobotomyImageMaster.FEAR_LEVEL_COLOR[level].cpy();
        this.word = TEXT[level];
        this.duration = 3;
    }

    public void update(){
        if (this.duration == 3){
            if (this.level == 5){
                //this.target.damage(new DamageInfo(this.target, (int)(this.target.maxHealth * 0.5F), DamageInfo.DamageType.HP_LOSS));
                AbstractDungeon.actionManager.addToTop(new LatterAction(()->{
                    this.target.damage(new DamageInfo(this.target, (int)(this.target.maxHealth * 0.5F), DamageInfo.DamageType.HP_LOSS));
                }));
            }
            else {
               //this.target.damage(new DamageInfo(this.target, (int)(this.target.maxHealth * 0.3F), DamageInfo.DamageType.HP_LOSS));
                AbstractDungeon.actionManager.addToTop(new LatterAction(()->{
                    this.target.damage(new DamageInfo(this.target, (int)(this.target.maxHealth * 0.3F), DamageInfo.DamageType.HP_LOSS));
                }));
            }
        }

        this.duration -= Gdx.graphics.getDeltaTime();
        if (this.duration <= 0){
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        float x = this.target.hb.cX - this.img.getWidth() / 2.0F * 0.8F;
        float y = this.target.drawY + this.target.hb.height;
        sb.setColor(this.color);
        sb.draw(this.img, x, y, this.img.getWidth() * 0.8F, this.img.getHeight() * 0.8F);
        float x1 = x + 196 * Settings.scale * 0.8F, y1 = y + 58 * Settings.scale * 0.8F;
        this.gl.setText(LobExtendFontHelper.FearFont, this.word);
        x1 -= this.gl.width / 2;
        y1 += this.gl.height / 2;
        FontHelper.renderSmartText(sb, LobExtendFontHelper.FearFont, this.word, x1, y1, Settings.WIDTH, 0.0F, this.color);
    }

    public void dispose(){}
}
