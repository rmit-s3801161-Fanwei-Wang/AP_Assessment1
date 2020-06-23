package model;

public class Reply {
    private String postId; // the id of the post which the reply is associated with
    private double value; // a decimal number, responder need to enter a positive value
    private String responderId; // the id of the student who replies

    public Reply(String postId, double value, String responderId) {
        this.postId = postId;
        this.value = value;
        this.responderId = responderId;
    }

    public Reply() {

    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public String getResponderId() {
        return responderId;
    }

    public void setResponderId(String responderId) {
        this.responderId = responderId;
    }


    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        ret.append(postId);
        ret.append("\t");
        ret.append(value);
        ret.append("\t");
        ret.append(responderId);
        return ret.toString();
    }
}
