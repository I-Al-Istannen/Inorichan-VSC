package EventsAndCommands.FunCommands.MemeCommands;

import EventsAndCommands.Categories;
import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

import java.util.Properties;

public class DistractedBoyfriendCommand extends Command {

    private final String memeId;
    private HttpResponse<String> response = null;
    private Properties config;



    public DistractedBoyfriendCommand(Properties config) {
        this.config = config;
        this.name = "distractedbf";
        this.help = "Distracted Boyfriend meme, usage: >distractedbf [option a ; b ; c]";
        this.category = Categories.Memes;
        this.memeId = "112126428";
    }


    @Override
    protected void execute(CommandEvent commandEvent) {
        String[] parameters = commandEvent.getArgs().split(";");

        String text0;
        String text1;
        String text2;

        try {
            text0 = commandEvent.getArgs().split(";")[0];
            text1 = commandEvent.getArgs().split(";")[1];
            text2 = commandEvent.getArgs().split(";")[2];

        } catch (Exception | Error e) {
            commandEvent.getChannel().sendMessage("usage: >distractedbf [a ; b ; c]").queue();
            return;
        }


        try {
            response = com.mashape.unirest.http.Unirest.get("https://api.imgflip.com/caption_image")
                    .queryString("username", config.getProperty("IMGFLIP_USERNAME"))
                    .queryString("template_id", this.memeId)
                    .queryString("password", config.getProperty("IMGFLIP_PASSWORD"))
                    .queryString("boxes[0][text]", text0)
                    .queryString("boxes[1][text]", text1)
                    .queryString("boxes[2][text]",text2)
                    .queryString("cache-control", "no-cache")
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();

;
        }

        JSONObject json = null;
        if (response != null) {
            json = new JSONObject(response.getBody());
        }
        String str = null;
        if (json != null) {
            str = json.getJSONObject("data").getString("url");
        }

        commandEvent.reply("By " + commandEvent.getMember().getAsMention() + "\n" + str, sent -> commandEvent.getMessage().delete().queue());


    }
}
