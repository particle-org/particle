package com.particle.model.item.types;

import java.util.HashMap;
import java.util.Map;

public class ItemPrototypeDictionary {
    private Map<String, ItemPrototype> dictionary = new HashMap<>();
    private Map<Integer, ItemPrototype> idDictionary = new HashMap<>();

    private static ItemPrototypeDictionary instance = new ItemPrototypeDictionary();

    public static ItemPrototypeDictionary getDictionary() {
        return ItemPrototypeDictionary.instance;
    }

    private ItemPrototypeDictionary() {
        for (ItemPrototype itemPrototype : ItemPrototype.values()) {
            dictionary.put(itemPrototype.getName(), itemPrototype);
            idDictionary.put(itemPrototype.getId(), itemPrototype);
        }
    }

    public ItemPrototype map(String name) {
        return dictionary.get(name);
    }

    public ItemPrototype map(Integer id) {
        return idDictionary.get(id);
    }
}
