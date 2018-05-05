package dk.sdu.mmmi.fppt.gsonquickcheck;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

public class TestObjectGenerator extends Generator<TestObject> {

    public TestObjectGenerator() {
        super(TestObject.class);
    }


    //Simple random generator - uden json regler
    @Override
    public TestObject generate(SourceOfRandomness sourceOfRandomness, GenerationStatus generationStatus) {
        String text = gen().type(String.class).generate(sourceOfRandomness, generationStatus);
        int number = sourceOfRandomness.nextInt();
        String[] texts = gen().type(String[].class).generate(sourceOfRandomness, generationStatus);
        boolean bool = sourceOfRandomness.nextBoolean();
        return new TestObject(text, number, texts, bool);
    }


    private TestObjectInterface testObjectInterface;

    public void configure(TestObjectInterface testObjectInterface) {
        this.testObjectInterface = testObjectInterface;
    }
}
