/**
*  Minecraft Clone Math edition : Cybersecurity - A serious game to learn network and cybersecurity
*  Copyright (C) 2024 MeAndTheHomies (Math)
*
*  This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
*
*  This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
*
*  You should have received a copy of the GNU General Public License along with this program. If not, see <https://www.gnu.org/licenses/>.
*/

package fr.math.minecraft.server.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.math.minecraft.logger.LogType;
import fr.math.minecraft.logger.LoggerUtility;
import fr.math.minecraft.server.Client;
import fr.math.minecraft.server.MinecraftServer;
import fr.math.minecraft.server.TimeoutHandler;
import fr.math.minecraft.server.command.Command;
import fr.math.minecraft.server.command.TeleportCommand;
import fr.math.minecraft.shared.ChatColor;
import fr.math.minecraft.shared.Utils;
import fr.math.minecraft.shared.network.HttpResponse;
import fr.math.minecraft.shared.network.HttpUtils;
import fr.math.minecraft.shared.world.World;
import org.apache.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;

public class ConnectionInitHandler extends PacketHandler implements Runnable {

    private final static Logger logger = LoggerUtility.getClientLogger(ConnectionInitHandler.class, LogType.TXT);

    public ConnectionInitHandler(JsonNode packetData, InetAddress address, int clientPort) {
        super(packetData, address, clientPort);
    }

    @Override
    public void run() {
        MinecraftServer server = MinecraftServer.getInstance();
        Map<String, Client> clients = server.getClients();
        Map<String, Long> lastActivities = server.getLastActivities();

        String uuid = null;
        String skinUrl = null;

        try {
            String token = packetData.get("token").asText();
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode tokenNode = mapper.createObjectNode();
            tokenNode.put("token", token);
            HttpResponse response = HttpUtils.POST("http://localhost:3001/auth/validtoken", tokenNode);
            JsonNode responseData = mapper.readTree(response.getResponse().toString());
            JsonNode playerData = responseData.get("user");
            uuid = playerData.get("id").asText();
            skinUrl = playerData.get("skin").get("link").asText();
            String playerName = playerData.get("name").asText();

            ObjectNode node = mapper.createObjectNode();
            Client client = new Client(uuid, playerName, address, clientPort);
            World world = server.getWorld();

            LoadingMapHandler loadingMapHandler = new LoadingMapHandler(packetData, address, clientPort);
            //loadingMapHandler.run();

            client.getPosition().x = world.getSpawnPosition().x;
            client.getPosition().y = world.getSpawnPosition().y;
            client.getPosition().z = world.getSpawnPosition().z;

            node.put("uuid", uuid);
            node.put("spawnX", world.getSpawnPosition().x);
            node.put("spawnY", world.getSpawnPosition().y);
            node.put("spawnZ", world.getSpawnPosition().z);
            node.put("seed", world.getSeed());
            node.set("worldData", world.toJSONObject());

            byte[] buffer = mapper.writeValueAsString(node).getBytes(StandardCharsets.UTF_8);

            synchronized (server.getClients()) {
                server.getClients().put(uuid, client);
                try {
                    BufferedImage skin = Utils.loadBase64Skin(skinUrl);
                    client.setSkin(skin);
                    ImageIO.write(skin, "png", new File("skins/" + uuid + ".png"));
                    logger.info("Le skin du joueur" + playerName + " (" + uuid + ") a été sauvegardé avec succès.");
                } catch (IOException e) {
                    buffer = "SERVER_ERROR".getBytes(StandardCharsets.UTF_8);
                    server.sendPacket(new DatagramPacket(buffer, buffer.length, address, clientPort));
                    logger.error(e.getMessage());
                    return;
                }
            }

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, address, clientPort);
            logger.info(playerName + " (" + uuid + ") a rejoint le serveur ! (" + clients.size() + "/???)");
            server.getPluginManager().invokePlayerJoin(server.getClients().size());
            server.announceMessage(playerName + " a rejoint le serveur.", ChatColor.YELLOW);

            if (!lastActivities.containsKey(uuid)) {
                lastActivities.put(uuid, System.currentTimeMillis());
                TimeoutHandler handler = new TimeoutHandler(server, uuid);
                handler.start();
            }

            server.sendPacket(packet);

            Command tp = server.getCommands().get("/tp");
            tp.initTree(server);

        } catch (IOException e) {
            byte[] buffer = "INVALID_TOKEN".getBytes(StandardCharsets.UTF_8);
            server.sendPacket(new DatagramPacket(buffer, buffer.length, address, clientPort));
            logger.error(e.getMessage());
        }
    }
}
