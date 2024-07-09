# RTLS Server Project

This project is an RTLS (Real-Time Location System) server that processes data from anchors and communicates with devices using the UDP protocol with custom packet structures. Devices register with the server, and their data is stored and managed in a database.

## Features
- **Device Registration:** Devices register with the server, and registration information is stored in a database.
- **Heartbeat Mechanism:** Regularly updates the status of registered devices (online/offline) in the database.
- **UDP Communication:** Uses custom packet structures for communication between the server and devices.
- **Location Data Processing:** Filters location data by tag ID and time, calculates x, y, z coordinates, and stores them in the database.
- **Log Data Storage:** Incoming log data from devices is stored in the database.

## Requirements
- Java Development Kit (JDK)
- Database (PostgreSQL)
- Network configuration for UDP communication

## Installation
1. Clone this repository:
    ```sh
    git clone https://github.com/burakkcmn/RtlsServerExample.git
    ```
2. Navigate to the project directory:
    ```sh
    cd RtlsServerExample
    ```
3. Add necessary repositories to your project dependencies.

## Usage
1. **Device Registration:**
   - Devices register with the server using a custom packet structure via UDP.
   - Registration information, including device ID and IP, is stored in the database.

2. **Heartbeat Mechanism:**
   - The server regularly receives heartbeat signals from devices to update their status (online/offline).

3. **Location Data Processing:**
   - Location data is received from devices and filtered by tag ID and time.
   - Coordinates (x, y, z) are calculated and stored in the database.

4. **Log Data Storage:**
   - Incoming log data from devices is stored in the database for further analysis.

## Code Structure
- `src/main/java`: Contains the Java source files.
  - `com.server`: Main package containing the application entry point and core classes.

## Contributing
Contributions are welcome! Please submit a pull request or open an issue to discuss your changes.

## License
This project is licensed under the BSD 3-Clause License - see the [LICENSE](LICENSE) file for details.

This README provides a comprehensive overview of your project, including setup instructions, usage details, and code structure. Adjust the content as needed to better fit your specific project requirements.

## Author
- **Burak KOCAMAN**
  - GitHub: [burakkcmn](https://github.com/burakkcmn)
  - Email: [kocaman.burak.bk@gmail.com](mailto:kocaman.burak.bk@gmail.com)
