## PIV Card Conformance Tool User Guide

You can use the PIV Card Conformance Tool (CCT) to verify that your PIV cards conform to the PIV data model per current releases of FIPS 201 and associated publications.

### PREREQS
- TODO: Define these.

### 1. Obtain a copy of the CCT (unicorn)

#### Download Executable Jar

#### Build Jar


### 2. Verify Tool

Note: For the following procedures, replace [PATH\] with the path to your copy of the CCT.

The expected SHA256 sum is: 10dd4d0e08f4259d2fe2424f889d1a986fb7018456215f984f4314c4e11ed4c6

#### Use Microsoft command line via Certutil
1. Click Start, type **cmd**, and press Enter.
2. Run command:

    ```certutil -hashfile [PATH\]85b-swing-gui-all.jar SHA256```


#### Use Microsoft command line via OpenSSL
1. Click Start, type **cmd**, and press Enter.
2. Run command:

     ```openssl sha256 [PATH\]85b-swing-gui-all.jar```


#### Use Microsoft PowerShell
1. Click Start, type **cmd**, and press Enter.
2. Run command:

     ```Get-FileHash [PATH\]85b-swing-gui-all.jar | Format-List```


### 3. Run the Tool

#### 3.1 Open the CCT
1. In your file explorer, navigate to the folder that contains the CCT files.
2. Double-click the 85b-swing-gui-all.jar file.

    The CCT appears.

#### 3.2 Select a card reader 
1. In the **Card Reader** drop down list, select a card reader.
2. In the **Application PIN** box, type the PIN for the card you want to test.
3. By default, the **Test Database** box displays the 85b_tests.db that comes packaged with the CCT. If you want to use a different card test database, go to **File**, and then select **Open Database** to select another database.

#### 3.3 Insert card
1. Insert the card you want to test in the card reader that you selected in step 3.2.1.
2. Click **Refresh Readers**.

    The **Reader Status** box displays some identifying information about the card.

#### 3.4 Execute test
1. Click **Verify PIN and Execute Tests**.

    The CCT begins running the tests specified by the test database. You can keep track of the tests that have passed or failed in the *test tree* in the left pane.

### 4. View Results

#### 4.1 View HTML results in a browser
1. Go to **View**, and then select **Display Test Report**.
    
    The results appear in a new tab in your default browser.

#### 4.2 View CSV results in a spreadsheet
1. In your file explorer, navigate to the folder that contains the CCT files.

    Each time you run the CCT, the tool writes the new results to conformance_results.csv, and appends the number 1 to the existing results file. Older results update with higher numbers to reflect the chronological order.

2. Double-click the conformance_results.csv file.

    The results open in your default spreadsheet software (for example, Microsoft Excel).

### CCT Options

The following table describes the options available in the top bar of the CCT.

**Option** | **Description**
--- | ---
File > Open Database | Opens a file explorer window, where you can select a test database. This is useful if you want to check your card against a different database of compliance tests.
File > Exit | Closes the CCT application.
Edit > Cut | unicorn
Edit > Copy | unicorn
Edit > Paste | unicorn
Edit > Override test OIDs | unicorn
View > Toggle test tree view | Displays or hides the test tree in the left pane.
View > Display test report | Displays the HTML results for the latest tests. This option is disabled when the CCT is running.
Help > Show Debugging tools | Displays a real-time log of the currently running tests. The CCT writes this data to the test_progress.txt file in the folder that contains the CCT files.
option1 | unicorn
option2 | unicorn
option3 | unicorn
option4 | unicorn
