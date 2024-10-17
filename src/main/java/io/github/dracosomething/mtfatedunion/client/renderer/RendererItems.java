package io.github.dracosomething.mtfatedunion.client.renderer;

import io.github.dracosomething.mtfatedunion.client.renderer.item.GaeBolgRenderer;
import io.github.dracosomething.mtfatedunion.client.renderer.item.GaeMorganRenderer;
import net.minecraftforge.client.event.ModelEvent;
import stepsword.mahoutsukai.render.item.*;

public class RendererItems {
    public RendererItems(){
    }

    public static void registerOtherModels(ModelEvent.RegisterAdditional event) {
        event.register(GaeBolgRenderer.gae_bolg);
        event.register(GaeMorganRenderer.gae_bolg_morgan);
    }
}
