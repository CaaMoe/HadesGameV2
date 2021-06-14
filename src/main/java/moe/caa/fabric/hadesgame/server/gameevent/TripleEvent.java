package moe.caa.fabric.hadesgame.server.gameevent;

import moe.caa.fabric.hadesgame.server.GameCore;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;
import java.util.List;

public class TripleEvent extends ImplicitAbstractEvent {
    public TripleEvent() {
        super("tripleEvent", "三倍事件", true, 30, 100);
    }

    @Override
    public void callEvent() {
        List<AbstractEvent> triple = new ArrayList<>();
        triple.add(GameCore.INSTANCE.eventList.randomGet());
        triple.add(GameCore.INSTANCE.eventList.randomGet());
        triple.add(GameCore.INSTANCE.eventList.randomGet());
        GameCore.INSTANCE.allPlayerHandler(player->{
            player.sendMessage(new LiteralText("选中的三倍事件："), false);
            player.sendMessage(new LiteralText(triple.get(0).EVENT_NAME), false);
            player.sendMessage(new LiteralText(triple.get(1).EVENT_NAME), false);
            player.sendMessage(new LiteralText(triple.get(2).EVENT_NAME), false);
        });
        triple.forEach(AbstractEvent::callEvent);
    }
}
