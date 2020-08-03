package utilitys;

import java.awt.*;

public enum Colors {
    ERROR (new Color(249, 26, 60)),
    INFO (new Color(26, 227, 249)),
    SUCCESS (new Color(19, 239, 141)),
    WARNING (new Color(249, 215, 26)),
    UNIMPORTANT (new Color(115, 143, 138));
    //UNIMPORTANT (new Color(73, 73, 73));

    private final Color color;

    Colors(Color color) {
        this.color = color;
    }

    public Color color() {
        return this.color;
    }

}