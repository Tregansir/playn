/**
 * Copyright 2010 The PlayN Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package playn.html;

import com.google.gwt.canvas.dom.client.CanvasPixelArray;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.ImageData;

import playn.core.Canvas;
import playn.core.CanvasImage;
import playn.core.gl.GLContext;

class HtmlCanvasImage extends HtmlImage implements CanvasImage {

  private HtmlCanvas canvas;

  public HtmlCanvasImage(GLContext ctx, HtmlCanvas canvas) {
    super(ctx, canvas.canvas());
    this.canvas = canvas;
  }

  @Override
  public Canvas canvas() {
    return canvas;
  }

  @Override
  public int ensureTexture(boolean repeatX, boolean repeatY) {
    if (canvas.dirty()) {
      canvas.clearDirty();
      clearTexture();
    }
    return super.ensureTexture(repeatX, repeatY);
  }

  @Override
  public void setRgb(int startX, int startY, int width, int height,
      int[] rgbArray, int offset, int scanSize) {
    Context2d ctx = canvas.canvas().getContext2d();
    ImageData imageData = ctx.getImageData(startX, startY, width, height);
    CanvasPixelArray pixelData = imageData.getData();
    int i = 0;
    int dst = offset;
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x ++) {
        int rgba = rgbArray[dst + x];
        pixelData.set(i++, (rgba >> 16) & 255);
        pixelData.set(i++, (rgba >> 8) & 255);
        pixelData.set(i++, (rgba) & 255);
        pixelData.set(i++, (rgba >> 24) & 255);
      }
      dst += scanSize;
    }
  }
}
