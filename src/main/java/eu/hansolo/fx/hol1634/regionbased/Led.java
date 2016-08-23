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

package eu.hansolo.fx.hol1634.regionbased;

import javafx.beans.DefaultProperty;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.scene.Node;
import javafx.scene.effect.BlurType;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

import java.util.List;


@DefaultProperty("children")
public class Led extends Region {
    // Model/Controller related
    private static final StyleablePropertyFactory<Led> FACTORY =
        new StyleablePropertyFactory<>(Region.getClassCssMetaData());

    // CSS pseudo classes
    private static final PseudoClass                    ON_PSEUDO_CLASS = PseudoClass.getPseudoClass("on");
    private              BooleanProperty                on;

    // CSS Styleable property
    private static final CssMetaData<Led, Color>        COLOR           = FACTORY.createColorCssMetaData("-color", s -> s.color, Color.RED, false);
    private        final StyleableProperty<Color>       color;

    // View related
    private static final double                         PREFERRED_SIZE  = 16;
    private static final double                         MINIMUM_SIZE    = 8;
    private static final double                         MAXIMUM_SIZE    = 1024;
    private              double                         size;
    private              Region                         frame;
    private              Region                         main;
    private              Region                         highlight;
    private              InnerShadow                    innerShadow;
    private              DropShadow                     glow;


    // ******************** Constructors **************************************
    public Led() {
        on    = new BooleanPropertyBase(false) {
            @Override protected void invalidated() { pseudoClassStateChanged(ON_PSEUDO_CLASS, get()); }
            @Override public Object getBean() { return this; }
            @Override public String getName() { return "on"; }
        };
        color = new SimpleStyleableObjectProperty<>(COLOR, this, "color");
        init();
        initGraphics();
        registerListeners();
    }


    // ******************** Initialization ************************************
    private void init() {
        if (Double.compare(getWidth(), 0) <= 0 || Double.compare(getHeight(), 0) <= 0 ||
            Double.compare(getPrefWidth(), 0) <= 0 || Double.compare(getPrefHeight(), 0) <= 0) {
            setPrefSize(PREFERRED_SIZE, PREFERRED_SIZE);
        }
        if (Double.compare(getMinWidth(), 0) <= 0 || Double.compare(getMinHeight(), 0) <= 0) {
            setMinSize(MINIMUM_SIZE, MINIMUM_SIZE);
        }
        if (Double.compare(getMaxWidth(), 0) <= 0 || Double.compare(getMaxHeight(), 0) <= 0) {
            setMaxSize(MAXIMUM_SIZE, MAXIMUM_SIZE);
        }
    }

    private void initGraphics() {
        // Apply the base CSS style class to the control
        getStyleClass().add("led");

        // Create the needed nodes and apply their CSS style classes
        frame = new Region();
        frame.getStyleClass().setAll("frame");

        main = new Region();
        main.getStyleClass().setAll("main");

        // We handle effects in code because we have to chain them and
        // we need to calculate the shadow spread dependent of the size
        innerShadow = new InnerShadow(BlurType.TWO_PASS_BOX, Color.rgb(0, 0, 0, 0.65), 8, 0, 0, 0);

        glow = new DropShadow(BlurType.TWO_PASS_BOX, getColor(), 20, 0, 0, 0);
        glow.setInput(innerShadow);

        highlight = new Region();
        highlight.getStyleClass().setAll("highlight");

        // Add all nodes
        getChildren().addAll(frame, main, highlight);
    }

    private void registerListeners() {
        widthProperty().addListener(o -> handleControlPropertyChanged("RESIZE"));
        heightProperty().addListener(o -> handleControlPropertyChanged("RESIZE"));
        onProperty().addListener(o -> handleControlPropertyChanged("ON"));
        colorProperty().addListener(o -> handleControlPropertyChanged("COLOR"));
    }


    // ******************** Methods *******************************************
    @Override public void layoutChildren() {
        super.layoutChildren();
    }

    protected void handleControlPropertyChanged(final String PROPERTY) {
        if ("RESIZE".equals(PROPERTY)) {
            resize();
        } else if ("COLOR".equals(PROPERTY)) {
            main.setStyle(String.join("", "-color: ", (getColor()).toString().replace("0x", "#"), ";"));
            resize();
        } else if ("ON".equals(PROPERTY)) {
            main.setEffect(isOn() ? glow : innerShadow);
        }
    }

    public boolean isOn() { return on.get(); }
    public void setOn(final boolean ON) { on.set(ON); }
    public BooleanProperty onProperty() { return on; }

    @Override public ObservableList<Node> getChildren() { return super.getChildren(); }


    // ******************** CSS Stylable Properties ***************************
    public Color getColor() { return color.getValue(); }
    public void setColor(final Color COLOR) { color.setValue(COLOR); }
    public ObjectProperty<Color> colorProperty() { return (ObjectProperty<Color>) color; }


    // ******************** Style related *************************************
    @Override public String getUserAgentStylesheet() {
        return Led.class.getResource("led.css").toExternalForm();
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() { return FACTORY.getCssMetaData(); }

    @Override public List<CssMetaData<? extends Styleable, ?>> getCssMetaData() { return FACTORY.getCssMetaData(); }


    // ******************** Resizing ******************************************
    private void resize() {
        double width  = getWidth() - getInsets().getLeft() - getInsets().getRight();
        double height = getHeight() - getInsets().getTop() - getInsets().getBottom();
        size          = width < height ? width : height;

        if (size > 0) {
            // Center component
            if (getWidth() > getHeight()) {
                setTranslateX(0.5 * (getWidth() - size));
            } else if (getHeight() > getWidth()) {
                setTranslateY(0.5 * (getHeight() - size));
            }

            // Adjust shadow radii related to the current size
            innerShadow.setRadius(0.07 * size);
            glow.setRadius(0.36 * size);
            glow.setColor(getColor());

            // Adjust size and location of the child nodes
            frame.setPrefSize(size, size);

            main.setPrefSize(0.72 * size, 0.72 * size);
            main.relocate(0.14 * size, 0.14 * size);

            // Set effect dependent on current state
            main.setEffect(isOn() ? glow : innerShadow);

            highlight.setPrefSize(0.58 * size, 0.58 * size);
            highlight.relocate(0.21 * size, 0.21 * size);
        }
    }
}
