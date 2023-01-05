package dev.zemco.codegame.presentation;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Provides a simple way to manage an existing subscription to a JavaFX {@link ObservableValue observable}.
 * This interface was created to abstract away management of listeners used by the controllers.
 * Currently, this includes {@link #unsubscribe() unsubscribing} from an {@link ObservableValue observable},
 * as JavaFX doesn't use weak references for this type of subscriptions.
 *
 * @author Erik Zemčík
 */
public interface IListenerSubscription {

    /**
     * Unsubscribes from the {@link ObservableValue observable} by removing the backing listener.
     * We recommend implementation through the {@link ObservableValue#removeListener(ChangeListener)} method.
     */
    void unsubscribe();

}
