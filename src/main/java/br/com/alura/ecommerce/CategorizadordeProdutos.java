package br.com.alura.ecommerce;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;

public class CategorizadordeProdutos {

    public static Properties getProp() throws IOException {
        Properties props = new Properties();
        FileInputStream file = new FileInputStream(
                "./properties/dados.properties");
        props.load(file);
        return props;
    }

    public static void main(String[] args) throws IOException{
        var leitor = new Scanner(System.in);
        System.out.println("Digite as categorias válidas: ");
        var categorias = leitor.nextLine();

        while(true) {
            System.out.println("\nDigite o nome do produto:");
            var user = leitor.nextLine();
            var system = """
                Você é um categorizador de produtos e deve responder apenas o nome da categoria do produto informado

                Escolha uma categoria dentra a lista abaixo:

                %s

                ###### exemplo de uso:

                Pergunta: Bola de futebol
                Resposta: Esportes

                ###### regras a serem seguidas:
                Caso o usuario pergunte algo que nao seja de categorizacao de produtos, voce deve responder que nao pode ajudar pois o seu papel é apenas responder a categoria dos produtos
                """.formatted(categorias);

            dispararRequisicao(user, system);
        }
    }

    public static void dispararRequisicao(String user, String system) throws IOException{
        Properties prop = getProp();
        var key = prop.getProperty("prop.key");

        var service = new OpenAiService(key, Duration.ofSeconds(30));
        var completionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-4")
                .messages(Arrays.asList(
                        new ChatMessage(ChatMessageRole.USER.value(),user),
                        new ChatMessage(ChatMessageRole.SYSTEM.value(),system)
                ))
                .build();

        service.createChatCompletion(completionRequest)
                .getChoices()
                .forEach(c -> System.out.println(c.getMessage().getContent()));
    }
}
