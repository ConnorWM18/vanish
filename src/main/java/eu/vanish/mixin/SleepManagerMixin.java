package eu.vanish.mixin;

import java.util.Iterator;
import java.util.List;
import eu.vanish.Vanish;
import eu.vanish.data.VanishedList;
import eu.vanish.data.VanishedPlayer;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.SleepManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.apache.logging.log4j.LogManager;

@Mixin(SleepManager.class)
public class SleepManagerMixin {
	@Shadow
	private int sleeping;
	
	@Shadow
	private int total;
	
	@Overwrite
	public boolean update(List<ServerPlayerEntity> players) {
		//SleepManager that = (SleepManager)(Object)this;
		int i = this.total;
		int j = this.sleeping;
		this.total = 0;
		this.sleeping = 0;
		Iterator var4 = players.iterator();
		
		VanishedList vanishedPlayers = Vanish.INSTANCE.vanishedPlayers;

		while(var4.hasNext()) {
			ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity)var4.next();
			if (!serverPlayerEntity.isSpectator() && vanishedPlayers.isNotVanished(serverPlayerEntity)) {
				++this.total;
				if (serverPlayerEntity.isSleeping()) {
					++this.sleeping;
				}
			}
		}
		
		LogManager.getLogger().info("Total set {}", this.total);
		return (j > 0 || this.sleeping > 0) && (i != this.total || j != this.sleeping);
	}
}
