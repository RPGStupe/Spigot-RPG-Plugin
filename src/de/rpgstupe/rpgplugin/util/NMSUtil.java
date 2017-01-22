package de.rpgstupe.rpgplugin.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import net.minecraft.server.v1_11_R1.Entity;
import net.minecraft.server.v1_11_R1.EntityTypes;
import net.minecraft.server.v1_11_R1.MinecraftKey;
import net.minecraft.server.v1_11_R1.RegistryMaterials;

public class NMSUtil extends RegistryMaterials {

    private static NMSUtil instance = null;

    private final BiMap<MinecraftKey, Class<? extends Entity>> customEntities = HashBiMap.create();
    private final BiMap<Class<? extends Entity>, MinecraftKey> customEntityClasses = this.customEntities.inverse();
    private final Map<Class<? extends Entity>, Integer> customEntityIds = new HashMap<>();

    private final RegistryMaterials wrapped;

    private NMSUtil(RegistryMaterials original) {
        this.wrapped = original;
    }
    
    public static NMSUtil getInstance() {
        if (instance != null) {
            return instance;
        }

        instance = new NMSUtil(EntityTypes.b);

        try {
            //TODO: Update name on version change (RegistryMaterials)
            Field registryMaterialsField = EntityTypes.class.getDeclaredField("b");
            registryMaterialsField.setAccessible(true);

            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(registryMaterialsField, registryMaterialsField.getModifiers() & ~Modifier.FINAL);

            registryMaterialsField.set(null, instance);
        } catch (Exception e) {
            instance = null;

            throw new RuntimeException("Unable to override the old entity RegistryMaterials", e);
        }

        return instance;
    }
    
    /**
     * Must be called when a custom entity is introduced. Most likely in
     * <code>onLoad()</code> of the plugin
     * 
     * @param entityId
     *            The id of the entity overridden (e.g. <code>120</code> for
     *            villager)
     * @param entityName
     *            The name of the entity overridden (e.g. <code>villager</code> for
     *            villager)
     * @param entityClass
     *            The class of the custom entity
     */
    public static void registerCustomEntity(int entityId, String entityName, Class<? extends Entity> entityClass) {
        getInstance().putCustomEntity(entityId, entityName, entityClass);
    }

    public void putCustomEntity(int entityId, String entityName, Class<? extends Entity> entityClass) {
        MinecraftKey minecraftKey = new MinecraftKey(entityName);

        this.customEntities.put(minecraftKey, entityClass);
        this.customEntityIds.put(entityClass, entityId);
    }

    @Override
    public Class<? extends Entity> get(Object key) {
        if (this.customEntities.containsKey(key)) {
            return this.customEntities.get(key);
        }

        return (Class<? extends Entity>) wrapped.get(key);
    }

    @Override
    public int a(Object key) { //TODO: Update name on version change (getId)
        if (this.customEntityIds.containsKey(key)) {
            return this.customEntityIds.get(key);
        }

        return this.wrapped.a(key);
    }

    @Override
    public MinecraftKey b(Object value) { //TODO: Update name on version change (getKey)
        if (this.customEntityClasses.containsKey(value)) {
            return this.customEntityClasses.get(value);
        }

        return (MinecraftKey) wrapped.b(value);
    }
}