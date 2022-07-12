package com.eaton.maven.plugin.karaf.validation.osgi.export;

import com.eaton.maven.plugin.karaf.validation.report.CriticalityLevel;
import com.eaton.maven.plugin.karaf.validation.report.Issue;
import com.eaton.maven.plugin.karaf.validation.report.Report;
import com.eaton.maven.plugin.karaf.validation.util.MavenUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.jar.JarInputStream;
import org.apache.felix.utils.manifest.Parser;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * Analyze the whole project and output all errors in bundles export-package.
 */
@Mojo(name = "check-packages", aggregator = true, defaultPhase = LifecyclePhase.VERIFY)
public class OsgiExportPackageCheckMojo extends AbstractMojo {

    // Constants --------------------------------------------------------------

    private static final String TARGET_PACKAGING = "bundle";

    private static final String EXPORT_PACKAGE = "Export-Package";

    // Fields -----------------------------------------------------------------

    /**
     * Contains the full list of projects in the reactor.
     */
    @Parameter(defaultValue = "${reactorProjects}", readonly = true, required = true)
    private List<MavenProject> reactorProjects;

    // Mojo methods -----------------------------------------------------------

    /*
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    @Override
    public void execute() throws MojoFailureException {
        var report = new Report("'check-packages' execution report:",
                "Issues:", "Warnings:", "Tips:");
        var packageExportAnalyzer = new PackageExportAnalyzer(report);
        for (MavenProject project : reactorProjects) {

            // Check if the module will generate an OSGI bundle
            var packaging = project.getPackaging();
            if (!TARGET_PACKAGING.equalsIgnoreCase(packaging)) {
                continue;
            }

            // Read manifest and list all package exports
            var build = project.getBuild();

            String exportPackage = null;
            try (var input = new JarInputStream( new FileInputStream( build.getDirectory() + File.separator + build.getFinalName() + ".jar" ) );) {
                exportPackage = input.getManifest().getMainAttributes().getValue( EXPORT_PACKAGE );
            } catch (IOException exception) {
                // No manifest found
                report.setSuccess(false);
                report.add(new Issue(CriticalityLevel.ERROR,
                        String.format("Module '%s''s packaging is 'bundle' and it does not contain manifest file.", MavenUtil.getModuleIdentifier(project))));
            }

            // No export-package found
            if ( exportPackage == null || exportPackage.isEmpty() ) {
                report.add(new Issue(CriticalityLevel.WARN,
                        String.format("Module '%s''s packaging is 'bundle' and there is no Export-Package entry into the manifest file.", MavenUtil.getModuleIdentifier(project))));
            }

            // Add all clauses to the analyzer
            for ( var clause : Parser.parseHeader( exportPackage ) )
            {
                packageExportAnalyzer.add(project, clause);
            }
        }

        // Validate that no module exports are colliding
        packageExportAnalyzer.validate();

        var reportString= report.toString();
        // log report
        // TODO add logger and log in any cases

        // throw exception with the report
        if(!report.getSuccess()){
            throw new MojoFailureException(reportString);
        }
    }

}