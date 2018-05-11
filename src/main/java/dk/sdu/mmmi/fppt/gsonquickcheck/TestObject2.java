package dk.sdu.mmmi.fppt.gsonquickcheck;

import java.util.Arrays;
import java.util.Objects;
import org.apache.commons.text.StringEscapeUtils;

public class TestObject2 {
    private String text;
    private int number;
    private String[] texts;
    private boolean bool;
    private TestObject2 nestedObject;

    public TestObject2(String text, int number, String[] texts, boolean bool, TestObject2 nestedObject) {
        this.text = text;
        this.number = number;
        this.texts = texts;
        this.bool = bool;
        this.nestedObject = nestedObject;
    }

    public String getText() {
        return text;
    }
    
    public TestObject2 getNestedObject() {
        return this.nestedObject;
    }
    
    public void setNestedObject(TestObject2 obj) {
        this.nestedObject = obj;
    }
    
    public TestObject copy() {
        return new TestObject(text, number, texts, bool);
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
        TestObject2 that = (TestObject2) o;
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
                ", nestedObject=" + nestedObject +
                '}';
    }
    
    public String getJsonTextFromTexts(){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for(int i = 0; i < getTexts().length; i++){
            if(i != getTexts().length-1){
                sb.append("\"").append(StringEscapeUtils.escapeJava(getTexts()[i])).append("\"").append(",");
            }else {
                sb.append("\"").append(StringEscapeUtils.escapeJava(getTexts()[i])).append("\"");
            }
        }
        sb.append("]");
        return sb.toString();
    }
    
    public String toJson() {
        String objToJson = "{\"text\"" + ":\"" + StringEscapeUtils.escapeJava(getText()) +"\"," 
                + "\"number\"" + ":" + getNumber() + "," 
                + "\"texts\"" + ":" + getJsonTextFromTexts()+ ","
                + "\"bool\"" + ":" + isBool() + "}";
                return objToJson;
    }
}
