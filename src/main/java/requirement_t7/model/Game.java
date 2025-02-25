package requirement_t7.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import requirement_t7.model.util.FileCreator;
import requirement_t7.model.util.FileToStringReader;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

@Service
public class Game {

    private String inputTestClassName = "InputTestClass";
    private String inputClassName = "InputClass";
    private String inputClassCode;
    private String inputTestClassCode;

    @Autowired
    private FileCreator fileCreator;

    private Class<?> clazz;

    public Game(){
        inputClassCode = obtainCode(inputClassName);
        inputTestClassCode = obtainCode(inputTestClassName);
    }

    private String obtainCode(String input){
        String res;
        try {
            res = FileToStringReader.convert(new File(input + ".txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return res;
    }

    public String compile(){
        //Compile input class
        try {
            compileClass();
        } catch (Exception e) {
            //Return String with error if something fails
            return "Error in compiling" + inputClassName + " => " + e.getMessage();
        }
        try {
            compileTest();
        } catch (Exception e) {
            return "Error in compiling" + inputTestClassName + " => " + e.getMessage();
        }
        return "Compiled";
    }

    public String execute(){
        String res;
        if(clazz != null){
            //Run the test
            res = Execution.runTests(clazz);
        }
        else{
            res = "Cannot execute because you have not compiled";
        }
        return res;
    }

    private void compileTest() throws Exception {
        clazz = Compilation.compileClass("requirement_t7.model."+ inputTestClassName,inputTestClassCode);
    }

    private void compileClass() throws Exception {
        Compilation.compileClass("requirement_t7.model." + inputClassName,inputClassCode);
        fileCreator.createFile(inputClassName,inputClassCode);
    }
}
