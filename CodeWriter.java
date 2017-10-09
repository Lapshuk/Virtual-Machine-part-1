
package cs220_Roman_Lapshuk;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;


//The class that writes the code to the .asm file
public class CodeWriter {
    PrintWriter output;
    private int eq,gt,lt;
    String fileNameNoExt;
    String pointsTo;
    
    //constructor throws the exception
    //no need to handel it here, for if there is a problem with input file
    //opperations in multiple classes will not work
    //so it is better to catch the IO exception in one location.
    //if the exception was not thrown in main() it will not get thrown in here.
    CodeWriter(String fileName,String fileNameNoExt) 
            throws IOException, FileNotFoundException{

        this.fileNameNoExt = fileNameNoExt;
        output = new PrintWriter(new PrintWriter(new FileOutputStream(fileName+".asm")));

    }
    /*
     * Precondition: There a line contains the C_Arithmetic command
     * Postcondition: The requested command was outputed to file
     */
    public void writeArithmetic(String command){
        switch(command){
            case "add":
                writeAdd();
                break;
            case "sub":
                writeSub();
                break;
            case "neg":
                writeNeg();
                break;
            case "and":
                writeAnd();
                break;
            case "or":
                writeOr();
                break;  
            case "not":
                writeNot();
                break;
            case "eq":
                writeEqualityLogic(command);
                break;
            case "gt":
                writeEqualityLogic(command);
                break;
            case "lt":
                writeEqualityLogic(command);
                break;    
            default:
                break;
                    
        }
        
    }
    //shortcut to increment SP
    private void incSP(){
        output.println("@SP");
        output.println("M=M+1");
    }
    //shortcut to dencrement SP
    private void decSP(){
        output.println("@SP");
        output.println("M=M-1");
    }
    //shortcut to push D
    private void pushD(){
        output.println("@SP");
        output.println("A=M");
        output.println("M=D");
        output.println("@SP");
        output.println("M=M+1");
    }
    //shortcut to pop D
    private void popD(){
        output.println("@SP");
        output.println("AM=M-1");
        output.println("D=M");
    }
    /*
     * Precondition: The line of text contains the push cmd, one of memory segments,
     *               and a decimal argument.
     * Postcondition: The push command to the corresponding segment was outputed to file
     */
    public void writePush(String segment, String arg){
        if(segment.equals("constant")){
            
            output.println("//push constant "+arg);
            output.println("@"+arg);
            output.println("D=A");
            output.println("@SP");
            output.println("AM=M+1");
            output.println("A=A-1");
            output.println("M=D");
    
        } else if(segment.equals("static")){
           
            output.println("//push static "+arg);
            output.println("@"+fileNameNoExt+"."+arg);
            output.println("D=M");
            pushD();
            
        } else if(segment.equals("pointer")){          
            	
            if(arg.equals("0")){
                pointsTo = "THIS";
            } else if(arg.equals("1")){
                pointsTo = "THAT";
            }    
            output.println("//push pointer "+arg);
            output.println("@"+pointsTo);
            output.println("D=M");
            pushD();
                   
        } else if(segment.equals("this")){
            
            output.println("//push this "+arg);
            output.println("@THIS");
            output.println("D=M");
            output.println("@"+arg);
            output.println("D=D+A");
            output.println("A=D");
            output.println("D=M");
            pushD();
               
        } else if(segment.equals("that")){
            output.println("//push that "+arg);
            output.println("@THAT");
            output.println("D=M");
            output.println("@"+arg);
            output.println("D=D+A");
            output.println("A=D");
            output.println("D=M");
            output.println("@SP");
            output.println("A=M");
            output.println("M=D");
            incSP();
                         
        } else if (segment.equals("local")){
            
            output.println("//push local "+arg);
            output.println("@"+arg);
            output.println("D=A");
            output.println("@LCL");
            output.println("A=M");
            output.println("D=D+A");
            output.println("A=D");
            output.println("D=M");
            pushD();
   
        } else if (segment.equals("argument")){
            
            output.println("//push arugment "+arg);
            output.println("@"+arg);
            output.println("D=A");
            output.println("@ARG");
            output.println("A=M");
            output.println("D=D+A");
            output.println("A=D");
            output.println("D=M");
            pushD();
            
        }
    }
    /*
     * Precondition: The line of text contains the pop command and one of 
     *               memory Segments and the argument.
     * Postcondition: The pop cmd with corresponding segment was written to file
     */
    public void writePop(String segment, String arg){
        if(segment.equals("static")){
            
            output.println("//pop static "+arg);
            popD();
            output.println("@"+fileNameNoExt+"."+arg);
            output.println("M=D");   
            
        } else if(segment.equals("local")){
            
            output.println("//pop local "+arg);
            
            output.println("@LCL");
            output.println("D=M");
            output.println("@"+arg);
            output.println("D=D+A");
            output.println("@R15");
            output.println("M=D");
            popD();
            output.println("@R15");
            output.println("A=M");
            output.println("M=D");
            
        } else if (segment.equals("pointer")){
            if(arg.equals("0")){
                pointsTo = "THIS";
            } else if(arg.equals("1")){
                pointsTo = "THAT";
            }    
            output.println("//pop pointer "+arg);
            popD();
            output.println("@"+pointsTo);
            output.println("M=D");           
            
        } else if (segment.equals("this")){
            
            output.println("//pop this "+arg);
            output.println("@"+arg);
            output.println("D=A");
            output.println("@THIS");
            output.println("A=M");
            output.println("D=D+A");
            output.println("@THIS");
            output.println("M=D");
            popD();
            output.println("@THIS");
            output.println("A=M");
            output.println("M=D");
            output.println("@"+arg);
            output.println("D=A");
            output.println("@THIS");
            output.println("A=M");
            output.println("D=A-D");
            output.println("@THIS");
            output.println("M=D");

        } else if(segment.equals("that")){
            
            output.println("//pop that "+arg);
            output.println("@"+arg);
            output.println("D=A");
            output.println("@THAT");
            output.println("A=M");
            output.println("D=D+A");
            output.println("@THAT");
            output.println("M=D");
            popD();
            output.println("@THAT");
            output.println("A=M");
            output.println("M=D");
            output.println("@"+arg);
            output.println("D=A");
            output.println("@THAT");
            output.println("A=M");
            output.println("D=A-D");
            output.println("@THAT");
            output.println("M=D");
    
        } else if (segment.equals("argument")){
            
            output.println("//pop argument "+arg);
            output.println("@"+arg);
            output.println("D=A");
            output.println("@ARG");
            output.println("A=M");
            output.println("D=D+A");
            output.println("@ARG");
            output.println("M=D");
            popD();
            output.println("@ARG");
            output.println("A=M");
            output.println("M=D");
            output.println("@"+arg);
            output.println("D=A");
            output.println("@ARG");
            output.println("A=M");
            output.println("D=A-D");
            output.println("@ARG");
            output.println("M=D");
            
        }
    }
    /*
     * Precondition: The line of text contains Arithmetic cmd
     * Postcondition: Add command was written to file
     */
    private void writeAdd() {

        output.println("// command = add");
        popD();
        output.println("A=A-1");
        output.println("M=M+D");
        
    }
    /*
     * Precondition: The line of text contains Arithmetic cmd
     * Postcondition: Sub command was written to file
     */
    private void writeSub() {
        
        output.println("// command = sub");
        popD();
        output.println("A=A-1");
        output.println("M=M-D"); 
        
    }
    
    /*
     * Precondition: The line of text contains Arithmetic cmd
     * Postcondition: Not command was written to file
     */
    private void writeNot() {
        
        output.println("// command = not");
        output.println("@SP");
        output.println("A=M-1");
        output.println("M=!M");
        
    }
    /*
     * Precondition: The line of text contains Arithmetic cmd
     * Postcondition: Neg command was written to file
     */
    private void writeNeg() {
    
        output.println("// command = neg");
        output.println("@SP");
        output.println("A=M-1");
        output.println("M=-M");
        output.println("D=A");
        output.println("@SP");
        output.println("M=D+1");
            
    }
    /*
     * Precondition: The line of text contains Arithmetic cmd
     * Postcondition: And command was written to file
     */
    private void writeAnd(){
        
        output.println("// command = and");
        popD();
        output.println("@SP");
        output.println("AM=M-1");
        output.println("M=M&D");
        output.println("@SP");
        output.println("M=M+1");
        output.println();

    }
    /*
     * Precondition: The line of text contains Arithmetic cmd
     * Postcondition: Or command was written to file
     */
    private void writeOr(){

        output.println("// command = or");
        output.println("@SP");
        output.println("AM=M-1");
        output.println("D=M");
        output.println("@SP");
        output.println("AM=M-1");
        output.println("M=M|D");
        output.println("@SP");
        output.println("M=M+1");
        
    }
    /*
     * Precondition: The line of text contains the Arithmetic that test equality
     * Postcondition: The equality was tested and outputed to .asm file
     */
    private void writeEqualityLogic(String jumpCommand){
        String conditionalJump = null;
        int counter = 0;
        
        switch(jumpCommand){
            case "eq":
                conditionalJump = "JEQ";
                counter = eq;
                eq++;//incrementing the counter of each type of equalitues
                break;
            case "gt":
                conditionalJump = "JGT";
                counter = gt;//setting counter to the counter of current equality check
                gt++;
                break;
            case "lt":
                conditionalJump = "JLT";//converting the .vm cmd to .asm cmd
                counter = lt;
                lt++;
                break;
            default:
                break;
        }
        //The logic is the same for any kind of jump but the condition changes
        output.println("// condtition check = " + jumpCommand);
        decSP();
        output.println("@SP");
        output.println("A=M");
        output.println("D=M");
        output.println("@R15");
        output.println("M=D");
        decSP();
        output.println("@SP");
        output.println("A=M");
        output.println("D=M");
        output.println("@R15");
        output.println("D=D-M");
        output.println("@"+conditionalJump+"."+"TRUE."+counter);
        output.println("D;"+conditionalJump);
        output.println("@SP");
        output.println("A=M");
        output.println("D=0");
        output.println("@"+conditionalJump+"."+"END."+counter);
        output.println("0;JMP");
        output.println("("+conditionalJump+"."+"TRUE."+counter+")");
        output.println("@SP");
        output.println("A=M");
        output.println("D=-1");
        output.println("("+conditionalJump+"."+"END."+counter+")");
        output.println("@SP");
        output.println("A=M");
        output.println("M=D");
        incSP();

    }
    
    public void closeOutputFile(){
        //closing comments to end the program
        output.println("(INFINITE_LOOP)");
        output.println("@INFINITE_LOOP");
        output.println("0;JEQ");
        output.close();
        
    }
    
}//end of CodeWriter
