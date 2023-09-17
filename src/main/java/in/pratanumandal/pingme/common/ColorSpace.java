package in.pratanumandal.pingme.common;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ColorSpace {

    private final List<Color> generatedColors;
    private final Random random;

    public ColorSpace() {
        generatedColors = new ArrayList<>();
        random = new Random();
    }

    public Color generateColor(int iterations, double threshold) {
        Map<Color, Double> colorMap = new HashMap<>();
        double distance;

        do {
            double h = random.nextDouble(0, 360);
            double s = random.nextDouble(0.4, 0.8);
            double b = random.nextDouble(0.4, 0.8);
            Color color = Color.hsb(h, s, b);

            distance = Double.MAX_VALUE;
            for (Color other : generatedColors) {
                double dist = distance(color, other);

                if (dist < distance) {
                    distance = dist;
                }
            }

            colorMap.put(color, distance);
            iterations--;
        }
        while (distance < threshold && iterations > 0);

        Color color = Collections.max(colorMap.entrySet(), Map.Entry.comparingByValue()).getKey();
        generatedColors.add(color);

        return color;
    }

    public Color generateColor() {
        return generateColor(10, 0.3);
    }

    private double distance(Color color1, Color color2) {
        double rMean = (color1.getRed() + color2.getRed()) / 2.0;
        return Math.sqrt((2 + rMean / 256) * Math.pow(color1.getRed() - color2.getRed(), 2) +
                4 * Math.pow(color1.getGreen() - color2.getGreen(), 2) +
                (2 + (255 - rMean) / 256) * Math.pow(color1.getBlue() - color2.getBlue(), 2));
    }

}
