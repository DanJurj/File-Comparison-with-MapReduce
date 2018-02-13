package Document;

import java.util.concurrent.ConcurrentHashMap;

public class IndexedDocument extends Document {
    private String name;
    private ConcurrentHashMap<String, Integer> indexList;


    public IndexedDocument(String name, ConcurrentHashMap<String, Integer> indexList) {
        super(name);
        this.name = name;
        this.indexList = indexList;
    }

    public String getName() {
        return name;
    }

    public ConcurrentHashMap<String, Integer> getIndexList() {
        return indexList;
    }

    /**
     * Calculates the frequency on the specific word in its document with 3 decimals
     * @param nrWord the frequency of the word in the document
     * @param totalWordsCount the total number of words the document contains
     * @return the frequency with 3 decimals
     */
    private double getFrequency(int nrWord, int totalWordsCount) {
        double result = ((double) nrWord / (double) totalWordsCount);
        int precision = 1000;
        result = Math.floor(result * precision) / precision;
        return result;
    }

    /**
     *compares all the suspected documents with the original one
     * @param originalDocument
     * @return
     */
    public double sim(IndexedDocument originalDocument) {
        double s = 0;
        //checking to see which document is shorter for optimization
        if (this.getWordCount() > originalDocument.getWordCount()) {
            for (String word : indexList.keySet()) {
                if (originalDocument.getIndexList().containsKey(word)) {
                    s += getFrequency(indexList.get(word), getWordCount()) * getFrequency(originalDocument.indexList.get(word), originalDocument.getWordCount());
                }
            }
        } else {
            for (String word : originalDocument.getIndexList().keySet()) {
                if (indexList.containsKey(word)) {
                    s += getFrequency(indexList.get(word), getWordCount()) * getFrequency(originalDocument.indexList.get(word), originalDocument.getWordCount());
                }
            }
        }
        s *= 100; //procenting the result
        return Math.floor(s * 1000) / 1000; //return it with only 3 decimals
    }
}
