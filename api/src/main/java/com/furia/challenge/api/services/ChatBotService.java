package com.furia.challenge.api.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ChatBotService {

    @Autowired
    private WebClientService webClientService;

    public String sentenceAnalysis(String content){
        String lower = content.toLowerCase();

        List<String> teamKeywords = Arrays.asList("qual é a informação sobre a equipe?", "me mostre os detalhes da equipe", 
        "quero saber sobre a equipe", "qual a lineup", "quem está na equipe?", "qual a atual lineup",
        "detalhes da equipe");

        List<String> calendarKeywords = Arrays.asList("qual é o calendario da equipe?", "me mostre o calendario de jogos", 
        "proximos jogos", "quais são os próximos jogos", "quando é o próximo", "quando vai ser o próximo jogo", 
        "agenda de jogos", "proximo jogo");
        
        List<String> lastGamesKeywords = Arrays.asList("quais foram os últimos jogos", "me mostre os últimos jogos",
        "últimos jogos", "jogos anteriores", "últimas partidas", "quais partidas já aconteceram", "histórico de jogos",
        "jogos passados", "jogos que já aconteceram", "quando foi o último jogo", "resultados recentes", "partidas passadas",
        "último jogo");

        if (this.containsAny(lower, teamKeywords)) {
            String json = webClientService.webClient("/csgo/teams?filter[slug]=furia");

            return timeResponse(json);
        } else if (containsAny(lower, calendarKeywords)) {
            String json = webClientService.webClient("/csgo/matches/upcoming?filter[opponent_id]=furia");

            return calendarioResponse(json);
        } else if (this.containsAny(lower, lastGamesKeywords)){
            String json = webClientService.webClient("https://api.pandascore.co/csgo/matches/past?filter[opponent_id]=furia&page[size]=3");

            return pastMatchesResponse(json);
        }

        return "Sinto muito Não encontrei nada";
    }

    private boolean containsAny(String sentence, List<String> keywords) {
        for (String keyword : keywords) {
            if (sentence.contains(keyword)) {
                return true;
            }
        }
        return false;
    }

    private String timeResponse(String json){
        try {
            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(json);

            JsonNode players = root.get(0).get("players");

            ArrayList<String> jogadores = new ArrayList<>();

            for (JsonNode player : players) {
                if (player.get("active").asBoolean()) {
                    jogadores.add(player.get("name").asText());
                }
            }

            String mensagem = "🔥🚨 E O ELENCO DE HOJE DA FURIA CHEGOU COM TUDO! 🚨🔥\n\n" +
            "No time da FURIA temos nomes de peso prontos para brilhar e conquistar o palco! 💥🎮 " +
            "Preparem-se para ver os verdadeiros protagonistas dessa batalha, com direito a uma performance de tirar o fôlego! 🎬🔥\n\n" +
            "Hoje, a FURIA traz para o combate os monstros da nossa line-up: " + 
            String.join(", ", jogadores) + 
            ". Eles são os protagonistas dessa jornada épica, com habilidades e coragem que vão deixar a torcida de queixo caído! ⚡👊";

            return mensagem;
        } catch (Exception e) {
            return null;
        }
    }

    private String calendarioResponse(String json){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(json);

            StringBuilder mensagemFinal = new StringBuilder();

            mensagemFinal.append("🔥🚨 FURIA FANS, PREPAREM-SE! 🚨🔥\n\n");
            mensagemFinal.append("O próximo mês de batalhas promete ser épico! 🎮🔥 Vamos apoiar nosso time em cada momento da jornada! 💪🏆\n");
            mensagemFinal.append("Aqui estão os próximos jogos da FURIA que você não pode perder! 🔥👊\n\n");

            for (JsonNode matchNode : rootNode) {
                String homeTeam = matchNode.path("opponents").get(0).path("opponent").path("name").asText();
                String awayTeam = matchNode.path("opponents").get(1).path("opponent").path("name").asText();
                String dateString = matchNode.path("scheduled_at").asText();
                String tournament = matchNode.path("serie").path("name").asText();
                String league = matchNode.path("league").path("name").asText();
                String matchName = matchNode.path("name").asText();

                String datePart = dateString.split("T")[0];
                String timePart = dateString.split("T")[1].split("Z")[0];
                
                String[] timeSplit = timePart.split(":");
                String formattedTime = timeSplit[0] + "h" + timeSplit[1] + "min";

                String mensagem = "⚔️ " + matchName + " ⚔️\n" + 
                    "🏆 Torneio: " + tournament + " - Liga: " + league + "\n" +
                    "🆚 " + homeTeam + " x " + awayTeam + "\n" +
                    "🗓 Data: " + datePart + " " + formattedTime + "\n\n";

                mensagemFinal.append(mensagem).append("\n\n");
            }

            int length = mensagemFinal.length();
            if (length >= 2 && mensagemFinal.substring(length - 2).equals("\n\n")) {
                mensagemFinal.setLength(length - 3); // remove os dois últimos '\n'
            }
            return mensagemFinal.toString();

        } catch (Exception e) {
            return null;
        }
    }

    private String pastMatchesResponse(String json){
        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode rootNode = mapper.readTree(json);

            StringBuilder mensagemFinal = new StringBuilder();
            mensagemFinal.append("🔥 RESULTADOS DAS PARTIDAS DA FURIA 🔥\n\n");

            for (JsonNode match : rootNode) {
                JsonNode opponents = match.path("opponents");
                String team1 = opponents.get(0).path("opponent").path("name").asText();
                String team2 = opponents.get(1).path("opponent").path("name").asText();

                JsonNode results = match.path("results");
                int score1 = results.get(0).path("score").asInt();
                int score2 = results.get(1).path("score").asInt();

                String winner = match.path("winner") != null && match.path("winner").has("name")
                        ? match.path("winner").path("name").asText()
                        : "Empate ou não definido";

                String tournament = match.path("tournament").path("name").asText();

                String serie = match.path("serie").path("full_name").asText();
                String dateString = match.path("begin_at").asText();
                String datePart = dateString.split("T")[0];
                String timePart = dateString.split("T")[1].split("Z")[0];
                String[] timeSplit = timePart.split(":");
                String formattedTime = timeSplit[0] + "h" + timeSplit[1] + "min";

                String matchName = match.path("name").asText();

                String mensagem = "⚔️ " + matchName + " ⚔️\n" +
                        "🏆 Torneio: " + tournament + " - Série: " + serie + "\n" +
                        "🆚 " + team1 + " " + score1 + " x " + score2 + " " + team2 + "\n" +
                        "🏅 Vencedor: " + winner + "\n" +
                        "📅 Data: " + datePart + " " + formattedTime + "\n\n";

                mensagemFinal.append(mensagem);
            }

            return mensagemFinal.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return "Erro ao processar os dados das partidas.";
        }
    }
}
