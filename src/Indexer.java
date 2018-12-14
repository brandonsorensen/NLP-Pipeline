import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.LinkedList;

public static class Indexer {

    Indexer(String filePath) throws FileNotFoundException {
        this.filePath = filePath;
        tweets = index(filePath);
    }

    private String[] clean(String line) {
        return line.split("\t");
    }

    LinkedList index(String filePath) throws FileNotFoundException {
        File file = new File(filePath);
        Scanner scanner = new Scanner(file);
        scanner.useDelimiter(",");
        LinkedList<String> retVal = new LinkedList<>();

        while (scanner.hasNext()) {
            String tweet = scanner.next().split("\t")[0];
            System.out.println(tweet);
            retVal.add(tweet);
        }
        return retVal;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LinkedList<String> getTweets() {
        return tweets;
    }

    public void setTweets(LinkedList<String> tweets) {
        this.tweets = tweets;
    }
}
