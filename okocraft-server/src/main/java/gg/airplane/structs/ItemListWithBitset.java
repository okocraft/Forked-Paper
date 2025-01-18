package gg.airplane.structs;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang.Validate;
import org.jetbrains.annotations.NotNull;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.List;

public class ItemListWithBitset extends AbstractList<ItemStack> {

    // Hopper / Hopper Minecart: 5
    // Dispenser / Dropper: 9
    // Chest / Chest Minecart / Trap Chest / Barrel / Shulker Box: 27
    private static final int MAX_SIZE = Integer.SIZE;

    private static final int ALL_BITS_5 = createAllBits(5);
    private static final int ALL_BITS_9 = createAllBits(9);
    private static final int ALL_BITS_27 = createAllBits(27);

    private static int createAllBits(int size) {
        return ((1 << size) - 1);
    }

    private static int allBits(int size) {
        if (size == 5) {
            return ALL_BITS_5;
        } else if (size == 9) {
            return ALL_BITS_9;
        } else if (size == 27) {
            return ALL_BITS_27;
        } else {
            return createAllBits(size);
        }
    }

    public static ItemListWithBitset fromList(List<ItemStack> list) {
        if (list instanceof ItemListWithBitset ours) {
            return ours;
        }
        return new ItemListWithBitset(list);
    }

    private static ItemStack[] createArray(int size) {
        ItemStack[] array = new ItemStack[size];
        Arrays.fill(array, ItemStack.EMPTY);
        return array;
    }

    private final ItemStack[] items;

    private int bitSet = 0;

    private static class OurNonNullList extends NonNullList<ItemStack> {
        protected OurNonNullList(List<ItemStack> delegate) {
            super(delegate, ItemStack.EMPTY);
        }
    }

    public final NonNullList<ItemStack> nonNullList = new OurNonNullList(this);

    private ItemListWithBitset(List<ItemStack> list) {
        this(list.size());

        for (int i = 0; i < list.size(); i++) {
            this.set(i, list.get(i));
        }
    }

    public ItemListWithBitset(int size) {
        Validate.isTrue(size < MAX_SIZE, "size is too large");

        this.items = createArray(size);
    }

    public boolean isCompletelyEmpty() {
        return this.bitSet == 0;
    }

    public boolean isSlotFilled() {
        int allBits = allBits(this.items.length);
        return (this.bitSet & allBits) == allBits;
    }

    @Override
    public ItemStack set(int index, @NotNull ItemStack itemStack) {
        ItemStack existing = this.items[index];

        this.items[index] = itemStack;

        if (itemStack.isEmpty()) {
            this.bitSet &= ~(1 << index);
        } else {
            this.bitSet |= (1 << index);
        }

        return existing;
    }

    @NotNull
    @Override
    public ItemStack get(int var0) {
        return this.items[var0];
    }

    @Override
    public int size() {
        return this.items.length;
    }

    @Override
    public void clear() {
        Arrays.fill(this.items, ItemStack.EMPTY);
    }

    // these are unsupported for block inventories which have a static size
    @Override
    public void add(int var0, ItemStack var1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ItemStack remove(int var0) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return "ItemListWithBitset{" +
                "items=" + Arrays.toString(this.items) +
                ", slotState=" + Long.toString(this.bitSet, 2) +
                ", size=" + this.items.length +
                '}';
    }
}
