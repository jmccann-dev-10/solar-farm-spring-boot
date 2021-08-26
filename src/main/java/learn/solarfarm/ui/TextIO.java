package learn.solarfarm.ui;

import learn.solarfarm.models.UserSelectable;

public interface TextIO {

    void println(Object value);

    void print(Object value);

    void printf(String format, Object... values);

    String readString(String prompt);

    String readStringWithFallback(String prompt, String currentValue);

    String readRequiredString(String prompt);

    boolean readBoolean(String prompt);

    boolean readBooleanWithFallback(String prompt, boolean currentValue);

    int readInt(String prompt);

    int readInt(String prompt, int max);

    int readInt(String prompt, int min, int max);

    int readIntWithFallback(String prompt, int currentValue);

    int readIntWithFallback(String prompt, int max, int currentValue);

    int readIntWithFallback(String prompt, int min, int max, int currentValue);

    // NOTE: The following enum related methods are a bit of a cheat to use at this point in the course
    // as students haven't been taught about defining generic methods.

    <E extends Enum<E>> E readEnum(String prompt, Class<E> enumClass);

    <E extends Enum<E>> E readEnumWithFallback(String prompt, Class<E> enumClass, E currentValue);

    <E extends Enum<E> & UserSelectable> E selectEnum(String prompt, Class<E> enumClass);

    <E extends Enum<E> & UserSelectable> E selectEnumWithFallback(String prompt, Class<E> enumClass, E currentValue);
}
