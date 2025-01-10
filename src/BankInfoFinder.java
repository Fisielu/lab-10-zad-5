import java.io.*;
import java.net.URL;
import java.util.Scanner;

public class BankInfoFinder {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        System.out.print("Podaj trzy pierwsze cyfry numeru konta: ");
        String bankCode = scanner.nextLine();

        if (!bankCode.matches("\\d{3}")) {
            System.out.println("Błędny format. Wprowadź dokładnie trzy cyfry.");
            return;
        }


        String fileUrl = "https://ewib.nbp.pl/plewibnra?dokNazwa=plewibnra.txt";
        String tempFileName = "bank_data.txt";

        try {

            downloadFile(fileUrl, tempFileName);

            findBankInfo(tempFileName, bankCode);

            new File(tempFileName).delete();
        } catch (IOException e) {
            System.err.println("Wystąpił błąd: " + e.getMessage());
        }
    }

    private static void downloadFile(String fileUrl, String outputFileName) throws IOException {
        try (InputStream in = new URL(fileUrl).openStream();
             FileOutputStream out = new FileOutputStream(outputFileName)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            System.out.println("Plik został pobrany.");
        }
    }

    private static void findBankInfo(String fileName, String bankCode) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(bankCode)) {
                    String[] parts = line.split("\\t");
                    if (parts.length >= 2) {
                        String shortBankNumber = parts[0];
                        String bankName = parts[1];
                        System.out.println("Numer banku: " + shortBankNumber);
                        System.out.println("Nazwa banku: " + bankName);
                        return;
                    }
                }
            }
            System.out.println("Nie znaleziono informacji o banku dla kodu: " + bankCode);
        }
    }
}
