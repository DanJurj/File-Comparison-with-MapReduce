package Map;

public class MapingWorkerThread implements Runnable {
    private Mapper mapper;

    public MapingWorkerThread(Mapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void run() {
        mapper.map();
    }
}
