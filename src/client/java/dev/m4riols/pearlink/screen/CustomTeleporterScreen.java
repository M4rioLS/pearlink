package dev.m4riols.pearlink.screen;

import dev.m4riols.pearlink.Pearlink;
import dev.m4riols.pearlink.screenhandler.CustomTeleporterScreenHandler;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

public class CustomTeleporterScreen extends AbstractContainerScreen<CustomTeleporterScreenHandler> {
    private static final Identifier TEXTURE = Pearlink.id("textures/gui/container/custom_teleporter.png");

    public CustomTeleporterScreen(CustomTeleporterScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(extractor, mouseX, mouseY, partialTick);
        // Texture must be 256x256; adjust u/v offsets if the shown region is not at (0,0).
        extractor.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, this.leftPos, this.topPos, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
    }
}
