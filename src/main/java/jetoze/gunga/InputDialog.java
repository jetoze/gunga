package jetoze.gunga;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.ImmutableList.toImmutableList;
import static java.util.Objects.requireNonNull;

import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import javax.annotation.Nullable;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.google.common.collect.ImmutableList;

import jetoze.gunga.widget.Focusable;
import jetoze.gunga.widget.Widget;

public class InputDialog { // TODO: I need a better name.

    @Nullable
    private final JFrame owner;
    @Nullable
    private final String title;
    @Nullable
    private final Icon icon;
    private final InputOptions inputOptions;
    private final Supplier<JComponent> contentSupplier;
    private final Focusable focusReceiver;
    private final boolean modal;

    private InputDialog(JFrame owner, String title, Icon icon, InputOptions inputOptions,
            Supplier<JComponent> contentSupplier, Focusable focusReceiver, boolean modal) {
        this.owner = owner;
        this.title = title;
        this.icon = icon;
        this.inputOptions = inputOptions;
        this.contentSupplier = contentSupplier;
        this.focusReceiver = focusReceiver;
        this.modal = modal;
    }

    // TODO: This method should return the selected Option.
    public void open() {
        JDialog dialog = new JDialog(owner, title, modal);
        
        ActionListener dialogCloser = e -> dialog.setVisible(false);
        JButton[] buttons = inputOptions.getButtons().toArray(new JButton[0]);
        Arrays.stream(buttons).forEach(b -> b.addActionListener(dialogCloser));
        JButton defaultButton = inputOptions.getDefaultButton();
        
        JOptionPane optionPane = new JOptionPane(
                contentSupplier.get(),
                JOptionPane.PLAIN_MESSAGE,
                inputOptions.swingIdentifier,
                null,
                buttons,
                defaultButton);
        dialog.setContentPane(optionPane);
        dialog.pack();
        dialog.setLocationRelativeTo(owner);
        dialog.getRootPane().setDefaultButton(defaultButton);
        dialog.addWindowListener(new WindowAdapter() {

            @Override
            public void windowOpened(WindowEvent e) {
                UiThread.runLater(focusReceiver::requestFocus);
            }
        });
        // Using a ComponentListener rather than a WindowListener because I've observed
        // that the WindowListener isn't notified consistently when the dialog is closed.
        dialog.addComponentListener(new ComponentAdapter() {
            
            @Override
            public void componentHidden(ComponentEvent e) {
                Arrays.stream(buttons).forEach(b -> b.removeActionListener(dialogCloser));
            }
        });
        KeyBindings.whenAncestorOfFocusedComponent(dialog.getRootPane())
            .add(KeyStrokes.ESCAPE, "escape", () -> {
                UiThread.runLater(inputOptions.getJobToRunOnEscape());
                dialog.setVisible(false);
            });
        dialog.setVisible(true);
    }
    

    // TODO: Add more options here, as neeed
    public enum Option {
        OK("OK"),
        CANCEL("Cancel");
        
        private String defaultLabel;
        
        private Option(String defaultLabel) {
            this.defaultLabel = defaultLabel;
        }
        
        public MaterializedOption materialize() {
            return new MaterializedOption(this, this.defaultLabel, () -> {});
        }
        
        public MaterializedOption materialize(String label) {
            return new MaterializedOption(this, label, () -> {});
        }
        
        public MaterializedOption materialize(Runnable job) {
            return new MaterializedOption(this, defaultLabel, job);
        }
        
        public MaterializedOption materialize(String label, Runnable job) {
            return new MaterializedOption(this, label, job);
        }
    }
    
    
    public static class MaterializedOption {
        
        private final Option option;
        private final Runnable job;
        private final JButton button;
        
        public MaterializedOption(Option option, String label, Runnable job) {
            this.option = requireNonNull(option);
            this.job = requireNonNull(job);
            requireNonNull(label);
            button = new JButton(label);
            button.addActionListener(e -> UiThread.runLater(job));
        }

        public Option getOption() {
            return option;
        }
        
        private JButton getButton() {
            return button;
        }
        
        public void setEnabled(boolean enabled) {
            button.setEnabled(enabled);
        }
        
        public void select() {
            if (button.isEnabled()) {
                button.doClick();
            }
        }
    }
    
    
    public static class InputOptions {
        private final int swingIdentifier;
        private final ImmutableList<MaterializedOption> options;
        private final Option defaultOption;
        private final Option escapeOption;
        
        private InputOptions(int swingIdentifier, 
                             List<MaterializedOption> options, 
                             Option defaultOption,
                             Option escapeOption) {
            this.swingIdentifier = swingIdentifier;
            this.options = ImmutableList.copyOf(options);
            this.defaultOption = defaultOption;
            this.escapeOption = escapeOption;
        }
        
        public MaterializedOption getMaterializedOption(Option option) {
            requireNonNull(option);
            return options.stream()
                    .filter(mo -> mo.option == option)
                    .findFirst()
                    .orElseThrow();
        }
        
        private ImmutableList<JButton> getButtons() {
            return options.stream()
                    .map(MaterializedOption::getButton)
                    .collect(toImmutableList());
        }
        
        private JButton getDefaultButton() {
            return getMaterializedOption(defaultOption).getButton();
        }
        
        private Runnable getJobToRunOnEscape() {
            return getMaterializedOption(escapeOption).job;
        }
        
        public DialogBuilder dialogBuilder() {
            return new DialogBuilder(this);
        }
    }
    
    
    // TODO: Provide a way to configure labels and default option.
    public static InputOptions okCancel(Runnable okJob, Runnable cancelJob) {
        return new InputOptions(JOptionPane.OK_CANCEL_OPTION, 
                ImmutableList.of(Option.OK.materialize(okJob), Option.CANCEL.materialize(cancelJob)), 
                Option.OK, 
                Option.CANCEL);
    }
    
    
    
    public static class DialogBuilder {
        // TODO: Support for customized button labels.
        private final InputOptions inputOptions;
        private Supplier<JComponent> contentSupplier;
        private Focusable focusReceiver;
        @Nullable
        private JFrame owner;
        @Nullable
        private String title;
        @Nullable
        private Icon icon;
        private boolean modal = true;

        private DialogBuilder(InputOptions inputOptions) {
            this.inputOptions = requireNonNull(inputOptions);
        }

        public DialogBuilder withContent(Widget widget) {
            requireNonNull(widget);
            this.contentSupplier = widget::getUi;
            this.focusReceiver = widget;
            return this;
        }
        
        public DialogBuilder withContent(JComponent component) {
            requireNonNull(component);
            this.contentSupplier = () -> component;
            this.focusReceiver = Focusable.of(component);
            return this;
        }
        
        public DialogBuilder withOwner(JFrame owner) {
            this.owner = requireNonNull(owner);
            return this;
        }
        
        public DialogBuilder withTitle(String title) {
            this.title = requireNonNull(title);
            return this;
        }
        
        public DialogBuilder withIcon(Icon icon) {
            this.icon = requireNonNull(icon);
            return this;
        }
        
        public DialogBuilder modeless() {
            this.modal = false;
            return this;
        }
        
        public InputDialog build() {
            checkState(contentSupplier != null, "Must provide the content before building the dialog");
            return new InputDialog(owner, title, icon, inputOptions, contentSupplier, focusReceiver, modal);
        }
    }
}
