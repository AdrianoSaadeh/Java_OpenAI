package br.com.alura.ecommerce;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class TestaIntegracao {

    public static Properties getProp() throws IOException {
        Properties props = new Properties();
        FileInputStream file = new FileInputStream(
                "./properties/dados.properties");
        props.load(file);
        return props;
    }

    public static void main(String[] args) throws IOException {
        var user = "Gere 5 produtos";
        var system = "Você é um gerador de produtos fictícios para um ecommerce e deve gerar apenas o nome dos produtos solicitados pelo usuário";

        Properties prop = getProp();
        var key = prop.getProperty("prop.key");

        var service = new OpenAiService(key);
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
