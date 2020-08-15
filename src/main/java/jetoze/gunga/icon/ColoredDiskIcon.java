package jetoze.gunga.icon;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

/**
 * Icon implementation that draws a colored circle with an optional border.
 */
public class ColoredDiskIcon implements Icon {

    /**
     * The color of the disk.
     */
    private final Color diskColor;
    /**
     * The color of the border.
     */
    private final Color borderColor;
    /**
     * The diameter (in pixels) of the disk.
     */
    private final int diameter;

    // TODO: Allow the width of the border to be configured.

    /**
     * Creates a ColoredDiskIcon without a border.
     * 
     * @param diskColor the color of the disk
     * @param diameter  the diameter in pixels of the disk
     */
    public ColoredDiskIcon(Color diskColor, int diameter) {
        this(diskColor, diskColor, diameter);
    }

    /**
     * Creates a ColoredDiskIcon without a border.
     * 
     * @param diskColor   the color of the disk
     * @param borderColor the color of the border
     * @param diameter    the diameter in pixels of the disk
     */
    public ColoredDiskIcon(Color diskColor, Color borderColor, int diameter) {
        checkArgument(diameter > 0, "diameter must be > 0 (was %s)", diameter);
        this.diskColor = requireNonNull(diskColor);
        this.borderColor = requireNonNull(borderColor);
        this.diameter = requireNonNull(diameter);
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Color originalColor = g.getColor();
        g.setColor(diskColor);
        g.fillOval(x, y, getIconWidth(), getIconHeight());
        if (!diskColor.equals(borderColor)) {
            g.setColor(borderColor);
            g.drawOval(x, y, getIconWidth(), getIconHeight());
        }
        g.setColor(originalColor);
    }

    @Override
    public int getIconWidth() {
        return diameter;
    }

    @Override
    public int getIconHeight() {
        return diameter;
    }
}
