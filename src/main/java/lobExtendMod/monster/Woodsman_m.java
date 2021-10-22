package lobExtendMod.monster;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ChangeStateAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lobExtendMod.action.DamageAllAction;
import lobExtendMod.relic.Logging_r;
import lobExtendMod.vfx.GainRelicEffect;
import lobotomyMod.action.common.LatterAction;
import lobotomyMod.card.uncommonCard.Woodsman;
import lobotomyMod.monster.friendlyMonster.AbstractFriendlyMonster;

/**
 * @author hoykj
 */
public class Woodsman_m extends AbstractFriendlyMonster {
    public static final String ID = "Woodsman_m";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Woodsman_m");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;

    public Woodsman_m(float x, float y, AbstractMonster monster) {
        super(NAME, "Woodsman_m", 43, 0.0F, -4.0F, 400.0F, 460.0F, null, x, y);
        loadAnimation("lobExtendMod/images/monsters/Woodsman/Lumberjack.atlas", "lobExtendMod/images/monsters/Woodsman/Lumberjack.json", 1.4F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Room_Default", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("Room_Default", "Room_Special", 0.1F);
        this.stateData.setMix("Escape_Default", "Escape_Special", 0.1F);
        this.stateData.setMix("Escape_Default", "Escape_Attack_01", 0.1F);
        this.stateData.setMix("Escape_Default", "Escape_Attack_02", 0.1F);
        this.stateData.setMix("Escape_Default", "Escape_Dead", 0.1F);
        this.damage.add(new DamageInfo(this, 10));
        this.damage.add(new DamageInfo(this, 4));
        this.drawX = x;
        this.drawY = y;
        this.target = monster;
        this.currentHealth = 23;
        this.escape_room();
    }

    protected void getMove(int num) {
        if (num >= 30){
            setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base);
        }
        else {
            setMove((byte) 2, Intent.ATTACK, this.damage.get(1).base, 7, true);
        }
        this.randomTarget();
    }

    public void takeTurn() {
        switch (this.nextMove){
            case 1:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK1"));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(0), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                }, 1.0F));
                break;
            case 2:
                AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ATTACK2"));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                }, 1.8F));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                }, 0.2F));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                }, 0.2F));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                }, 0.2F));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                }, 0.2F));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_VERTICAL));
                }, 0.2F));
                AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
                    AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(1), AbstractGameAction.AttackEffect.SLASH_HEAVY));
                }, 0.6F));
                break;
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    private void escape_room(){
        AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "ESCAPE"));
        AbstractDungeon.actionManager.addToBottom(new LatterAction(()->{
            AbstractDungeon.actionManager.addToTop(new DamageAction(this.target, this.damage.get(0), AbstractGameAction.AttackEffect.NONE));
        }, 0.6F));
    }

    @Override
    public void update() {
        if (this.target == null || this.target.isDeadOrEscaped() || this.target == this) {
            if (this.target != null && this.target.isDying){
                AbstractCreature tmp = this.target;
                AbstractDungeon.actionManager.addToBottom(new LatterAction(() -> {
                    for (int i = 0; i < AbstractDungeon.getMonsters().monsters.size(); i ++){
                        if (AbstractDungeon.getMonsters().monsters.get(i) == tmp){
                            AbstractDungeon.getMonsters().monsters.remove(i);
                            break;
                        }
                    }
                    AbstractDungeon.actionManager.addToBottom(new ChangeStateAction(this, "HEAL"));
                    AbstractDungeon.actionManager.addToBottom(new LatterAction(() -> {
                        AbstractDungeon.actionManager.addToBottom(new HealAction(this, this, 20));
                    }, 2.8F));
                }, 0.1F));
            }
            if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                this.randomTarget();
            }
        }
        super.update();
    }

    public void die() {
        super.die();
        this.deathTimer += 2.7F;
        this.changeState("DIE");
        AbstractDungeon.effectsQueue.add(new GainRelicEffect(this.hb.cX, this.hb.cY, Logging_r.ID));
        AbstractDungeon.player.masterDeck.addToBottom(new Woodsman());
    }

    public void changeState(String key)
    {
        switch (key)
        {
            case "ESCAPE":
                this.state.setAnimation(0, "Room_Special", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Escape_Default", true, 0.0F);
                break;
            case "ATTACK1":
                this.state.setAnimation(0, "Escape_Attack_02", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Escape_Default", true, 0.0F);
                break;
            case "ATTACK2":
                this.state.setAnimation(0, "Escape_Attack_01", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Escape_Default", true, 0.0F);
                break;
            case "HEAL":
                this.state.setAnimation(0, "Escape_Special", false);
                this.state.setTimeScale(1F);
                this.state.addAnimation(0, "Escape_Default", true, 0.0F);
                break;
            case "DIE":
                this.state.setAnimation(0, "Escape_Dead", false);
                this.state.setTimeScale(1F);
                break;
        }
    }
}
