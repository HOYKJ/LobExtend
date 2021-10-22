package lobExtendMod.monster;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import lobExtendMod.LobExtendMod;
import lobExtendMod.action.DamageAllAction;
import lobExtendMod.relic.Justitia_r;
import lobExtendMod.relic.SolemnVow_r;
import lobExtendMod.relic.SwordSharpenedByTears_r;
import lobExtendMod.vfx.ButterflyAttackEffect;
import lobExtendMod.vfx.ButterflyCastRange;
import lobExtendMod.vfx.GainRelicEffect;
import lobotomyMod.action.common.LatterAction;
import lobotomyMod.card.uncommonCard.Funeral;
import lobotomyMod.card.uncommonCard.LongBird;
import lobotomyMod.monster.Ordeal.AbstractOrdealMonster;
import lobotomyMod.monster.Ordeal.Machine.MachineNight;
import lobotomyMod.monster.friendlyMonster.AbstractFriendlyMonster;
import lobotomyMod.vfx.action.LatterEffect;
import lobotomyMod.vfx.ordeal.OrdealTitleBack;

/**
 * @author hoykj
 */
public class LongBird_m extends AbstractFriendlyMonster {
    public static final String ID = "LongBird_m";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("LongBird_m");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private boolean cast;
    private boolean walk, moveRight;

    public LongBird_m(float x, float y) {
        super(NAME, "LongBird_m", 80, 0.0F, 0.0F, 240.0F, 440.0F, null, x, y);
        loadAnimation("lobExtendMod/images/monsters/LongBird/longbird.atlas", "lobExtendMod/images/monsters/LongBird/longbird.json", 2.6F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Standing", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("Standing", "Walk", 0.1F);
        this.stateData.setMix("Standing", "Seeing", 0.1F);
        this.stateData.setMix("Standing", "Judgement", 0.1F);
        this.stateData.setMix("Walk", "Seeing", 0.1F);
        this.stateData.setMix("Walk", "Judgement", 0.1F);
        this.moveRight = true;
        this.walk = false;
        this.cast = false;
    }

    protected void getMove(int num) {
        if(!this.cast) {
            setMove((byte) 1, Intent.UNKNOWN);
        }
        else {
            setMove((byte) 2, Intent.ATTACK_DEBUFF, 15);
        }
    }

    public void takeTurn() {
        if(this.nextMove == 1){
            AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "CAST"));
            AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                this.walk = false;
            }));
            this.cast = true;
        }
        else if(this.nextMove == 2){
            AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK"));
            AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                this.walk = false;
            }));
            AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                CardCrawlGame.sound.play("LongBird_Stun");
                AbstractDungeon.actionManager.addToTop(new DamageAllAction(this, 15, DamageInfo.DamageType.HP_LOSS, AbstractGameAction.AttackEffect.NONE));
            }, 4.8F));
            this.cast = false;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    @Override
    public void update() {
        super.update();
        float moveSpeed = 100.0F;
        if(this.walk){
            if(this.moveRight){
                this.drawX += Gdx.graphics.getDeltaTime() * moveSpeed;
                if(this.drawX + this.hb.width / 2 > Settings.WIDTH - 100){
                    this.moveRight = false;
                }
            }
            else {
                this.drawX -= Gdx.graphics.getDeltaTime() * moveSpeed;
                if(this.drawX - this.hb.width / 2 < 100){
                    this.moveRight = true;
                }
            }
            this.flipHorizontal = !this.moveRight;
        }
        if (!this.isDying && this.state.getTracks().get(0).getAnimation().getName().equals("Standing")){
            if(!this.walk){
                this.walk = true;
                this.changeState("WALK");
            }
            else{
                this.changeState("WALK");
            }
        }
    }

    public void die() {
        super.die();
        AbstractDungeon.effectsQueue.add(new GainRelicEffect(this.hb.cX, this.hb.cY, Justitia_r.ID));
        AbstractDungeon.player.masterDeck.addToBottom(new LongBird());
    }

    public void changeState(String key)
    {
        switch (key)
        {
            case "CAST":
                this.state.setAnimation(0, "Seeing", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Standing", true, 0.0F);
                break;
            case "ATTACK":
                this.state.setAnimation(0, "Judgement", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Standing", true, 0.0F);
                break;
            case "WALK":
                this.state.setAnimation(0, "Walk", true);
                this.state.setTimeScale(1F);
                break;
            case "STAND":
                this.state.setAnimation(0, "Standing", true);
                this.state.setTimeScale(1F);
                break;
        }
    }
}
