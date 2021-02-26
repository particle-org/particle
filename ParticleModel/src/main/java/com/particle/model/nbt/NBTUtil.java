package com.particle.model.nbt;

public final class NBTUtil {

    public static boolean func_181123_a(NBTBase p_181123_0_, NBTBase p_181123_1_, boolean p_181123_2_) {
        if (p_181123_0_ == p_181123_1_) {
            return true;
        } else if (p_181123_0_ == null) {
            return true;
        } else if (p_181123_1_ == null) {
            return false;
        } else if (!p_181123_0_.getClass().equals(p_181123_1_.getClass())) {
            return false;
        } else if (p_181123_0_ instanceof NBTTagCompound) {
            NBTTagCompound nbttagcompound = (NBTTagCompound) p_181123_0_;
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) p_181123_1_;

            for (String s : nbttagcompound.getKeySet()) {
                NBTBase nbtbase1 = nbttagcompound.getTag(s);

                if (!func_181123_a(nbtbase1, nbttagcompound1.getTag(s), p_181123_2_)) {
                    return false;
                }
            }

            return true;
        } else if (p_181123_0_ instanceof NBTTagList && p_181123_2_) {
            NBTTagList nbttaglist = (NBTTagList) p_181123_0_;
            NBTTagList nbttaglist1 = (NBTTagList) p_181123_1_;

            if (nbttaglist.tagCount() == 0) {
                return nbttaglist1.tagCount() == 0;
            } else {
                for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                    NBTBase nbtbase = nbttaglist.get(i);
                    boolean flag = false;

                    for (int j = 0; j < nbttaglist1.tagCount(); ++j) {
                        if (func_181123_a(nbtbase, nbttaglist1.get(j), p_181123_2_)) {
                            flag = true;
                            break;
                        }
                    }

                    if (!flag) {
                        return false;
                    }
                }

                return true;
            }
        } else {
            return p_181123_0_.equals(p_181123_1_);
        }
    }
}