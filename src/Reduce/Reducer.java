package Reduce;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class Reducer {
    private String fileName;
    private ConcurrentHashMap<String, Integer> finalList;


    public Reducer(String fileName, ConcurrentHashMap<String, Integer> finalList) {
        this.fileName = fileName;
        this.finalList = finalList;
    }

    /**
     * Concurential access to a final list which'll contain all the words with their total frequency
     * @param mapList
     */
    public synchronized void reduce(HashMap<String, Integer> mapList) {
        for (String word : mapList.keySet()) {
            Integer count = mapList.get(word);
            if (finalList.containsKey(word)) {
                Integer lastValue = finalList.get(word);
                finalList.replace(word, lastValue + count);
            } else {
                finalList.put(word,count);
            }
        }
    }
}
