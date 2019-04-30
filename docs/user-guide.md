## Card Conformance Tool User-Guide

### PREREQS
- TODO: Define these.

### 1. Obtain a copy of the CCT

#### Download Executable Jar

#### Build Jar


### 2. Verify Tool

Note: For the following procedures, replace [PATH\] with the path to your copy of the CCT.

The expected SHA256 sum is: 10dd4d0e08f4259d2fe2424f889d1a986fb7018456215f984f4314c4e11ed4c6

#### Use Microsoft command line via Certutil
- Click Start, type cmd, and press Enter.
- Run command:

    ```certutil -hashfile [PATH\]85b-swing-gui-all.jar SHA256```


#### Use Microsoft command line via OpenSSL
- Click Start, type cmd, and press Enter.
- Run command:

     ```openssl sha256 [PATH\]85b-swing-gui-all.jar```


#### Use Microsoft PowerShell
- Click Start, type cmd, and press Enter.
- Run command:

     ```Get-FileHash [PATH\]85b-swing-gui-all.jar | Format-List```


### 3. Using the Tool

#### 3.1 Open the tool
- Navigate to the directory of the tool
- Double click on the 85b-swing-gui-all.jar file

#### 3.2 Select a card reader 
- Using the drop down list, select a card reader

#### 3.3 Enter the Smart Card Pin
- [do it]

#### 3.4 Select a card test database
- [need more description of how this works]

#### 3.5 Insert Card
- If card does not display, click refresh.

#### 3.6 Execute test






#### BLAH
