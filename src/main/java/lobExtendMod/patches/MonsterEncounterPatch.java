package lobExtendMod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import lobExtendMod.event.isolate.BurrowingHeaven;
import lobotomyMod.monster.WhiteNightMonster;

/**
 * @author hoykj
 */
@SpirePatch(cls="com.megacrit.cardcrawl.helpers.MonsterHelper", method="getEncounter")
public class MonsterEncounterPatch {
    public static MonsterGroup Postfix(MonsterGroup __result, String key) {
        switch (key) {
            case "WhiteNightMonster":
                return new MonsterGroup(new AbstractMonster[]{new WhiteNightMonster(0.0F, 0.0F)});
        }
        return __result;
    }
}
