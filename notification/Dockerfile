FROM node:16.15.1-slim

WORKDIR /usr/src/app

COPY package*.json ./

RUN npm install

COPY prod.env ./.env
COPY src/ ./src
COPY split.yml ./split.yml

EXPOSE 3000
CMD [ "node", "src/index.js" ]
