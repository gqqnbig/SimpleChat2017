package gqqnbig.simplechat;

/**
 * Created by CHOUUT on 12/8/2017.
 */

public class ChatBubble {

    private String content;
    private int side;

    public ChatBubble(String content, int notMyMessage) {
        this.content = content;
        this.side = notMyMessage;
    }

    public String getContent() {
        return content;
    }

    public int notMyMessage() {
        return side;
    }
}