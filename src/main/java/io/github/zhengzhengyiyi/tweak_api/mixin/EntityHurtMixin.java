package io.github.zhengzhengyiyi.tweak_api.mixin;

import io.github.zhengzhengyiyi.tweak_api.api.event.EntityHurtCallback;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Mixin for {@link LivingEntity} to inject into the damage calculation process.
 */
@Mixin(LivingEntity.class)
public abstract class EntityHurtMixin extends Entity {
    protected EntityHurtMixin() {
        super(null, null);
    }

    /**
     * Injects into the {@link LivingEntity#damage} method.
     *
     */
    @Inject(
        method = "applyDamage",
        at = @At("HEAD"),
        cancellable = false
    )
    private void applyDamage(DamageSource source, float amount, CallbackInfo ci) {
        EntityHurtCallback.EVENT
            .invoker()
            .onHurt((LivingEntity) (Object) this, source, amount);
    }
}
