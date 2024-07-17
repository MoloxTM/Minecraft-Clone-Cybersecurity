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

package fr.math.minecraft.shared.world;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.joml.Vector3i;

import java.util.Objects;

public class PlacedBlock {

    private final String clientUuid;
    private final Vector3i worldPosition;
    private final Vector3i localPosition;
    private final byte block;

    public PlacedBlock(String clientUuid, Vector3i worldPosition, Vector3i localPosition, byte block) {
        this.clientUuid = clientUuid;
        this.worldPosition = worldPosition;
        this.localPosition = localPosition;
        this.block = block;
    }

    public PlacedBlock(JsonNode data) {
        int worldX = data.get("wx").asInt();
        int worldY = data.get("wy").asInt();
        int worldZ = data.get("wz").asInt();

        int localX = data.get("lx").asInt();
        int localY = data.get("ly").asInt();
        int localZ = data.get("lz").asInt();

        byte block = (byte) data.get("block").asInt();
        String playerUuid = data.get("playerUuid").asText();

        this.worldPosition = new Vector3i(worldX, worldY, worldZ);
        this.localPosition = new Vector3i(localX, localY, localZ);
        this.block = block;
        this.clientUuid = playerUuid;
    }

    public Vector3i getWorldPosition() {
        return worldPosition;
    }

    public Vector3i getLocalPosition() {
        return localPosition;
    }

    public byte getBlock() {
        return block;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlacedBlock that = (PlacedBlock) o;
        return block == that.block && Objects.equals(worldPosition, that.worldPosition) && Objects.equals(localPosition, that.localPosition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(worldPosition, localPosition, block);
    }

    @Override
    public String toString() {
        return "PlacedBlock{" +
                "worldPosition=" + worldPosition +
                ", localPosition=" + localPosition +
                ", block=" + block +
                '}';
    }

    public ObjectNode toJSONObject() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode blockNode = mapper.createObjectNode();

        blockNode.put("wx", worldPosition.x);
        blockNode.put("wy", worldPosition.y);
        blockNode.put("wz", worldPosition.z);
        blockNode.put("lx", localPosition.x);
        blockNode.put("ly", localPosition.y);
        blockNode.put("lz", localPosition.z);
        blockNode.put("block", block);
        blockNode.put("playerUuid", clientUuid);

        return blockNode;
    }

    public String getClientUuid() {
        return clientUuid;
    }
}
