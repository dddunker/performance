package com.company;

import java.io.*;

public class FormatDate {

    StringBuffer buffer = new StringBuffer(32);

    public void dateFormatter() {
        BufferedReader reader = null;
        String s = null;
        String line = null;
        try {
            reader = new BufferedReader(new FileReader("/home/binkovskiy/Рабочий стол/LOAD_TESTING/to_aval/to_Aval_5/sorted/opers/op"));
            while ((s = reader.readLine()) != null) {
                String[] str = s.split(",");
                String clnId = str[0];
                String date = str[1];
                String docsCount = str[2];
                String docType = str[3];

                String[] tmpDate = date.split("-");
                String dd = tmpDate[0];
                String mm = tmpDate[1];
                String yyyy = tmpDate[2];

                String resultDate = yyyy + "-" + mm + "-" + dd;
                line = clnId + "," + resultDate + "," + docsCount + "," + docType + "\n";

                buffer.append(line);
            }
            bufferWriter(buffer.toString());
            reader.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ArrayIndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
    }

    public void bufferWriter(String content) {
        File file = new File("/home/binkovskiy/Рабочий стол/LOAD_TESTING/to_aval/to_Aval_5/sorted/opers/out");
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter bw = new BufferedWriter(new FileWriter(file), 2048);
            bw.write(content);

            bw.flush();
            bw.close();

            System.out.println("Done");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
