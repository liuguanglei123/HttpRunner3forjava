package org.hrun.junittest;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.junit.platform.launcher.listeners.SummaryGeneratingListener;
import org.junit.platform.launcher.listeners.TestExecutionSummary;

import static org.junit.platform.engine.discovery.ClassNameFilter.includeClassNamePatterns;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;

public class MainTest {
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
//        JUnitCore.runClasses(new Class[] {AppTest.class});
        LauncherDiscoveryRequest request = LauncherDiscoveryRequestBuilder.request()
                .selectors(
//                        selectPackage("org.example"),
                        selectClass(AppTest.class)
                )
                .filters(
                        includeClassNamePatterns(".*Test")
                )
                .build();

        Launcher launcher = LauncherFactory.create();

        // Register a listener of your choice
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        launcher.registerTestExecutionListeners(listener);

        launcher.execute(request);

        TestExecutionSummary summary = listener.getSummary();
        // Do something with the TestExecutionSummary.
    }
}
