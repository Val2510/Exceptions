import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class UserDataApplication {
    public static void main(String[] args) {

        try (Scanner scanner = new Scanner(System.in)) {
            String userDataInput = getUserDataInput(scanner);
            processUserData(userDataInput);

            System.out.println("Данные успешно записаны в файл.");
        } catch (Exception e) {
            System.err.println("Ошибка при записи данных в файл: " + e.getMessage());
        }
    }

    private static String getUserDataInput(Scanner scanner) {
        System.out.println("Введите: Фамилия Имя Отчество датарождения номертелефона пол");
        return scanner.nextLine();
    }

    private static void processUserData(String userDataInput) {
        try {
            String[] userDataArray = userDataInput.split(" ");
            if (userDataArray.length != 6) {
                throw new IllegalArgumentException("Неверное количество данных. Должно быть 6.");
            }

            String surname = userDataArray[0];
            String firstName = userDataArray[1];
            String lastName = userDataArray[2];
            String birthDateStr = userDataArray[3];
            String phoneNumberStr = userDataArray[4];
            String gender = userDataArray[5];

            validateNameStrings(surname, firstName, lastName);
            validateDateFormat(birthDateStr);
            long phoneNumber = validatePhoneNumber(phoneNumberStr);
            validateGender(gender);

            String userDataLine = buildUserDataLine(surname, firstName, lastName, birthDateStr, phoneNumber, gender);
            writeToFile(surname, userDataLine);
        } catch (Exception e) {
            System.err.println("Ошибка при записи данных в файл: " + e.getMessage());
        }
    }

    private static String buildUserDataLine(String surname, String firstName, String lastName,
                                            String birthDateStr, long phoneNumber, String gender) {
        return String.format("%s %s %s %s %d %s", surname, firstName, lastName, birthDateStr, phoneNumber, gender);
    }

    private static void validateDateFormat(String dateStr) throws ParseException, IllegalArgumentException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        dateFormat.setLenient(false);

        Date date = dateFormat.parse(dateStr);
        long ageMillis = System.currentTimeMillis() - date.getTime();
        long ageYears = ageMillis / (365L * 24 * 60 * 60 * 1000);

        if (ageYears > 150) {
            throw new IllegalArgumentException("Неверный возраст. Человек не может быть старше 150 лет.");
        }
    }

    private static long validatePhoneNumber(String phoneNumberStr) throws IllegalArgumentException {
        if (!phoneNumberStr.matches("\\d+")) {
            throw new IllegalArgumentException("Номер телефона должен содержать только цифры.");
        }

        long phoneNumber = Long.parseLong(phoneNumberStr);

        if (phoneNumberStr.length() > 13 || phoneNumberStr.length() < 11) {
            throw new IllegalArgumentException("Номер телефона не может содержать не менее 11 и не более 13 цифр.");
        }
        return phoneNumber;
    }

    private static void validateGender(String gender) throws IllegalArgumentException {
        if (!gender.equals("f") && !gender.equals("m")) {
            throw new IllegalArgumentException("Неверный формат пола. Используйте 'f' или 'm'.");
        }
    }
    private static void validateNameStrings(String surname, String firstName, String lastName) throws IllegalArgumentException {
        if (!surname.matches("[a-zA-Zа-яА-Я]+") || !firstName.matches("[a-zA-Zа-яА-Я]+") || !lastName.matches("[a-zA-Zа-яА-Я]+")) {
            throw new IllegalArgumentException("Фамилия, имя и отчество должны содержать только буквы.");
        }
    }

    private static void writeToFile(String fileName, String data) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true))) {
            writer.write(data + System.lineSeparator());
        }
    }
}