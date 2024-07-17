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

package fr.math.minecraft.client.meshs;

import fr.math.minecraft.client.buffers.EBO;
import fr.math.minecraft.client.buffers.VAO;
import fr.math.minecraft.client.buffers.VBO;

import static org.lwjgl.opengl.GL33.*;

public class SkyboxMesh extends Mesh {

    private final float[] vertices;

    public SkyboxMesh() {
        this.vertices = new float[] {
            //   Coordinates
            -1.0f, -1.0f,  1.0f,//        7--------6
            1.0f, -1.0f,  1.0f,//       /|       /|
            1.0f, -1.0f, -1.0f,//      4--------5 |
            -1.0f, -1.0f, -1.0f,//      | |      | |
            -1.0f,  1.0f,  1.0f,//      | 3------|-2
            1.0f,  1.0f,  1.0f,//      |/       |/
            1.0f,  1.0f, -1.0f,//      0--------1
            -1.0f,  1.0f, -1.0f
        };
        this.indices = new int[] {
            // Right
            1, 5, 6,
            6, 2, 1,
            // Left
            0, 4, 7,
            7, 3, 0,
            // Top
            4, 7, 6,
            6, 5, 4,
            // Bottom
            0, 3, 2,
            2, 1, 0,
            // Back
            0, 4, 5,
            5, 1, 0,
            // Front
            3, 7, 6,
            6, 2, 3
        };
        this.init();
    }

    @Override
    public void init() {
        this.vao = new VAO();
        vao.bind();

        this.vbo = new VBO(vertices);
        this.ebo = new EBO(indices);

        vao.linkAttrib(vbo, 0, 3, GL_FLOAT, 3 * Float.BYTES, 0);

        vao.unbind();
        vbo.unbind();
        ebo.unbind();
    }

    @Override
    public void draw() {
        vao.bind();
        glDrawElements(GL_TRIANGLES, 36, GL_UNSIGNED_INT, 0);
        vao.unbind();
    }
}
