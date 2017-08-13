package gqqnbig.simplechat;

/**
 * Created by CHOUUT on 12/8/2017.
 */

public class ChatBubble {

    private String content;
    private boolean notMyMessage;

    public ChatBubble(String content, boolean notMyMessage) {
        this.content = content;
        this.notMyMessage = notMyMessage;
    }

    public String getContent() {
        return content;
    }

    public boolean notMyMessage() {
        return notMyMessage;
    }
}