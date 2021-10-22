package lobExtendMod.npc.event;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import lobExtendMod.event.isolate.AbstractIsolate;

/**
 * @author hoykj
 */
public abstract class AbstractActionNpc {
    public boolean remove;

    public void onEnterRoom(AbstractIsolate isolate){
    }

    public void onCheck(AbstractIsolate isolate){
    }

    public void onAction(AbstractIsolate isolate){
    }

    public void onLeave(AbstractIsolate isolate){
    }

    public void update(AbstractIsolate isolate){
    }

    public void render(SpriteBatch sb, AbstractIsolate isolate){
    }

    public void dispose() {
    }
}
