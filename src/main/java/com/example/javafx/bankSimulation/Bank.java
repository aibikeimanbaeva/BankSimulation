package com.example.javafx.bankSimulation;

import java.io.*;
import java.util.*;

public class Bank implements Loader {
	public String nameCompany;
	public int atmBalance;
	public CardHolder cardHolders;
	public ATM optimaATM;
	Scanner scan = new Scanner(System.in);
	Random random = new Random();
	BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/example/javafx/bankSimulation/user_dataBase.txt"));
	List<String> lines = new ArrayList<>();
	private Map<String, String> registeredUsers = new HashMap<>();
	private BankBills bankBills;

	public Bank(String nameCompany, BankBills bankBills, int atmBalance) throws IOException {
		this.nameCompany = nameCompany;
		this.bankBills = bankBills;
		this.atmBalance = atmBalance;
		this.optimaATM = new ATM(cardHolders, bankBills, atmBalance);
	}



// ...

	public String showCardholders() throws IOException {
	String cardholderInfoList = "";

		BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/example/javafx/bankSimulation/user_dataBase.txt"));
		String line;
		while ((line = reader.readLine()) != null) {
			String[] parts = line.split(" ");
			if (parts.length == 3) {

				String userNameFromFile = parts[0];
				cardholderInfoList += userNameFromFile.toUpperCase() + ", ";
			}
		}
		return cardholderInfoList;
	}


	public boolean checkUserRegister(User user) throws IOException, InterruptedException {
		try (BufferedReader reader = new BufferedReader(new FileReader("src/main/java/com/example/javafx/bankSimulation/user_dataBase.txt"))) {
			String line;
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(" ");
				if (parts.length == 3){

					String userNameFromFile = parts[0];
					String userIdFromFile = parts[1];
					String userSalary = parts[2];
					if (userNameFromFile.equals(user.name)) {
						Loader.Loading();
						return true;
					}
				}
			}
			createNewCardHolder(user);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return false;
	}

	public void createNewCardHolder(User user) throws InterruptedException, IOException {
		boolean hasUser = false;
		while (!hasUser) {
			System.out.println("Вас принимает банк, сейчас мы выдадим вам ваш персональный айди");
			Loader.Loading();
			int randomNum = random.nextInt(100);
			String newID = "000" + randomNum;
			System.out.println("ваш персональный айди " + newID);
			System.out.println("Теперь отсканировали ваши данные и внесли их в базу данных");
			Loader.Loading();
			System.out.println("придумайте пароль для карты");
			int passOfCard = scan.nextInt();
			CardHolder newCardHolder = new CardHolder(user.name, newID, bankBills, passOfCard);
//			cardHolders.add(newCardHolder);
			String newUserLine = user.name + " " + newID + " " + passOfCard;
			lines.add(newUserLine);

			System.out.println("Мы закончили, поздравляю, теперь вы держатель карты");
			saveDataToFile(user);
			break;


		}


	}



	public boolean saveDataToFile(User user) throws IOException {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter
				("src/main/java/com/example/javafx/bankSimulation/user_dataBase.txt", true))) {
			for (String line1 : lines) {
				writer.write(line1);
				writer.newLine();
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return true;
	}


	public String requestCredit(User user) throws InterruptedException {
		System.out.println("Вы хотите взять в кредит?");
		String userAnswer = scan.next();
		if (userAnswer.equalsIgnoreCase("да")) {
			System.out.println("У нас 35% годовых");
			System.out.println("Вы согласны на выдачу кредита?");
			String userAns = scan.next();
			if (userAns.equalsIgnoreCase("да")) {
				Loader.Loading();
				giveCredit(user.name);
			}
		} else {
			return "увидимся в след раз";
		}
		return "";
	}

	public double giveCredit(String userName) {
		System.out.println("Введите сумму кредита");
		int sumOfCredit = scan.nextInt();
		double credit = sumOfCredit * 1.35;
		return credit;
	}


	public void showBills() {
		System.out.println(bankBills.getBalance());
	}

}