package dev.skullition.lockium.proxy;

import dev.skullition.lockium.model.GrowtopiaDetail;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class GrowtopiaDetailProxy {
    private static final Logger logger = LoggerFactory.getLogger(GrowtopiaDetailProxy.class);
    @Value("${growtopia.detail-url}")
    private String growtopiaDetailUrl;

    public Optional<GrowtopiaDetail> getGrowtopiaDetail() {
        String body;
        try {
            body = Jsoup.connect(growtopiaDetailUrl).get().body().text();
        } catch (IOException e) {
            return Optional.empty();
        }

        String usersOnline = body.substring(body.indexOf("_user\":\"") + 8, body.indexOf("\",\"world"));

        String wotdName = body.substring(body.indexOf("worlds") + 8, body.indexOf(".png")).toUpperCase();

        logger.debug("Fetching Growtopia Detail with online users: {} and wotdName: {}", usersOnline, wotdName);
        return Optional.of(new GrowtopiaDetail(usersOnline, wotdName));
    }
}
