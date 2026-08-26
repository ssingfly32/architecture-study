@echo off
if exist "C:\programming\architecture-study\loadtest\jmeter\result.jtl" del "C:\programming\architecture-study\loadtest\jmeter\result.jtl"
if exist "C:\programming\architecture-study\loadtest\jmeter\report" rmdir /s /q "C:\programming\architecture-study\loadtest\jmeter\report"

"C:\Users\SangHee\AppData\Local\Programs\JMeter\bin\jmeter.bat" -n -t "C:\programming\architecture-study\loadtest\jmeter\get-posts.jmx" -l "C:\programming\architecture-study\loadtest\jmeter\result.jtl" -e -o "C:\programming\architecture-study\loadtest\jmeter\report"

echo Done. Report: C:\programming\architecture-study\loadtest\jmeter\report\index.html
pause
