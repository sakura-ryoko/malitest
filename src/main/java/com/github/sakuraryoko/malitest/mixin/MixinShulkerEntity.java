package com.github.sakuraryoko.malitest.mixin;

import com.github.sakuraryoko.malitest.MaLiTest;
import com.llamalad7.mixinextras.sugar.Local;

import net.minecraft.entity.mob.ShulkerEntity;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ShulkerEntity.class)
public class MixinShulkerEntity
{
    @Redirect(method = "spawnNewShulker",
              at = @At(value = "INVOKE",
                     target = "Lnet/minecraft/util/math/random/Random;nextFloat()F")
    )
    private float malitest_checkChunkerSpawns(Random instance, @Local float f)
    {
        final float rand = instance.nextFloat();
        MaLiTest.logger.error("CheckShulkerSpawns(): rand: [{}] < f: [{}]", rand, f);
        return rand;
    }
}
