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

package fr.math.minecraft.shared.inventory.items.shovel;

import fr.math.minecraft.shared.inventory.CraftData;
import fr.math.minecraft.shared.inventory.CraftRecipes;
import fr.math.minecraft.shared.inventory.ItemStack;
import fr.math.minecraft.shared.world.Material;

public class DiamondShovelCraft extends CraftRecipes {

    public DiamondShovelCraft() {
        super(new ItemStack(Material.DIAMOND_SHOVEL, 1));
        fillRecipe();
    }
    @Override
    public void fillRecipe() {
        CraftData data1 = new CraftData(new byte[]
                {
                        Material.DIAMOND.getId(), Material.AIR.getId(), Material.AIR.getId(),
                        Material.STICK.getId(), Material.AIR.getId(), Material.AIR.getId(),
                        Material.STICK.getId(), Material.AIR.getId(), Material.AIR.getId()
                }
        );

        CraftData data2 = new CraftData(new byte[]
                {
                        Material.AIR.getId(), Material.DIAMOND.getId(), Material.AIR.getId(),
                        Material.AIR.getId(), Material.STICK.getId(), Material.AIR.getId(),
                        Material.AIR.getId(), Material.STICK.getId(), Material.AIR.getId()
                }
        );

        CraftData data3 = new CraftData(new byte[]
                {
                        Material.AIR.getId(), Material.AIR.getId(), Material.DIAMOND.getId(),
                        Material.AIR.getId(), Material.AIR.getId(), Material.STICK.getId(),
                        Material.AIR.getId(), Material.AIR.getId(), Material.STICK.getId()
                }
        );

        craftingTable.add(data1);
        craftingTable.add(data2);
        craftingTable.add(data3);

    }
}

