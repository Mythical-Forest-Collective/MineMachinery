package systems.plurality.foc.minemachine.machinery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import systems.plurality.foc.minemachine.machinery.exceptions.MachineException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class GlobalConfig {
    public static Logger LOGGER = LoggerFactory.getLogger("MineMachine");
    public static ArrayList<Path> PROGRAM_LOOKUP_DIRS = new ArrayList<>();

    public static Path LookupProgram(String programName) throws MachineException {
        for (Path programDir : PROGRAM_LOOKUP_DIRS) {
            try {
                List<Path> programs = Files.list(programDir).toList();
                Path program = Paths.get(programDir.toString(), programName);

                if (Files.exists(program)) {
                    return program;
                }
            } catch (IOException e) {
                LOGGER.error("Couldn't open the `"+programDir.toString()+"` folder!", e);
            }
        }
        throw new MachineException(MachineErrors.ProgramNotFound, "Could not find the program!");
    }
}
