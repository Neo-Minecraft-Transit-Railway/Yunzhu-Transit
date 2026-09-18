package top.xfunny.mod.client.view;

import org.mtr.mod.render.StoredMatrixTransformations;

public interface RenderView {
    String getId();

    void render();

    void setStoredMatrixTransformations(StoredMatrixTransformations storedMatrixTransformations);

    float getWidth();

    float getHeight();

    float[] getMargin();

    Gravity getGravity();

    void setGravity(Gravity gravity);

    void setMargin(float left, float top, float right, float bottom);

    void setPosition(float x, float y);

    void setParentDimensions(float parentWidth, float parentHeight);

    void calculateLayoutWidth();

    void calculateLayoutHeight();

    float[] calculateChildGravityOffset(float childWidth, float childHeight, float[] childMargin, Gravity childGravity);

    Object getParentType();

    void setParentType(Object thisObject);//预留方法
}
