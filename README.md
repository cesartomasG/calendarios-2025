![Build](https://github.com/tu-usuario/tu-repo/actions/workflows/maven.yml/badge.svg)

# Calendarios Inteligentes - Java

Trabajo práctico realizado para la materia **Diseño de Sistemas** en **UTN FRBA**, 2025.

## Descripción

Este proyecto modela un sistema de gestión de calendarios personales que permite a los usuarios organizar eventos únicos y recurrentes, recibir recordatorios automáticos y verificar si pueden llegar a tiempo a sus próximos compromisos. Se trabaja con principios de diseño orientado a objetos, tests unitarios, integración con servicios externos simulados (como mapas y notificaciones) y una interfaz de uso sencilla y extensible.

Incluye funcionalidades como solapamiento de eventos, cálculo de tiempo restante, recordatorios configurables y ubicación geográfica, todo probado con **JUnit 5** y **Mockito**.

## Funcionalidades

- ✅ Un usuario puede tener múltiples calendarios personales
- ✅ En cada calendario se pueden agendar múltiples eventos
- ✅ Cada evento registra: nombre, fecha y hora de inicio y fin, ubicación, y una lista de invitados
- ✅ Posibilidad de listar eventos entre dos fechas (para un calendario o un usuario)
- ✅ Consultar cuánto tiempo falta para un evento
- ✅ Verificar si dos eventos están solapados, y obtener con cuáles del calendario colisionan
- ✅ Soporte para eventos recurrentes diarios, semanales, mensuales o anuales
- ✅ Detección de si el usuarie llega a tiempo al próximo evento según su ubicación actual y estimación de viaje
- ✅ Los eventos pueden tener múltiples recordatorios que se activan según el tiempo restante (por ejemplo, 10 minutos antes)

## Tecnologías

- Java 17
- Maven
- JUnit 5
- Mockito

## Estructura

- `src/main/java`: código fuente del sistema de calendarios
- `src/test/java`: tests unitarios e integración con mocks
- `pom.xml`: configuración de Maven y dependencias

## Ejecución

### Ejecutar los tests

```bash
mvn test
