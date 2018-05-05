/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.sdu.mmmi.fppt.gsonquickcheck;

import com.pholser.junit.quickcheck.generator.GenerationStatus;
import com.pholser.junit.quickcheck.generator.Generator;
import com.pholser.junit.quickcheck.random.SourceOfRandomness;

/**
 *
 * @author Bogs
 */
public class JSONGenerator extends Generator<String> {

    public JSONGenerator() {
        super(String.class);
    }
    
    @Override
    public String generate(SourceOfRandomness sor, GenerationStatus gs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

   
    
}
