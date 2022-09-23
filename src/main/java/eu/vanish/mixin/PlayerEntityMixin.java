package eu.vanish.mixin;

import java.lang.Boolean;
import eu.vanish.Vanish;
import eu.vanish.data.VanishedList;
import eu.vanish.data.VanishedPlayer;
import eu.vanish.mixinterface.IPlayerEntityMixin;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements IPlayerEntityMixin {
	@Shadow
	protected int sleepTimer;
	
	@Inject(at = @At("RETURN"), method = "canResetTimeBySleeping", cancellable = true)
	private void canResetTimeBySleeping(CallbackInfoReturnable<Boolean> cir) {
		VanishedList vanishedPlayers = Vanish.INSTANCE.vanishedPlayers;
		cir.setReturnValue(cir.getReturnValue() || vanishedPlayers.isVanished( (ServerPlayerEntity)(Object)this ));
	}
	public void updateSleeping() {
		World world = ((Entity)(Object)this).world;
		if (world instanceof ServerWorld) {
			((ServerWorld)world).updateSleepingPlayers();
		}
	}
}
