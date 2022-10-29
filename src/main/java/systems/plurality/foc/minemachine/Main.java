package systems.plurality.foc.minemachine;

import systems.plurality.foc.minemachine.machinery.GlobalConfig;
import systems.plurality.foc.minemachine.machinery.Machine;
import systems.plurality.foc.minemachine.machinery.exceptions.MachineException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class Main {
    public static void main(String[] args) {
        Path programs = Paths.get("programs").toAbsolutePath();
        try {
            Files.createDirectories(programs);
        } catch (IOException e) {
            GlobalConfig.LOGGER.error("Couldn't create the programs dir!", e);
        }

        String[] programList = new String[0];

        InputStream is = Main.class.getClassLoader().getResourceAsStream("programs/list.txt");
        try {
            programList = new String(is.readAllBytes(), StandardCharsets.UTF_8).split("\n");
            is.close();
        } catch (IOException e) {
            GlobalConfig.LOGGER.error("There was an error while attempting to read the program index!", e);
        }

        for (String program : programList) {
            copyResourceToFolder("programs/"+program, Paths.get(programs.toString(), program));
        }

        GlobalConfig.PROGRAM_LOOKUP_DIRS.add(programs.toAbsolutePath());

        Machine m = new Machine();
        m.executeWasm(args[0]);
    }

    public static void copyResourceToFolder(String resourceName, Path outputPath) {
        try {
            InputStream is = Main.class.getClassLoader().getResourceAsStream(resourceName);
            Files.copy(is, outputPath, StandardCopyOption.REPLACE_EXISTING);
            is.close();
        } catch (IOException e) {
            GlobalConfig.LOGGER.error("There was an error while attempting to copy the resource!", e);
        }
    }
}
