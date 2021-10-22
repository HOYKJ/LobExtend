package lobExtendMod.vfx;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.GameCursor;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import lobExtendMod.helper.LobExtendFontHelper;
import lobExtendMod.helper.LobExtendImageMaster;
import lobExtendMod.monster.Mercenary_m;
import lobotomyMod.card.uncommonCard.Mercenary;
import lobotomyMod.relic.CogitoBucket;

import java.lang.reflect.Field;

/**
 * @author hoykj
 */
public class WantedRequestEffect extends AbstractGameEffect {
    @SpireEnum
    private static GameCursor.CursorType WANTED;
    private static boolean renderInfo = false, enoughPE = false;
    private static Color color, color2;
    private static Color code[] = new Color[5];
    private static int renderCode;
    private static int codeCost[] = new int[5];

    public WantedRequestEffect(){
        renderInfo = false;
        enoughPE = false;
        renderCode = 0;
        color = new Color(1, 148 / 255.0F, 66 / 255.0F, 1);
        color2 = new Color(252 / 255.0F, 58 / 255.0F, 58 / 255.0F, 1);
        code[0] = new Color(34 / 255.0F, 249 / 255.0F, 0, 1);
        code[1] = new Color(26 / 255.0F, 161 / 255.0F, 1, 1);
        code[2] = new Color(1, 249 / 255.0F, 0, 1);
        code[3] = new Color(122 / 255.0F, 47 / 255.0F, 242 / 255.0F, 1);
        code[4] = new Color(1, 0, 0, 1);
        codeCost[0] = 40;
        codeCost[1] = 80;
        codeCost[2] = 120;
        codeCost[3] = 160;
        codeCost[4] = 200;
    }

    public void update(){
        CardCrawlGame.cursor.changeType(WantedRequestEffect.WANTED);

        AbstractCreature target = null;
        if(AbstractDungeon.player.hb.hovered){
            target = AbstractDungeon.player;
        }
        for (int i = (AbstractDungeon.getCurrRoom().monsters.monsters.size() - 1); i >= 0; i--) {
            AbstractMonster m = AbstractDungeon.getCurrRoom().monsters.monsters.get(i);
            if ((!(m.isDying)) && (m.currentHealth > 0) && (!(m.isEscaping))) {
                if(m.hb.hovered){
                    target = m;
                }
            }
        }
        if(target != null){
            renderInfo = true;
            renderCode = getCode(target);
            enoughPE = getEnoughPE();
        }
        else {
            renderInfo = false;
        }

        if(InputHelper.justClickedLeft){
            InputHelper.justClickedLeft = false;
            this.isDone = true;
            for(AbstractGameEffect effect : AbstractDungeon.topLevelEffects){
                if(effect instanceof WantedRequestEffect){
                    effect.isDone = true;
                }
            }

            if(target != null && enoughPE){
                AbstractDungeon.player.getRelic(CogitoBucket.ID).counter -= codeCost[renderCode];
                AbstractDungeon.player.masterDeck.removeCard(Mercenary.ID);
                AbstractDungeon.actionManager.addToTop(new SpawnMonsterAction(new Mercenary_m(100.0F * Settings.scale, AbstractDungeon.player.drawY, target), false));
            }
        }
        else if(InputHelper.justClickedRight){
            this.isDone = true;
        }
    }

    @Override
    public void render(SpriteBatch sb) {
    }

    private int getCode(AbstractCreature target){
        if (target instanceof AbstractPlayer){
            return 4;
        }
        else if(target instanceof AbstractMonster) {
            if (AbstractDungeon.floorNum <= 20) {
                switch (((AbstractMonster) target).type){
                    case NORMAL:
                        return 0;
                    case ELITE:
                        return 1;
                    case BOSS:
                        return 2;
                }
            }
            else if (AbstractDungeon.floorNum <= 40) {
                switch (((AbstractMonster) target).type){
                    case NORMAL:
                        return 1;
                    case ELITE:
                        return 2;
                    case BOSS:
                        return 3;
                }
            }
            else {
                switch (((AbstractMonster) target).type){
                    case NORMAL:
                        return 2;
                    case ELITE:
                        return 3;
                    case BOSS:
                        return 4;
                }
            }
        }
        return 4;
    }

    private boolean getEnoughPE(){
        if(AbstractDungeon.player.hasRelic(CogitoBucket.ID)){
            return AbstractDungeon.player.getRelic(CogitoBucket.ID).counter >= codeCost[renderCode];
        }
        return false;
    }

    public void dispose(){

    }

    @SpirePatch(
            clz= GameCursor.class,
            method="render"
    )
    public static class render {
        @SpireInsertPatch(rloc=4)
        public static void Insert(GameCursor _inst, SpriteBatch sb) throws NoSuchFieldException, IllegalAccessException {
            if (!Settings.isTouchScreen)
            {
                Field type = _inst.getClass().getDeclaredField("type");
                type.setAccessible(true);

                if(type.get(_inst) == WantedRequestEffect.WANTED){
                    sb.setColor(Color.WHITE);
                    sb.draw(LobExtendImageMaster.WANTED_CURSOR, InputHelper.mX - 128.0F, InputHelper.mY - 128.0F, 128.0F, 128.0F, 256.0F, 256.0F, Settings.scale, Settings.scale, 0, 0, 0, 256, 256, false, false);
                    FontHelper.renderSmartText(sb, LobExtendFontHelper.WantedFont, "WANTED", InputHelper.mX - 44.0F, InputHelper.mY + 66.0F, Settings.WIDTH,
                            0.0F, WantedRequestEffect.color);
                    if(WantedRequestEffect.renderInfo){
                        String text = "", text2 = WantedRequestEffect.codeCost[WantedRequestEffect.renderCode] + "";
                        BitmapFont font = LobExtendFontHelper.WantedFont;
                        switch (WantedRequestEffect.renderCode){
                            case 0:
                                text = "ZAYIN";
                                font = LobExtendFontHelper.WantedFont_2;
                                break;
                            case 1:
                                text = "TETH";
                                break;
                            case 2:
                                text = "HE";
                                break;
                            case 3:
                                text = "WAW";
                                break;
                            case 4:
                                text = "ALEPH";
                                font = LobExtendFontHelper.WantedFont_2;
                                break;
                        }
                        FontHelper.renderSmartText(sb, font, text, InputHelper.mX - 48.0F, InputHelper.mY - 46, Settings.WIDTH,
                                0.0F, WantedRequestEffect.code[WantedRequestEffect.renderCode]);
                        FontHelper.renderSmartText(sb, font, text2, InputHelper.mX + 12.0F, InputHelper.mY - 46, Settings.WIDTH,
                                0.0F, (WantedRequestEffect.enoughPE? WantedRequestEffect.color: WantedRequestEffect.color2));
                    }
                }
            }
        }
    }
}
