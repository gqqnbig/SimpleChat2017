package gqqnbig.simplechat;

/**
 * Created by CHOUUT on 12/8/2017.
 */

public class ChatBubble {

    private String content;
    private boolean myMessage;

    public ChatBubble(String content, boolean myMessage) {
        this.content = content;
        this.myMessage = myMessage;
    }

    public String getContent() {
        return content;
    }

    public boolean myMessage() {
        return myMessage;
    }
}