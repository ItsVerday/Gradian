package gg.valgo.gradian.input;

import java.util.Objects;

public class Token<DataType> {
    private DataType data;
    private String id;

    public Token(String id, DataType data) {
        this.id = id;
        this.data = data;
    }

    public DataType getData() {
        return data;
    }

    public void setData(DataType data) {
        this.data = data;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean idEquals(Token<?> token) {
        return getId().equals(token.getId());
    }

    public static Token<Object> dataless(String id) {
        return new Token<>(id, null);
    }

    @Override
    public String toString() {
        if (data == null) {
            return getId();
        }

        return getId() + "(" + getData() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;
        Token<?> token = (Token<?>) o;
        return Objects.equals(getData(), token.getData()) &&
                getId().equals(token.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getData(), getId());
    }
}