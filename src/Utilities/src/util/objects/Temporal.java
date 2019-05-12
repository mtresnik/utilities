package util.objects;

import util.objects.Updatable;


public class Temporal implements Updatable {

    public long prev;
    public long dt;
    public long thresh;

    public Temporal() {
        this(-1);
    }
    public Temporal(long thresh) {
        prev = System.currentTimeMillis();
        dt = 1;
        this.thresh = thresh;
    }
    
    public float FPS(){
        return 1000.0f / dt;
    }

    @Override
    public String toString() {
        return "Temporal{" + "prev=" + prev + ", dt=" + dt + '}';
    }

    @Override
    public void update() {
        long current = System.currentTimeMillis();
        this.dt = current - prev;
        prev = current;
    }
    
    public boolean preTOver() {
        long temp = System.currentTimeMillis() - prev;
        return (temp > thresh);
    }
    
    public void updateIfOver(){
        if(this.preTOver()){
            this.update();
        }
    }
    
    public void runIf(Runnable runnable){
        if(this.preTOver()){
            this.update();
            runnable.run();
        }
    }
    
    
    
}
