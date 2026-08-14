# Farm MVP

A text-based farm shop management program built for **CSSE2002 (Programming in the Large)**, Assignment 1, Semester 2 2024, at the University of Queensland.

The program simulates Farmer Ali's shop, allowing her to manage inventory, customers, sales transactions, and sales history through a command-line interface.

## Features

- **Inventory management** — track farm products (Milk, Egg, Wool, Jam) with quality levels, using either a basic (no quantities) or fancy (quantity-supporting) inventory system
- **Customer address book** — store and retrieve customer details, with duplicate and not-found handling
- **Sales processing** — build a cart, checkout, and support special transaction types (categorised, special sale/discounted)
- **Sales history** — view stats, reprint receipts, and identify the most popular or highest-grossing transactions

## Project Structure

```
src/
├── farm/
│   ├── core/                      # FarmManager, Farm, exceptions
│   ├── customer/                  # AddressBook, Customer
│   ├── inventory/                 # Inventory, BasicInventory, FancyInventory
│   │   └── product/                 # Product, Egg, Jam, Milk, Wool
│   │       └── data/                  # Barcode, Quality (provided)
│   └── sales/                     # Cart, TransactionManager, TransactionHistory
│       └── transaction/             # Transaction, CategorisedTransaction, SpecialSaleTransaction
ai/
└── README.txt                     # Generative AI usage declaration (if applicable)
```

## Setup

1. Clone the repository:
   ```bash
   git clone <your-repo-url>
   ```
2. Open the project in IntelliJ (or your preferred IDE).
3. Requires **Temurin JDK 21** and **JUnit 4**.

## Running the Program

The provided `Main` class lets you run each development stage individually by uncommenting the relevant lines.

```bash
javac -d bin src/farm/**/*.java
java -cp bin Main
```

Once running, use the CLI commands to navigate:

| Command | Description |
|---|---|
| `q` | Quit the application |
| `inventory` | Manage the farm's inventory |
| `address` | Manage the farm's address book |
| `sales` | Enter sales mode |
| `history` | View sales history |

See the assignment spec for the full set of sub-commands within each mode.

## Development Stages

- [ ] **Stage 0** — `AddressBook` and `Customer`
- [ ] **Stage 1** — `Product` subclasses, `Transaction` subclasses, `Cart`, plus Stage 1 features for `AddressBook`/`Customer`
- [ ] **Stage 2** — `TransactionManager`, `TransactionHistory`, `BasicInventory`, `Farm`
- [ ] **Stage 3** — `FancyInventory`

## Testing

Run the provided JUnit tests (and your own) via IntelliJ's test runner, or:

```bash
# adjust classpath to include your JUnit 4 jar
javac -cp .:junit-4.jar -d bin src/farm/**/*.java test/**/*.java
java -cp bin:junit-4.jar org.junit.runner.JUnitCore <TestClassName>
```

## Author

Sandy Nguyen — completed for CSSE2002, Semester 2 2024, University of Queensland.

## Note

This repository is kept **private** in line with the course's academic integrity policy, as CSSE2002 reuses this assignment across semesters.
