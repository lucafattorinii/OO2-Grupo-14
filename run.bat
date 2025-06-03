@echo off

:: Obtener el puerto desde el argumento o usar 8081 por defecto
set PORT=%1
if "%PORT%"=="" set PORT=8081

:: Verificar si hay un proceso en el puerto
FOR /F "tokens=5" %%A IN ('netstat -aon ^| findstr :%PORT% ^| findstr LISTENING') DO (
    set PID=%%A
)

:: Confirmar y matar si hay proceso
if defined PID (
    echo Puerto %PORT% en uso por PID %PID%
    set /p CONFIRM="¿Querés terminar ese proceso? (s/n): "
    if /I "%CONFIRM%"=="s" (
        taskkill /F /PID %PID%
        echo Proceso terminado.
    ) else (
        echo Abortado por el usuario.
        exit /b
    )
) else (
    echo Puerto %PORT% libre.
)

:: Ejecutar Spring Boot app
start cmd /k "mvnw spring-boot:run"

:: Esperar unos segundos y abrir navegador
ping -n 6 127.0.0.1 > nul
start http://localhost:%PORT%
