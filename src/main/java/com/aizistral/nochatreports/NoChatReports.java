package com.aizistral.nochatreports;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aizistral.nochatreports.handlers.NoReportsConfig;
import com.aizistral.nochatreports.network.ServerChannelHandler;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class NoChatReports implements ModInitializer {
	public static final Logger LOGGER = LogManager.getLogger();
	protected static final ResourceLocation CHANNEL = new ResourceLocation("nochatreports", "sync");

	@Override
	public void onInitialize() {
		LOGGER.info("KONNICHIWA ZA WARUDO!");

		ServerPlayNetworking.registerGlobalReceiver(CHANNEL, ServerChannelHandler.INSTANCE);
		ServerPlayConnectionEvents.JOIN.register(this::onPlayReady);
		NoReportsConfig.loadConfig();
	}

	private void onPlayReady(ServerGamePacketListenerImpl handler, PacketSender sender, MinecraftServer server) {
		server.execute(() -> {
			if (!ServerPlayNetworking.canSend(handler, CHANNEL)) {
				handler.disconnect(Component.translatable("disconnect.nochatreports.server"));
			}
		});
	}
}
