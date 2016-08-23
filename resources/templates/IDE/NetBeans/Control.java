<#if package?? && package != "">
package ${package};

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.BooleanPropertyBase;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.DoublePropertyBase;
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
 * User: ${user}
 * Date: ${date}
 * Time: ${time}
 */
public class ${name} extends Control {    
    private static final StyleablePropertyFactory<${name}> FACTORY         = new StyleablePropertyFactory<>(Control.getClassCssMetaData());

    // CSS pseudo classes
    private static final PseudoClass                       ON_PSEUDO_CLASS = PseudoClass.getPseudoClass("on");
    private              BooleanProperty                   on;

    // CSS Styleable property
    private static final CssMetaData<${name}, Color>       COLOR           = FACTORY.createColorCssMetaData("-color", s -> s.color, Color.RED, false);
    private        final StyleableProperty<Color>          color;

    // Properties
    private              DoubleProperty                    value;
    private              double                            _value;


    // ******************** Constructors **************************************
    public ${name}() {
        getStyleClass().add("my-control");        
        on       = new BooleanPropertyBase(false) {
            @Override protected void invalidated() { pseudoClassStateChanged(ON_PSEUDO_CLASS, get()); }
            @Override public Object getBean() { return ${name}.this; }
            @Override public String getName() { return "on"; }
        };
        color    = new SimpleStyleableObjectProperty<>(COLOR, ${name}.this, "color");
        _value   = 0;
    }


    // ******************** Methods *******************************************
    public boolean isOn() { return on.get(); }
    public void setOn(final boolean ON) { on.set(ON); }
    public BooleanProperty onProperty() { return on; }

    public double getValue() { return null == value ? _value : value.get(); }
    public void setValue(final double VALUE) {
        if (null == value) {
            _value = VALUE;
        } else {
            value.set(VALUE);
        }
    }
    public DoubleProperty valueProperty() {
        if (null == value) {
            value = new DoublePropertyBase(_value) {
                @Override protected void invalidated() { super.invalidated(); }
                @Override public Object getBean() { return ${name}.this; }
                @Override public String getName() { return "value"; }
            };
        }
        return value;
    }


    // ******************** CSS Styleable Properties **************************
    public Color getColor() { return color.getValue(); }
    public void setColor(final Color COLOR) { color.setValue(COLOR); }
    public ObjectProperty<Color> colorProperty() { return (ObjectProperty<Color>) color; }


    // ******************** Style related *************************************
    @Override protected Skin createDefaultSkin() { return new ${name}Skin(${name}.this); }

    @Override public String getUserAgentStylesheet() { return ${name}.class.getResource("control.css").toExternalForm(); }

    public static List<CssMetaData<? extends Styleable, ?>> getClassCssMetaData() { return FACTORY.getCssMetaData(); }

    @Override public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() { return FACTORY.getCssMetaData(); }
}
