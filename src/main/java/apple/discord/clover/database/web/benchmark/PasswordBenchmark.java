package apple.discord.clover.database.web.benchmark;

import com.password4j.Argon2Function;
import com.password4j.BenchmarkResult;
import com.password4j.SystemChecker;
import com.password4j.types.Argon2;
import org.apache.logging.log4j.Logger;

public class PasswordBenchmark {

    public static void benchmark(Logger logger) {
        logger.info("Running password benchmark");
        long maxMilliseconds = 500;
        int memory = 4096;
        int threads = 4;
        int outputLength = 128;
        Argon2 type = Argon2.ID;

        BenchmarkResult<Argon2Function> result = SystemChecker.benchmarkForArgon2(maxMilliseconds, memory, threads, outputLength,
            type);
        logger.info("duuuu");

        int iterations = result.getPrototype().getIterations();
        long realElapsed = result.getElapsed();
        logger.info("iterations: {}, realElapsed: {}", iterations, realElapsed);
        logger.fatal("Ran password benchmark. exiting now");
    }
}
