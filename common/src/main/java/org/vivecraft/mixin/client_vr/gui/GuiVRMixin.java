package org.vivecraft.mixin.client_vr.gui;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import org.vivecraft.client_vr.ClientDataHolderVR;
import org.vivecraft.client_vr.extensions.GuiExtension;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vivecraft.client_xr.render_pass.RenderPassType;

@Mixin(Gui.class)
public abstract class GuiVRMixin implements GuiExtension {

    @Unique
    public boolean showPlayerList;
    @Shadow
    private int screenWidth;
    @Shadow
    private int screenHeight;
    @Final
    @Shadow
    private Minecraft minecraft;

    @Shadow
    protected abstract Player getCameraPlayer();

    @Inject(method = "renderVignette", at = @At("HEAD"), cancellable = true)
    void cancelRenderVignette(GuiGraphics guiGraphics, Entity entity, CallbackInfo ci) {
        if (RenderPassType.isGuiOnly()) {
            RenderSystem.enableDepthTest();
            ci.cancel();
        }
    }

    @Inject(method = "renderTextureOverlay", at = @At("HEAD"), cancellable = true)
    void cancelRenderOverlay(GuiGraphics guiGraphics, ResourceLocation resourceLocation, float f, CallbackInfo ci) {
        if (RenderPassType.isGuiOnly()) {
            ci.cancel();
        }
    }
    @Inject(at = @At("HEAD"), method = "renderCrosshair", cancellable = true)
    public void cancelRenderCrosshair(GuiGraphics guiGraphics, CallbackInfo ci) {
        if (RenderPassType.isGuiOnly()) {
            ci.cancel();
        }
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/KeyMapping;isDown()Z"), method = "render")
    public boolean toggleableTabList(KeyMapping instance) {
        return instance.isDown() || showPlayerList;
    }
    private static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation("textures/gui/widgets.png");

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V", ordinal = 1, shift = At.Shift.AFTER), method = "renderHotbar")
    public void hotbarContext(float f, GuiGraphics guiGraphics, CallbackInfo ci) {
        int i = this.screenWidth / 2;
        if (ClientDataHolderVR.getInstance().interactTracker.hotbar >= 0 && ClientDataHolderVR.getInstance().interactTracker.hotbar < 9 && this.getCameraPlayer().getInventory().selected != ClientDataHolderVR.getInstance().interactTracker.hotbar) {
            RenderSystem.setShaderColor(0.0F, 1.0F, 0.0F, 1.0F);
            guiGraphics.blit(WIDGETS_LOCATION, i - 91 - 1 + ClientDataHolderVR.getInstance().interactTracker.hotbar * 20, this.screenHeight - 22 - 1, 0, 22, 24, 22);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;isEmpty()Z", ordinal = 0), method = "renderHotbar")
    public boolean slotSwap(ItemStack instance) {
        return !(!instance.isEmpty() || ClientDataHolderVR.getInstance().vrSettings.vrTouchHotbar);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V", ordinal = 2, shift = At.Shift.BEFORE), method = "renderHotbar")
    public void renderVRHotbarLeft(float f, GuiGraphics guiGraphics, CallbackInfo ci) {
        if (ClientDataHolderVR.getInstance().interactTracker.hotbar == 9) {
            RenderSystem.setShaderColor(0.0F, 0.0F, 1.0F, 1.0F);
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V", ordinal = 2, shift = At.Shift.AFTER), method = "renderHotbar")
    public void renderVRHotbarLeftReset(float f, GuiGraphics guiGraphics, CallbackInfo ci) {
        if (ClientDataHolderVR.getInstance().interactTracker.hotbar == 9) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V", ordinal = 3, shift = At.Shift.BEFORE), method = "renderHotbar")
    public void renderVRHotbarRight(float f, GuiGraphics guiGraphics, CallbackInfo ci) {
        if (ClientDataHolderVR.getInstance().interactTracker.hotbar == 9){
            RenderSystem.setShaderColor(0.0F, 0.0F, 1.0F, 1.0F);
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;blit(Lnet/minecraft/resources/ResourceLocation;IIIIII)V", ordinal = 3, shift = At.Shift.AFTER), method = "renderHotbar")
    public void renderVRHotbarRightReset(float f, GuiGraphics guiGraphics, CallbackInfo ci) {
        if (ClientDataHolderVR.getInstance().interactTracker.hotbar == 9){
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }

    // do remap because of forge
    @Inject(at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;disableBlend()V"), method = "renderHotbar")
    public void renderVive(float f, GuiGraphics guiGraphics, CallbackInfo ci){
        this.renderViveHudIcons(guiGraphics);
    }

    private void renderViveHudIcons(GuiGraphics guiGraphics) {
        if (this.minecraft.getCameraEntity() instanceof Player) {
            int i = this.minecraft.getWindow().getGuiScaledWidth();
            int j = this.minecraft.getWindow().getGuiScaledHeight();
            Font font = this.minecraft.gui.getFont();
            Player player = (Player)this.minecraft.getCameraEntity();
            int k = 0;
            MobEffect mobeffect = null;

            if (player.isSprinting()) {
                mobeffect = MobEffects.MOVEMENT_SPEED;
            }

            if (player.isVisuallySwimming()) {
                mobeffect = MobEffects.DOLPHINS_GRACE;
            }

            if (player.isShiftKeyDown()) {
                mobeffect = MobEffects.BLINDNESS;
            }

            if (player.isFallFlying()) {
                k = -1;
            }
            if (ClientDataHolderVR.getInstance().crawlTracker.crawling) {
                k = -2;
            }

            int l = this.minecraft.getWindow().getGuiScaledWidth() / 2 - 109;
            int i1 = this.minecraft.getWindow().getGuiScaledHeight() - 39;

            if (k == -1) {
                guiGraphics.renderFakeItem(new ItemStack(Items.ELYTRA), l, i1);
                mobeffect = null;
            }
            else if (k == -2) {
                if (player.isShiftKeyDown()) {
                    l -= 19;
                }
                else {
                    mobeffect = null;
                }
                guiGraphics.renderFakeItem(new ItemStack(Items.RABBIT_FOOT), l, i1);
            }
            if (mobeffect != null) {
                TextureAtlasSprite textureatlassprite = this.minecraft.getMobEffectTextures().get(mobeffect);
                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                RenderSystem.setShaderTexture(0, textureatlassprite.atlasLocation());
                guiGraphics.blit(l, i1, 0, 18, 18, textureatlassprite);
            }
        }
    }

    @Override
    public boolean getShowPlayerList() {
        return this.showPlayerList;
    }

    @Override
    public void setShowPlayerList(boolean showPlayerList) {
        this.showPlayerList = showPlayerList;
    }
    private static final ResourceLocation GUI_ICONS_LOCATION = new ResourceLocation("textures/gui/icons.png");
    @Override
    public void drawMouseMenuQuad(int mouseX, int mouseY) {
        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        //uhhhh //RenderSystem.disableLighting();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, GUI_ICONS_LOCATION);
        float f = 16.0F * ClientDataHolderVR.getInstance().vrSettings.menuCrosshairScale;
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.ONE_MINUS_DST_COLOR, GlStateManager.DestFactor.ZERO, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        this.drawCentredTexturedModalRect(mouseX, mouseY, f, f, 0, 0, 15, 15);
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    }

    public void drawCentredTexturedModalRect(int centreX, int centreY, float width, float height, int u, int v, int texWidth, int texHeight) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.vertex((double)((float)centreX - width / 2.0F), (double)((float)centreY + height / 2.0F), 0).uv((float)(u + 0) * f, (float)(v + texHeight) * f1).endVertex();
        bufferbuilder.vertex((double)((float)centreX + width / 2.0F), (double)((float)centreY + height / 2.0F), 0).uv((float)(u + texWidth) * f, (float)(v + texHeight) * f1).endVertex();
        bufferbuilder.vertex((double)((float)centreX + width / 2.0F), (double)((float)centreY - height / 2.0F), 0).uv((float)(u + texWidth) * f, (float)(v + 0) * f1).endVertex();bufferbuilder.vertex((double)((float)centreX - width / 2.0F), (double)((float)centreY - height / 2.0F), 0).uv((float)(u + 0) * f, (float)(v + 0) * f1).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }
}
