package com.redhat.developer.utils;

import java.security.SecureRandom;

import com.redhat.developer.model.Model;
import com.redhat.developer.xai.ExplanationTestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExplainabilityUtilsTest {

    private static final SecureRandom random = new SecureRandom();

    @Test
    public void testExplainabilityNoExplanation() {
        double v = ExplainabilityUtils.quantifyExplainability(0, 0, 0);
        assertFalse(Double.isNaN(v));
        assertFalse(Double.isInfinite(v));
        assertEquals(0, v);
    }

    @Test
    public void testExplainabilityNoExplanationWithInteraction() {
        double v = ExplainabilityUtils.quantifyExplainability(0, 0, 1);
        assertFalse(Double.isNaN(v));
        assertFalse(Double.isInfinite(v));
        assertEquals(0, v);
    }

    @Test
    public void testExplainabilityRandomchunksNoInteraction() {
        double v = ExplainabilityUtils.quantifyExplainability(random.nextInt(), random.nextInt(), 1d /
                random.nextInt(100));
        assertFalse(Double.isNaN(v));
        assertFalse(Double.isInfinite(v));
        assertTrue(v >= 0 && v <= 1);
    }

    @Test
    public void testExplainabilityRandom() {
        double v = ExplainabilityUtils.quantifyExplainability(random.nextInt(), random.nextInt(), random.nextDouble());
        assertFalse(Double.isNaN(v));
        assertFalse(Double.isInfinite(v));
        assertTrue(v >= 0 && v <= 1);
    }
}