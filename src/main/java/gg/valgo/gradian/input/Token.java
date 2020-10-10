package gg.valgo.gradian.input;

import java.util.Objects;

/**
 * Represents a token in a token list. All tokens have IDs, and some have data attached to them as well.
 * @param <DataType> The type of the token data.
 */
public class Token<DataType> {
    /**
     * The token data.
     */
    private DataType data;

    /**
     * The token id.
     */
    private String id;

    /**
     * Creates a new token with a given ID and data. Use null for no data.
     * @param id The token id.
     * @param data The token data.
     */
    public Token(String id, DataType data) {
        this.id = id;
        this.data = data;
    }

    /**
     * Gets the token data.
     * @return The token data.
     */
    public DataType getData() {
        return data;
    }

    /**
     * Sets the token data.
     * @param data The token data.
     */
    public void setData(DataType data) {
        this.data = data;
    }

    /**
     * Gets the token id.
     * @return The token id.
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the token id.
     * @param id The token id.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Checks if two tokens have the same ID.
     * @param token The other token.
     * @return Whether the two IDs are equal.
     */
    public boolean idEquals(Token<?> token) {
        return getId().equals(token.getId());
    }

    /**
     * Converts this token into its string representation.
     * @return The string representation.
     */
    @Override
    public String toString() {
        if (data == null) {
            return getId();
        }

        return getId() + "(" + getData() + ")";
    }

    /**
     * Checks if two tokens are equal.
     * @param o The other token.
     * @return Whether the two tokens are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;
        Token<?> token = (Token<?>) o;
        return Objects.equals(getData(), token.getData()) &&
                getId().equals(token.getId());
    }

    /**
     * Hashes this token.
     * @return A hash.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getData(), getId());
    }

    /**
     * Creates a token with no data, only an ID.
     * @param id The id.
     * @return The dataless token.
     */
    public static Token<Object> dataless(String id) {
        return new Token<>(id, null);
    }
}