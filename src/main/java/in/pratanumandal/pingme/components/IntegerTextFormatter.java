package in.pratanumandal.pingme.components;

import javafx.scene.control.TextFormatter;

import java.util.regex.Pattern;

public class IntegerTextFormatter extends TextFormatter<String> {

    public static final Pattern INTEGER_PATTERN = Pattern.compile("\\d*");

    public IntegerTextFormatter() {
        super(change -> {
            String newText = change.getControlNewText();
            if (INTEGER_PATTERN.matcher(newText).matches()) {
                return change;
            }
            return null;
        });
    }

}
