package io.github.wynncraft_overhaul.wynnquester.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.wynncraft_overhaul.wynnquester.WynnQuester;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;


import java.util.concurrent.CompletableFuture;

class QuestSuggestionProvider implements SuggestionProvider<FabricClientCommandSource> {
    @Override
    public CompletableFuture<Suggestions> getSuggestions(CommandContext<FabricClientCommandSource> context, SuggestionsBuilder builder) {
        String name = context.getInput().replace("/quest ", "").toLowerCase();
        for (String key : WynnQuester.quests.keySet()) {
            if (key.toLowerCase().contains(name)) {
                builder.suggest(key);
            }
        }

        return builder.buildFuture();
    }
}