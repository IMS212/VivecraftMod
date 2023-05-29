package org.vivecraft.mixin.client_vr.gui;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.vivecraft.client_vr.VRState;
import org.vivecraft.client_vr.gameplay.screenhandlers.KeyboardHandler;

@Mixin(EditBox.class)
public abstract class EditBoxVRMixin extends AbstractWidget{

	@Shadow
	private boolean canLoseFocus;

	public EditBoxVRMixin(int p_93629_, int p_93630_, int p_93631_, int p_93632_, Component p_93633_) {
		super(p_93629_, p_93630_, p_93631_, p_93632_, p_93633_);
	}

	//TODO test
	@Redirect(at = @At(value = "FIELD", target = "Lnet/minecraft/client/gui/components/EditBox;canLoseFocus:Z"), method = "setFocused")
	public boolean focus(EditBox instance, boolean bl) {
		if (bl && VRState.vrRunning) {
			KeyboardHandler.setOverlayShowing(true);
		}
		return  canLoseFocus || !this.isFocused();
	}

}
