package io.joshuasalcedo.desktop.service;

import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Runs external processes off the JavaFX Application Thread.
 * <p>
 * Three usage patterns:
 * <ul>
 *   <li>{@link #exec(List)} — fire-and-forget, returns a CompletableFuture</li>
 *   <li>{@link #exec(List, Consumer)} — streams output line-by-line to a callback on the FX thread</li>
 *   <li>{@link #createTask(List)} — returns a JavaFX Task you can bind to UI (progress, message, etc.)</li>
 * </ul>
 */
@Component
public class ProcessService {

    private static final Logger logger = LoggerFactory.getLogger(ProcessService.class);

    /**
     * Run a command and return a future with the exit code.
     * Output goes to the logger.
     */
    public CompletableFuture<ProcessResult> exec(List<String> command) {
        return exec(command, null, null, null);
    }

    /**
     * Run a command, streaming each stdout line to the callback on the FX thread.
     */
    public CompletableFuture<ProcessResult> exec(List<String> command, Consumer<String> onOutput) {
        return exec(command, null, null, onOutput);
    }

    /**
     * Full options: command, working directory, environment, output callback.
     */
    public CompletableFuture<ProcessResult> exec(
            List<String> command,
            Path workingDir,
            Map<String, String> env,
            Consumer<String> onOutput
    ) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                ProcessBuilder pb = buildProcess(command, workingDir, env);
                Process process = pb.start();

                StringBuilder stdout = new StringBuilder();
                StringBuilder stderr = new StringBuilder();

                Thread outThread = readStream(process.inputReader(), stdout, onOutput);
                Thread errThread = readStream(process.errorReader(), stderr, onOutput);

                int exitCode = process.waitFor();
                outThread.join();
                errThread.join();

                var result = new ProcessResult(exitCode, stdout.toString(), stderr.toString());

                if (exitCode != 0) {
                    logger.warn("Process {} exited with code {}: {}", command.getFirst(), exitCode, stderr);
                } else {
                    logger.debug("Process {} completed successfully", command.getFirst());
                }

                return result;
            } catch (Exception e) {
                logger.error("Failed to execute: {}", command, e);
                return new ProcessResult(-1, "", e.getMessage());
            }
        });
    }

    /**
     * Creates a JavaFX Task for running a process. Bind to UI:
     * <pre>
     *   var task = processService.createTask(List.of("git", "status"));
     *   statusLabel.textProperty().bind(task.messageProperty());
     *   progressBar.progressProperty().bind(task.progressProperty());
     *   new Thread(task).start();
     * </pre>
     */
    public Task<ProcessResult> createTask(List<String> command) {
        return createTask(command, null, null);
    }

    public Task<ProcessResult> createTask(List<String> command, Path workingDir, Map<String, String> env) {
        return new Task<>() {
            @Override
            protected ProcessResult call() throws Exception {
                updateMessage("Running: " + command.getFirst());

                ProcessBuilder pb = buildProcess(command, workingDir, env);
                Process process = pb.start();

                StringBuilder stdout = new StringBuilder();
                StringBuilder stderr = new StringBuilder();

                Thread outThread = readStream(process.inputReader(), stdout, line ->
                        Platform.runLater(() -> updateMessage(line)));
                Thread errThread = readStream(process.errorReader(), stderr, null);

                int exitCode = process.waitFor();
                outThread.join();
                errThread.join();

                var result = new ProcessResult(exitCode, stdout.toString(), stderr.toString());

                if (exitCode == 0) {
                    updateMessage("Done: " + command.getFirst());
                } else {
                    updateMessage("Failed: " + command.getFirst() + " (exit " + exitCode + ")");
                }

                return result;
            }

            @Override
            protected void cancelled() {
                updateMessage("Cancelled: " + command.getFirst());
            }
        };
    }

    private ProcessBuilder buildProcess(List<String> command, Path workingDir, Map<String, String> env) {
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(false);
        if (workingDir != null) pb.directory(workingDir.toFile());
        if (env != null) pb.environment().putAll(env);
        return pb;
    }

    private Thread readStream(BufferedReader reader, StringBuilder buffer, Consumer<String> onLine) {
        Thread thread = Thread.ofVirtual().start(() -> {
            try {
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append('\n');
                    if (onLine != null) {
                        String l = line;
                        Platform.runLater(() -> onLine.accept(l));
                    }
                }
            } catch (Exception e) {
                logger.debug("Stream read interrupted", e);
            }
        });
        return thread;
    }

    /**
     * Result of a process execution.
     */
    public record ProcessResult(int exitCode, String stdout, String stderr) {
        public boolean success() {
            return exitCode == 0;
        }
    }
}
