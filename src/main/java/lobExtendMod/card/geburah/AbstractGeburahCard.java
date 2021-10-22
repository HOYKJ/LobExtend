package lobExtendMod.card.geburah;

import basemod.abstracts.CustomCard;
import lobotomyMod.character.LobotomyHandler;

/**
 * @author hoykj
 */
public abstract class AbstractGeburahCard extends CustomCard {

    public AbstractGeburahCard(String id, String name, int cost, String description, CardType type, CardTarget target) {
        super(id, name, getCardImage(id), cost, description, type, CardColor.COLORLESS, CardRarity.RARE, target);
    }

    private static String getCardImage(String id) {
        return "lobExtendMod/images/cards/geburah/" + id + ".png";
    }
}
