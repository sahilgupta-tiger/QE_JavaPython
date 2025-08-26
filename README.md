# TSC One Platform Java Automation Framework
This repository contains test automation scripts for Web, Mobile, POS, and REST API testing using TestNG with Cucumber BDD approach.

## Table of Contents
- [Prerequisites](#prerequisites)
- [Project Structure](#project-structure)
- [Setup](#setup)
- [Run all test](#run-all-test)
- [Reporting](#running-tests)



## Prerequisites

Before you begin, ensure you have the following installed on your machine:
- Java JDK 20 or higher (set env variable)
- Maven 3.6.0 or higher
- Node.js and npm (for Appium server and set env variable)
- Android SDK (set env variable for Mobile testing)
- Appium server (can be installed via npm)



## Project Structure

```plaintext
src
├── main
│   └── java
│       ├── allurereportGeneration                                    # Report utilities
│       ├── apicoreutils                                              # Api utilities
│       ├── database                                                  # DataBase utilities
│       ├── genericwrappers                                           # Common Methods
│       ├── listeners                                                 # Listeners classes
│       ├── mobile                                                    # Page Object Models for Mobile
│       ├── mobilepos                                                 # Page Object Models for Pos
│       ├── mobileutils                                               # Mobile utility classes
│       ├── web                                                       # Page Object Models for Web
│       ├── webutils                                                  # Web utility classes
│                        
├── test
│   ├── java
│   │   ├── api                                                       # API-specific tests
│   │   │   ├── resources\testrunner\MicroservicesSmoke_TestNG.xml    # Run this xml file for api suite
│   │   ├── mobile                                                    # Mobile-specific tests
│   │   │   ├── resources\testrunner\Mobile_TestNG.xml                # Run this xml file for mobile suite
│   │   ├── posmobile                                                 # Pos_Mobile specific tests
│   │   │   ├── resources\testrunner\POS_TestNG.xml                   # Run this xml file for pos suite
│   │   ├── web                                                       # Web-specific tests    
│   │   │   ├── resources\testrunner\SingleBrowserExecute.xml         # Run this xml file for web suite
│   │               
│   └── resources
│       └── allure.properties                                         # Configuration files
│       └── DBConfing.properties
│       └── environment.properties
│       └── Executionconfig.properties
│  
├── pom.xml                                                           # Maven configuration file
└── README.md
```
## Setup

#### 1. Clone the repository

> git clone https://github.com/Tractor-Supply-Engineering-Productivity/TSC-OnePlatform-Java.git
cd automation-framework

#### 2. Install dependencies

> mvn clean install

#### 3. Configure Appium
###### Install Appium globally using npm:
> npm install -g appium

###### Start the Appium server:
> appium

#### 4. Set up Android SDK and Environment Variables
Ensure Android SDK is installed and environment variables ANDROID_HOME and PATH are set correctly.

## Run all test
Execute the TestNG.Xml under each package.

1. Go to your TSC-OnePlatform-Java folder and run below command for web test cases
> run SingleBrowserExecute.xml under .\src\test\java\web\resources\testrunner\SingleBrowserExecute.xml
2. Go to your TSC-OnePlatform-Java folder and run below command for Mobile test cases
> run Mobile_TestNG.xml under .\src\test\java\mobile\resources\testrunner\Mobile_TestNG.xml
3. Go to your TSC-OnePlatform-Java folder and run below command for POS test cases
> run POS_TestNG.xml under .\src\test\java\posmobile\resources\testrunner\POS_TestNG.xml
4. Go to your TSC-OnePlatform-Java folder and run below command for Microservices test cases
> run MicroservicesSmoke_TestNG.xml under .\src\test\java\api\resources\testrunner\MicroservicesSmoke_TestNG.xml

## Reporting
After running tests, reports are generated in the target/allure-reports directory.

### Generate allure report
#### Manual Installation
1. Download the latest version as zip archive from Maven Central.
2. Unpack the archive to allure-commandline directory.
3. Navigate to bin directory.
4. Use allure.bat for Windows or allure for other Unix platforms.
5. Add allure to system PATH.
#### Check the installation
    > allure --version
    
#### Generate allure report
    > allure serve .\test_web\tests_results\allure_report

## Additional Notes
Update pom.xml to add any additional dependencies or plugins required for the project.
