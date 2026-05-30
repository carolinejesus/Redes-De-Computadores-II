package com.carol.chat_pubsub.util;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONObject;


import com.carol.chat_pubsub.model.Mensagem;

public class JsonUtil {
    public static JSONObject parse(String texto) throws Exception {
        return (JSONObject) new JSONParser().parse(texto);
    }
    
    public static JSONObject mensagemToJson(Mensagem msg){
        JSONObject json = new JSONObject();
        json.put("type", "MESSAGE");
        json.put("topic", msg.getTopico());
        json.put("from", msg.getRemetente());
        json.put("message", msg.getConteudo());
        return json;
    }
}
