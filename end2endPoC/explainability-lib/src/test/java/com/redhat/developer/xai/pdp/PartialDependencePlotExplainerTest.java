package com.redhat.developer.xai.pdp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.DoubleStream;

import com.redhat.developer.model.Model;
import com.redhat.developer.model.TabularData;
import com.redhat.developer.xai.ExplanationTestUtils;
import com.redhat.developer.xai.pdp.PartialDependencePlotExplainer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PartialDependencePlotExplainerTest {

    @Test
    public void testPdpTextClassifier() throws FileNotFoundException {
        PartialDependencePlotExplainer partialDependencePlotProvider = new PartialDependencePlotExplainer();
        Model modelInfo = ExplanationTestUtils.getTextClassifier();
        Collection<TabularData> pdps = partialDependencePlotProvider.explain(modelInfo);
        assertNotNull(pdps);
        for (TabularData tabularData : pdps) {
            writeAsciiGraph(tabularData, new PrintWriter(new File("target/pdp" + tabularData.getFeature().getName() + ".txt")));
        }
    }

    private void writeAsciiGraph(TabularData tabularData, PrintWriter out) {
        double[] outputs = tabularData.getY();
        double max = DoubleStream.of(outputs).max().getAsDouble();
        double min = DoubleStream.of(outputs).min().getAsDouble();
        outputs = Arrays.stream(outputs).map(d -> d * max / min).toArray();
        double curMax = 1 + DoubleStream.of(outputs).max().getAsDouble();
        ;
        int tempIdx = -1;
        for (int k = 0; k < tabularData.getX().length; k++) {
            double tempMax = -Integer.MAX_VALUE;
            for (int j = 0; j < outputs.length; j++) {
                double v = outputs[j];
                if ((int) v < (int) curMax && (int) v > (int) tempMax && tempIdx != j) {
                    tempMax = v;
                    tempIdx = j;
                }
            }
            writeDot(tabularData, tempIdx, out);
            curMax = tempMax;
        }
        out.flush();
        out.close();
    }

    private void writeDot(TabularData data, int i, PrintWriter out) {
        for (int j = 0; j < data.getX()[i]; j++) {
            out.print(" ");
        }
        out.println("*");
    }
}