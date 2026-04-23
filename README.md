# whatsapp-notifications-template
Teste
caio.silva@TCCRMNCRC5KB4 MINGW64 ~/projetos/whatsapp-notifications-template (envio-meta-button-ok)

compilar
javac -cp "lib/json-20240303.jar;." -encoding UTF-8 -d out $(find src/main/java -name "*.java")
$ cp -r src/main/resources/* out/

rodar
java -cp "out;lib/json-20240303.jar" br.com.torra.whatsapp.Main
Servidor rodando em http://localhost:8080/webhook


curl -X POST https://graph.facebook.com/v25.0/1157240740795278/messages -H "Authorization: Bearer EAApo24Aj0C4BRRYhFGxk6jwoAPMWCLWF62JEbnDZBCZCZBoSbJy3qZBobcw1ZAO7n32rLl27wSZBIZCN6jHz1beALvIi7lUpxBwCSttFmsHshK13U3pB5Hzhq3uZBZBieNxB1sUrOAuqUK2Ucb1VMciA6Ac9loEB9ZCxorwZCWeeO7dCjTZCd1vuS38PnRxbL2EI5I2pdKzZCZCeIa8j5KFgUjHwqpl3E68tF7IKga1XZAGUh2biyybfcYJpv3KUtrbdoO0pZAtlMHdDfeU0a6xeShYSP2IDby8ZA" -H "Content-Type: application/json" -d '{
  "messaging_product": "whatsapp",
  "to": "5511984696489",
  "type": "template",
  "template": {
    "name": "dados_hora",
    "language": { "code": "pt_BR" },
    "components": [
      {
        "type": "body",
        "parameters": [
          { "type": "text", "text": "Loja Centro SP" },
          { "type": "text", "text": "10/04/2026" }
        ]
      }
    ]
  }
}'

