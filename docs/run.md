## Como rodar?

- Vá até resources
- Adicione os arquivos private.key e public key
- Para isso use os comandos no Linux
  ```env
  openssl genrsa > private.key
  openssl rsa -in private.key -pubout -out public.key
  ```
- **Warning:** Não vai funciona caso esteja usando Windows
  - Nesse caso recomendo usar o wsl para gerar os arquivos
  - Ou use um gerador de rsa de sua escolha
  - Após gerados copie o conteudo dentro deles
  - E adicione ao resources
- Após configurar o RSA é hora de configurar o .env

  ```env
    # Token do pandascore
    PANDASCORE_TOKEN=

    # Banco de dados
    SPRING_DATASOURCE_URL=
    SPRING_DATASOURCE_USERNAME=
    SPRING_DATASOURCE_PASSWORD=

    # Configuração do bot
    BOT_EMAIL=
    BOT_PASSWORD=
    BOT_CPF=
    BOT_FIRST_NAME=
    BOT_LAST_NAME=
    BOT_USERNAME=
  ```

- Depois do .env configurado é só inicializar

  ```bash
      # Primeiro no terminal do root rode:
      docker compose up -d

      # Segundo no terminal da api rode:
      .\gradlew.bat run    # caso esteja usando Windows
      ./gradlew run        # caso esteja usando Linux

      # Terceiro no terminal client rode:
      npm run dev
  ```
