package lobExtendMod.action;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import lobotomyMod.monster.friendlyMonster.AbstractFriendlyMonster;

import java.util.Iterator;

/**
 * @author hoykj
 */
public class DamageAllAction extends AbstractGameAction {
    public int[] damage;
    private int baseDamage;
    private boolean firstFrame;

    public DamageAllAction(DamageInfo damageInfo, AttackEffect effect) {
        this.source = damageInfo.owner;
        this.firstFrame = true;
        this.baseDamage = damageInfo.base;
        this.actionType = ActionType.DAMAGE;
        this.damageType = damageInfo.type;
        this.attackEffect = effect;
        this.duration = 0;
    }

    public DamageAllAction(AbstractCreature source, int baseDamage, DamageInfo.DamageType type, AttackEffect effect) {
        this.source = source;
        this.firstFrame = true;
        this.baseDamage = baseDamage;
        this.actionType = ActionType.DAMAGE;
        this.damageType = type;
        this.attackEffect = effect;
        this.duration = 0;
    }

    public void update() {
        if (this.firstFrame) {
            boolean playedMusic = false;

            AbstractDungeon.effectList.add(new FlashAtkImgEffect(AbstractDungeon.player.hb.cX, AbstractDungeon.player.hb.cY, this.attackEffect));
            if (this.source instanceof AbstractFriendlyMonster && ((AbstractFriendlyMonster) this.source).identifier != null){
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (!m.isDeadOrEscaped() && (!(m instanceof AbstractFriendlyMonster) || ((AbstractFriendlyMonster) m).identifier == null
                            || !((AbstractFriendlyMonster) m).identifier.equals(((AbstractFriendlyMonster) this.source).identifier))) {
                        if (playedMusic) {
                            AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, this.attackEffect, true));
                        } else {
                            playedMusic = true;
                            AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, this.attackEffect));
                        }
                    }
                }
            }
            else {
                for (AbstractMonster m : AbstractDungeon.getMonsters().monsters) {
                    if (!m.isDeadOrEscaped() && m != this.source) {
                        if (playedMusic) {
                            AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, this.attackEffect, true));
                        } else {
                            playedMusic = true;
                            AbstractDungeon.effectList.add(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, this.attackEffect));
                        }
                    }
                }
            }

            this.firstFrame = false;
        }

        this.tickDuration();
        if (this.isDone) {
            if (this.attackEffect == AttackEffect.POISON) {
                AbstractDungeon.player.tint.color.set(Color.CHARTREUSE);
                AbstractDungeon.player.tint.changeColor(Color.WHITE.cpy());
            } else if (this.attackEffect == AttackEffect.FIRE) {
                AbstractDungeon.player.tint.color.set(Color.RED);
                AbstractDungeon.player.tint.changeColor(Color.WHITE.cpy());
            }

            AbstractDungeon.player.damage(new DamageInfo(this.source, this.baseDamage, this.damageType));

            if (this.source instanceof AbstractFriendlyMonster && ((AbstractFriendlyMonster) this.source).identifier != null){
                for (int i = 0; i < AbstractDungeon.getMonsters().monsters.size(); i ++) {
                    AbstractMonster m = AbstractDungeon.getMonsters().monsters.get(i);
                    if (!m.isDeadOrEscaped() && (!(m instanceof AbstractFriendlyMonster) || ((AbstractFriendlyMonster) m).identifier == null
                            || !((AbstractFriendlyMonster) m).identifier.equals(((AbstractFriendlyMonster) this.source).identifier))) {
                        if (this.attackEffect == AttackEffect.POISON) {
                            m.tint.color.set(Color.CHARTREUSE);
                            m.tint.changeColor(Color.WHITE.cpy());
                        } else if (this.attackEffect == AttackEffect.FIRE) {
                            m.tint.color.set(Color.RED);
                            m.tint.changeColor(Color.WHITE.cpy());
                        }

                        m.damage(new DamageInfo(this.source, this.baseDamage, this.damageType));
                    }
                }
            }
            else {
                for (int i = 0; i < AbstractDungeon.getMonsters().monsters.size(); i ++) {
                    AbstractMonster m = AbstractDungeon.getMonsters().monsters.get(i);
                    if (!m.isDeadOrEscaped() && m != this.source) {
                        if (this.attackEffect == AttackEffect.POISON) {
                            m.tint.color.set(Color.CHARTREUSE);
                            m.tint.changeColor(Color.WHITE.cpy());
                        } else if (this.attackEffect == AttackEffect.FIRE) {
                            m.tint.color.set(Color.RED);
                            m.tint.changeColor(Color.WHITE.cpy());
                        }

                        m.damage(new DamageInfo(this.source, this.baseDamage, this.damageType));
                    }
                }
            }

            if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
                AbstractDungeon.actionManager.clearPostCombatActions();
            }

//            if (!Settings.FAST_MODE) {
//                this.addToTop(new WaitAction(0.1F));
//            }
        }

    }
}
