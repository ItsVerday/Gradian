package gg.valgo.gradian;

import java.util.List;

public abstract class ParserInputList<ElementType> {
    protected int index;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public abstract List<ElementType> getElements();

    public List<ElementType> getTruncated() {
        List<ElementType> elements = getElements();
        return elements.subList(index, elements.size());
    }

    public ElementType getCurrent() {
        return getElements().get(index);
    }

    public int getLength() {
        return getElements().size();
    }

    public boolean isEndOfInput() {
        return index >= getLength();
    }
}