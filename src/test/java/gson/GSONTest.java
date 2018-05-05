/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gson;

import com.pholser.junit.quickcheck.Property;
import com.pholser.junit.quickcheck.runner.JUnitQuickcheck;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;

/**
 *
 * @author Bogs
 */
@RunWith(JUnitQuickcheck.class)
public class GSONTest {
        @Property public void concatenationLength(String s1, String s2) {
            assertEquals(s1.length() + s2.length(), (s1 + s2).length());
        }
}
