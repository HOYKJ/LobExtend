package lobExtendMod.relic;

import basemod.helpers.SuperclassFinder;
import com.badlogic.gdx.utils.Pool;
import com.esotericsoftware.spine.Animation;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import lobExtendMod.card.geburah.*;
import lobotomyMod.character.LobotomyHandler;
import lobotomyMod.relic.AbstractLobotomyRelic;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author hoykj
 */
public class RedMistMask extends AbstractLobotomyRelic {
    public static final String ID = "RedMistMask";
    private boolean changed;
    public int state;

    public RedMistMask() {
        super("RedMistMask",  RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public void onEquip() {
        super.onEquip();
        try {
            Method loadAnimation = SuperclassFinder.getSuperClassMethod(AbstractDungeon.player.getClass(), "loadAnimation", String.class, String.class, float.class);
            loadAnimation.setAccessible(true);
            loadAnimation.invoke(AbstractDungeon.player, "lobotomyMod/images/monsters/Sephirah/Geburah/gebura.atlas", "lobotomyMod/images/monsters/Sephirah/Geburah/gebura.json", 3.2F);
            AbstractDungeon.player.state.setAnimation(0, "Phase_01_Default", true);
            AbstractDungeon.player.flipHorizontal = true;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        this.state = 0;
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
        this.state = 0;
        AbstractDungeon.player.state.setAnimation(0, "Phase_01_Default", true);
    }

    @Override
    public void atBattleStart() {
        super.atBattleStart();
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new RedEyes_geburah(), 1, true, true));
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new Penitence_geburah(), 1, true, true));
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDrawPileAction(new GoldRush_geburah(), 1, true, true));
        AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new LimitBreak_geburah(), 1));
    }

    @Override
    public void update() {
        super.update();
        if (AbstractDungeon.player == null){
            return;
        }
        if (!AbstractDungeon.player.hasRelic(this.relicId)){
            return;
        }
        if (AbstractDungeon.player.isDead){
            return;
        }
        if (!this.changed){
            this.changed = true;
            try {
                Method loadAnimation = SuperclassFinder.getSuperClassMethod(AbstractDungeon.player.getClass(), "loadAnimation", String.class, String.class, float.class);
                loadAnimation.setAccessible(true);
                loadAnimation.invoke(AbstractDungeon.player, "lobotomyMod/images/monsters/Sephirah/Geburah/gebura.atlas", "lobotomyMod/images/monsters/Sephirah/Geburah/gebura.json", 3.2F);
                AbstractDungeon.player.state.setAnimation(0, "Phase_01_Default", true);
                AbstractDungeon.player.flipHorizontal = true;
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        AbstractDungeon.player.flipHorizontal = true;
    }

    public void changeState(){
        this.state ++;
        switch (this.state){
            case 1:
                AbstractDungeon.player.state.setAnimation(0, "Phase_02_Default", true);
                ImageMaster.loadImage(LobotomyHandler.lobotomyRelicImage("RedMistMask_1"));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new LimitBreak_geburah(), 1));
                for (int i = 0; i < AbstractDungeon.player.drawPile.group.size(); i ++){
                    if (AbstractDungeon.player.drawPile.group.get(i) instanceof RedEyes_geburah){
                        AbstractDungeon.player.drawPile.group.set(i, new Mimicry_geburah());
                    }
                    else if (AbstractDungeon.player.drawPile.group.get(i) instanceof Penitence_geburah){
                        AbstractDungeon.player.drawPile.group.set(i, new DaCapo_geburah());
                    }
                    else if (AbstractDungeon.player.drawPile.group.get(i) instanceof GoldRush_geburah){
                        AbstractDungeon.player.drawPile.group.set(i, new Heaven_geburah());
                    }
                }
                for (int i = 0; i < AbstractDungeon.player.hand.group.size(); i ++){
                    if (AbstractDungeon.player.hand.group.get(i) instanceof RedEyes_geburah){
                        AbstractDungeon.player.hand.group.set(i, new Mimicry_geburah());
                    }
                    else if (AbstractDungeon.player.hand.group.get(i) instanceof Penitence_geburah){
                        AbstractDungeon.player.hand.group.set(i, new DaCapo_geburah());
                    }
                    else if (AbstractDungeon.player.hand.group.get(i) instanceof GoldRush_geburah){
                        AbstractDungeon.player.hand.group.set(i, new Heaven_geburah());
                    }
                }
                for (int i = 0; i < AbstractDungeon.player.discardPile.group.size(); i ++){
                    if (AbstractDungeon.player.discardPile.group.get(i) instanceof RedEyes_geburah){
                        AbstractDungeon.player.discardPile.group.set(i, new Mimicry_geburah());
                    }
                    else if (AbstractDungeon.player.discardPile.group.get(i) instanceof Penitence_geburah){
                        AbstractDungeon.player.discardPile.group.set(i, new DaCapo_geburah());
                    }
                    else if (AbstractDungeon.player.discardPile.group.get(i) instanceof GoldRush_geburah){
                        AbstractDungeon.player.discardPile.group.set(i, new Heaven_geburah());
                    }
                }
                break;
            case 2:
                AbstractDungeon.player.state.setAnimation(0, "Phase_02 to 03", false);
                AbstractDungeon.player.state.addAnimation(0, "Phase_03_Default", true, 0.0F);
                ImageMaster.loadImage(LobotomyHandler.lobotomyRelicImage("RedMistMask_2"));
                AbstractDungeon.actionManager.addToBottom(new MakeTempCardInDiscardAction(new LimitBreak_geburah(), 1));
                for (int i = 0; i < AbstractDungeon.player.drawPile.group.size(); i ++){
                    if (AbstractDungeon.player.drawPile.group.get(i) instanceof Mimicry_geburah){
                        AbstractDungeon.player.drawPile.group.set(i, new BlackSmile_geburah());
                    }
                    else if (AbstractDungeon.player.drawPile.group.get(i) instanceof DaCapo_geburah){
                        AbstractDungeon.player.drawPile.group.set(i, new Justitia_geburah());
                    }
                }
                for (int i = 0; i < AbstractDungeon.player.hand.group.size(); i ++){
                    if (AbstractDungeon.player.hand.group.get(i) instanceof Mimicry_geburah){
                        AbstractDungeon.player.hand.group.set(i, new BlackSmile_geburah());
                    }
                    else if (AbstractDungeon.player.hand.group.get(i) instanceof DaCapo_geburah){
                        AbstractDungeon.player.hand.group.set(i, new Justitia_geburah());
                    }
                }
                for (int i = 0; i < AbstractDungeon.player.discardPile.group.size(); i ++){
                    if (AbstractDungeon.player.discardPile.group.get(i) instanceof Mimicry_geburah){
                        AbstractDungeon.player.discardPile.group.set(i, new BlackSmile_geburah());
                    }
                    else if (AbstractDungeon.player.discardPile.group.get(i) instanceof DaCapo_geburah){
                        AbstractDungeon.player.discardPile.group.set(i, new Justitia_geburah());
                    }
                }
                break;
            case 3:
                AbstractDungeon.player.state.setAnimation(0, "Phase_03 to 04", false);
                AbstractDungeon.player.state.addAnimation(0, "Phase_04_Default", true, 0.0F);
                ImageMaster.loadImage(LobotomyHandler.lobotomyRelicImage("RedMistMask_3"));
                for (int i = 0; i < AbstractDungeon.player.drawPile.group.size(); i ++){
                    if (AbstractDungeon.player.drawPile.group.get(i) instanceof BlackSmile_geburah){
                        AbstractDungeon.player.drawPile.group.set(i, new Execute_geburah());
                    }
                    else if (AbstractDungeon.player.drawPile.group.get(i) instanceof Justitia_geburah){
                        AbstractDungeon.player.drawPile.group.set(i, new TheMovementOfCalm_geburah());
                    }
                    else if (AbstractDungeon.player.drawPile.group.get(i) instanceof Heaven_geburah){
                        AbstractDungeon.player.drawPile.group.set(i, new BloodyFogOnly_geburah());
                    }
                }
                for (int i = 0; i < AbstractDungeon.player.hand.group.size(); i ++){
                    if (AbstractDungeon.player.hand.group.get(i) instanceof BlackSmile_geburah){
                        AbstractDungeon.player.hand.group.set(i, new Execute_geburah());
                    }
                    else if (AbstractDungeon.player.hand.group.get(i) instanceof Justitia_geburah){
                        AbstractDungeon.player.hand.group.set(i, new TheMovementOfCalm_geburah());
                    }
                    else if (AbstractDungeon.player.hand.group.get(i) instanceof Heaven_geburah){
                        AbstractDungeon.player.hand.group.set(i, new BloodyFogOnly_geburah());
                    }
                }
                for (int i = 0; i < AbstractDungeon.player.discardPile.group.size(); i ++){
                    if (AbstractDungeon.player.discardPile.group.get(i) instanceof BlackSmile_geburah){
                        AbstractDungeon.player.discardPile.group.set(i, new Execute_geburah());
                    }
                    else if (AbstractDungeon.player.discardPile.group.get(i) instanceof Justitia_geburah){
                        AbstractDungeon.player.discardPile.group.set(i, new TheMovementOfCalm_geburah());
                    }
                    else if (AbstractDungeon.player.discardPile.group.get(i) instanceof Heaven_geburah){
                        AbstractDungeon.player.discardPile.group.set(i, new BloodyFogOnly_geburah());
                    }
                }
                break;
        }
    }

    public String getUpdatedDescription() {
        return this.DESCRIPTIONS[0];
    }

    public AbstractRelic makeCopy() {
        return new RedMistMask();
    }

    @SpirePatch(
            clz= AbstractCreature.class,
            method="loadAnimation"
    )
    public static class loadAnimation {
        @SpireInsertPatch(rloc = 0)
        public static SpireReturn Insert(AbstractCreature _inst, String atlasUrl, String skeletonUrl, float scale){
            if (_inst instanceof AbstractPlayer && ((AbstractPlayer) _inst).hasRelic(RedMistMask.ID) && !atlasUrl.equals("lobotomyMod/images/monsters/Sephirah/Geburah/gebura.atlas")){
                try {
                    Method loadAnimation = SuperclassFinder.getSuperClassMethod(AbstractDungeon.player.getClass(), "loadAnimation", String.class, String.class, float.class);
                    loadAnimation.setAccessible(true);
                    loadAnimation.invoke(AbstractDungeon.player, "lobotomyMod/images/monsters/Sephirah/Geburah/gebura.atlas", "lobotomyMod/images/monsters/Sephirah/Geburah/gebura.json", 3.2F);
                    AbstractDungeon.player.state.setAnimation(0, "Phase_01_Default", true);
                    AbstractDungeon.player.flipHorizontal = true;
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                return SpireReturn.Return(null);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz= AbstractPlayer.class,
            method="playDeathAnimation"
    )
    public static class playDeathAnimation {
        @SpireInsertPatch(rloc = 0)
        public static void Insert(AbstractPlayer _inst){
            if (AbstractDungeon.player.hasRelic(RedMistMask.ID)) {
                _inst.corpseImg = ImageMaster.loadImage("lobExtendMod/images/character/corpse.png");
                _inst.drawY -= 130;
                _inst.flipHorizontal = false;
            }
        }
    }

    @SpirePatch(
            clz= AnimationState.class,
            method="setAnimation",
            paramtypez = {
                    int.class,
                    String.class,
                    boolean.class
            }
    )
    public static class setAnimation {
        @SpireInsertPatch(rloc = 0)
        public static SpireReturn Insert(AnimationState _inst, int trackIndex, String animationName, boolean loop){
            try {
                Field data = SuperclassFinder.getSuperclassField(_inst.getClass(), "data");
                Field trackEntryPool = SuperclassFinder.getSuperclassField(_inst.getClass(), "trackEntryPool");
                data.setAccessible(true);
                trackEntryPool.setAccessible(true);
                Animation animation = ((AnimationStateData)data.get(_inst)).getSkeletonData().findAnimation(animationName);
                if (animation == null){
                    return SpireReturn.Return(((Pool<AnimationState.TrackEntry>)trackEntryPool.get(_inst)).obtain());
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(
            clz= AnimationState.class,
            method="addAnimation",
            paramtypez = {
                    int.class,
                    String.class,
                    boolean.class,
                    float.class
            }
    )
    public static class addAnimation {
        @SpireInsertPatch(rloc = 0)
        public static SpireReturn Insert(AnimationState _inst, int trackIndex, String animationName, boolean loop, float delay){
            try {
                Field data = SuperclassFinder.getSuperclassField(_inst.getClass(), "data");
                Field trackEntryPool = SuperclassFinder.getSuperclassField(_inst.getClass(), "trackEntryPool");
                data.setAccessible(true);
                trackEntryPool.setAccessible(true);
                Animation animation = ((AnimationStateData)data.get(_inst)).getSkeletonData().findAnimation(animationName);
                if (animation == null){
                    return SpireReturn.Return(((Pool<AnimationState.TrackEntry>)trackEntryPool.get(_inst)).obtain());
                }
            } catch (IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
            return SpireReturn.Continue();
        }
    }
}
