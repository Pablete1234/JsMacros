package xyz.wagyourtail.jsmacros.fabric.client.mixins.fix;

import net.caffeinemc.mods.sodium.client.gui.Dimensioned;
import net.caffeinemc.mods.sodium.client.gui.VideoSettingsScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import xyz.wagyourtail.jsmacros.client.api.classes.render.IDraw2D;
import xyz.wagyourtail.jsmacros.client.mixin.access.MixinScreen;

/**
 * Sodium's VideoSettingsScreen uses getWidth/getHeight as the "inner" width/height. This means
 * {@link MixinScreen#getWidth()} breaks sodium because it will mix-in and replace sodium's expected
 * inner-width-getter with a full-width-getter.
 * {@link MixinScreen} has that getter so that it implements {@link IDraw2D#getWidth()}, and prefixing
 * it is problematic as its part of the scripting api, so it would break any scripts using the method.
 * <br>
 * For the time being, simplest solution seems to be to mixin to sodium to restore original behavior.
 */
@SuppressWarnings("ALL")
@Pseudo
@Mixin(value = VideoSettingsScreen.class, priority = 2000)
public abstract class MixinSodiumVideoSettingsScreen {
    public int getWidth() {
        return ((Dimensioned) (this)).getDimensions().width();
    }
    public int getHeight() {
        return ((Dimensioned) (this)).getDimensions().height();
    }
}
