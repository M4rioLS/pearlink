package dev.m4riols.pearlink.util;

import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerAdvancementLoader;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class MiscUtil {

	public static void grantAdvancement(ServerPlayerEntity player, Identifier advancementId) {
		MinecraftServer server = player.getEntityWorld().getServer();
		ServerAdvancementLoader loader = server.getAdvancementLoader();
		AdvancementEntry advancement = loader.get(advancementId);

		if (advancement != null) {
			PlayerAdvancementTracker tracker = player.getAdvancementTracker();
			if (!tracker.getProgress(advancement).isDone()) {
				for (String criterion : advancement.value().requirements().getNames()) {
					tracker.grantCriterion(advancement, criterion);
				}
			}
		}
	}
}
