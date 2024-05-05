package fi.dy.masa.malitest.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import fi.dy.masa.malitest.commands.MaLiTestCommand;
import fi.dy.masa.malitest.data.DataManager;

@Mixin(ClientPlayerInteractionManager.class)
public class MixinClientPlayerInteractionManager
{
    @Inject(method = "interactBlock", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/network/ClientPlayerInteractionManager;sendSequencedPacket(Lnet/minecraft/client/world/ClientWorld;Lnet/minecraft/client/network/SequencedPacketCreator;)V"),
            cancellable = true
    )
    private void onInteractBlock(ClientPlayerEntity player, Hand hand, BlockHitResult hitResult, CallbackInfoReturnable<ActionResult> cir)
    {
        if (MaLiTestCommand.inspectOn == false)
        {
            return;
        }

        cir.setReturnValue(ActionResult.SUCCESS);
        DataManager.getInstance().onInspectBlock(hitResult.getBlockPos());
    }
}
