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

import fr.math.minecraft.client.Shader;
import fr.math.minecraft.client.buffers.EBO;
import fr.math.minecraft.client.buffers.VAO;
import fr.math.minecraft.client.buffers.VBO;
import fr.math.minecraft.client.fonts.CFont;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;

import static org.lwjgl.opengl.GL33.*;

public class FontMesh extends Mesh {

    public final static int BATCH_SIZE = 100;
    public final static int VERTEX_SIZE = 9;
    private final float[] vertices;
    private int size;
    private final CFont font;
    private final FloatBuffer buffer;

    public FontMesh(CFont font) {
        this.size = 0;
        this.font = font;
        this.vertices = new float[BATCH_SIZE * VERTEX_SIZE];
        indices = new int[] {
            0, 1, 3,
            1, 2, 3
        };
        this.buffer = BufferUtils.createFloatBuffer(BATCH_SIZE * VERTEX_SIZE);
        this.init();
    }


    @Override
    public void init() {

        vao = new VAO();
        vao.bind();

        vbo = new VBO(BATCH_SIZE * VERTEX_SIZE, GL_DYNAMIC_DRAW);

        int elementsSize = BATCH_SIZE * 5;
        int[] elementsBuffer = new int[elementsSize];

        for (int i = 0; i < elementsSize; i++) {
            elementsBuffer[i] = indices[i % 6] + (i / 6) * 4;
        }

        EBO ebo = new EBO(elementsBuffer);

        vao.linkAttrib(vbo, 0, 3, GL_FLOAT, VERTEX_SIZE * Float.BYTES, 0);
        vao.linkAttrib(vbo, 1, 3, GL_FLOAT, VERTEX_SIZE * Float.BYTES, 3 * Float.BYTES);
        vao.linkAttrib(vbo, 2, 2, GL_FLOAT, VERTEX_SIZE * Float.BYTES, 6 * Float.BYTES);
        vao.linkAttrib(vbo, 3, 1, GL_FLOAT, VERTEX_SIZE * Float.BYTES, 8 * Float.BYTES);

        vao.unbind();
        vbo.unbind();
        ebo.unbind();
    }

    @Override
    public void draw() {
        vao.bind();
        glDrawElements(GL_TRIANGLES, size * 6, GL_UNSIGNED_INT, 0);
        vao.unbind();
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public float[] getVertices() {
        return vertices;
    }

    public CFont getFont() {
        return font;
    }

    public void flush() {
        vbo.bind();
        vbo.bufferFloat(buffer, GL_DYNAMIC_DRAW);
        glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        this.draw();
        size = 0;
    }
}
