package dev.m4riols.pearlink.util;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.PlayerAdvancements;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.level.ServerPlayer;

public class MiscUtil {

	public static void grantAdvancement(ServerPlayer player, Identifier advancementId) {
		MinecraftServer server = player.level().getServer();
		ServerAdvancementManager loader = server.getAdvancements();
		AdvancementHolder advancement = loader.get(advancementId);

		if (advancement != null) {
			PlayerAdvancements tracker = player.getAdvancements();
			if (!tracker.getOrStartProgress(advancement).isDone()) {
				for (String criterion : advancement.value().requirements().names()) {
					tracker.award(advancement, criterion);
				}
			}
		}
	}
}
