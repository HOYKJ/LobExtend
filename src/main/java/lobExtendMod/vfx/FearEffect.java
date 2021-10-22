package lobExtendMod.vfx;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
import lobotomyMod.npc.AbstractNPC;

/**
 * @author hoykj
 */
public class FearEffect extends AbstractGameEffect {
    private static final UIStrings uiStrings = CardCrawlGame.languagePack.getUIString("FearLevel");
    public static final String[] TEXT = uiStrings.TEXT;
    private AbstractCreature target;
    private int level, code;
    private Texture img;
    private String word;
    private GlyphLayout gl = new GlyphLayout();

    public FearEffect(AbstractCreature target, int code){
        this.target = target;
        this.code = code;
        this.init();
        this.duration = 3;
    }

    public void update(){
        float damage = 0;
        if (this.duration == 3){
            switch (this.level){
                case 2:
                    damage = this.target.maxHealth * 0.1F;
                    break;
                case 3:
                    damage = this.target.maxHealth * 0.2F;
                    break;
                case 4:
                    damage = this.target.maxHealth * 0.3F;
                    break;
                case 5:
                    damage = this.target.maxHealth * 0.5F;
                    break;
            }
            float finalDamage = damage;
            AbstractDungeon.actionManager.addToTop(new LatterAction(()->{
                this.target.damage(new DamageInfo(this.target, (int) finalDamage, DamageInfo.DamageType.HP_LOSS));
            }));
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

    private void initLevel(){
        int tmp = 0;
        if (this.target instanceof AbstractPlayer){
            tmp = 4;
        }
        else if(target instanceof AbstractMonster) {
            if (AbstractDungeon.floorNum <= 20) {
                switch (((AbstractMonster) target).type){
                    case NORMAL:
                        tmp = 0;
                        break;
                    case ELITE:
                        tmp = 1;
                        break;
                    case BOSS:
                        tmp = 2;
                        break;
                }
            }
            else if (AbstractDungeon.floorNum <= 40) {
                switch (((AbstractMonster) target).type){
                    case NORMAL:
                        tmp = 1;
                        break;
                    case ELITE:
                        tmp = 2;
                        break;
                    case BOSS:
                        tmp = 3;
                        break;
                }
            }
            else {
                switch (((AbstractMonster) target).type){
                    case NORMAL:
                        tmp = 2;
                        break;
                    case ELITE:
                        tmp = 3;
                        break;
                    case BOSS:
                        tmp = 4;
                        break;
                }
            }
        }

        this.level = this.code - tmp;
    }

    private void init(){
        this.initLevel();
        this.img = LobotomyImageMaster.FEAR_LEVEL[this.level];
        this.color = LobotomyImageMaster.FEAR_LEVEL_COLOR[this.level].cpy();
        this.word = TEXT[this.level];
    }

    public void dispose(){}
}
