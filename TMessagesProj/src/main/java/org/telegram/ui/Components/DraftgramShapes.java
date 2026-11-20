// Java · DraftgramShapes.java · Android (Telegram-Android fork) · M3E shape paths and accent presets for Draftgram

package org.telegram.ui.Components;

import android.graphics.Path;
import android.graphics.RectF;

import org.telegram.utils.settings.DraftgramSettings;

public final class DraftgramShapes {

    public static final String[] SHAPE_NAMES = {"Круг", "Squircle", "Печенька 9", "Солнышко", "Цветок 6", "Клевер", "Печенька 12"};

    public static final String[] ACCENT_NAMES = {"Синий", "Зелёный", "Красный", "Фиолетовый", "Оранжевый", "Жёлтый", "Белый", "Серый"};
    public static final int[] ACCENT_COLORS = {
            0xFF0061A4, 0xFF006E1C, 0xFFB3261E, 0xFF6750A4,
            0xFFB35900, 0xFF8A6A00, 0xFFFFFFFF, 0xFF5F5F5F
    };

    private DraftgramShapes() {
    }

    public static int shapeCount() {
        return SHAPE_NAMES.length;
    }

    public static int accentCount() {
        return ACCENT_NAMES.length;
    }

    public static int avatarShapeIndex() {
        int index = DraftgramSettings.avatarShape.get();
        return index >= 0 && index < SHAPE_NAMES.length ? index : 0;
    }

    public static int accentColor() {
        int index = DraftgramSettings.accent.get();
        return ACCENT_COLORS[index >= 0 && index < ACCENT_COLORS.length ? index : 0];
    }

    public static int darker(int color) {
        int r = (int) (((color >> 16) & 0xFF) * 0.85f);
        int g = (int) (((color >> 8) & 0xFF) * 0.85f);
        int b = (int) ((color & 0xFF) * 0.85f);
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }

    /** Builds the outline of shape {@code index} centred on (cx, cy) with radius r. */
    public static Path buildPath(int index, float cx, float cy, float r) {
        Path path = new Path();
        switch (index) {
            case 1: {
                RectF rect = new RectF(cx - r, cy - r, cx + r, cy + r);
                float corner = r * 0.6f;
                path.addRoundRect(rect, corner, corner, Path.Direction.CW);
                break;
            }
            case 2:
                addStar(path, 9, cx, cy, r, r * 0.84f);
                break;
            case 3:
                addStar(path, 8, cx, cy, r, r * 0.84f);
                break;
            case 4:
                addStar(path, 6, cx, cy, r, r * 0.68f);
                break;
            case 5: {
                float petal = r * 0.5f;
                float offset = r * 0.5f;
                path.addCircle(cx, cy - offset, petal, Path.Direction.CW);
                Path other = new Path();
                other.addCircle(cx, cy + offset, petal, Path.Direction.CW);
                path.op(other, Path.Op.UNION);
                other.reset();
                other.addCircle(cx - offset, cy, petal, Path.Direction.CW);
                path.op(other, Path.Op.UNION);
                other.reset();
                other.addCircle(cx + offset, cy, petal, Path.Direction.CW);
                path.op(other, Path.Op.UNION);
                other.reset();
                other.addCircle(cx, cy, r * 0.6f, Path.Direction.CW);
                path.op(other, Path.Op.UNION);
                break;
            }
            case 6:
                addStar(path, 12, cx, cy, r, r * 0.86f);
                break;
            default:
                path.addCircle(cx, cy, r, Path.Direction.CW);
                break;
        }
        return path;
    }

    private static void addStar(Path path, int points, float cx, float cy, float outer, float inner) {
        int steps = points * 2;
        for (int i = 0; i < steps; i++) {
            float radius = (i % 2 == 0) ? outer : inner;
            double angle = Math.PI * i / points - Math.PI / 2;
            float x = cx + (float) (radius * Math.cos(angle));
            float y = cy + (float) (radius * Math.sin(angle));
            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        path.close();
    }
}
