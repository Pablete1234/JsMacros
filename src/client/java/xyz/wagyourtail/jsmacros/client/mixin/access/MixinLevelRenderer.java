package xyz.wagyourtail.jsmacros.client.mixin.access;

import com.google.common.collect.ImmutableSet;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.state.level.LevelRenderState;
import net.minecraft.util.profiling.Profiler;
import net.minecraft.util.profiling.ProfilerFiller;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.wagyourtail.jsmacros.client.JsMacros;
import xyz.wagyourtail.jsmacros.client.api.classes.render.Draw3D;
import xyz.wagyourtail.jsmacros.client.api.library.impl.FHud;

@Mixin(LevelRenderer.class)
public class MixinLevelRenderer {

    @Inject(method = "submitFeatures",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;finalizeGizmoCollection()V"))
    private void submitJsMacrosDraw3D(
            LevelRenderState levelRenderState,
            SubmitNodeCollector submitNodeCollector,
            boolean renderOutline,
            CallbackInfo ci) {
        ProfilerFiller profiler = Profiler.get();
        try {
            profiler.push("jsmacros_d3d");

            float tickDelta = Minecraft.getInstance().getDeltaTracker().getGameTimeDeltaPartialTick(true);
            PoseStack matrixStack = new PoseStack();
            for (Draw3D d : ImmutableSet.copyOf(FHud.renders)) {
                d.render(matrixStack, submitNodeCollector, tickDelta);
            }
        } catch (Throwable e) {
            JsMacros.LOGGER.error("Draw3D render error", e);
        } finally {
            profiler.pop();
        }
    }

}