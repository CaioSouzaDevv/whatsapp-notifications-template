# whatsapp-notifications-template
Teste
caio.silva@TCCRMNCRC5KB4 MINGW64 ~/projetos/whatsapp-notifications-template (envio-meta-button-ok)

compilar
javac -cp "lib/json-20240303.jar;." -encoding UTF-8 -d out $(find src/main/java -name "*.java")
cp -r src/main/resources/* out/

rodar
java -cp "out;lib/json-20240303.jar" br.com.torra.whatsapp.Main
Servidor rodando em http://localhost:8080/webhook


curl -X POST https://graph.facebook.com/v25.0/1157240740795278/messages \
-H "Authorization: Bearer EAApo24Aj0C4BRaoQI4HRRSPs6rEH4YN0qiQJUjPbpqmiJse0G40U1CD27VVm0c82ojj53J7eUyLEfWKx7KKNp8ZBF7s9TkpvLA40afrUVPx4nsmgTKcZCky0ZBrZCXFK5r1lPfyxZAGvr2ZCiI3HCMnN22k4UvvzSEZCciblFUSTvYrcOLnbBlsAS3PzkEXXiBSftFbkhsZBq2vdBRZBu1RtQnjfxjZByX0qnWFOEmWeoZCrEYEZAPuVkHsj99wWfo0XldUtNyHr7EuLMZCJU9Yf30HZBQZAu2q" \
-H "Content-Type: application/json" \
-d '{
  "messaging_product": "whatsapp",
  "to": "5511984696489",
  "type": "template",
  "template": {
    "name": "modelo_hora",
    "language": { "code": "pt_br" },
    "components": [
      {
        "type": "body",
        "parameters": [
          { "type": "text", "text": "https://seusistema.com/metas?phone=5511984696489" }
        ]
      }
    ]
  }
}'



