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

import fr.math.minecraft.client.entity.player.Player;
import fr.math.minecraft.server.command.Command;
import fr.math.minecraft.shared.world.Chunk;
import fr.math.minecraft.shared.world.Coordinates;
import org.joml.Vector3f;
import org.joml.Vector3i;

import java.util.HashMap;

public class Utils {

    public static double distance(Player player, Vector3i position) {

        double x = Math.pow(player.getPosition().x - position.x, 2);
        double y = Math.pow(player.getPosition().y - position.y, 2);
        double z = Math.pow(player.getPosition().z - position.z, 2);

        return Math.sqrt(x + y + z);
    }

    public static double distance(Coordinates c1, Coordinates c2) {

        double x = Math.pow(c1.getX() - c2.getX(), 2);
        double y = Math.pow(c1.getY() - c2.getY(), 2);
        double z = Math.pow(c1.getZ() - c2.getZ(), 2);

        return Math.sqrt(x + y + z);
    }

    public static double distance(Vector3f c1, Vector3f c2) {

        double x = Math.pow(c1.x - c2.x, 2);
        double y = Math.pow(c1.y - c2.y, 2);
        double z = Math.pow(c1.z - c2.z, 2);

        return Math.sqrt(x + y + z);
    }

    public static double distance(Player player, Coordinates position) {
        return distance(player, new Vector3f(position.getX(), position.getY(), position.getZ()));
    }

    public static double distance(Player player, Vector3f position) {

        double x = Math.pow(player.getPosition().x - position.x, 2);
        double y = Math.pow(player.getPosition().y - position.y, 2);
        double z = Math.pow(player.getPosition().z - position.z, 2);

        return Math.sqrt(x + y + z);
    }

    public static Vector3i worldToLocal(Vector3i worldPosition) {
        int blockX = worldPosition.x % Chunk.SIZE;
        int blockY = worldPosition.y % Chunk.SIZE;
        int blockZ = worldPosition.z % Chunk.SIZE;

        blockX = blockX < 0 ? blockX + Chunk.SIZE : blockX;
        blockY = blockY < 0 ? blockY + Chunk.SIZE : blockY;
        blockZ = blockZ < 0 ? blockZ + Chunk.SIZE : blockZ;

        return new Vector3i(blockX, blockY, blockZ);
    }

    public static Vector3i getChunkPosition(float x, float y, float z) {

        int chunkX = (int) Math.floor(x / (double) Chunk.SIZE);
        int chunkY = (int) Math.floor(y / (double) Chunk.SIZE);
        int chunkZ = (int) Math.floor(z / (double) Chunk.SIZE);

        return new Vector3i(chunkX, chunkY, chunkZ);
    }

    public static boolean isCommand(HashMap<String, Command> commands, StringBuilder message) {
        String clearedMsg = message.toString();
        int firstSpace = clearedMsg.indexOf(" ");
        System.out.println("Message cleared :" + clearedMsg);
        if(!clearedMsg.startsWith("/")) return false;
        if(commands.containsKey(clearedMsg)) return true;
        return false;
    }

}
