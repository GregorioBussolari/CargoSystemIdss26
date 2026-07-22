## ----------------------------------------------------------
## Dockerfile for webgui
## ----------------------------------------------------------

FROM eclipse-temurin:17.0.5_8-jre-focal AS builder

## Espone la porta Web per il browser (8050) e la porta usata da QAk (8040)
EXPOSE 8050/tcp
EXPOSE 8040/tcp
EXPOSE 8040/udp

## Estrae automaticamente l'archivio tar generato da gradlew (bootDistTar o distTar)
ADD ./build/distributions/webgui-1.0.tar /
WORKDIR /webgui-1.0/bin

## Copia i file di configurazione QAk o Spring Boot necessari a runtime
COPY ./*.pl ./
COPY ./*.json ./
COPY ./*.txt ./
# COPY ./application.properties ./  <-- Decommenta se usi un file properties esterno per Spring Boot

## Comando di avvio dell'applicazione (assicurati che lo script si chiami 'webgui')
CMD ["bash", "webgui"]

## Crea il file tar (usa bootDistTar se è un progetto Spring Boot, altrimenti distTar)
#./gradlew distTar 
#
## Costruisci l'immagine Docker
#docker build -t webgui:1.0 .