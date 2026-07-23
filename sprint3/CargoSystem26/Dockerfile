## ----------------------------------------------------------
## Dockerfile for cargosystem
## ----------------------------------------------------------

FROM eclipse-temurin:17.0.5_8-jre-focal AS builder

## Espone la porta usata da QAk per CoAP/TCP
EXPOSE 8120/tcp
EXPOSE 8120/udp

## Estrae automaticamente l'archivio tar generato da gradlew distTar
ADD ./build/distributions/cargosystem-1.0.tar /
WORKDIR /cargosystem-1.0/bin

## Copia i file di configurazione Prolog, JSON o testo necessari a runtime
COPY ./*.pl ./
COPY ./Californium3.properties ./
COPY ./properties.txt ./

## Comando di avvio dell'applicazione QAk
CMD ["bash", "cargosystem"]

## ==========================================================
## NOTE OPERATIVE PER IL DEPLOYMENT (Workflow)
## ==========================================================
## 1) Ripulire eventuali vecchie build:
##    docker rmi cargosystem:1.0
##
## 2) Generare la distribuzione tarball tramite Gradle:
##    ./gradlew distTar   (oppure gradlew.bat distTar su Windows)
##
## 3) Costruire l'immagine locale:
##    docker build -t cargosystem:1.0 .
##
## 4) Taggare l'immagine per il proprio repository DockerHub:
##    docker tag cargosystem:1.0 natbodocker/cargosystem:1.0
##
## 5) Effettuare il push (previa autenticazione):
##    docker push natbodocker/cargosystem:1.0