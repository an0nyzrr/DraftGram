// Java · DraftgramAvatarView.java · Android (Telegram-Android fork) · avatar clipped to a Draftgram M3E shape

package org.telegram.ui.Components;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;

public class DraftgramAvatarView extends BackupImageView {

    private final Path clip = new Path();
    private int shape;

    public DraftgramAvatarView(Context context) {
        super(context);
    }

    public void setShape(int shape) {
        this.shape = shape;
        rebuildClip();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rebuildClip();
    }

    private void rebuildClip() {
        clip.reset();
        float r = Math.min(getWidth(), getHeight()) / 2f;
        if (r <= 0) {
            return;
        }
        clip.set(DraftgramShapes.buildPath(shape, getWidth() / 2f, getHeight() / 2f, r));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.clipPath(clip);
        super.onDraw(canvas);
        canvas.restore();
    }
}
