# Especificações do Projeto

## Front-end

O **Client** é desenvolvido utilizando **TypeScript** com **React**, por ser uma biblioteca reativa, eficiente e bastante consolidada no mercado com uma grande comunidade ativa. Além disso, possuo familiaridade com ambas tecnologias. Também é usado o **Vite** como build tool, por ser bom, simples e interagir bem com **React**. O token jwt enviado pela backend é armazenado via localStorage. É usado o **sockjs** para fazer a conexão com o live chat da back-end.

- **Linguagem:** TypeScript
- **Bibliotecas:** React, React Router, sockJs
- **Estilos:** Tailwind CSS, Lucide, Shadcn
- **Build:** Vite

## Back-end

A **API** é desenvolvida com **Java** utilizando o **Spring Boot**, escolhidos pela robustez, segurança e pela grande quantidade de conteúdo e soluções na internet. A estrutura do projeto seguirá boas práticas de versionamento de banco de dados com o uso do **Flyway**. Além disso, é usado o **Gradle** como build tool, por ser bem simples, rápida e também por minha familiaridade. Também é usado como meio de autenticação e autorização o **oauth2-resource-server** junto com um token **jwt** que é criptografado usando criptográfia assimétrica. Estamos usando também o protocolo **STOMP** junto com a biblioteca **WebSocket** para a criação de um chat em tempo real.

- **Linguagem:** Java 21
- **Framework:** Spring Boot 3.4.5
- **Bibliotecas:** modelmapper, lombok
- **Versionamento de banco:** Flyway
- **Build:** Gradle

## Banco de Dados

O sistema utilizará **PostgreSQL** como banco de dados relacional, pela sua confiabilidade, desempenho e compatibilidade com ferramentas modernas de desenvolvimento.

- **Banco de dados:** PostgreSQL
