package cs220_Roman_Lapshuk;

import java.util.Scanner;

public class Parser {
    private Scanner inputFile;
    private int actualLineNumber;
    private String cleanLine;
    private String arg1 = null;
    private String arg2 = null;
    private Command commandType;
    private String command;
    private String segment;
    private String[] lineParts;
    
    
    public enum Command{
        C_ARITHMETIC,C_PUSH,C_POP,C_LABEL,C_GOTO,C_IF,C_FUNCTION,C_RETURN,C_CALL,
        NONE
    }
    
    
    public Parser(){
        //empty
    }
    //Parser constructor as argument takes the Scanner Class file
    public Parser(Scanner file){
        
        inputFile = file;
    }
    /*
     * Precondition: There a line of text in the file
     * Postcondition: The comments and white spaces was removed from the line
     */
    public void cleanLine(){
        try{
            if(hasMoreCommands()){
                String rawLine = inputFile.nextLine();

                while(rawLine.isEmpty() || rawLine.trim().equals("") 
                        || rawLine.trim().equals("\n")
                        || rawLine.charAt(0) == '/'){

                    rawLine = inputFile.nextLine();
                    actualLineNumber++;//increment line even when the line is comment

                }
                //in case if there is a comments after command take a substring 
                //before comments
                if(rawLine.contains("//")){
                    rawLine = rawLine.substring(0,rawLine.lastIndexOf("//"));
                }
                cleanLine = rawLine.trim();
                lineParts = cleanLine.split(" ");
                actualLineNumber++;//increment any line


                //System.out.println(cleanLine);//testing
            } else {
                System.err.println("Reached the end of the file. Line #"+actualLineNumber);
            }
        }catch(Exception e){
            System.err.println("Exception: Invalid char. Line #"+actualLineNumber);
        }    
    }
    
    /*
     * Precondition: commandType variable has a value.
     * Postcondition: Command Type was returned.
     */
    public Command commandType(){
        return commandType;
    }
    
    /*
     * Precondition: Command Type is not C_Arithmetic.
     * Postcondition: RAM segment was parsed from the command line
     */
    private void parseSegment(){
        try{
            segment = lineParts[1];
        } catch (Exception e) {
            segment = null;
        }
    }
    
    /*
     * Precondition: The line is not blank
     * Postcondition: Command type was parsed from the command line
     */
    private void parseCommandType(){
        
        command = lineParts[0];//command should be the first word on line

        if(command.equals("add")||command.equals("sub")||command.equals("neg")
                ||command.equals("eq")||command.equals("gt")
                ||command.equals("lt")||command.equals("and")
                ||command.equals("or")||command.equals("not")){
            commandType = Command.C_ARITHMETIC;
        } else if (command.equals("push")){
            commandType = Command.C_PUSH;
        } else if (command.equals("pop")){
            commandType = Command.C_POP;
        } else if (command.equals("label")){
            commandType = Command.C_LABEL;
        } else if (command.equals("goto")){
            commandType = Command.C_GOTO;
        } else if (command.equals("if-goto")){
            commandType = Command.C_IF;
        } else if (command.equals("function")){
            commandType = Command.C_FUNCTION;
        } else if (command.equals("call")){
            commandType = Command.C_CALL;
        } else {
            commandType = Command.NONE;
            System.err.println("Ivalid line of text. No matching command");
            System.err.println("Line #"+actualLineNumber);
            System.exit(0);
        }
    }
    
    /*
     * Precondition: Command Type is C_Arithmetic.
     * Postcondition: argument is set to the same value as the command
     */
    private void arg1(){
        if(commandType == Command.C_ARITHMETIC){
            arg1 = command;
        } else {
            arg1 = null;
        }    
    }
    /*
     * Precondition: Command Type is not C_Arithmetic.
     * Postcondition: arg2 was set to the value of the las argument on the line
     */
    private void arg2(){

        if(commandType == Command.C_PUSH || commandType == Command.C_POP ||
                commandType == Command.C_FUNCTION || commandType == Command.C_CALL){

            String lastWord = lineParts[lineParts.length - 1];
            arg2 = lastWord;

        } else {
            arg2 = null;
        }    
    }
    /*
     * Precondition: The argument was parsed successfully
     * Postcondition: First argument returned
     */
    public String getArg1(){
        return arg1;
    }
    /*
     * Precondition: The argument was parsed successfully
     * Postcondition: Second argument returned
     */
    public String getArg2(){
        return arg2;
    }
    /*
     * Precondition: The line of text was succesfully cleaned and command was parsed
     * Postcondition: command (first word on a line) returned
     */
    public String getCommand(){
        return command;
    }
    /*
     * Precondition: The cmd type is not Arithmetic. Memory segment was parsed.
     * Postcondition: Memory segment returned.
     */
    public String getSegment(){
        return segment;
    }
    /*
     * Precondition: File was succesfully opened and has some content.
     * Postcondition: Returns true if file still has lines to read
     */
    public boolean hasMoreCommands(){
        return inputFile.hasNextLine();
    }
    /*
     * Precondition: The line number was incremented with each line
     * Postcondition: Line number returned.
     */
    public int getActualLineNumber(){
        return actualLineNumber;
    }
    /*
     * Precondition: The input file has not reached the end
     * Postcondition: Command was parsed and splited on parts
     */
    public void parse(){
        parseCommandType();
        arg1();
        arg2();
        parseSegment();
    }
    
}//end of Parser
