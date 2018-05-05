package dk.sdu.mmmi.fppt.gsonquickcheck;

import java.util.Arrays;
import java.util.Objects;

public class TestObject {
    private String text;
    private int number;
    private String[] texts;
    private boolean bool;

    public TestObject(String text, int number, String[] texts, boolean bool) {
        this.text = text;
        this.number = number;
        this.texts = texts;
        this.bool = bool;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String[] getTexts() {
        return texts;
    }

    public void setTexts(String[] texts) {
        this.texts = texts;
    }

    public boolean isBool() {
        return bool;
    }

    public void setBool(boolean bool) {
        this.bool = bool;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestObject that = (TestObject) o;
        return number == that.number &&
                bool == that.bool &&
                Objects.equals(text, that.text) &&
                Arrays.equals(texts, that.texts);
    }

    @Override
    public int hashCode() {

        int result = Objects.hash(text, number, bool);
        result = 31 * result + Arrays.hashCode(texts);
        return result;
    }

    @Override
    public String toString() {
        return "TestObject{" +
                "text='" + text + '\'' +
                ", number=" + number +
                ", texts=" + Arrays.toString(texts) +
                ", bool=" + bool +
                '}';
    }
}
