package systems.plurality.foc.minemachine.machinery;

import org.graalvm.polyglot.Context;
import org.graalvm.polyglot.Engine;
import org.graalvm.polyglot.Source;
import org.graalvm.polyglot.Value;
import org.graalvm.polyglot.io.ByteSequence;
import systems.plurality.foc.minemachine.machinery.exceptions.MachineException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Machine {
    public Engine engine;
    public Context context;

    public Machine() {
        Engine.Builder engineBuilder = Engine.newBuilder("wasm");
        engine = engineBuilder.build();

        Context.Builder contextBuilder = Context.newBuilder("wasm")
                .engine(engine);
        context = contextBuilder.build();
    }

    public void executeWasm(String programName) {
        Path program;
        try {
            program = GlobalConfig.LookupProgram(programName);
        } catch (MachineException e) {
            GlobalConfig.LOGGER.error("Can't read the program!", e);
            return;
        }

        byte[] programBytes;
        try {
            programBytes = Files.readAllBytes(program);
        } catch (IOException e) {
            GlobalConfig.LOGGER.error("Could not read the WASM file!", e);
            return;
        }

        Source.Builder sourceBuilder = Source.newBuilder("wasm", ByteSequence.create(programBytes), programName);

        Source source;
        try {
            source = sourceBuilder.build();
        } catch (IOException e) {
            GlobalConfig.LOGGER.error(e.getMessage(), e);
            return;
        }

        context.eval(source);

        Value mainFunction = context.getBindings("wasm").getMember("main").getMember("_start");
        mainFunction.execute();

        context.close();
    }
}