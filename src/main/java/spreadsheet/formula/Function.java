package spreadsheet.formula;

import java.util.Collections;
import java.util.List;

public enum Function {
    SUMA, MIN, MAX, PROMEDIO;

    public double apply(List<Double> values) {
        return switch (this) {
            case SUMA -> values.stream().mapToDouble(Double::doubleValue).sum();
            case MIN -> Collections.min(values);
            case MAX -> Collections.max(values);
            case PROMEDIO -> values.stream().mapToDouble(Double::doubleValue).average().orElseThrow();
        };
    }
}
