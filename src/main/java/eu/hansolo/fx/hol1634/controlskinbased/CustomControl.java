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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.css.CssMetaData;
import javafx.css.PseudoClass;
import javafx.css.SimpleStyleableObjectProperty;
import javafx.css.Styleable;
import javafx.css.StyleableProperty;
import javafx.css.StyleablePropertyFactory;
import javafx.scene.control.Control;
import javafx.scene.control.Skin;
import javafx.scene.paint.Color;

import java.util.List;


/**
 * Created by hansolo on 12.08.16.
 */
public class CustomControl extends Control {
    public enum SkinType { LED, SWITCH }

    private static final StyleablePropertyFactory<CustomControl> FACTORY         = new StyleablePropertyFactory<>(Control.getClassCssMetaData());

    // CSS pseudo classes
    private static final PseudoClass                             ON_PSEUDO_CLASS = PseudoClass.getPseudoClass("on");
    private              BooleanProperty                         on;

    // CSS Styleable property
    private static final CssMetaData<CustomControl, Color>       COLOR           = FACTORY.createColorCssMetaData("-color", s -> s.color, Color.RED, false);
    private        final StyleableProperty<Color>                color;

    // Properties
    private              SkinType skinType;


    // ******************** Constructors **************************************
    public CustomControl() {
        this(SkinType.LED);
    }
    public CustomControl(final SkinType SKIN_TYPE) {
        getStyleClass().add("custom-control");
        skinType = SKIN_TYPE;
        on       = new BooleanPropertyBase(false) {
            @Override protected void invalidated() { pseudoClassStateChanged(ON_PSEUDO_CLASS, get()); }
            @Override public Object getBean() { return this; }
            @Override public String getName() { return "on"; }
        };
        color    = new SimpleStyleableObjectProperty<>(COLOR, this, "color");
    }


    // ******************** Methods *******************************************
    public boolean isOn() { return on.get(); }
    public void setOn(final boolean ON) { on.set(ON); }
    public BooleanProperty onProperty() { return on; }


    // ******************** CSS Styleable Properties **************************
    public Color getColor() { return color.getValue(); }
    public void setColor(final Color COLOR) { color.setValue(COLOR); }
    public ObjectProperty<Color> colorProperty() { return (ObjectProperty<Color>) color; }


    // ******************** Style related *************************************
    @Override protected Skin createDefaultSkin() {
        switch(skinType) {
            case SWITCH: return new SwitchSkin(CustomControl.this);
            case LED   :
            default    : return new LedSkin(CustomControl.this);
        }
    }

    @Override public String getUserAgentStylesheet() {
        switch(skinType) {
            case SWITCH: return CustomControl.class.getResource("switch.css").toExternalForm();
            case LED   :
            default    : return CustomControl.class.getResource("custom-control.css").toExternalForm();
        }
    }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() { return FACTORY.getCssMetaData(); }

    @Override public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() { return FACTORY.getCssMetaData(); }
}
