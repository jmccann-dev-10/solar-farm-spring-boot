package learn.solarfarm.models;

/**
 * Indicates that a type is user selectable in the user interface.
 * Ensures that every type that implements this interface defines
 * a getDisplayText method to get the display text for a value.
 *
 * NOTE: This interface isn't a requirement of this project.
 * Removing the enum related methods from the UI layer would allow this interface to be removed.
 */
public interface UserSelectable {
    String getDisplayText();
}
