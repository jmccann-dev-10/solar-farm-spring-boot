package learn.solarfarm.ui;

import learn.solarfarm.models.UserSelectable;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Scanner;

@Component
@Profile("file-repository")
public class ConsoleIO implements TextIO {

    private final Scanner console = new Scanner(System.in);

    @Override
    public void println(Object value) {
        System.out.println(value);
    }

    @Override
    public void print(Object value) {
        System.out.print(value);
    }

    @Override
    public void printf(String format, Object... values) {
        System.out.printf(format, values);
    }

    @Override
    public String readString(String prompt) {
        // for consistent display of prompts
        // remove any leading and trailing whitespace and add a space after the prompt
        print(prompt.trim() + ": ");
        return console.nextLine();
    }

    @Override
    public String readStringWithFallback(String prompt, String currentValue) {
        String value = readString(String.format("%s (%s)", prompt, currentValue));
        if (value != null && !value.isBlank()) {
            return value;
        }
        return currentValue;
    }

    @Override
    public String readRequiredString(String prompt) {
        while (true) {
            String value = readString(prompt);
            if (value != null && !value.isBlank()) {
                return value;
            }
            printf("[Error]%nYou must provide a value.%n");
        }
    }

    @Override
    public boolean readBoolean(String prompt) {
        String result = readString(prompt);
        return result.equalsIgnoreCase("y");
    }

    @Override
    public boolean readBooleanWithFallback(String prompt, boolean currentValue) {
        String value = readString(String.format("%s (%s)", prompt, currentValue ? "yes" : "no"));
        if (value != null && !value.isBlank()) {
            return value.equalsIgnoreCase("y");
        }
        return currentValue;
    }

    @Override
    public int readInt(String prompt) {
        while (true) {
            String value = readString(prompt);
            try {
                int result = Integer.parseInt(value);
                return result;
            } catch (NumberFormatException ex) {
                printf("'%s' is not a valid number.%n", value);
            }
        }
    }

    @Override
    public int readInt(String prompt, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value <= max) {
                return value;
            }
            printf("[Error]%nValue must be less than or equal to %s.%n", max);
        }
    }

    @Override
    public int readInt(String prompt, int min, int max) {
        while (true) {
            int value = readInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            printf("[Error]%nValue must be between %s and %s.%n", min, max);
        }
    }

    @Override
    public int readIntWithFallback(String prompt, int currentValue) {
        while (true) {
            String value = readString(String.format("%s (%s)", prompt, currentValue));
            if (value != null && !value.isBlank()) {
                try {
                    int result = Integer.parseInt(value);
                    return result;
                } catch (NumberFormatException ex) {
                    printf("[Error]%n'%s' is not a valid number.%n", value);
                }
            } else {
                return currentValue;
            }
        }
    }

    @Override
    public int readIntWithFallback(String prompt, int max, int currentValue) {
        while (true) {
            String value = readString(String.format("%s (%s)", prompt, currentValue));
            if (value != null && !value.isBlank()) {
                try {
                    int result = Integer.parseInt(value);
                    if (result <= max) {
                        return result;
                    }
                    printf("[Error]%nValue must be less than or equal to %s.%n", max);
                } catch (NumberFormatException ex) {
                    printf("[Error]%n'%s' is not a valid number.%n", value);
                }
            } else {
                return currentValue;
            }
        }
    }

    @Override
    public int readIntWithFallback(String prompt, int min, int max, int currentValue) {
        while (true) {
            String value = readString(String.format("%s (%s)", prompt, currentValue));
            if (value != null && !value.isBlank()) {
                try {
                    int result = Integer.parseInt(value);
                    if (result >= min && result <= max) {
                        return result;
                    }
                    printf("[Error]%nValue must be between %s and %s.%n", min, max);
                } catch (NumberFormatException ex) {
                    printf("[Error]%n'%s' is not a valid number.%n", value);
                }
            } else {
                return currentValue;
            }
        }
    }

    // NOTE: The following enum related methods are a bit of a cheat to use at this point in the course
    // as students haven't been taught about defining generic methods.

    /**
     * Prompts the user for an enum value.
     * If the user doesn't provide a valid enum value a list of possible values
     * will be displayed so the user can see the acceptable values.
     *
     * @param prompt The prompt to display to the user.
     * @param enumClass The enum class.
     * @param <E> The enum type.
     * @return The selected enum value.
     */
    @Override
    public <E extends Enum<E>> E readEnum(String prompt, Class<E> enumClass) {
        while (true) {
            String value = readString(prompt);
            try {
                E result = Enum.valueOf(enumClass, value);
                return result;
            } catch (IllegalArgumentException ex) {
                printf("[Error]%n'%s' is not a valid %s.%n", value, enumClass.getSimpleName());

                ArrayList<String> values = new ArrayList<>();
                for (E enumValue : EnumSet.allOf(enumClass)) {
                    values.add(enumValue.toString());
                }
                printf("Possible values: %s%n", String.join(", ", values));
            }
        }
    }

    /**
     * Prompts the user for an enum value (with a fallback value).
     * If the user doesn't provide a valid enum value a list of possible values
     * will be displayed so the user can see the acceptable values.
     *
     * @param prompt The prompt to display to the user.
     * @param enumClass The enum class.
     * @param currentValue The current value (this value will be returned if the user enters "").
     * @param <E> The enum type.
     * @return The selected enum value.
     */
    @Override
    public <E extends Enum<E>> E readEnumWithFallback(String prompt, Class<E> enumClass, E currentValue) {
        while (true) {
            String value = readString(String.format("%s (%s)", prompt, currentValue));
            if (value != null && !value.isBlank()) {
                try {
                    E result = Enum.valueOf(enumClass, value);
                    return result;
                } catch (IllegalArgumentException ex) {
                    printf("[Error]%n'%s' is not a valid %s.%n", value, enumClass.getSimpleName());

                    ArrayList<String> values = new ArrayList<>();
                    for (E enumValue : EnumSet.allOf(enumClass)) {
                        values.add(enumValue.toString());
                    }
                    printf("Possible values: %s%n", String.join(", ", values));
                }
            } else {
                return currentValue;
            }
        }
    }

    /**
     * Displays a list of available enum values for the user to select from.
     *
     * Select a Material:
     * 1. Value 1
     * 2. Value 2
     * Choose [1-2]:
     *
     * @param prompt The prompt to display to the user.
     * @param enumClass The enum class.
     * @param <E> The enum type.
     * @return The selected enum value.
     */
    @Override
    public <E extends Enum<E> & UserSelectable> E selectEnum(String prompt, Class<E> enumClass) {
        Object[] enumValues = EnumSet.allOf(enumClass).toArray();

        printf("Select a %s:%n", enumClass.getSimpleName());
        for (int enumOptionNumber = 0; enumOptionNumber < enumValues.length; enumOptionNumber++) {
            // explicitly cast the enum value to UserSelectable in order to call the getDisplayText method
            String displayText = ((UserSelectable)enumValues[enumOptionNumber]).getDisplayText();
            printf("%s. %s%n", enumOptionNumber + 1, displayText);
        }

        int selectedEnumOptionNumber = readInt(String.format("Choose [1-%s]", enumValues.length), 1, enumValues.length);

        return (E)enumValues[selectedEnumOptionNumber - 1];
    }

    /**
     * Displays a list of available enum values for the user to select from (with a fallback value).
     *
     * Select a Material:
     * 1. Value 1
     * 2. Value 2
     * Choose [1-2] (Value 2):
     *
     * @param prompt The prompt to display to the user.
     * @param enumClass The enum class.
     * @param currentValue The current value (this value will be returned if the user enters "").
     * @param <E> The enum type.
     * @return The selected enum value.
     */
    @Override
    public <E extends Enum<E> & UserSelectable> E selectEnumWithFallback(String prompt, Class<E> enumClass, E currentValue) {
        Object[] enumValues = EnumSet.allOf(enumClass).toArray();

        printf("Select a %s:%n", enumClass.getSimpleName());
        for (int enumOptionNumber = 0; enumOptionNumber < enumValues.length; enumOptionNumber++) {
            // explicitly cast the enum value to UserSelectable in order to call the getDisplayText method
            String displayText = ((UserSelectable)enumValues[enumOptionNumber]).getDisplayText();
            printf("%s. %s%n", enumOptionNumber + 1, displayText);
        }

        while (true) {
            String value = readString(String.format("Choose [1-%s] (%s)", enumValues.length, currentValue.getDisplayText()));
            if (value != null && !value.isBlank()) {
                try {
                    int result = Integer.parseInt(value);
                    if (result >= 1 && result <= enumValues.length) {
                        return (E)enumValues[result - 1];
                    }
                    printf("[Error]%nValue must be between 1 and %s.%n", enumValues.length);
                } catch (NumberFormatException ex) {
                    printf("[Error]%n'%s' is not a valid number.%n", value);
                }
            } else {
                return currentValue;
            }
        }
    }
}
