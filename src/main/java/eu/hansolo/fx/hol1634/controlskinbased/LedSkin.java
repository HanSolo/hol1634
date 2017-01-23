/*
 * Copyright (c) 2016 by Gerrit Grunwald
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package eu.hansolo.fx.hol1634.controlskinbased;

import javafx.beans.InvalidationListener;
import javafx.scene.control.Skin;
import javafx.scene.control.SkinBase;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;


/**
 * Created by hansolo on 12.08.16.
 */
public class LedSkin extends SkinBase<CustomControl> implements Skin<CustomControl> {
    private static final double               PREFERRED_WIDTH  = 16;
    private static final double               PREFERRED_HEIGHT = 16;
    private static final double               MINIMUM_WIDTH    = 8;
    private static final double               MINIMUM_HEIGHT   = 8;
    private static final double               MAXIMUM_WIDTH    = 1024;
    private static final double               MAXIMUM_HEIGHT   = 1024;
    private              double               size;
    private              Region               frame;
    private              Region               main;
    private              Region               highlight;
    private              InnerShadow          innerShadow;
    private              DropShadow           glow;
    private              CustomControl        control;
    private              InvalidationListener sizeListener;
    private              InvalidationListener colorListener;
    private              InvalidationListener onListener;


    // ******************** Constructors **************************************
    public LedSkin(final CustomControl CONTROL) {
        super(CONTROL);
        control       = CONTROL;
        sizeListener  = o -> handleControlPropertyChanged("RESIZE");
        colorListener = o -> handleControlPropertyChanged("COLOR");
        onListener    = o -> handleControlPropertyChanged("ON");
        initGraphics();
        registerListeners();
    }


    // ******************** Initialization ************************************
    private void initGraphics() {
        if (Double.compare(control.getPrefWidth(), 0.0) <= 0 || Double.compare(control.getPrefHeight(), 0.0) <= 0 ||
            Double.compare(control.getWidth(), 0.0) <= 0 || Double.compare(control.getHeight(), 0.0) <= 0) {
            if (control.getPrefWidth() > 0 && control.getPrefHeight() > 0) {
                control.setPrefSize(control.getPrefWidth(), control.getPrefHeight());
            } else {
                control.setPrefSize(PREFERRED_WIDTH, PREFERRED_HEIGHT);
            }
        }

        frame = new Region();
        frame.getStyleClass().setAll("frame");

        main = new Region();
        main.getStyleClass().setAll("main");
        main.setStyle(String.join("", "-color: ", control.getColor().toString().replace("0x", "#"), ";"));

        innerShadow = new InnerShadow(BlurType.TWO_PASS_BOX, Color.rgb(0, 0, 0, 0.65), 8, 0, 0, 0);

        glow = new DropShadow(BlurType.TWO_PASS_BOX, control.getColor(), 20, 0, 0, 0);
        glow.setInput(innerShadow);

        highlight = new Region();
        highlight.getStyleClass().setAll("highlight");

        getChildren().addAll(frame, main, highlight);
    }

    private void registerListeners() {
        control.widthProperty().addListener(sizeListener);
        control.heightProperty().addListener(sizeListener);
        control.colorProperty().addListener(colorListener);
        control.onProperty().addListener(onListener);
    }


    // ******************** Methods *******************************************
    @Override public void layoutChildren(final double X, final double Y, final double WIDTH, final double HEIGHT) {
        super.layoutChildren(X, Y, WIDTH, HEIGHT);
    }

    @Override protected double computeMinWidth(final double HEIGHT, final double TOP, final double RIGHT, final double BOTTOM, final double LEFT)  { return MINIMUM_WIDTH; }
    @Override protected double computeMinHeight(final double WIDTH, final double TOP, final double RIGHT, final double BOTTOM, final double LEFT)  { return MINIMUM_HEIGHT; }
    @Override protected double computePrefWidth(final double HEIGHT, final double TOP, final double RIGHT, final double BOTTOM, final double LEFT) { return super.computePrefWidth(HEIGHT, TOP, RIGHT, BOTTOM, LEFT); }
    @Override protected double computePrefHeight(final double WIDTH, final double TOP, final double RIGHT, final double BOTTOM, final double LEFT) { return super.computePrefHeight(WIDTH, TOP, RIGHT, BOTTOM, LEFT); }
    @Override protected double computeMaxWidth(final double HEIGHT, final double TOP, final double RIGHT, final double BOTTOM, final double LEFT)  { return MAXIMUM_WIDTH; }
    @Override protected double computeMaxHeight(final double WIDTH, final double TOP, final double RIGHT, final double BOTTOM, final double LEFT)  { return MAXIMUM_HEIGHT; }

    protected void handleControlPropertyChanged(final String PROPERTY) {
        if ("RESIZE".equals(PROPERTY)) {
            resize();
        } else if ("COLOR".equals(PROPERTY)) {
            main.setStyle(String.join("", "-color: ", (control.getColor()).toString().replace("0x", "#"), ";"));
            resize();
        } else if ("ON".equals(PROPERTY)) {
            main.setEffect(control.isOn() ? glow : innerShadow);
        }
    }

    @Override public void dispose() {
        control.widthProperty().removeListener(sizeListener);
        control.heightProperty().removeListener(sizeListener);
        control.colorProperty().removeListener(colorListener);
        control.onProperty().removeListener(onListener);
        control = null;
    }


    // ******************** Resizing ******************************************
    private void resize() {
        double width  = control.getWidth() - control.getInsets().getLeft() - control.getInsets().getRight();
        double height = control.getHeight() - control.getInsets().getTop() - control.getInsets().getBottom();
        size          = width < height ? width : height;

        if (size > 0) {
            innerShadow.setRadius(0.07 * size);
            glow.setRadius(0.36 * size);
            glow.setColor(control.getColor());

            frame.setMaxSize(size, size);

            main.setMaxSize(0.72 * size, 0.72 * size);
            main.relocate(0.14 * size, 0.14 * size);
            main.setEffect(control.isOn() ? glow : innerShadow);

            highlight.setMaxSize(0.58 * size, 0.58 * size);
            highlight.relocate(0.21 * size, 0.21 * size);
        }
    }
}
