package io.github.wynncraft_overhaul.wynnquester.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import io.github.wynncraft_overhaul.wynnquester.WynnQuester;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import static com.mojang.brigadier.arguments.StringArgumentType.getString;
import static com.mojang.brigadier.arguments.StringArgumentType.greedyString;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;

public class QuestCommand {
    public static void register(CommandDispatcher<FabricClientCommandSource> fabricClientCommandSourceCommandDispatcher, CommandRegistryAccess commandRegistryAccess) {
        fabricClientCommandSourceCommandDispatcher.register(literal("quest")
                .then(argument("name", greedyString()).suggests(new QuestSuggestionProvider())
                        .executes(QuestCommand::handle)));
    }

    public static int handle(CommandContext<FabricClientCommandSource> context) throws CommandSyntaxException {
        String name = getString(context, "name");
        if (!WynnQuester.quests.has(name)) {
            throw new SimpleCommandExceptionType(Text.literal("'" + name + "'" + " is not a valid quest!")).create();
        }
        context.getSource().getPlayer().sendMessage(Text.literal("Quest guide for: ").append(Text.literal(name).setStyle(Style.EMPTY.withColor(Formatting.AQUA).withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, WynnQuester.quests.get(name).getAsString())).withUnderline(true))));
        return Command.SINGLE_SUCCESS;
    }
}