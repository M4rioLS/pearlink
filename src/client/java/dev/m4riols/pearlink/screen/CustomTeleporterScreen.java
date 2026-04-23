package dev.m4riols.pearlink.screen;

import dev.m4riols.pearlink.Pearlink;
import dev.m4riols.pearlink.screenhandler.CustomTeleporterScreenHandler;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class CustomTeleporterScreen extends HandledScreen<CustomTeleporterScreenHandler>{
    private static final Identifier TEXTURE = Pearlink.id("textures/gui/container/custom_teleporter.png");

    public CustomTeleporterScreen(CustomTeleporterScreenHandler handler, PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init(){
        super.init();
    }

    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        // Texture must be 256x256; adjust u/v offsets if the shown region is not at (0,0).
        context.drawTexture(RenderPipelines.GUI_TEXTURED, TEXTURE, this.x, this.y, 0.0F, 0.0F, this.backgroundWidth, this.backgroundHeight, 256, 256);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta){
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
