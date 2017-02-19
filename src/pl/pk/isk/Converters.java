package pl.pk.isk;

import java.util.Optional;

public final class Converters {
    public static Integer checkStringAndConvertToInt(String string) {
        Optional<String> optional = Optional.of(string);
        if (optional.isPresent() && optional.get().length() > 0 && optional.get().matches("\\d+"))
            return Integer.valueOf(optional.get()).intValue();
        return null;
    }
}
