# QA_Tiger_Automation

## Clone QA_Tiger_Automation repo
```command prompt
> git clone git@github.com:tigerrepository/QA_Tiger_Automation.git
```

-----------------------------------------------------------------------------------


## Install python packages:
```command prompt
> pip install --no-cache-dir -r requirements.txt
```


## Run all test
#### 1. Go to your QA_Tiger_Automation folder and run below command
```command prompt
> python run.py
> pytest -m run.py
```


## Generate allure report
###  Manual Installation
#### 1. Download the latest version as zip archive from Maven Central.
#### 2. Unpack the archive to allure-commandline directory.
#### 3. Navigate to bin directory.
#### 4. Use allure.bat for Windows or allure for other Unix platforms.
#### 5. Add allure to system PATH.

### Check the installation
```command prompt
> allure --version
```


### Generate allure report
```command prompt
> allure serve .\tests_results\reports\allure_report
```
