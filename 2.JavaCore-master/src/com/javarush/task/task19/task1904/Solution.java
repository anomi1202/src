package com.javarush.task.task19.task1904;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

/* 
И еще один адаптер
*/

public class Solution {

    public static void main(String[] args) throws IOException, ParseException {
        String fileName = "C:\\Users\\Ru\\Desktop\\JavaRushHomeWork\\JavaRushHomeWork\\src\\com\\javarush\\test\\level19\\lesson03\\task04\\1";
        PersonScannerAdapter psa = new PersonScannerAdapter(new Scanner(new File(fileName)));

        System.out.println(psa.read());
        psa.close();
    }

    public static class PersonScannerAdapter implements PersonScanner {
        private Scanner fileScanner;

        public PersonScannerAdapter (Scanner fileScanner){
            this.fileScanner = fileScanner;
        }

        @Override
        public Person read() throws IOException {
            //В файле хранится большое количество людей, данные одного человека находятся в одной строке.
            //Метод read() должен читать данные одного человека.
            //Данные в файле хранятся в следующем виде: Иванов Иван Иванович 31 12 1950
            Person person = null;

            String line = "";
            if (fileScanner.hasNext())
                line = fileScanner.nextLine();
            String lastName = line.split(" ")[0];
            String firstName = line.split(" ")[1];
            String middleName = line.split(" ")[2];
            String birthDt = line.split(" ")[3] + " " + line.split(" ")[4] + " " + line.split(" ")[5];
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd MM yyyy");
            try {
                Date birthDate = dateFormat.parse(birthDt);
                person = new Person(firstName, middleName, lastName, birthDate);
            }catch (ParseException e) {e.printStackTrace();}
            return person;
        }

        @Override
        public void close() throws IOException {
            fileScanner.close();
        }
    }
}
