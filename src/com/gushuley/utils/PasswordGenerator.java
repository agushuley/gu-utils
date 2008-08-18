package com.gushuley.utils;

import java.util.Random;

/**
 * Генератор безопасных паролей
 * 
 * @author Andrey.Gushuley
 */
public final class PasswordGenerator {
	public static final int DEFALUT_PASSWORD_LENGTH = 8;

	/**
	 * Закрытый конструктор, предотвращает создание ээкземпляра класса
	 */
	private PasswordGenerator() {
	}

	protected static final String[] categries = new String[] {
			"ABCDEFGHIJKLMNOPQSTUVWXYZ", "abcdefghijklmnopqstuvwxyz",
			"0123456789", "!@$%^&*()_+|-=,.:;/" };

	/**
	 * Генерирует безопасный пароль по следующим правилам: <br/>
	 * <nl>
	 * <li>Пароль должен содержать не менее определенного количества символов
	 * (параметр safePasswordLenght).</li>
	 * <li>Символы пароля должны входить как минимум в три категории из: Буквы
	 * верхнего регистра, Буквы нижнего регистра, Цифры, Символы и знаки
	 * препинания </li>
	 * </ul>
	 * 
	 * @param safePasswordLenght -
	 *            Минимальная длина пароля
	 * 
	 * @returns Пароль
	 */
	public static String generate(int safePasswordLenght) {
		int[] categoriesUsage = new int[categries.length];

		Random rndGen = new Random();

		boolean isPasswordStrong;
		StringBuffer password = new StringBuffer();

		int categoriesUsed = 0;
		do {
			int rnd = rndGen.nextInt();
			if (rnd < 0) {
				rnd = -rnd;
			}
			int category = rnd % categries.length;
			int charPos = (rnd - category) % categries[category].length();

			if (categoriesUsage[category] == 0) {
				categoriesUsed++;
			}

			categoriesUsage[category]++;
			password = password.append(categries[category].charAt(charPos));

			isPasswordStrong = password.length() >= safePasswordLenght
					&& categoriesUsed >= categries.length - 1;
		} while (!((password.length() >= safePasswordLenght && isPasswordStrong) || password
				.length() >= safePasswordLenght * 2));

		return password.toString();
	}

	/**
	 * Генерирует безопасный пароль стандартной длины - 8 символов.
	 */
	public static String generate() {
		return generate(DEFALUT_PASSWORD_LENGTH);
	}

	public static void main(String[] args) {
		int length = PasswordGenerator.DEFALUT_PASSWORD_LENGTH;
		if (args.length == 1) {
			try {
				length = Integer.parseInt(args[0]);
			} catch (NumberFormatException e) {
				System.err.println("Error parsing number");
				return;
			}
			if (length <= 1) {
				System.err.println("Password length must be greater 0");
				return;
			}
		} else if (args.length > 1) {
			System.err.println("Usage: genpasswd [len]");
			return;
		}
		System.out.print(generate(length));
	}
}
