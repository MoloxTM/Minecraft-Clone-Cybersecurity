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

package fr.math.minecraft.shared;

public class GameConfiguration {

    public final static float UPS = 200.0f;
    public final static float TICK_PER_SECONDS = 20.0f;
    public final static float TICK_RATE = 1.0f / TICK_PER_SECONDS;
    public final static float UPDATE_TICK = 1.0f / UPS;
    public final static float WINDOW_WIDTH = 854.0f;
    public final static float WINDOW_HEIGHT = 480.0f;
    public final static float WINDOW_CENTER_X = WINDOW_WIDTH / 2.0f;
    public final static float WINDOW_CENTER_Y = WINDOW_HEIGHT / 2.0f;
    public final static String FONT_FILE_PATH = "res/fonts/Monocraft.ttf";
    public final static int FONT_SIZE = 64;
    public final static float NAMETAG_FONT_SIZE = 32.0f;
    public final static int CHUNK_RENDER_DISTANCE = 6;
    public final static String SPLASHES_FILE_PATH = "res/splashes.txt";
    public final static float DEFAULT_SCALE = 0.28f;
    public final static float MENU_TITLE_SCALE = 0.3f;
    public final static float MENU_SUBTITLE_SCALE = DEFAULT_SCALE;
    public final static int BUFFER_SIZE = 1024;
    public final static float CHUNK_TICK = 60.0f;
    public final static float CHUNK_TICK_RATE = 1000.0f / CHUNK_TICK;
    public final static float ATTACK_REACH = 3.5f;
    public final static float BUILDING_REACH = 4.5f;
    public final static float BREAKING_REACH = 4.5f;
    public final static float DEFAULT_SPEED= 0.0125f;
    public final static float SPRINT_SPEED = DEFAULT_SPEED * 2f ;
    public final static int BLOCK_BREAK_COOLDOWN = (int) UPS / 3;
    public final static int REGION_SIZE = 8;
    public final static float INVENTORY_TEXTURE_WIDTH = 256.0f;
    public final static float INVENTORY_TEXTURE_HEIGHT = 256.0f;

    public final static float TRAME_TEXTURE_WIDTH = 252.0f;
    public final static float TRAME_TEXTURE_HEIGHT = 183.0f;
    public final static int PLAYER_INVENTORY_SIZE = 27;
    public final static float KNOCK_BACK_X = 0.03f;
    public final static float KNOCK_BACK_Y = 0.14f;
    public final static float KNOCK_BACK_Z = 0.03f;
    public final static float MIN_AGGRO_DISTANCE = 10;
    public static String WORLD_TYPE = "SUPERFLAT_WORLD";
    public final static float MAX_ASTAR_DISTANCE = 150;
    public final static String MAP_FILE_PATH = "res/schematics/house.schematic";
    public final static int MAX_BLOCK_PACKET = 500;

    private boolean entityInterpolation;
    private boolean occlusionEnabled;
    private boolean debugging;
    private boolean musicEnabled;
    private float guiScale;
    private boolean entitesPathEnabled;
    private static GameConfiguration instance = null;

    private GameConfiguration() {
        this.entityInterpolation = true;
        this.occlusionEnabled = true;
        this.debugging = true;
        this.musicEnabled = true;
        this.entitesPathEnabled = true;
        this.guiScale = 1.0f;
    }

    public boolean isOcclusionEnabled() {
        return occlusionEnabled;
    }

    public void setOcclusionEnabled(boolean occlusionEnabled) {
        this.occlusionEnabled = occlusionEnabled;
    }

    public boolean isEntityInterpolationEnabled() {
        return entityInterpolation;
    }

    public void setEntityInterpolation(boolean entityInterpolation) {
        this.entityInterpolation = entityInterpolation;
    }

    public void setDebugging(boolean debugging) {
        this.debugging = debugging;
    }

    public boolean isDebugging() {
        return debugging;
    }

    public boolean isMusicEnabled() {
        return musicEnabled;
    }

    public void disableMusic() {
        musicEnabled = false;
    }

    public void enableMusic() {
        musicEnabled = true;
    }

    public float getGuiScale() {
        return guiScale;
    }

    public void setGuiScale(float guiScale) {
        this.guiScale = guiScale;
    }

    public boolean isEntitesPathEnabled() {
        return entitesPathEnabled;
    }

    public void setEntitesPathEnabled(boolean entitesPathEnabled) {
        this.entitesPathEnabled = entitesPathEnabled;
    }

    public static void setWorldType(String worldType) {
        WORLD_TYPE = worldType;
    }

    public static GameConfiguration getInstance() {
        if (instance == null) {
            instance = new GameConfiguration();
        }
        return instance;
    }
}