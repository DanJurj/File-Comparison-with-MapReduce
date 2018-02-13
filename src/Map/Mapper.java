package Map;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class Mapper {
    private String fileName;
    private int startPosition, endPosition;
    private HashMap<String, HashMap<String, Integer>> maping;

    /**
     * Map.Mapper Constructor
     *
     * @param fileName      - the name of the file from which the mapper reads its segment
     * @param startPosition - the start position in the file
     * @param endPosition   - the end position in the file
     */
    public Mapper(String fileName, int startPosition, int endPosition) {
        this.fileName = fileName;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        maping = new HashMap<>();
    }

    /**
     * maping getter
     *
     * @return
     */
    public HashMap<String, HashMap<String, Integer>> getMaping() {
        return maping;
    }

    public void map() {
        try {
            HashMap<String, Integer> list = new HashMap<>();
            Scanner scanner = new Scanner(new File(fileName));
            //getting to the start position
            for (int i = 0; i < startPosition && scanner.hasNext(); ) {
                String word = scanner.next();
                i += word.length();
            }
            StringBuilder stringBuilder = new StringBuilder();
            //reading the block in a StringBuilder
            for (int i=0; i<endPosition-startPosition && scanner.hasNext();) {
                if (scanner.hasNext()) {
                    String word = scanner.next();
                    i += word.length();
                    stringBuilder.append(" ");
                    stringBuilder.append(word);
                }
            }
            //creating the mapping of the list of words
            String data = new String(stringBuilder).toLowerCase();
            String[] dataList = data.split("\\W+");
            for (String cuvant : dataList) {
                if (!cuvant.equals(""))
                if (list.containsKey(cuvant)) {
                    Integer lastValue = list.get(cuvant);
                    list.replace(cuvant, lastValue + 1);
                } else {
                    list.put(cuvant, 1);
                }
            }
            //memorizing the final list with the name of the file and the list of words
            maping.put(fileName, list);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
