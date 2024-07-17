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

import fr.math.minecraft.logger.LogType;
import fr.math.minecraft.logger.LoggerUtility;
import org.apache.log4j.Logger;

import java.util.Map;

public class TimeoutHandler extends Thread {

    private final MinecraftServer server;
    private final static double TIMEOUT_DELAY_MS = 15000;
    private final String uuid;
    private final static Logger logger = LoggerUtility.getServerLogger(TimeoutHandler.class, LogType.TXT);;

    public TimeoutHandler(MinecraftServer server, String uuid) {
        this.server = server;
        this.uuid = uuid;
    }

    @Override
    public void run() {
        boolean timeout = false;
        while (!timeout) {
            long currentTime = System.currentTimeMillis();
            synchronized (server.getLastActivities()) {
                long lastTimeSeen = server.getLastActivities().get(uuid);
                if (currentTime - lastTimeSeen > TIMEOUT_DELAY_MS) {
                    synchronized (server.getClients()) {
                        Client client = server.getClients().get(uuid);
                        if (!client.isActive()) continue;
                        String clientName = client.getName();
                        timeout = true;

                        server.getClients().remove(uuid);
                        server.getLastActivities().remove(uuid);
                        logger.info("La connexion avec le client " + uuid + " (" + clientName + ") a été perdu... (déconnexion)");
                        logger.info(clientName + " a quitté la partie. (" + server.getClients().size() + "/???)");
                    }
                }
            }
        }
    }
}
