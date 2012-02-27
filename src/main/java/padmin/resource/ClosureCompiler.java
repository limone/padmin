package padmin.resource;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import padmin.app.PadminException;

import com.google.javascript.jscomp.CompilationLevel;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.JSSourceFile;
import com.google.javascript.jscomp.WarningLevel;

public class ClosureCompiler {
  private static final Logger log = LoggerFactory.getLogger(ClosureCompiler.class);

  public static String compileJavaScript(String[] externalJsFiles, String[] primaryJsFiles) throws PadminException {
    try {
      log.debug("Compiling JS files.");
      Compiler compiler = new Compiler();

      CompilerOptions options = new CompilerOptions();
      CompilationLevel.SIMPLE_OPTIMIZATIONS.setOptionsForCompilationLevel(options);
      WarningLevel.QUIET.setOptionsForWarningLevel(options);

      List<JSSourceFile> externalJavascriptFiles = new ArrayList<>();
      for (String filename : externalJsFiles) {
        InputStream is = ClosureCompiler.class.getResourceAsStream("/padmin/resource/js/" + filename);
        externalJavascriptFiles.add(JSSourceFile.fromInputStream(filename, is));
      }

      List<JSSourceFile> primaryJavascriptFiles = new ArrayList<>();
      for (String filename : primaryJsFiles) {
        InputStream is = ClosureCompiler.class.getResourceAsStream("/padmin/resource/js/" + filename);
        primaryJavascriptFiles.add(JSSourceFile.fromInputStream(filename, is));
      }

      compiler.compile(externalJavascriptFiles, primaryJavascriptFiles, options);

      log.debug("Files compiled.");
      return compiler.toSource();
    } catch (Exception ex) {
      log.error("Could not compile JavaScript.", ex);
      throw new PadminException("Could not compile JavaScript.", ex);
    }
  }
}