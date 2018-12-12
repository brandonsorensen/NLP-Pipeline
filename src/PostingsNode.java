import java.util.LinkedList;

class PostingsNode {
    private LinkedList<String> postingsList;
    private int frequency;

    PostingsNode(LinkedList<String> postingsList, int frequency) {
        this.postingsList = postingsList;
        frequency = postingsList.size();
    }

    int getFrequency() {
        return frequency;
    }

    LinkedList<String> getPostings() {
        return postingsList;
    }
}
