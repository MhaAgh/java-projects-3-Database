/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cpit305project;

import java.io.*;
import java.net.*;
import java.sql.*;
import java.util.*;
import java.util.concurrent.locks.*;

public class DBthread extends Thread {

    Socket client;
    boolean exit = true;
    String name;
    String fileName;
    private ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private Lock writeLock = rwl.writeLock();
    static String drv = "com.mysql.jdbc.Driver";
    File file = null;
    FileWriter fw = null;
    BufferedWriter bw = null;
    ResultSet queryResults = null;

    public DBthread(Socket client, int i) {
        this.client = client;
        name = "Client" + "(" + i + ")";
        fileName = "result" + "(" + i + ").txt";
    }

    public void writeURslt(PrintWriter Eout, String rslt) {
        try {
            file = new File(fileName);
            fw = new FileWriter(file, true);
            bw = new BufferedWriter(fw);
            bw.write("\n------------------------------------------\n\n");
            bw.write(rslt);
            bw.close();
            fw.close();
            Eout.println("The result of your request has been written into the \"" + fileName + "\" file. ");
            System.out.println("The result of the user request has been written into the \"" + fileName + "\" file. ");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            System.out.println(name + " connect via: " + client.getInetAddress().getHostAddress());
            writeLock.lock();
            Scanner Ein = new Scanner(client.getInputStream()); // ناخذ منه ركويست الكلاينت
            PrintWriter Eout = new PrintWriter(client.getOutputStream(), true); // نرسل فيه رسايل للكلاينت اذا احتجنا 

            while (true) {
                String str = Ein.nextLine(); // ياخذ الريكويست من الكلاينت 
                System.out.println(name + " requests: " + str); // يطبعه
                if (str.trim().equalsIgnoreCase("0")) {
                    client.close();
                    System.out.println("Server waiting Connection...");
                    break;
                }
/////////////////////////////////////وهنا امر التنفيذ ينكتب////////////////////////////////////////////////

                try {
                    Class.forName(drv).newInstance();
// create a Connection object
                    Connection conn;
                    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db1", "root", "");
                    //--------------------------------------------------------------------------------------------------//
                    if (str.trim().equalsIgnoreCase("1")) {
                        Eout.println("1- SELECT * FROM db1.Course. 2- SELECT num FROM db1.Course. 3- SELECT num, name FROM db1.Course. 0- Exit");
                        String str2 = Ein.nextLine();
                        //// SELECT
                        String s1 = "SELECT * FROM db1.Course";
                        String s2 = "SELECT num FROM db1.Course";
                        String s3 = "SELECT num, name FROM db1.Course";
                        // String s4 = "SELECT name, mark FROM db1.Course";

                        // create a Statement object
                        Statement stmt;
                        stmt = conn.createStatement();

                        if (str2.equalsIgnoreCase("1")) {
// use the Statement object to execute a SQL command statement
                            queryResults = stmt.executeQuery(s1);
//write the result of the request. 
                            try {
                                file = new File(fileName);
                                fw = new FileWriter(file, true);

                                bw = new BufferedWriter(fw);
                                bw.write("\n------------------------------------------\n\n");
                                while (queryResults.next()) {
                                    bw.write("Course num: " + queryResults.getString(1) + ", Course name: " + queryResults.getString(2) + ", Course mark: " + queryResults.getInt(3) + ".\n");
                                }
                                bw.close();
                                Eout.println("The result of your request has been written into the \"" + fileName + "\" file. ");
                                System.out.println("The result of the user request has been written into the \"" + fileName + "\" file. ");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            bw.close();
                            queryResults.close();
                        }// select *
                        else if (str2.equalsIgnoreCase("2")) {
// use the Statement object to execute a SQL command statement
                            queryResults = stmt.executeQuery(s2);
//write the result of the request.  
                            try {
                                file = new File(fileName);
                                fw = new FileWriter(file, true);

                                bw = new BufferedWriter(fw);
                                bw.write("\n------------------------------------------\n\n");
                                while (queryResults.next()) {
                                    bw.write("Course num: " + queryResults.getString(1) + ".\n");
                                }
                                bw.close();
                                Eout.println("The result of your request has been written into the \"" + fileName + "\" file. ");
                                System.out.println("The result of the user request has been written into the \"" + fileName + "\" file. ");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            bw.close();
                            queryResults.close();
                        }// select num
                        else if (str2.equalsIgnoreCase("3")) {
// use the Statement object to execute a SQL command statement
                            queryResults = stmt.executeQuery(s3);
//write the result of the request.  
                            try {
                                file = new File(fileName);
                                fw = new FileWriter(file, true);

                                bw = new BufferedWriter(fw);
                                bw.write("\n------------------------------------------\n\n");
                                while (queryResults.next()) {
                                    bw.write("Course num: " + queryResults.getString(1) + ", Course name: " + queryResults.getString(2) + ".\n");
                                }
                                bw.close();
                                Eout.println("The result of your request has been written into the \"" + fileName + "\" file. ");
                                System.out.println("The result of the user request has been written into the \"" + fileName + "\" file. ");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            bw.close();
                            queryResults.close();
                        }// select name
                        else if (str2.equalsIgnoreCase("0")) {
                            client.close();
                        } else {
                            Eout.println("Wrong Input!!! Enter your request: ");
                        }
                    } else if (str.equalsIgnoreCase("2")) {
                        //// UPDATE
                        Eout.println("1- UPDATE db1.Course.num || 2- UPDATE db1.Course.name || 3- UPDATE db1.Course.mark ||  0- Exit");
                        String str2 = Ein.nextLine();

                        String s1 = "UPDATE Course SET num = ? " + "WHERE num = ?";
                        String s2 = "UPDATE Course SET name = ? " + "WHERE num = ?";
                        String s3 = "UPDATE Course SET mark = ? " + "WHERE num = ?";

                        // create a preparedStatement object
                        PreparedStatement stmt1 = conn.prepareStatement(s1);
                        PreparedStatement stmt2 = conn.prepareStatement(s2);
                        PreparedStatement stmt3 = conn.prepareStatement(s3);

                        if (str2.equalsIgnoreCase("1")) {
                            Eout.println("Enter the course old num: ");
                            String Onum = Ein.nextLine();
                            Eout.println("Enter the new num: ");
                            String Nnum = Ein.nextLine();
                            stmt1.setString(1, Nnum);
                            stmt1.setString(2, Onum);

                            int rc = stmt1.executeUpdate();
                            String rslt = "Query OK, " + rc + " row affected.\n";
                            writeURslt(Eout, rslt);
// all done, so close the preparedStatement and Connection objects
                            stmt1.close();
                            conn.close();
                        }// UPDATE num
                        else if (str2.equalsIgnoreCase("2")) {
                            Eout.println("Enter the course num: ");
                            String num = Ein.nextLine();
                            Eout.println("Enter the new name: ");
                            String Nname = Ein.nextLine();
                            stmt2.setString(1, Nname);
                            stmt2.setString(2, num);

                            int rc = stmt2.executeUpdate();
                            String rslt = "Query OK, " + rc + " row affected.\n";
                            writeURslt(Eout, rslt);
// all done, so close the preparedStatement and Connection objects
                            stmt2.close();
                            conn.close();
                        }// UPDATE name
                        else if (str2.equalsIgnoreCase("3")) {
                            Eout.println("Enter the course num: ");
                            String num = Ein.nextLine();
                            Eout.println("Enter the new mark: ");
                            String Nmark = Ein.nextLine();
                            stmt3.setInt(1, Integer.parseInt(Nmark));
                            stmt3.setString(2, num);

                            int rc = stmt3.executeUpdate();
                            String rslt = "Query OK, " + rc + " row affected.\n";
                            writeURslt(Eout, rslt);
// all done, so close the preparedStatement and Connection objects
                            stmt3.close();
                            conn.close();
                        } else if (str2.equalsIgnoreCase("0")) {
                            client.close();
                        } else {
                            Eout.println("Wrong Input!!! Enter your request: ");
                        }// UPDATE mark
                    } else if (str.equalsIgnoreCase("3")) {
                        Statement stmt = null;
                        String s = "SELECT * FROM Course";
                        stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                        ResultSet rs = stmt.executeQuery(s);
                        Eout.println("Enter the new course num: ");
                        String num = Ein.nextLine();
                        Eout.println("Enter the new course name: ");
                        String name = Ein.nextLine();
                        Eout.println("Enter the new course mark: ");
                        String mark = Ein.nextLine();
                        //Check the result set is an updatable result set
                        int concurrency = rs.getConcurrency();
                        if (concurrency == ResultSet.CONCUR_UPDATABLE) {
//move the cursor to a special position, called the insert row.
                            rs.moveToInsertRow();
//Make the required update to the row fields 
                            rs.updateString(1, num);
                            rs.updateString(2, name);
                            rs.updateInt(3, Integer.parseInt(mark));
//deliver the new row to the database. 
                            rs.insertRow();
// move the cursor back to the position before the call to moveToInsertRow.
                            rs.moveToCurrentRow();
//Check the insert is done
                            String rslt = "Query OK, 1 row affected.\n";
                            writeURslt(Eout, rslt);
                        } else {
                            String rslt = "Query OK, 0 row affected.\n";
                            writeURslt(Eout, rslt);
                        }
// all done, so close the preparedStatement and Connection objects
                        stmt.close();
                        rs.close();
                        conn.close();
                    } // Insert
                    else if (str.equalsIgnoreCase("4")) {
                        //// DELETE
                        String delete = "DELETE FROM Course WHERE num = ?";
                        // create a preparedStatement object
                        PreparedStatement stmt = conn.prepareStatement(delete);
                        Eout.println("Enter the course num: ");
                        String num = Ein.nextLine();

                        stmt.setString(1, num);

                        int rc = stmt.executeUpdate();
                        String rslt = "Query OK, " + rc + " row affected.\n";
                        writeURslt(Eout, rslt);
// all done, so close the preparedStatement and Connection objects
                        stmt.close();
                        conn.close();
                    } // delete
                    else if (str.equalsIgnoreCase("5")) {

                        CallableStatement callable = conn.prepareCall("{ call BONUS(?,?) }");
                        Eout.println("Enter the course num: ");
                        String num = Ein.nextLine();
                        Eout.println("Enter the BONUS: ");
                        String bonus = Ein.nextLine();

                        callable.setInt(1, Integer.parseInt(bonus));
                        callable.setString(2, num);
                        int rc = callable.executeUpdate();

                        String rslt = "Query OK, " + rc + " row affected.\n";
                        writeURslt(Eout, rslt);
                        callable.close();
                        conn.close();

                    } //callable
                    else if (str.equalsIgnoreCase("6")) {
// GetMetaData
                        String s = "SELECT * FROM Course";
                        PreparedStatement stmt = conn.prepareStatement(s);

                        ResultSet rs = stmt.executeQuery();
                        rs = stmt.getResultSet();
                        ResultSetMetaData rsmd = rs.getMetaData();

                        String tableName = "";
                        int columnCount = rsmd.getColumnCount();

                        for (int i = 1; i < 2; i++) {
                            tableName = rsmd.getTableName(1);
                        }

                        String rslt = "The table name is: " + tableName + ".\n" + "The number of columns in the table is: " + columnCount + ".\n\n";
                        writeURslt(Eout, rslt);

                        stmt.close();
                        rs.close();
                        conn.close();

                    } else if (str.equalsIgnoreCase("0")) {
                        client.close();
                    } else {
                        Eout.println("Wrong Input!!! Enter your request: ");
                    }

                } catch (ClassNotFoundException ex) {
                    System.err.println(ex.getStackTrace());
                } catch (SQLException sqle) {
                    System.err.println("JDBC Error: " + sqle.getStackTrace() + sqle.getMessage());
                } catch (Exception e) {
                    System.err.println("Error: " + e.getStackTrace() + " " + e.getMessage());
                }
            }
            writeLock.unlock();
            client.close();
        } catch (UnknownHostException e) {
            System.err.println("Host not found");
        } catch (java.net.ConnectException e) {
            System.err.println("There are no connection at this port");
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Exception exp) {
            System.err.println(exp.getMessage());
        }
    }

}
