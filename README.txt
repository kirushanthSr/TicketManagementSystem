Ticket Management System - README

Overview
The Ticket Management System is a multithreaded Java application designed to manage the selling of tickets for an event. The system involves vendors releasing tickets and customers purchasing them. It supports two modes of interaction: **CLI (Command Line Interface)** and **GUI (Graphical User Interface)**.

The program ensures efficient ticket management by coordinating multiple vendors and customers through threads, maintaining synchronization, and avoiding race conditions.

---

Features
1. CLI and GUI Modes
   - Users can start the application in either CLI or GUI mode.
   - CLI mode offers commands to manage ticket operations directly from the terminal.
   - GUI mode (via `TicketSystemApp`) provides a graphical interface for managing tickets.

2. Multithreaded Design
   - Vendors release tickets at a specified rate.
   - Customers purchase tickets at their defined retrieval rate.
   - Threads are managed for optimal coordination between vendors and customers.

3. Configuration Management
   - Configuration is loaded from a JSON file (`Data.json`) or created anew if not present.
   - Includes details such as event name, ticket price, ticket capacity, vendor and customer count, and release/retrieval rates.

4. **Real-Time Ticket Monitoring**
   - Users can monitor the status of the ticket pool in real-time during CLI mode.

---

## Prerequisites
1. Java Development Kit (JDK): Version 8 or higher.
2. GSON Library: Ensure the GSON library is included in your classpath for JSON handling.
3. TicketSystemApp: GUI implementation should be available in the `View` package.

---

Execution Steps

1. Compile the Application
1. Navigate to the project's root directory.
2. Use the following command to compile the project:
   ```
   javac -d bin src/**/*.java
   ```

2. Run the Application
Run the main class to start the application:
```
java -cp bin Controller.Main
```

---

Using the Application

Starting the Application
1. Upon starting the application, you will see a welcome message.
2. You will be prompted to select a mode:
   - Enter `CLI` for Command Line Interface mode.
   - Enter `GUI` for Graphical User Interface mode.

---

CLI Mode
After selecting CLI mode, the application will provide you with the following commands:

1. start
   Begin ticket operations. Vendors start releasing tickets, and customers purchase them until all tickets are sold.

2. monitor
   Displays the current status of the ticket pool:
   - Remaining tickets.
   - Tickets currently in the pool.

3. stop
   Terminates the program.

---

GUI Mode
If GUI mode is selected:
- The `TicketSystemApp` launches.
- You can manage ticket operations through a user-friendly graphical interface.
- Refer to the GUI-specific instructions within the application for detailed navigation.

---

Configuration Management
1. If a `Data.json` configuration file exists, the application will prompt you to load it.
   - Select `yes` to use the existing configuration.
   - Select `no` to create a new configuration.

2. If no configuration file is found, you will be asked to input event details manually:
   - Event name
   - Ticket price
   - Total number of tickets
   - Ticket release and retrieval rates
   - Maximum ticket pool capacity
   - Number of vendors and customers

3. The configuration is saved for future use.

---

Notes
1. Direct Access to GUI
   You can launch the GUI directly via the `TicketSystemApp` class.

2. Switching Between Modes
   CLI users can stop the application and restart it in GUI mode if needed. However, the modes cannot be switched dynamically during a session.

3. Thread Synchronization
   The application ensures thread-safe operations for ticket management. The ticket pool uses synchronized methods to prevent race conditions.

---

Troubleshooting
1. Configuration File Issues
   If the application fails to load the configuration file, it will prompt you to create a new one.

2. GSON Dependency
   Ensure the GSON library is included in your project classpath to handle JSON operations.

3. Java Version Compatibility
   Use JDK 8 or higher to avoid compatibility issues with the threading and GUI features.

---

This README should provide you with all the necessary information to run and use the Ticket Management System effectively.