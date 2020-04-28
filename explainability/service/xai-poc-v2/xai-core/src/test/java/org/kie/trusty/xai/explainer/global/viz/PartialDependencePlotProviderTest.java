package org.kie.trusty.xai.explainer.global.viz;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.DoubleStream;

import io.swagger.client.api.GlobalApi;
import org.junit.Test;
import org.kie.trusty.xai.explainer.utils.DataUtils;
import org.kie.trusty.xai.handler.ApiException;
import org.kie.trusty.xai.model.DataDistribution;
import org.kie.trusty.xai.model.ModelInfo;
import org.kie.trusty.xai.model.Output;
import org.kie.trusty.xai.model.PredictionInput;
import org.kie.trusty.xai.model.PredictionOutput;
import org.kie.trusty.xai.model.TabularData;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PartialDependencePlotProviderTest {

    @Test
    public void testPdp() throws FileNotFoundException {
        GlobalApi api = new TestGlobalApi();
        PartialDependencePlotProvider partialDependencePlotProvider = new PartialDependencePlotProvider(api);
        ModelInfo modelInfo = mock(ModelInfo.class);
        when(modelInfo.getInputShape()).thenReturn(10);
        when(modelInfo.getOutputShape()).thenReturn(1);
        Collection<TabularData> pdps = partialDependencePlotProvider.explain(modelInfo);
        assertNotNull(pdps);
        for (TabularData tabularData : pdps) {
            writeAsciiGraph(tabularData, new PrintWriter(new File("target/pdp" + tabularData.getFeature().getName() + ".txt")));
        }
    }

    private void writeAsciiGraph(TabularData tabularData, PrintWriter out) {
        double[] outputs = tabularData.getYAxis().stream().mapToDouble(bd -> bd.doubleValue()).toArray();
        double max = DoubleStream.of(outputs).max().getAsDouble();
        double min = DoubleStream.of(outputs).min().getAsDouble();
        outputs = Arrays.stream(outputs).map(d -> d * max / min).toArray();
        double curMax = 1 + DoubleStream.of(outputs).max().getAsDouble();
        ;
        int tempIdx = -1;
        for (int k = 0; k < tabularData.getXAxis().size(); k++) {
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
        for (int j = 0; j < data.getXAxis().get(i).intValue(); j++) {
            out.print(" ");
        }
        out.println("*");
    }

    private static class TestGlobalApi extends GlobalApi {

        private final DataDistribution dataDistribution = DataUtils.generateDistribution(10);

        @Override
        public List<PredictionOutput> predict(List<PredictionInput> body) throws ApiException {
            List<PredictionOutput> outputs = new ArrayList<>(body.size());
            for (PredictionInput input : body) {
                double result = DoubleStream.of(DataUtils.toNumbers(input)).summaryStatistics().getAverage() - 0.5;
                PredictionOutput output = new PredictionOutput();
                Output e1 = new Output();
                e1.setLabel(String.valueOf(result));
                e1.setScore(new BigDecimal(result));
                output.setOutputs(List.of(e1));
                outputs.add(output);
            }
            return outputs;
        }

        @Override
        public DataDistribution dataDistribution() throws ApiException {
            return dataDistribution;
        }
    }
}