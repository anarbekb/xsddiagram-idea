package ru.balmukanov.tomato.action;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

/**
 * Action class to demonstrate how to interact with the IntelliJ Platform.
 * The only action this class performs is to provide the user with a popup dialog as feedback.
 * Typically this class is instantiated by the IntelliJ Platform framework based on declarations
 * in the plugin.xml file. But when added at runtime this class is instantiated by an action group.
 */
public class PopupDialogAction extends AnAction {

    /**
     * This default constructor is used by the IntelliJ Platform framework to instantiate this class based on plugin.xml
     * declarations. Only needed in {@link PopupDialogAction} class because a second constructor is overridden.
     *
     * @see AnAction#AnAction()
     */
    public PopupDialogAction() {
        super();
    }

    /**
     * Gives the user feedback when the dynamic action menu is chosen.
     * Pops a simple message dialog. See the psi_demo plugin for an
     * example of how to use {@link AnActionEvent} to access data.
     *
     * @param event Event received when the associated menu item is chosen.
     */
    @Override
    public void actionPerformed(@NotNull AnActionEvent event) {
        final Notification notification = new Notification("ProjectOpenNotification", "Started tomato",
                "Started tomato(50 min)", NotificationType.INFORMATION);
        notification.notify(event.getProject());

        ProgressManager.getInstance().run(new Task.Backgroundable(event.getProject(), "Tomato") {
            public void run(@NotNull ProgressIndicator progressIndicator) {

                try {
                    progressIndicator.setIndeterminate(false);
                    for (int i = 1; i <= 3; i++) {
                        TimeUnit.MINUTES.sleep(1);

                        progressIndicator.setFraction(i);
                        progressIndicator.setText(String.format("Tomato: %d min passed", i));
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                final Notification notificationComplete = new Notification("ProjectOpenNotification", "Hey you!",
                        "Get some rest", NotificationType.WARNING);
                notification.setImportant(true);
                notificationComplete.notify(event.getProject());
            }
        });
    }

    /**
     * Determines whether this menu item is available for the current context.
     * Requires a project to be open.
     *
     * @param e Event received when the associated group-id menu is chosen.
     */
    @Override
    public void update(AnActionEvent e) {
        // Set the availability based on whether a project is open
        Project project = e.getProject();
        e.getPresentation().setEnabledAndVisible(project != null);
    }

}