#!/bin/bash

# Script de construcción para Mesa de Ayuda
# Autor: juanulb

echo "🔨 Construyendo Sistema Mesa de Ayuda..."

# Crear directorios necesarios
mkdir -p build/classes
mkdir -p dist
mkdir -p docs

# Limpiar archivos anteriores
echo "Limpiando archivos anteriores..."
rm -rf build/classes/*
rm -rf dist/*

# Compilar el proyecto
echo "⚙️ Compilando código fuente..."
javac -cp src -d build/classes \
    src/mesadeayudajuname/model/*.java \
    src/mesadeayudajuname/service/*.java \
    src/mesadeayudajuname/util/*.java \
    src/mesadeayudajuname/app/*.java \
    src/mesadeayudajuname/*.java

if [ $? -eq 0 ]; then
    echo "✅ Compilación completada exitosamente"
else
    echo "❌ Error en la compilación"
    exit 1
fi

# Generar Javadoc
echo "📚 Generando documentación Javadoc..."
javadoc -d docs \
    -cp src \
    -sourcepath src \
    -subpackages mesadeayudajuname \
    -windowtitle "Sistema Mesa de Ayuda" \
    -doctitle "Sistema Mesa de Ayuda - Documentación API" \
    -author \
    -version \
    -use

if [ $? -eq 0 ]; then
    echo "✅ Documentación Javadoc generada en docs/"
else
    echo "⚠️ Advertencia: Error al generar Javadoc"
fi

# Crear JAR ejecutable
echo "📦 Creando archivo JAR ejecutable..."
jar cfe dist/mesadeayudajuname.jar mesadeayudajuname.Mesadeayudajuname -C build/classes .

if [ $? -eq 0 ]; then
    echo "✅ JAR creado: dist/mesadeayudajuname.jar"
else
    echo "❌ Error al crear JAR"
    exit 1
fi

# Mostrar tamaño del JAR
JAR_SIZE=$(du -sh dist/mesadeayudajuname.jar | cut -f1)
echo "📊 Tamaño del JAR: $JAR_SIZE"

echo ""
echo "🎉 ¡Construcción completada exitosamente!"
echo ""
echo "Para ejecutar la aplicación:"
echo "   java -jar dist/mesadeayudajuname.jar"
echo ""
echo "Documentación disponible en:"
echo "   docs/index.html"
echo ""