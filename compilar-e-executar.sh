#!/usr/bin/env bash
# Compila e executa o projeto (Linux/macOS). Uso: ./compilar-e-executar.sh [--demo]
set -e
cd "$(dirname "$0")"
rm -rf out
javac -encoding UTF-8 -d out $(find src/main/java -name "*.java")
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -cp out br.mackenzie.minharefeicao.App "$@"
