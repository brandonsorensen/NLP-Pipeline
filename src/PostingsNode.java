import java.util.LinkedList;

class PostingsNode {
    private LinkedList postingsList;
    private int frequency;

    PostingsNode(LinkedList<String> postingsList) {
        this.postingsList = postingsList;
        frequency = postingsList.size();
    }

    int getFrequency() {
        return frequency;
    }

    LinkedList getPostings() {
        return postingsList;
    }
}
