package fi.dy.masa.malitest.commands;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.text.Text;
import fi.dy.masa.malitest.data.DataManager;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public class MaLiTestCommand
{
    public static boolean inspectOn = false;

    public static void register()
    {
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> dispatcher.register(
                literal("malitest")
                        .then(literal("inspect")
                                .executes((ctx) -> inspectRoot(ctx.getSource()))
                                .then(literal("on")
                                        .executes((ctx) -> turnOn(ctx.getSource()))
                                )
                                .then(literal("off")
                                        .executes((ctx) -> turnOff(ctx.getSource()))
                                )
                        )
                        .then(literal("search")
                                .then(argument("param", StringArgumentType.greedyString())
                                        .executes((ctx) -> sendSearchQuery(ctx.getSource(), StringArgumentType.getString(ctx, "param")))
                                )
                        )
        ));
    }

    private static int sendSearchQuery(FabricClientCommandSource source, String param)
    {
        source.sendFeedback(Text.literal("Sending Search Query."));
        DataManager.getInstance().sendSearchQuery(param);

        return 1;
    }

    private static int inspectRoot(FabricClientCommandSource source)
    {
        inspectOn = !inspectOn;

        if (inspectOn)
        {
            source.sendFeedback(Text.literal("Enabled client-side inspect."));
        }
        else
        {
            source.sendFeedback(Text.literal("Disabled client-side inspect."));
        }

        return 1;
    }

    private static int turnOn(FabricClientCommandSource source)
    {
        inspectOn = true;
        source.sendFeedback(Text.literal("Enabled client-side inspect."));

        return 1;
    }

    private static int turnOff(FabricClientCommandSource source)
    {
        inspectOn = false;
        source.sendFeedback(Text.literal("Disabled client-side inspect."));

        return 1;
    }
}
