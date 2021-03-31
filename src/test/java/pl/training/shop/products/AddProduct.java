package pl.training.shop.products;

import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.junit.JUnitStory;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;

import java.util.Properties;

import static org.jbehave.core.reporters.StoryReporterBuilder.Format.*;

public class AddProduct extends JUnitStory {

    @Override
    public Configuration configuration() {
        var viewResources = new Properties();
        viewResources.put("decorateNonHtml", "true");
        return new MostUsefulConfiguration().useStoryReporterBuilder(new StoryReporterBuilder()
                .withViewResources(viewResources).withFormats(CONSOLE, TXT, HTML, XML));
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), new AddProductSteps());
    }

}
