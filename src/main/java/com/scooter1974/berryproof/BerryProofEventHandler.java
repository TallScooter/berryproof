package com.scooter1974.berryproof;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.core.Holder;

import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.bus.api.SubscribeEvent;

public class BerryProofEventHandler {

    // Tags
    private static final TagKey<Item> LEGGINGS_TAG = TagKey.create(
        net.minecraft.core.registries.Registries.ITEM,
        ResourceLocation.fromNamespaceAndPath(BerryProofMod.MODID, "leggings"));

    private static final TagKey<net.minecraft.world.entity.EntityType<?>> PROTECTED_ENTITIES_TAG = TagKey.create(
        net.minecraft.core.registries.Registries.ENTITY_TYPE,
        ResourceLocation.fromNamespaceAndPath(BerryProofMod.MODID, "protected_entities"));

    private static final TagKey<DamageType> DAMAGE_SOURCES_TAG = TagKey.create(
        net.minecraft.core.registries.Registries.DAMAGE_TYPE,
        ResourceLocation.fromNamespaceAndPath(BerryProofMod.MODID, "damage_sources"));

    @SubscribeEvent
    public void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        DamageSource source = event.getSource();

    if (!(entity.level() instanceof ServerLevel level)) return;

        // Check if source is protected
        Holder<DamageType> damageTypeHolder = source.typeHolder();
        boolean isProtectedSource = damageTypeHolder.is(DAMAGE_SOURCES_TAG);

    if (!isProtectedSource) return;

        boolean entityIsProtected = entity.getType().is(PROTECTED_ENTITIES_TAG);
        boolean hasTaggedLeggings = false;
        ItemStack leggings = entity.getItemBySlot(EquipmentSlot.LEGS);
        if (!leggings.isEmpty() && leggings.is(LEGGINGS_TAG)) {
            hasTaggedLeggings = true;
        }

        // Protect if entity is in protected_entities tag, or player is wearing tagged leggings
        boolean applyProtection = (BerryProofConfigMidnight.enableProtectedEntities && entityIsProtected)
                || (entity instanceof net.minecraft.world.entity.player.Player && hasTaggedLeggings);

        if (applyProtection) {
            event.setCanceled(true); // Block damage

            // Play a sound when walking through berry bushes
            if (BerryProofConfigMidnight.enableSound) {
                level.playSound(null, entity.getX(), entity.getY(), entity.getZ(),
                        net.minecraft.sounds.SoundEvents.GRASS_STEP,
                        entity.getSoundSource(),
                        (float) BerryProofConfigMidnight.soundVolume, 0.5F);
            }

            // Debug logging
            if (BerryProofConfigMidnight.logTagMatches) {
                String debugMsg = String.format(
                        "[BerryProof Debug]\nBlocked damage from: %s\nEntity: %s\nWearing tagged leggings: %s\nIs protected entity: %s",
                        source.getMsgId(), entity.getName().getString(), hasTaggedLeggings, entityIsProtected);
                BerryProofMod.LOGGER.info(debugMsg);
            }

            // Advancement: only when blocking sweet berry bush damage with tagged leggings
            if (entity instanceof net.minecraft.server.level.ServerPlayer player) {
                boolean isSweetBerryBush = damageTypeHolder.is(DamageTypes.SWEET_BERRY_BUSH);
                if (isSweetBerryBush && hasTaggedLeggings) {
                    var id = ResourceLocation.parse(BerryProofMod.MODID + ":berryproofed");
                    var advancement = level.getServer().getAdvancements().get(id);
                    if (advancement != null) {
                        var progress = player.getAdvancements().getOrStartProgress(advancement);

                        // Only award if the advancement hasn't already been completed
                        var crit = progress.getCriterion("blocked_berry_bush_damage");
                        boolean alreadyDone = progress.isDone() || (crit != null && crit.isDone());
                        if (!alreadyDone) {
                            player.getAdvancements().award(advancement, "blocked_berry_bush_damage");
                        }
                    }
                }
            }

            // Advancement: only when blocking cactus damage with tagged leggings
            if (entity instanceof net.minecraft.server.level.ServerPlayer player) {
                boolean isCactus = damageTypeHolder.is(DamageTypes.CACTUS);
                if (isCactus && hasTaggedLeggings) {
                    var id = ResourceLocation.parse(BerryProofMod.MODID + ":cactusproofed");
                    var advancement = level.getServer().getAdvancements().get(id);
                    if (advancement != null) {
                        var progress = player.getAdvancements().getOrStartProgress(advancement);

                        // Only award if the advancement hasn't already been completed
                        var crit = progress.getCriterion("blocked_cactus_damage");
                        boolean alreadyDone = progress.isDone() || (crit != null && crit.isDone());
                        if (!alreadyDone) {
                            player.getAdvancements().award(advancement, "blocked_cactus_damage");
                        }
                    }
                }
            }
        }
    }
}