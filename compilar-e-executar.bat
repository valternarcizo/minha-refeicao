@echo off
REM Compila e executa o projeto (Windows). Uso: compilar-e-executar.bat [--demo]
chcp 65001 > nul
cd /d "%~dp0"
if exist out rmdir /s /q out
dir /s /b src\main\java\*.java > fontes.txt
javac -encoding UTF-8 -d out @fontes.txt
del fontes.txt
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -cp out br.mackenzie.minharefeicao.App %*
