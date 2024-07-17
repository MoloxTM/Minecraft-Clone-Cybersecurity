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

package fr.math.minecraft.shared.entity;

import org.joml.Vector3f;

public enum EntityType {

    PLAYER("Steve", 20.0f, 20.0f, 18.0f, 20.0f, new Vector3f(0, 0.25f, 0)),
    VILLAGER("Dummy", 100.0f, 100.0f, 0.0f, 0.0f, new Vector3f(0.25f, 0.32f, 0.25f)),
    ZOMBIE("Zombie", 100.0f, 100.0f, 0.0f, 0.0f, new Vector3f(0, 0.25f, 0));

    private final String name;
    private final float health;
    private final float maxHealth;
    private final float hunger;
    private final float maxHunger;
    private final Vector3f offset;

    EntityType(String name, float health, float maxHealth, float hunger, float maxHunger, Vector3f offset) {
        this.name = name;
        this.health = health;
        this.maxHealth = maxHealth;
        this.offset = offset;
        this.hunger = hunger;
        this.maxHunger = maxHunger;
    }

    public String getName() {
        return name;
    }

    public float getHealth() {
        return health;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public Vector3f getOffset() {
        return offset;
    }

    public float getHunger() {
        return hunger;
    }

    public float getMaxHunger() {
        return maxHunger;
    }
}
