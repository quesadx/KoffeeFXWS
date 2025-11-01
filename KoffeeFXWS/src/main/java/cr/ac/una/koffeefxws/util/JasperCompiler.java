package cr.ac.una.koffeefxws.util;

import net.sf.jasperreports.engine.JasperCompileManager;
import java.io.File;

/**
 * Utility class to compile JRXML files to .jasper at build time
 */
public class JasperCompiler {
    
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: JasperCompiler <input.jrxml> <output.jasper>");
            System.exit(1);
        }
        
        String inputFile = args[0];
        String outputFile = args[1];
        
        try {
            // Ensure output directory exists
            File output = new File(outputFile);
            output.getParentFile().mkdirs();
            
            // Set compiler to use JDT with current classpath
            System.setProperty("net.sf.jasperreports.compiler.class", "net.sf.jasperreports.engine.design.JRJdtCompiler");
            System.setProperty("net.sf.jasperreports.compiler.classpath", System.getProperty("java.class.path"));
            System.setProperty("net.sf.jasperreports.compiler.keep.java.file", "false");
            
            System.out.println("Compiling " + inputFile + " to " + outputFile);
            JasperCompileManager.compileReportToFile(inputFile, outputFile);
            System.out.println("Successfully compiled report to " + outputFile);
        } catch (Exception e) {
            System.err.println("Error compiling report: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
