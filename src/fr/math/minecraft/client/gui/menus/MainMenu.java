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

package fr.math.minecraft.client.gui.menus;

import fr.math.minecraft.client.Camera;
import fr.math.minecraft.client.Game;
import fr.math.minecraft.client.gui.buttons.AuthButton;
import fr.math.minecraft.client.gui.buttons.CyberPlayButton;
import fr.math.minecraft.shared.GameConfiguration;
import fr.math.minecraft.client.entity.player.Player;
import fr.math.minecraft.client.gui.buttons.BlockButton;
import fr.math.minecraft.client.gui.GuiText;
import fr.math.minecraft.client.gui.buttons.PlayButton;
import fr.math.minecraft.client.manager.FontManager;
import fr.math.minecraft.client.meshs.FontMesh;

public class MainMenu extends Menu {

    private GuiText splashText;
    private float splashScale, scaleFactor;

    public MainMenu(Game game) {
        super(game);
        this.splashScale = GameConfiguration.DEFAULT_SCALE;
        this.scaleFactor = 1;
    }

    @Override
    public void loadContent() {

        FontManager fontManager = new FontManager();
        FontMesh fontMesh = Game.getInstance().getRenderer().getFontMesh();

        float splashOffset = (float) (splashScale % 0.1) * 100;
        float splashWidth = fontManager.getTextWidth(fontMesh, game.getSplashText());

        BlockButton playButton = new PlayButton();
        BlockButton cyberPlayButton = new CyberPlayButton();
        BlockButton authButton = new AuthButton();
        GuiText versionText = new GuiText("Minecraft 1.0.0", 5, 5, 0xFFFFFF);
        GuiText copyrightText = new GuiText("Copyright Me and the hoes.", GameConfiguration.WINDOW_WIDTH - fontManager.getTextWidth(fontMesh, "Copyright Me and the hoes.") - 5, 5, 0xFFFFFF);
        this.splashText = new GuiText(game.getSplashText(), (float)((GameConfiguration.WINDOW_WIDTH * 0.7) - (splashWidth / 2.0f) - splashOffset), (float) (GameConfiguration.WINDOW_HEIGHT - (GameConfiguration.WINDOW_HEIGHT* 0.25)), -9, 0xFFFF00);

        splashText.rotate(10);

        this.buttons.add(playButton);
        this.buttons.add(cyberPlayButton);
        this.buttons.add(authButton);
        this.texts.add(versionText);
        this.texts.add(copyrightText);
        this.texts.add(splashText);
    }

    @Override
    public MenuBackgroundType getBackgroundType() {
        return MenuBackgroundType.SKYBOX_BACKGROUND;
    }

    @Override
    public void update() {
        if (splashScale >= 1.1f * GameConfiguration.DEFAULT_SCALE) scaleFactor = -1;
        if (splashScale <= GameConfiguration.DEFAULT_SCALE) scaleFactor = 1;

        splashScale += (scaleFactor * 0.0005);
        splashText.scale(splashScale);

        Player player = game.getPlayer();
        Camera camera = game.getCamera();

        player.setYaw(player.getYaw() + 0.03f);
        camera.update(player);
    }

}
