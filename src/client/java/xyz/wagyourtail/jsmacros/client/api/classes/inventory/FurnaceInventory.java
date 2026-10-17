package xyz.wagyourtail.jsmacros.client.api.classes.inventory;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import xyz.wagyourtail.jsmacros.client.api.helper.inventory.ItemStackHelper;

import java.util.Map;

/**
 * @author Etheradon
 * @since 1.8.4
 */
@SuppressWarnings("unused")
public class FurnaceInventory extends RecipeInventory<AbstractFurnaceScreen<?>> {

    public FurnaceInventory(AbstractFurnaceScreen<?> inventory) {
        super(inventory);
    }

    @Override
    public ItemStackHelper getOutput() {
        return new ItemStackHelper(inventory.getMenu().getResultSlot().getItem());
    }

    /**
     * @param x the x position of the input, will always be 0
     * @param y the y position of the input, will always be 0
     * @return the currently smelting item.
     * @since 1.8.4
     */
    public ItemStackHelper getInput(int x, int y) {
        return getSmeltedItem();
    }

    @Override
    public int getCraftingWidth() {
        return 1;
    }

    @Override
    public int getCraftingHeight() {
        return 1;
    }

    @Override
    public int getCraftingSlotCount() {
        return 1;
    }

    /**
     * @return the currently smelting item.
     * @since 1.8.4
     */
    public ItemStackHelper getSmeltedItem() {
        return getSlot(0);
    }

    /**
     * @return the fuel item.
     * @since 1.8.4
     */
    public ItemStackHelper getFuel() {
        return getSlot(1);
    }

    /**
     * @param stack the item to check
     * @return {@code true} if the item is a valid fuel, {@code false} otherwise.
     * @since 1.8.4
     */
    public boolean canUseAsFuel(ItemStackHelper stack) {
        return inventory.getMenu().isFuel(stack.getRaw());
    }

    /**
     * @param stack the item to check
     * @return {@code true} if the item can be smelted, {@code false} otherwise.
     * @since 1.8.4
     */
    public boolean isSmeltable(ItemStackHelper stack) {
        return inventory.getMenu().canSmelt(stack.getRaw());
    }

    /**
     * @return an empty map. Client no longer has this data.
     * @since 1.8.4
     * @deprecated Fuels are no longer synced with the client
     */
    @Deprecated
    public Map<String, Integer> getFuelValues() {
        // No-op, return empty map
        return new Object2IntOpenHashMap<>();
    }

    /**
     * If the returned value equals {@link #getTotalSmeltingTime()} then the item is done smelting.
     *
     * @return the current Smelting progress in ticks.
     * @since 1.8.4
     */
    public int getSmeltingProgress() {
        return inventory.getMenu().data.get(2);
    }

    /**
     * @return the total smelting time of a single input item in ticks.
     * @since 1.8.4
     */
    public int getTotalSmeltingTime() {
        return inventory.getMenu().data.get(3);
    }

    /**
     * @return the remaining time of the smelting progress in ticks.
     * @since 1.8.4
     */
    public int getRemainingSmeltingTime() {
        return getTotalSmeltingTime() - getSmeltingProgress();
    }

    /**
     * @return the remaining fuel time in ticks.
     * @since 1.8.4
     */
    public int getRemainingFuelTime() {
        return inventory.getMenu().data.get(0);
    }

    /**
     * @return the total fuel time of the current fuel item in ticks.
     * @since 1.8.4
     */
    public int getTotalFuelTime() {
        return inventory.getMenu().data.get(1);
    }

    /**
     * @return {@code true} if the furnace is currently smelting an item, {@code false} otherwise.
     * @since 1.8.4
     */
    public boolean isBurning() {
        return inventory.getMenu().isLit();
    }

    @Override
    public String toString() {
        return String.format("FurnaceInventory:{}");
    }

}
