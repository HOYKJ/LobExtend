package lobExtendMod.monster;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.MonsterStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import lobExtendMod.action.DamageAllAction;
import lobExtendMod.npc.event.HeavenNpc;
import lobExtendMod.relic.Heaven_r;
import lobExtendMod.vfx.GainRelicEffect;

/**
 * @author hoykj
 */
public class Heaven_m extends AbstractMonster {
    public static final String ID = "Heaven_m";
    private static final MonsterStrings monsterStrings = CardCrawlGame.languagePack.getMonsterStrings("Heaven_m");
    public static final String NAME = monsterStrings.NAME;
    public static final String[] MOVES = monsterStrings.MOVES;
    public static final String[] DIALOG = monsterStrings.DIALOG;
    private HeavenNpc npc;

    public Heaven_m(float x, float y, HeavenNpc npc) {
        super(NAME, "Heaven_m", 80, 0.0F, 0.0F, 700.0F, 500.0F, null, x, y);
        loadAnimation("lobExtendMod/images/monsters/BurrowingHeaven/mustLook.atlas", "lobExtendMod/images/monsters/BurrowingHeaven/mustLook.json", 1.2F);
        AnimationState.TrackEntry e = this.state.setAnimation(0, "Change", true);
        e.setTime(e.getEndTime() * MathUtils.random());
        this.stateData.setMix("Change", "Dead", 0.1F);
        this.damage.add(new DamageInfo(this, 1, DamageInfo.DamageType.THORNS));
        this.npc = npc;
    }

    protected void getMove(int num) {
        setMove((byte) 1, Intent.ATTACK, this.damage.get(0).base, 25, true);
    }

    public void takeTurn() {
        if(this.nextMove == 1){
            for (int i = 0; i < 25; i ++) {
                AbstractGameAction.AttackEffect effect;
                switch (MathUtils.random(2)){
                    case 0:
                        effect = AbstractGameAction.AttackEffect.SLASH_DIAGONAL;
                        break;
                    case 1:
                        effect = AbstractGameAction.AttackEffect.SLASH_VERTICAL;
                        break;
                    default:
                        effect = AbstractGameAction.AttackEffect.SLASH_HORIZONTAL;
                        break;
                }
                AbstractDungeon.actionManager.addToBottom(new DamageAllAction(this.damage.get(0), effect));
            }
        }
        AbstractDungeon.actionManager.addToBottom(new RollMoveAction(this));
    }

    public void die() {
        super.die();
        this.changeState("DIE");
        this.deathTimer += 1.5F;
        this.npc.remove = true;
        if (!AbstractDungeon.player.hasRelic(Heaven_r.ID)) {
            AbstractDungeon.effectsQueue.add(new GainRelicEffect(this.hb.cX, this.hb.cY, Heaven_r.ID));
        }
    }

    public void changeState(String key)
    {
        switch (key)
        {
            case "DIE":
                this.state.setAnimation(0, "Dead", false);
                this.state.setTimeScale(1F);
                break;
        }
    }
}
