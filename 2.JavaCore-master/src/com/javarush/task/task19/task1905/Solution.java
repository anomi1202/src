package com.javarush.task.task19.task1905;

import java.util.HashMap;
import java.util.Map;

/* 
Закрепляем адаптер
*/

public class Solution {
    public static Map<String,String> countries = new HashMap<String,String>();
    static{
        countries.put("UA", "Ukraine");
        countries.put("RU", "Russia");
        countries.put("CA", "Canada");
    }

    public static void main(String[] args) {
        Customer customer = new Customer() {
            @Override
            public String getCompanyName() {
                return "JavaRush Ltd.";
            }

            @Override
            public String getCountryName() {
                return "Russia";
            }
        };
        Contact contact = new Contact() {
            @Override
            public String getName() {
                return "Ivanov, Ivan";
            }

            @Override
            public String getPhoneNumber() {
                return "+38(050)123-45-67";
            }
        };

        DataAdapter ri = new DataAdapter(customer, contact);

        System.out.println(ri.getCountryCode());
        System.out.println(ri.getCompany());
        System.out.println(ri.getContactFirstName());
        System.out.println(ri.getContactLastName());
        System.out.println(ri.getDialString());
    }

    public static class DataAdapter implements RowItem {
        private Customer customer;
        private Contact contact;

        public DataAdapter(Customer customer, Contact contact) {
            this.customer = customer;
            this.contact = contact;
        }

        @Override
        public String getCountryCode() {
            //example UA
            String countryCode = "";
            for (Map.Entry<String, String> pair : countries.entrySet()){
                if (pair.getValue().equals(customer.getCountryName())){
                    countryCode = pair.getKey();
                    break;
                }
            }
            return countryCode;
        }

        @Override
        public String getCompany() {
            //example JavaRush Ltd.
            return customer.getCompanyName();
        }

        @Override
        public String getContactFirstName() {
            //example Ivan
            return contact.getName().split(", ")[1];
        }

        @Override
        public String getContactLastName() {
            //example Ivanov
            return contact.getName().split(", ")[0];
        }

        @Override
        public String getDialString() {
            //example callto://+380501234567
            String dialString = "callto://";
            String[] phoneArray = contact.getPhoneNumber().split("-");
            String temp = phoneArray[0]; // temp = 38(050)123
            temp = temp.substring(0,temp.indexOf("(")) + temp.substring(temp.indexOf("(")+1,temp.indexOf(")")) + temp.substring(temp.indexOf(")")+1);
            dialString += temp + phoneArray[1] + phoneArray[2];

            return dialString;
        }
    }

    public static interface RowItem {
        String getCountryCode();        //example UA
        String getCompany();            //example JavaRush Ltd.
        String getContactFirstName();   //example Ivan
        String getContactLastName();    //example Ivanov
        String getDialString();         //example callto://+380501234567
    }

    public static interface Customer {
        String getCompanyName();        //example JavaRush Ltd.
        String getCountryName();        //example Ukraine
    }

    public static interface Contact {
        String getName();               //example Ivanov, Ivan
        String getPhoneNumber();        //example +38(050)123-45-67
    }
}