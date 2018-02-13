package Reduce;

import java.util.HashMap;

public class ReducingWorkerThread implements Runnable {
    private Reducer reducer;
    private HashMap<String, Integer> maping;

    public ReducingWorkerThread(Reducer reducer, HashMap<String, Integer> maping) {
        this.reducer = reducer;
        this.maping = maping;
    }

    @Override
    public void run() {
        reducer.reduce(maping);
    }
}
