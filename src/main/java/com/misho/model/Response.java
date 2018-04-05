package com.misho.model;

/**
 * Created by Grant Little grant@grantlittle.me
 */
public class Response {

    private int id = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Response{");
        sb.append("id=").append(id);
        sb.append('}');
        return sb.toString();
    }
}
