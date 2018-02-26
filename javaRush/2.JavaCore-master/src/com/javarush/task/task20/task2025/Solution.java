package com.javarush.task.task20.task2025;

import java.util.TreeSet;

/*
Алгоритмы-числа
*/
public class Solution {
    static long[][] num;//Массив цифр в разных степенях
    //Заполнение массива
    static {
        num = new long[19][10];
        num[0][0] = 0L;num[0][1] = 1L;num[0][2] = 2L;
        num[0][3] = 3L;num[0][4] = 4L;num[0][5] = 5L;
        num[0][6] = 6L;num[0][7] = 7L;num[0][8] = 8L;
        num[0][9] = 9L;
        for (int i = 1; i < 19; i++) {
            for (int j = 0; j < 10; j++) {
                num[i][j] = num[i - 1][j] * num[0][j];
            }
        }
    }

    public static long[] getNumbers(long N) {
        long[] result = null;
        long n = 0;
        TreeSet<Long> set = new TreeSet<>();

        while (n < N) {

            if (n % 10 != 9)
                n++;
            else n = filtr(n);
            if (n < N) {
                int nLength = (n + "").length();
                int NLength = (N + "").length();

                long sumPow = getPowSumm(n);
                if (sumPow < N)//проверяю, что summPow все еще меньше чем N. Необходимо при подаче на вход метода Long.MAX_VALUE
                {
                    if (isArmstrongNum(nLength, sumPow, N))
                        set.add(sumPow);

                    long tempN = n;
                    int tempNLength = (tempN + "").length();
                    for (int i = 0; i < NLength - tempNLength; i++) {
                        tempN = tempN * 10;
                        if (tempN < N && tempN > 0)//проверяю, что tempN все еще меньше чем N и не ушла в отриц область.
                        // Необходимо при подаче на вход метода Long.MAX_VALUE
                        {
                            tempNLength = (tempN + "").length();
                            sumPow = getPowSumm(tempN);
                            if (isArmstrongNum(tempNLength, sumPow, N))
                                set.add(sumPow);
                        }

                    }
                }
            }
        }

        result = new long[set.size()];
        if (set.size() > 0) {
            int index = 0;
            for (Long num : set) {
                result[index] = num;
                System.out.println(num);
            }
        }

        return result;
    }

    public static Long getPowSumm(long N){
        long summ = 0;

        char[] arrayN = (N + "").toCharArray();
        int degree = arrayN.length -1 ;

        for (int i = 0; i < arrayN.length; i++) {
            int num = Integer.parseInt(arrayN[i] + "");
            summ += Solution.num[degree][num];
            if (summ > Long.MAX_VALUE || summ < 0) {
                summ = Long.MAX_VALUE;
                break;
            }
        }
        return summ;
    }

    public static Long filtr(long N){
        long newN;
        int Nlength = (N + "").length();

        long tempN = N / 10 + 1L;
        //заменяем все нули на пробелы, потом убираем пробелы в конце
        //заменяеи оставшиеся пробелы на нули (если есть)
        String str = (tempN + "").replace("0", " ").trim().replace(" ","0");
        //проверяем не увеличилась ли разрядность
        if (Nlength < (N + 1 + "").length())
            Nlength++;
        while (str.length() < Nlength)
            str += str.substring(str.length() - 1);

        //пришлось воспользоваться данной конструкцией. по другому не придумал.
        //если str превышает Long.MAX_VALUE, то выскакивает исключение. его обрабатываем
        try {
            newN = Long.parseLong(str);
        }
        catch (NumberFormatException e){
            newN = Long.MAX_VALUE;
        }

        return newN;
    }

    public static boolean isArmstrongNum(int currentNLenght, long sumPow, long maxN){
        boolean isArmstrongNum = false;

        if (sumPow < maxN
                && sumPow == getPowSumm(sumPow)
                && (sumPow + "").length() == currentNLenght){
            isArmstrongNum = true;
        }

        return isArmstrongNum;
    }

    public static void main(String[] args) {
        long timeStart = System.currentTimeMillis();
        getNumbers(Integer.MAX_VALUE);
        long timeEnd = System.currentTimeMillis();
        long totalMemory = Runtime.getRuntime().totalMemory();
        long memoryInEnd = Runtime.getRuntime().freeMemory();

        System.out.println(String.format("\r\nTime of work: %-1.2f, s", (float) (timeEnd - timeStart)/1000L));
        System.out.println(String.format("Memory: %d, Mb",(totalMemory - memoryInEnd)/1024/1024));

    }
}
