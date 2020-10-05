package gg.valgo.gradian.input;

import java.util.Objects;

public class Token {
    private String id;

    public Token(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;
        Token token = (Token) o;
        return getId().equals(token.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}