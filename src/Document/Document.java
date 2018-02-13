package Document;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Document {
    private String name;
    private String content;
    private int wordCount;

    public Document(String name) {
        this.name = name;
        content = readContent(name);
        setWordCount();
    }

    private String readContent(String name) {
        try {
            return new String(Files.readAllBytes(Paths.get(name)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public void setWordCount() {
        String[] words = content.split("\\W+");
        wordCount = words.length;
    }

    public int getWordCount() {
        return wordCount;
    }
}
