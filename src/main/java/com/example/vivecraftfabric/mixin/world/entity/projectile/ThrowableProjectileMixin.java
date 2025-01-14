package com.example.vivecraftfabric.mixin.world.entity.projectile;

import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vivecraft.api.NetworkHelper;
import org.vivecraft.api.ServerVivePlayer;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@Mixin(ThrowableProjectile.class)
public abstract class ThrowableProjectileMixin extends Entity {

	protected ThrowableProjectileMixin(EntityType<? extends Projectile> p_37248_, Level p_37249_) {
		super(p_37248_, p_37249_);
		// TODO Auto-generated constructor stub
	}

	@Inject(at = @At("TAIL"), method = "<init>(Lnet/minecraft/world/entity/EntityType;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/level/Level;)V")
	public void init(EntityType<? extends ThrowableProjectile> p_37462_, LivingEntity p_37463_, Level p_37464_, CallbackInfo info) {
		ServerVivePlayer serverviveplayer = NetworkHelper.vivePlayers.get(p_37463_.getUUID());
		if (serverviveplayer != null && serverviveplayer.isVR()) {
			Vec3 vec3 = serverviveplayer.getControllerPos(serverviveplayer.activeHand, (Player)p_37463_);
			Vec3 vec31 = serverviveplayer.getControllerDir(serverviveplayer.activeHand).scale((double)0.6F);
			this.setPos(vec3.x + vec31.x, vec3.y + vec31.y, vec3.z + vec31.z);
		}
	}
	
}
