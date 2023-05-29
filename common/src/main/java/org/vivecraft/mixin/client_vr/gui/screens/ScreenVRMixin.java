package org.vivecraft.mixin.client_vr.gui.screens;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import org.vivecraft.client_vr.ClientDataHolderVR;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.vivecraft.client_vr.VRState;

@Mixin(Screen.class)
public abstract class ScreenVRMixin extends AbstractContainerEventHandler implements Renderable {

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fillGradient(IIIIII)V"), method = "renderBackground")
    public void vrBackground(GuiGraphics instance, int i, int j, int k, int l, int m, int n) {
        if (VRState.vrRunning && ClientDataHolderVR.getInstance().vrSettings != null && !ClientDataHolderVR.getInstance().vrSettings.menuBackground) {
            instance.fillGradient(i, j, k, l, 0, 0);
        } else {
            instance.fillGradient(i, j, k, l, m, n);
        }
    }

}