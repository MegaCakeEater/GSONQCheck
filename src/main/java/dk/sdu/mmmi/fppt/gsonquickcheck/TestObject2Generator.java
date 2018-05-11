package dk.sdu.mmmi.fppt.gsonquickcheck;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.text.StringEscapeUtils;

public class TestObject2Generator extends Generator<TestObject2> {

    public TestObject2Generator() {
        super(TestObject2.class);
    }

    @Override
    public boolean canShrink(Object larger) {
        TestObject t;
        try {
            t = (TestObject) larger;
        } catch (ClassCastException c) {
            return false;
        }
        return !t.equals(nullObject);
    }

    private TestObject nullObject = new TestObject("", 0, new String[0], false);

    @Override
    public List<TestObject2> doShrink(SourceOfRandomness random, TestObject2 larger) {
        if (nullObject.equals(larger)) return Collections.emptyList();

        TestObject2 nullString = new TestObject2("", larger.getNumber(), larger.getTexts(), larger.isBool(), null);
        TestObject2 nullArray = new TestObject2(larger.getText(), larger.getNumber(), new String[0], larger.isBool(), null);
        TestObject2 nullInt = new TestObject2(larger.getText(), 0, larger.getTexts(), larger.isBool(), null);
        TestObject2 nullBool = new TestObject2(larger.getText(), larger.getNumber(), larger.getTexts(), false, null);

        return Stream.of(
                nullArray,
                nullBool,
                nullInt,
                nullString)
                .filter(f -> !f.equals(nullObject)).distinct()
                .collect(Collectors.toList());
    }

    //Simple random generator - uden json regler
    @Override
    public TestObject2 generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
        String text = StringEscapeUtils.escapeJava(gen().type(String.class).generate(sourceOfRandomness, generationStatus));
        int number = sourceOfRandomness.nextInt();
        int arrayNumber = sourceOfRandomness.nextInt(10);
        String[] texts = new String[arrayNumber];
        for(int i = 0; i<arrayNumber; i++) {
            texts[i] = StringEscapeUtils.escapeJava(gen().type(String.class).generate(sourceOfRandomness, generationStatus));
        }
        int nestNumber = sourceOfRandomness.nextInt(20);
        boolean bool = sourceOfRandomness.nextBoolean();
        
        TestObject2 root = new TestObject2(text, number, texts, bool, null);
        TestObject2 test = root;
        for(int i = 0; i<nestNumber; i++) {
             test.setNestedObject(new TestObject2(text, number, texts, bool, null));
             test = test.getNestedObject();
        }
        
        return root;
    }

    @Override
    public BigDecimal magnitude(Object value) {
        TestObject t = (TestObject) value;
        return BigDecimal.valueOf(
                -(Math.abs(t.getNumber() / 100) + t.getTexts().length + t.getText().length() + (!t.isBool() ? 1 : 0))
        );  /*BigDecimal.valueOf(new Random().nextInt(Integer.MAX_VALUE));*/
    }
}
