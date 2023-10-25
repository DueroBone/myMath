
// import java.util.Map;
// import java.util.HashMap;
import java.util.Scanner;

public class CodeUseful {
	/**
	 * This class contains useful code that I have written for various purposes.
	 *
	 * @param args
	 */

	/**
	 * Since using doubles could result in floating point errors,
	 * here is a class that fixes that.
	 */
	static class Decimal {
		// ** The pure number without the decimal point */
		String number;

		/**
		 * Based off of decimal starting at end of number.
		 * also could be called the number of decimal places
		 * that need to be shifted to the left to get the
		 * actual number.
		 */
		int power;

		int debug = 0;

		Decimal() {
			this.number = "0";
			this.power = 0;
		}

		Decimal(int whole) {
			this.number = Integer.toString(whole);
			this.power = 0;
		}

		Decimal(double input) {
			this.power = findPower(Double.toString(input));
			while (input % 1 != 0) {
				input *= 10;
			}
			this.number = Double.toString(input);
		}

		Decimal(String input) {
			this.power = findPower(input);
			this.number = input.replace(".", "");
		}

		// ============================ //

		/** Adds the input to self */
		public Decimal add(Decimal input) {
			String[] leveled = levelDecimals(input);
			if (debug >= 1) {
				System.out.print("  " + this + " add(): " + input);
			}

			this.number = addStrings(leveled[0], leveled[1]);
			this.power = Integer.parseInt(leveled[2]);

			if (debug >= 1) {
				System.out.println(" = " + this);
			}

			return this;
		}

		/** Subtracts the input from self */
		public Decimal subtract(Decimal input) {
			String[] leveled = levelDecimals(input);
			if (debug == 1) {
				System.out.println("  " + this + " subtract(): " + input);
			}

			int carry = 0;
			for (int i = leveled[0].length() - 1; i >= 0; i--) {
				int sub = Integer.parseInt(leveled[0].substring(i, i + 1)) - Integer.parseInt(leveled[1].substring(i, i + 1))
						- carry;
				if (sub < 0) {
					sub += 10;
					carry = 1;
				} else {
					carry = 0;
				}
				leveled[0] = leveled[0].substring(0, i) + Integer.toString(sub) + leveled[0].substring(i + 1);
				if (carry > 0 && i == 0) {
					int currentDigit = Integer.parseInt(leveled[0].substring(i, i + 1));
					currentDigit += carry;
					leveled[0] = leveled[0].substring(0, i) + Integer.toString(currentDigit) + leveled[0].substring(i + 1);
				}
			}

			this.number = leveled[0];
			this.power = Integer.parseInt(leveled[2]);

			if (debug == 1) {
				System.out.println(" = " + this);
			}
			return this;
		}

		/** Multiplies the input from self */
		public Decimal multiply(Decimal input) {
			String thisNum = this.number;
			String inputNum = input.number;

			if (debug == 1) {
				System.out.print("  " + this + " multiply(): " + input);
			}

			int newPower = this.power + input.power;
			String newNumber = "0";
			int carry = 0;
			for (int i = inputNum.length() - 1; i >= 0; i--) {
				String temp = "";
				for (int j = 0; j < inputNum.length() - 1 - i; j++) {
					temp += "0";
				}
				int digit2 = Integer.parseInt(inputNum.substring(i, i + 1));
				for (int j = thisNum.length() - 1; j >= 0; j--) {
					int digit1 = Integer.parseInt(thisNum.substring(j, j + 1));
					int product = digit1 * digit2 + carry;
					carry = product / 10;
					temp = Integer.toString(product % 10) + temp;
				}
				if (carry > 0) {
					temp = Integer.toString(carry) + temp;
					carry = 0;
				}
				newNumber = addStrings(newNumber, temp);
			}

			this.number = newNumber;
			this.power = newPower;

			if (debug == 1) {
				System.out.println(" = " + this);
			}

			return this;
		}

		/** Divides the input from self */
		public Decimal divide(Decimal input) {
			String thisNum = this.number;
			String divisor = input.number;

			if (debug == 1) {
				System.out.print("  " + this + " divide(): " + input);
			}

			int newPower = this.power - input.power;
			String newNumber = "";
			boolean keepGoing = true;
			String dividend = thisNum;
			String remainder = "";
			// dividend is the number that is being divided
			// divisor is the number that is it dividing by

			while (keepGoing) {
				// Check if divisor is less than dividend
				while (!isGreaterThan(dividend, divisor)) {
					dividend += "0";
					newPower++;
					// TODO check if it is too big
				}
				if (remainder == "0") {
					break;
				}
			}

			if (debug == 1) {
				System.out.println(" = " + this);
			}

			this.power = newPower;
			this.number = newNumber;
			return this;
		}

		private boolean isGreaterThan(String numOne, String numTwo) {
			return true; // TODO
		}

		// Helper functions

		private String[] levelDecimals(Decimal input) {
			String strThis = this.toString();
			String strInput = input.toString();

			if (debug == 2) {
				System.out.println("levelDecimals(): ");
				System.out.println(" | start strThis: " + strThis);
				System.out.println(" | start strInput: " + strInput);
			}

			if (findPower(strThis) > findPower(strInput)) {
				while (findPower(strThis) != findPower(strInput)) {
					strInput += "0";
				}
			} else if (findPower(strThis) < findPower(strInput)) {
				while (findPower(strThis) != findPower(strInput)) {
					strThis += "0";
				}
			}

			int newPower = findPower(strThis);

			strThis = strThis.replace(".", "");
			strInput = strInput.replace(".", "");

			if (debug == 2) {
				System.out.println(" | end strThis: " + strThis);
				System.out.println(" | end strInput: " + strInput);
				System.out.println(" | end newPower: " + newPower);
			}

			return new String[] { strThis, strInput, Integer.toString(newPower) };
		}

		@Override
		public String toString() {
			if (debug == 2) {
				System.out.println("toString(): ");
				System.out.println(" | power: " + power);
				System.out.println(" | number: " + number);
			}
			if (power == 0) {
				return number + ".0";
			} else {
				return number.substring(0, number.length() - power) + "." + number.substring(number.length() - power);
			}
		}

		private int findPower(String input) {
			String[] split = input.split("\\.");
			if (split.length == 1) {
				return 0;
			}
			return split[1].length();
		}

		private String addStrings(String num1, String num2) {
			String result = "";
			int carry = 0;
			int i = num1.length() - 1;
			int j = num2.length() - 1;
			while (i >= 0 || j >= 0) {
				int digit1 = i >= 0 ? Integer.parseInt(num1.substring(i, i + 1)) : 0;
				int digit2 = j >= 0 ? Integer.parseInt(num2.substring(j, j + 1)) : 0;
				int sum = digit1 + digit2 + carry;
				carry = sum / 10;
				result = Integer.toString(sum % 10) + result;
				i--;
				j--;
			}
			if (carry > 0) {
				result = Integer.toString(carry) + result;
			}
			return result;
		}

		public Decimal debugMode() {
			return debugMode(1);
		}

		public Decimal debugMode(int level) {
			this.debug = level;
			if (level == 0) {
				return this;
			}
			// System.out.println("Debug Mode: " + level);
			// System.out.println(" | number: " + number);
			// System.out.println(" | power: " + power);
			return this;
		}
		// the end of the Decimal class
	}

	// ============================ //
	// ============================ //

	public static Decimal Decimal(String input) {
		return new Decimal(input);
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter a number: ");
		Decimal myNumber = Decimal(
				// scanner.nextLine()
				"12").debugMode(1);
		// System.out.print("Enter number to add: ");
		// myNumber.add(Decimal(scanner.nextLine()));
		myNumber.multiply(Decimal("2"));
		myNumber.multiply(Decimal("2.5"));
		myNumber.divide(Decimal("2"));
		myNumber.multiply(Decimal("10"));
		myNumber.divide(Decimal("2.5"));
		System.out.println("Final: " + myNumber);

		scanner.close();
	}
}