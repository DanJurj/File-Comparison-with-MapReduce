import Document.Document;
import Document.IndexedDocument;
import Map.MapingWorkerThread;
import Map.Mapper;
import Reduce.Reducer;
import Reduce.ReducingWorkerThread;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TheftChecker {
    private int numberOfThreadWorkers;
    private String inputFileName;
    private String outputFileName;
    private Document originalDocument;
    private int fragmentsDimension;
    private double similarityThreshold;
    private int numberOfSuspectedDocuments;
    private List<Document> suspectedDocuments;
    private IndexedDocument indexedOriginalDocument;
    private List<IndexedDocument> indexedSuspectDocuments;

    public TheftChecker(int numberOfThreadWorkers, String inputFileName, String outputFileName) {
        //memorizing inputs
        this.numberOfThreadWorkers = numberOfThreadWorkers;
        this.inputFileName = inputFileName;
        this.outputFileName = outputFileName;
        //initializing the list of the indexed suspected ducuments
        indexedSuspectDocuments = new LinkedList<>();
    }

    /**
     * Reading the input.txt file and memorizing the values
     */
    private void readInputs() {
        try {
            Scanner scanner = new Scanner(new File(inputFileName));
            String originalDocumentFileName = scanner.next();
            originalDocument = new Document(originalDocumentFileName);
            fragmentsDimension = Integer.parseInt(scanner.next());
            similarityThreshold = Double.parseDouble(scanner.next());
            numberOfSuspectedDocuments = Integer.parseInt(scanner.next());
            suspectedDocuments = new ArrayList<>();
            for (int i = 0; i < numberOfSuspectedDocuments; i++) {
                suspectedDocuments.add(new Document(scanner.next()));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * MapReduce Algorithm
     * @param filename - the name of the file to be indexed
     * @return an object of type IndexedDocument which holds the name of the file and a list with its words and their frequency
     */
    public IndexedDocument indexDocument(String filename) {
        //Mapping
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreadWorkers);
        File doc1 = new File(filename);
        long fileLength = doc1.length();
        List<Mapper> mapperList = new LinkedList<>();
        for (int currentPosition = 0; currentPosition < fileLength; currentPosition += fragmentsDimension) {
            Mapper mapper = new Mapper(filename, currentPosition, currentPosition + fragmentsDimension);
            Runnable worker = new MapingWorkerThread(mapper);
            executorService.execute(worker);
            mapperList.add(mapper);
        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
        }
        //Reducing
        ExecutorService executorService1 = Executors.newFixedThreadPool(numberOfThreadWorkers);
        ConcurrentHashMap<String, Integer> finalIndex = new ConcurrentHashMap<>();
        for (Mapper currentMapper : mapperList) {
            HashMap<String, HashMap<String, Integer>> maping = currentMapper.getMaping();
            Reducer reducer = new Reducer(filename, finalIndex);
            Runnable worker = new ReducingWorkerThread(reducer,maping.get(filename));
            executorService1.execute(worker);
        }
        executorService1.shutdown();
        while (!executorService1.isTerminated()) { }

        IndexedDocument indexedDocument = new IndexedDocument(filename, finalIndex);
        return indexedDocument;
    }

    /**
     * indexing all the documents from the input file
     */
    public void indexDocuments() {
        readInputs();
        indexedOriginalDocument = indexDocument(originalDocument.getName());
        for (Document suspectedDocument: suspectedDocuments) {
            indexedSuspectDocuments.add(indexDocument(suspectedDocument.getName()));
        }
    }

    /**
     * Checking to see which documents have a similarity above the one given as parameter
     */
    public void check() {
        try {
            PrintWriter writer = new PrintWriter(outputFileName);
            writer.println("Rezultate pentru: ("+indexedOriginalDocument.getName()+")");
            for (IndexedDocument suspectedDocument: indexedSuspectDocuments) {
                double sim = indexedOriginalDocument.sim(suspectedDocument);;
                if (sim > similarityThreshold) {
                    writer.println(suspectedDocument.getName() + " (" + sim + "%)");
                }
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
