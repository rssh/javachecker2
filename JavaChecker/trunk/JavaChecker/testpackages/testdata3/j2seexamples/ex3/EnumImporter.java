package j2seexamples.ex3;

/*
License for Java 1.5 'Tiger': A Developer's Notebook
     (O'Reilly) example package

Java 1.5 'Tiger': A Developer's Notebook (O'Reilly) 
by Brett McLaughlin and David Flanagan.
ISBN: 0-596-00738-8

You can use the examples and the source code any way you want, but
please include a reference to where it comes from if you use it in
your own products or services. Also note that this software is
provided by the author "as is", with no expressed or implied warranties. 
In no event shall the author be liable for any direct or indirect
damages arising in any way out of the use of this software.
*/


import static java.lang.System.out;
import static j2seexamples.ex3.Grade.*;

import java.io.IOException;
import java.io.PrintStream;


public class EnumImporter {

  private Student[] students = new Student[4];

  public EnumImporter() {
    students[0] = new Student("Brett", "McLaughlin");
    students[0].assignGrade(A);

    students[1] = new Student("Leigh", "McLaughlin");
    students[1].assignGrade(B);

    students[2] = new Student("Dean", "McLaughlin");
    students[2].assignGrade(C);

    students[3] = new Student("Robbie", "McLaughlin");
    students[3].assignGrade(INCOMPLETE);
  }

  public void printGrades(PrintStream out) throws IOException {
    for (Student student : students) {
      if ((student.getGrade() == INCOMPLETE) || 
          (student.getGrade() == D)) {
        // Make this student retake this class
      }
      out.println(student.getFullName()+","+student.getGrade());

    }
  }

  public static void main(String[] args) {
    try {
      EnumImporter importer = new EnumImporter();

      importer.printGrades(out);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}



class Student {

  private String firstName;
  private String lastName;
  private Grade grade;

  public Student(String firstName, String lastName) {
    this.firstName = firstName;
    this.lastName = lastName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getFullName() {
    return new StringBuffer(firstName)
           .append(" ")
           .append(lastName)
           .toString();
  }

  public void assignGrade(Grade grade) {
    this.grade = grade;
  }

  public Grade getGrade() {
    return grade;
  }
}

