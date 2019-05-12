package util.math;

public class Dimensions2d extends Dimensions {

    protected float width, height;

    public Dimensions2d(float width, float height) {
        super(width, height);
        this.width = width;
        this.height = height;
    }

    public float width() {
        return width;
    }

    public float height() {
        return height;
    }

    public void setWidth(float width) {
        this.width = width;
        updateDim();
    }

    public void setHeight(float height) {
        this.height = height;
        updateDim();
    }

    @Override
    public void updateDim() {
        this.setDim(width, height);
    }

    @Override
    public String toString() {
        return "Dimensions2d{" + "width=" + width + ", height=" + height + '}';
    }
    

}
