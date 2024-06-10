@echo off
title Allure Reports with Trends
echo ::Generate an Allure Report folder with latest Results::
powershell.exe "allure generate allure_results --clean -o allure_report"
echo ::Remove old History and Widgets files from Results folder::
powershell.exe "Remove-Item allure_results\history\* -Recurse"
powershell.exe "Remove-Item allure_results\widgets\* -Recurse"
powershell.exe "Start-Sleep -s 2"
echo ::Copy the newly generated History and Widgets files to Results folder::
powershell.exe "Copy-Item -Path allure_report\history\* -Destination allure_results\history -Exclude retry-trend.json,duration-trend.json -PassThru"
powershell.exe "Copy-Item -Path allure_report\widgets\* -Destination allure_results\widgets -Exclude retry-trend.json,duration-trend.json -PassThru"
powershell.exe "Start-Sleep -s 2"
echo ::Start the Allure Server to view Reports::
powershell.exe "allure serve allure_results"
timeout 5