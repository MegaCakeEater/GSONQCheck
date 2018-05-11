package dk.sdu.mmmi.fppt.gsonquickcheck;

import com.pholser.junit.quickcheck.From;
import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.generator.GeneratorConfiguration;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import org.apache.commons.text.StringEscapeUtils;

public class TestObjectGenerator extends Generator<TestObject> {

    public TestObjectGenerator() {
        super(TestObject.class);
    }


    @Target({PARAMETER, FIELD, ANNOTATION_TYPE, TYPE_USE})
    @Retention(RUNTIME)
    @GeneratorConfiguration
    @From(TestObjectGenerator.class)
    public @interface TestObjectInterface {
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
    public List<TestObject> doShrink(SourceOfRandomness random, TestObject larger) {
        if (nullObject.equals(larger)) return Collections.emptyList();

        TestObject nullString = new TestObject("", larger.getNumber(), larger.getTexts(), larger.isBool());
        TestObject nullArray = new TestObject(larger.getText(), larger.getNumber(), new String[0], larger.isBool());
        TestObject nullInt = new TestObject(larger.getText(), 0, larger.getTexts(), larger.isBool());
        TestObject nullBool = new TestObject(larger.getText(), larger.getNumber(), larger.getTexts(), false);

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
    public TestObject generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
        String text = StringEscapeUtils.escapeJava(gen().type(String.class).generate(sourceOfRandomness, generationStatus));
        int number = sourceOfRandomness.nextInt();
        int arrayNumber = sourceOfRandomness.nextInt(10);
        String[] texts = new String[arrayNumber];
        for(int i = 0; i<arrayNumber; i++) {
            texts[i] = StringEscapeUtils.escapeJava(gen().type(String.class).generate(sourceOfRandomness, generationStatus));
        }
        boolean bool = sourceOfRandomness.nextBoolean();
        return new TestObject(text, number, texts, bool);
    }

    @Override
    public BigDecimal magnitude(Object value) {
        TestObject t = (TestObject) value;
        return BigDecimal.valueOf(
                -(Math.abs(t.getNumber() / 100) + t.getTexts().length + t.getText().length() + (!t.isBool() ? 1 : 0))
        );  /*BigDecimal.valueOf(new Random().nextInt(Integer.MAX_VALUE));*/
    }

    private TestObjectInterface testObjectInterface;

    public void configure(TestObjectInterface testObjectInterface) {
        this.testObjectInterface = testObjectInterface;
    }
}
