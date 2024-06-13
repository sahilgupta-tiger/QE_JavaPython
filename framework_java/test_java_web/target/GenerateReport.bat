@echo off
title Allure Reports with Trends
echo ::Generate an Allure Report folder with latest Results::
powershell.exe "allure generate allure-results --clean -o allure-report"
echo ::Remove old History and Widgets files from Results folder::
powershell.exe "Remove-Item allure-results\history\* -Recurse"
powershell.exe "Remove-Item allure-results\widgets\* -Recurse"
powershell.exe "Start-Sleep -s 2"
echo ::Copy the newly generated History and Widgets files to Results folder::
powershell.exe "Copy-Item -Path allure-report\history\* -Destination allure-results\history -Exclude retry-trend.json,duration-trend.json -PassThru"
powershell.exe "Copy-Item -Path allure-report\widgets\* -Destination allure-results\widgets -Exclude retry-trend.json,duration-trend.json -PassThru"
powershell.exe "Copy-Item -Path environment.properties -Destination allure-results\environment.properties -PassThru"
powershell.exe "Start-Sleep -s 2"
echo ::Start the Allure Server to view Reports::
powershell.exe "allure serve allure-results"