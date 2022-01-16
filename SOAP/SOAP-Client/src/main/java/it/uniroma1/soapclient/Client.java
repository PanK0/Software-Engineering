/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.uniroma1.soapclient;

/**
 *
 * @author mecellone-dev
 */
import java.util.List;
import soapwsclient.generated.*;

/**
 *
 * @author studente
 */
public class Client {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        System.out.println("... adding new students ...");
        Student s1 = new Student();
        s1.setName("Andrea");
        System.out.println(".. just added " + Client.helloStudent(s1));
        
        Student s2 = new Student();
        s2.setName("Carla");
        System.out.println(".. just added " + Client.helloStudent(s2));
        
        System.out.println("... and now recovering the whole StudentMap ...");
        List<StudentEntry> result = Client.getStudents().getEntry();
        for (int i = 0 ; i<result.size(); i++){
            System.out.println(((StudentEntry)result.get(i)).getStudent().getName());
    }
        
    }

    private static StudentMap getStudents() {
        WSImplService service = new WSImplService();
        WSInterface port = service.getWSImplPort();
        return port.getStudents();
    }

    private static String helloStudent(Student arg0) {
        WSImplService service = new WSImplService();
        WSInterface port = service.getWSImplPort();
        return port.helloStudent(arg0);
    }
    
}
