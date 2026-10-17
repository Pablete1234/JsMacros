package xyz.wagyourtail.jsmacros.client.api.classes.inventory;

import net.minecraft.client.gui.screens.inventory.BrewingStandScreen;
import net.minecraft.world.item.crafting.PotionIngredient;
import net.minecraft.world.item.crafting.RecipeAccess;
import net.minecraft.world.item.crafting.RecipePropertySet;
import xyz.wagyourtail.jsmacros.client.api.helper.inventory.ItemStackHelper;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Etheradon
 * @since 1.8.4
 */
@SuppressWarnings("unused")
public class BrewingStandInventory extends Inventory<BrewingStandScreen> {

    public BrewingStandInventory(BrewingStandScreen inventory) {
        super(inventory);
    }

    /**
     * @param potion the potion to check
     * @return {@code true} if the given potion is can be brewed, {@code false} otherwise.
     * @since 1.8.4
     */
    public boolean isBrewablePotion(ItemStackHelper potion) {
        return !potion.isEmpty() && PotionIngredient.isPotionInput(potion.getRaw(), recipes());
    }

    /**
     * @param ingredient the item to check
     * @return {@code true} if the given item is a valid ingredient, {@code false} otherwise.
     * @since 1.8.4
     */
    public boolean isValidIngredient(ItemStackHelper ingredient) {
        return !ingredient.isEmpty() && recipes().propertySet(RecipePropertySet.BREWING_REAGENTS).test(ingredient.getRaw());
    }

    /**
     * @param potion     the potion to check
     * @param ingredient the ingredient to check
     * @return {@code true} if the given potion and ingredient can be brewed together, {@code false}
     * otherwise.
     * @since 1.8.4
     * @deprecated The client no longer has the required info for this, low-effort attempt is made instead
     */
    @Deprecated
    public boolean isValidRecipe(ItemStackHelper potion, ItemStackHelper ingredient) {
        return isBrewablePotion(potion) && isValidIngredient(ingredient);
    }

    /**
     * @return the left fuel.
     * @since 1.8.4
     */
    public int getFuelCount() {
        return inventory.getMenu().getFuel();
    }

    /**
     * @return the maximum fuel.
     * @since 1.8.4
     */
    public int getMaxFuelUses() {
        return inventory.getMenu().getTotalFuel();
    }

    /**
     * @return {@code true} if the brewing stand can brew any of the held potions with the current
     * ingredient, {@code false} otherwise.
     * @since 1.8.4
     * @deprecated The client no longer has the required info for this
     */
    @Deprecated
    public boolean canBrewCurrentInput() {
        ItemStackHelper ingredient = getIngredient();
        if (ingredient.isEmpty() || !isValidIngredient(ingredient)) {
            return false;
        }
        for (ItemStackHelper stack : getPotions()) {
            if (!stack.isEmpty() && isValidRecipe(stack, ingredient)) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return the time the potions have been brewing.
     * @since 1.8.4
     */
    public int getBrewTime() {
        return inventory.getMenu().getTotalBrewingTicks() - inventory.getMenu().getBrewingTicks();
    }

    /**
     * @return the remaining time the potions have to brew.
     * @since 1.8.4
     */
    public int getRemainingTicks() {
        return inventory.getMenu().getBrewingTicks();
    }

    /**
     * @param potion     the potion
     * @param ingredient the ingredient
     * @return the resulting potion of the given potion and ingredient if it exists and the potion
     * itself otherwise.
     * @since 1.8.4
     * @deprecated The client no longer has the required info for this
     */
    @Deprecated
    public ItemStackHelper previewPotion(ItemStackHelper potion, ItemStackHelper ingredient) {
        return potion;
    }

    /**
     * @return a list of all resulting potions of the current input.
     * @since 1.8.4
     * @deprecated The client no longer has the required info for this
     */
    @Deprecated
    public List<ItemStackHelper> previewPotions() {
        var ingredient = getIngredient();
        return getPotions().stream().map(stack -> previewPotion(stack, ingredient)).collect(Collectors.toList());
    }

    /**
     * @return the ingredient.
     * @since 1.8.4
     */
    public ItemStackHelper getIngredient() {
        return getSlot(3);
    }

    /**
     * @return the fuel item.
     * @since 1.8.4
     */
    public ItemStackHelper getFuel() {
        return getSlot(4);
    }

    /**
     * @return the first potion.
     * @since 1.8.4
     */
    public ItemStackHelper getFirstPotion() {
        return getSlot(0);
    }

    /**
     * @return the second potion.
     * @since 1.8.4
     */
    public ItemStackHelper getSecondPotion() {
        return getSlot(1);
    }

    /**
     * @return the third potion.
     * @since 1.8.4
     */
    public ItemStackHelper getThirdPotion() {
        return getSlot(2);
    }

    /**
     * @return a list of the potions inside the brewing stand.
     * @since 1.8.4
     */
    public List<ItemStackHelper> getPotions() {
        return Arrays.asList(getSlot(0), getSlot(1), getSlot(2));
    }

    private RecipeAccess recipes() {
        return Objects.requireNonNull(mc.getConnection()).recipes();
    }

    @Override
    public String toString() {
        return String.format("BrewingStandInventory:{}");
    }

}
