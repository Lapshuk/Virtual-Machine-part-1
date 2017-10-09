
package cs220_Roman_Lapshuk;

/* Chapter No. 7 – Project No.7 

 File Name: VM

 Programmer: Roman Lapshuk 

 Date Last Modified: Wednesday, March 11, 2015

 Problem Statement: Make the VM .hack to .asm 
  

 Overall Plan : 

1. Draw diagram of needed classes
2. Decided what methods are needed for each class
3. Implement the methods needed for parsing the command line
4. In Code Writer implement push and pop for each memory segment
5. In Code Writer implement arithmetic logic
6. In driver class implement the static method that would translate 1 file
        and output it in the same location as the source file
7. In main method get the user input file or directory
8. If a single file inputed translate it. If directory inputed keep looping
        until all files with .vm extension get translated and outputed
        to the given directory.
9. Implement error checking.


Imported classes needed and Purpose: 
                                    import java.io.File;
                                    import java.io.FileNotFoundException;
                                    import java.io.IOException;
                                    import java.util.Scanner;
                                    import javax.swing.JOptionPane;
                                    import java.io.FileNotFoundException;
                                    import java.io.FileOutputStream;
                                    import java.io.IOException;
                                    import java.io.PrintWriter;
Imported classes are used for a file input/output, exception handeling, simple gui.

*/
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.swing.JOptionPane;
import cs220_Roman_Lapshuk.Parser.Command;


public class VirtualMachinePart1 {

    
    public static void main(String[] args) {

        String fileOrFolderPath = "";
        try {
     
            fileOrFolderPath = JOptionPane.showInputDialog("Please specify the "
                    + "path to a folder that contains \"Sample.vm\" files \n"
                    + "or a path to specific *.vm file. Note: "
                    + "absolute path is required \"/.../sample.vm\".").trim();
            
            File path = new File(fileOrFolderPath);
            String fileExt = "";
            //check if it is the path to directory of specific file
            if(path.isDirectory()){
                //if path to directory store each file name in array
                File[] listOfFiles = path.listFiles();

                //iterate through the array check for file extension
                for (File file : listOfFiles) {
                    if (file.isFile()) {
                        fileExt = file.getName();
                        fileExt = fileExt.substring(
                                fileExt.lastIndexOf(".")+1,fileExt.length());
                        if(fileExt.equals("vm")){//if extension is .vm - translate
                            vmTranslator(file);
                        }    
                    }
                }
            //if the path is not to a dir but to file    
            } else {
                //getting the file name
                fileExt = path.getName();
                //getting the file extension 
                fileExt = fileExt.substring(
                                fileExt.lastIndexOf(".")+1,fileExt.length());
                if(fileExt.equals("vm")){
                    vmTranslator(path);
                } else {
                    JOptionPane.showMessageDialog(null, "The file specified"
                            + " is not *.vm type.");
                    System.exit(0);
                }
            }
            
        } catch (IOException ex) {
            System.err.println("IO Exception: Input File Not Found");
            System.exit(0);
        }catch (NullPointerException ex) {
            System.err.println("Null Pointer Exception: Probably you did not "
                    + "input the path to a file or folder");
            System.exit(0);
        } catch (Exception e){
            System.err.println("Exception Thrown. Please check the input file "
                    + "or directory");
            System.exit(0);
        }
        JOptionPane.showMessageDialog(null, "TRANSLATION COMPLETED\n"
                + "*.asm is in a source folder.");
            
    }//end of main.
    
    /*
     * Precondition:  User provided the correct path to a file or folder that 
     *                contains .vm files
     * Postcondition: The .vm file or files were translated to .asm and outputed
     *                in the same directory as the source file.
     */
    public static void vmTranslator(File fileToTranslate) throws 
            FileNotFoundException, IOException{
        
        Parser parser = null;
        CodeWriter codeWriter = null;
        String fileNameNoExt = "";
        String filePath = "";
        //getting the file name with no extenssion 
        fileNameNoExt = fileToTranslate.getName();
        //getting the path to a file
        filePath = fileToTranslate.getCanonicalPath(); 

        //Compatibility with Windows and Mac or Linux (was not tested on windows)
        if(filePath.contains("/")){
            //getting the File Name on Linux or Mac
            fileNameNoExt = 
                    fileNameNoExt.substring(fileNameNoExt.lastIndexOf('/')+1,
                            fileNameNoExt.lastIndexOf('.'));
            filePath = filePath.substring(0, filePath.lastIndexOf("/")+1);
            
        } else if (filePath.contains("\\")){
            //getting the File Name on Windows
            fileNameNoExt = fileNameNoExt.substring(
                    fileNameNoExt.lastIndexOf("\\")+1,fileNameNoExt.lastIndexOf('.'));
        }
        //calling the code writer method with the 
        //arguments(full path+filename with no extension because the extension .asm 
        //will be added in the constructor of Code Writer, fileName with no extension)     
        codeWriter = new CodeWriter(filePath+fileNameNoExt,fileNameNoExt);
        parser = new Parser(new Scanner(fileToTranslate));
        
        //while there is more lines to parse keep looping
        while(parser.hasMoreCommands()){
            parser.cleanLine();
            parser.parse();
            
            if(parser.commandType() == Command.C_POP){           
                codeWriter.writePop(parser.getSegment(), parser.getArg2());
            } else if(parser.commandType() == Command.C_PUSH){                
                codeWriter.writePush(parser.getSegment(), parser.getArg2());
            } else if(parser.commandType() == Command.C_ARITHMETIC){
                codeWriter.writeArithmetic(parser.getCommand());
                
            }

        }
        codeWriter.closeOutputFile();
        
    }
    
}//end of Program
