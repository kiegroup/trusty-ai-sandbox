package org.kie.trusty.xai.impl.explainer.global.viz;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.DoubleStream;

import org.junit.Test;
import org.kie.trusty.m2x.api.GlobalApi;
import org.kie.trusty.m2x.handler.ApiException;
import org.kie.trusty.m2x.model.DataDistribution;
import org.kie.trusty.m2x.model.Feature;
import org.kie.trusty.m2x.model.ModelInfo;
import org.kie.trusty.m2x.model.Output;
import org.kie.trusty.m2x.model.PredictionInput;
import org.kie.trusty.m2x.model.PredictionOutput;
import org.kie.trusty.m2x.model.Type;
import org.kie.trusty.m2x.model.Value;
import org.kie.trusty.xai.impl.explainer.utils.DataUtils;
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
        when(modelInfo.getPredictionEndpoint()).thenReturn(URI.create("/path/to/model"));
        PredictionInput syntInput = mock(PredictionInput.class);
        List<Feature> features = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            features.add(DataUtils.doubleToFeature(i));
        }
        when(syntInput.getFeatures()).thenReturn(features);
        when(modelInfo.getInputShape()).thenReturn(syntInput);
        PredictionOutput syntOutput = mock(PredictionOutput.class);
        List<Output> outputs = mock(List.class);
        when(outputs.size()).thenReturn(1);
        when(syntOutput.getOutputs()).thenReturn(outputs);
        when(modelInfo.getOutputShape()).thenReturn(syntOutput);
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

    private static class TestGlobalApi extends GlobalApi {

        private final DataDistribution dataDistribution = DataUtils.generateDistribution(10);

        @Override
        public List<PredictionOutput> predict(List<PredictionInput> body) {
            List<PredictionOutput> predictionOutputs = new ArrayList<>(body.size());
            for (PredictionInput input : body) {
                double result = DoubleStream.of(DataUtils.toNumbers(input)).summaryStatistics().getAverage() - 0.5;
                predictionOutputs.add(new PredictionOutput(List.of(new Output(new Value<>(result), Type.NUMBER, result))));
            }
            return predictionOutputs;
        }

        @Override
        public DataDistribution dataDistribution() {
            return dataDistribution;
        }
    }
}