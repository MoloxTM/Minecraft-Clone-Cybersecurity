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

package fr.math.minecraft.server;

import com.fasterxml.jackson.databind.JsonNode;
import fr.math.minecraft.client.network.packet.PlayerActionsPacket;
import fr.math.minecraft.logger.LogType;
import fr.math.minecraft.logger.LoggerUtility;
import fr.math.minecraft.server.command.*;
import fr.math.minecraft.server.handler.*;
import fr.math.minecraft.server.manager.ChunkManager;
import fr.math.minecraft.server.manager.PluginManager;
import fr.math.minecraft.server.pathfinding.AStar;
import fr.math.minecraft.server.pathfinding.Node;
import fr.math.minecraft.shared.ChatColor;
import fr.math.minecraft.shared.ChatMessage;
import fr.math.minecraft.shared.entity.Entity;
import fr.math.minecraft.shared.entity.EntityFactory;
import fr.math.minecraft.shared.entity.Villager;
import fr.math.minecraft.shared.entity.mob.Zombie;
import fr.math.minecraft.shared.entity.network.MainPC;
import fr.math.minecraft.shared.entity.network.Router;
import fr.math.minecraft.shared.inventory.Trame;
import fr.math.minecraft.shared.world.World;
import org.apache.log4j.Logger;
import org.joml.Vector3f;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class MinecraftServer {

    private static MinecraftServer instance = null;
    private DatagramSocket socket;
    private boolean running;
    private final byte[] buffer;
    private int port;
    private final static Logger logger = LoggerUtility.getServerLogger(MinecraftServer.class, LogType.TXT);;
    private final Map<String, Client> clients;
    private final Map<String, String> sockets;
    private final Map<String, Long> lastActivities;
    private final World world;
    private final static int MAX_REQUEST_SIZE = 16384;
    private final ThreadPoolExecutor packetQueue;
    private final ThreadPoolExecutor pathfindingQueue;
    private final TickHandler tickHandler;
    private final ChunkManager chunkManager;
    private final List<ChatMessage> chatMessages;
    private final PluginManager pluginManager;
    private final HashMap<String, Command> commands;
    private HashMap<String, Trame> tramesMap;

    private MinecraftServer(int port) {
        this.running = false;
        this.buffer = new byte[MAX_REQUEST_SIZE];
        this.port = port;
        this.clients = new HashMap<>();
        this.sockets = new HashMap<>();
        this.lastActivities = new HashMap<>();
        this.world = new World();
        this.pluginManager = new PluginManager();
        this.packetQueue = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
        this.pathfindingQueue = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
        this.tickHandler = new TickHandler();
        this.chunkManager = new ChunkManager();
        this.chatMessages = new ArrayList<>();
        this.commands = new HashMap<>();
        this.tramesMap = new HashMap<>();

        try {
            this.pluginManager.loadPlugins("plugins");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        logger.info("Point de spawn calculé en " + world.getSpawnPosition());



        Router router = new Router("Pablo");
        MainPC mainPC = new MainPC("Carlos");
        Trame villagerTrame = new Trame();
        villagerTrame.setTrame(router, mainPC, "test");
        Villager villager = new Villager("Gustavo", villagerTrame);

        tramesMap.put(villager.getName(), villagerTrame);

        villager.setPosition(new Vector3f(10, 7, 0));
        router.setPosition(new Vector3f(30, 7, 0));
        mainPC.setPosition(new Vector3f(30, 7, 20));

        /*
        villager.getCheckpoints().add(villager.getPosition());

        router.getRoutingTable().put(mainPC.getPosition(), router.setRoute(mainPC.getPosition()));

        villager.setServiceRequested(mainPC);
        */

        world.addEntity(villager);
        world.addEntity(router);
        world.addEntity(mainPC);

        initCommands();
    }

    public void start() throws IOException {
        this.running = true;
        socket = new DatagramSocket(this.port);
        System.out.println("Serveur en écoute sur le port " + this.port + "...");

        tickHandler.start();

        while (this.running) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.receive(packet);

            InetAddress address = packet.getAddress();
            int clientPort = packet.getPort();

            ClientHandler handler = new ClientHandler();
            JsonNode packetData = handler.parsePacket(packet);

            if (packetData == null)
                continue;

            String packetType = packetData.get("type").asText();
            byte[] buffer;
            switch (packetType) {
                case "CONNECTION_INIT_ACK":
                    ConnectionACKHandler ackHandler = new ConnectionACKHandler(packetData, address, clientPort);
                    ackHandler.run();
                    break;
                case "CONNECTION_INIT":
                    ConnectionInitHandler connectionInitHandler = new ConnectionInitHandler(packetData, address, clientPort);
                    connectionInitHandler.run();
                    break;
                case "PLAYER_ACTIONS":
                    String playerId = packetData.get("uuid").asText();

                    Client client = clients.get(playerId);
                    if (client == null) break;

                    PlayerActionsHandler actionsHandler = new PlayerActionsHandler(client, packetData, address, clientPort);
                    packetQueue.submit(actionsHandler);
                    break;
                case "SKIN_REQUEST":
                    SkinRequestHandler skinHandler = new SkinRequestHandler(packetData, address, clientPort);
                    skinHandler.run();
                    break;
                case "PING_PACKET":
                    PingHandler pingHandler = new PingHandler(packetData, address, clientPort);
                    pingHandler.run();
                    break;
                case "CHUNK_REQUEST":
                    ChunkRequestHandler chunkRequestHandler = new ChunkRequestHandler(packetData, address, clientPort);
                    packetQueue.submit(chunkRequestHandler);
                    break;
                case "PLAYERS_LIST_REQUEST":
                    PlayersListHandler playersListHandler = new PlayersListHandler(packetData, address, clientPort);
                    playersListHandler.run();
                    break;
                case "CHAT_MSG":
                    String content = packetData.get("content").asText();
                    if(content.charAt(0) != '/') {
                        ChatMessageHandler chatMessageHandler = new ChatMessageHandler(packetData, address, clientPort);
                        chatMessageHandler.run();
                    } else {
                        CommandHandler commandHandler = new CommandHandler(packetData, address, clientPort);
                        commandHandler.run();
                    }
                    break;
                case "LOADING_MAP_ACK":
                    LoadingMapACKHandler loadingMapACKHandler = new LoadingMapACKHandler(packetData, address, clientPort);
                    loadingMapACKHandler.run();
                    break;
                default:
                    String message = "UNAUTHORIZED_PACKET";
                    buffer = message.getBytes(StandardCharsets.UTF_8);
                    this.sendPacket(new DatagramPacket(buffer, buffer.length, address, clientPort));
                    logger.error("Type de packet : " + packetType + " non reconnu.");
            }
        }
        socket.close();
        for (Plugin plugin : pluginManager.getPlugins()) {
            plugin.onDisable();
        }
    }

    public synchronized void sendPacket(DatagramPacket packet) {
        try {
            socket.send(packet);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static MinecraftServer getInstance() {
        if(instance == null) {
            instance = new MinecraftServer(50000);
        }
        return instance;
    }

    public World getWorld() {
        return world;
    }

    public Map<String, Long> getLastActivities() {
        return lastActivities;
    }

    public Map<String, Client> getClients() {
        return clients;
    }

    public void broadcast(String message) {
        synchronized (this.getClients()) {
            byte[] buffer = message.getBytes(StandardCharsets.UTF_8);
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            for (Client client : this.getClients().values()) {
                if (!client.isActive()) continue;
                packet.setAddress(client.getAddress());
                packet.setPort(client.getPort());

                this.sendPacket(packet);
            }
        }
    }

    public void broadcast(byte[] buffer) {
        synchronized (this.getClients()) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            for (Client client : this.getClients().values()) {
                if (!client.isActive()) continue;

                packet.setAddress(client.getAddress());
                packet.setPort(client.getPort());

                this.sendPacket(packet);
            }
        }
    }

    public void initCommands() {
        commands.put("/tp", new TeleportCommand("tp", "Téléporte un joueur à une destination", Team.ADMIN));
        commands.put("/start", new StartCommand("start", "Lance la partie", Team.PLAYER));
        commands.put("/load", new LoadSchematicCommand("load", "Charge un schematic", Team.ADMIN));
        commands.put("/place", new PlaceBlockCommand("place", "Pose un block", Team.ADMIN));
        commands.put("/gamemode", new GamemodeCommand("/gamemode", "Change le gamemode du joueur", Team.ADMIN));
    }

    public void broadcastMessage(String message) {
        this.broadcastMessage(message, ChatColor.WHITE);
    }

    public void broadcastMessage(String message, ChatColor color) {
        chatMessages.add(new ChatMessage("CONSOLE", "CONSOLE", message, color));
    }

    public void announceMessage(String message) {
        this.announceMessage(message, ChatColor.WHITE);
    }

    public void announceMessage(String message, ChatColor color) {
        chatMessages.add(new ChatMessage("ANNOUNCE", "ANNOUNCE", message, color));
    }

    public DatagramSocket getSocket() {
        return socket;
    }

    public TickHandler getTickHandler() {
        return tickHandler;
    }

    public ChunkManager getChunkManager() {
        return chunkManager;
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public ThreadPoolExecutor getPathfindingQueue() {
        return pathfindingQueue;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public HashMap<String, Command> getCommands() {
        return commands;
    }

    public Client getClientByName(String name) {
        for (Client client: getClients().values()) {
            if(client.getName().equals(name)) {
                return client;
            }
        }
        return null;
    }

    public HashMap<String, Trame> getTramesMap() {
        return tramesMap;
    }
}
