package io.github.wynncraft_overhaul.wynnquester;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.github.wynncraft_overhaul.wynnquester.commands.QuestCommand;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;



public class WynnQuester implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("wynn-quester");
    public static final Gson GSON = new Gson();
    public static final Path CONFIG = FabricLoader.getInstance().getConfigDir().resolve("wynnquester/quests.json");
    public static JsonObject quests = null;

    public void getUpdatedQuests() throws IOException {
        try {
            Files.createDirectories(CONFIG.getParent());
            if (!Files.exists(CONFIG)) Files.createFile(CONFIG);
            HttpResponse<String> resp = HttpClient.newHttpClient().send(HttpRequest.newBuilder(new URI("https://raw.githubusercontent.com/Wynncraft-Overhaul/Wynn-Quester/master/quests.json")).GET().build(), HttpResponse.BodyHandlers.ofString());
            if (resp.statusCode() != 200) {
                LOGGER.error("Could not retrieve 'https://raw.githubusercontent.com/Wynncraft-Overhaul/Wynn-Quester/master/quests.json'");
                throw new IOException();
            }
            Files.write(CONFIG, resp.body().getBytes());
            quests = GSON.fromJson(resp.body(), JsonObject.class);
        } catch (IOException | URISyntaxException | InterruptedException ex) {
            LOGGER.error("Failed to update 'wynnquester/quests.json'");
            quests = GSON.fromJson(Files.newBufferedReader(FabricLoader.getInstance().getConfigDir().resolve("wynnquester/quests.json")), JsonObject.class);
        }
    }

    @Override
    public void onInitialize() {
        try {
            getUpdatedQuests();
            ClientCommandRegistrationCallback.EVENT.register(QuestCommand::register);
            LOGGER.info("Loaded!");
        } catch (IOException e) {
            LOGGER.error("Failed to init Wynn Quester!");
        }
    }
}