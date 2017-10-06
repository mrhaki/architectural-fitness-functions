package guru.nidi.codeassert.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GradleAnalyzerConfig extends AnalyzerConfig {
    public static Gradle gradle() {
        return gradle(null);
    }

    public static Gradle gradle(String module) {
        return new Gradle(module);
    }

    public static class Gradle {
        private final String module;

        public Gradle(String module) {
            this.module = module;
        }

        public AnalyzerConfig main(String... packages) {
            return new AnalyzerConfig(
                    path(packages, "src/main/java/"),
                    path(packages, "build/classes/java/main/", "out/production/classes"));
        }

        public AnalyzerConfig test(String... packages) {
            return new AnalyzerConfig(
                    path(packages, "src/test/java/"),
                    path(packages, "build/classes/test", "out/test/classes"));
        }

        public AnalyzerConfig mainAndTest(String... packages) {
            return new AnalyzerConfig(
                    path(packages, "src/main/java/", "src/test/java/"),
                    path(packages, "target/classes/main", "out/production/classes", "target/classes/test", "out/test/classes"));
        }

        private List<Path> path(String[] packs, String... paths) {
            final List<Path> res = new ArrayList<>();
            for (final String path : paths) {
                final String normPath = path(path);
                if (packs.length == 0) {
                    res.add(new Path(normPath, ""));
                } else {
                    for (final String pack : packs) {
                        final String normPack = pack.replace('.', '/');
                        res.add(new Path(normPath, normPack));
                    }
                }
            }
            return res;
        }

        private String path(String relative) {
            if (module == null || module.length() == 0 || runningInModuleDir()) {
                return relative;
            }
            return module.endsWith("/")
                    ? module + relative
                    : module + "/" + relative;
        }

        private boolean runningInModuleDir() {
            return new File("").getAbsoluteFile().getName().equals(module);
        }
    }
}
