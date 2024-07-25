package dev.skullition.lockium.proxy;

import dev.skullition.lockium.model.GrowtopiaDetail;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

@Service
public class GrowtopiaDetailProxy {
    private static final String GROWTOPIA_DETAIL_URL = "https://www.growtopiagame.com/detail";
    private static final Logger logger = LoggerFactory.getLogger(GrowtopiaDetailProxy.class);

    public Optional<GrowtopiaDetail> getGrowtopiaDetail() {
        try {
            String body = Jsoup.connect(GROWTOPIA_DETAIL_URL).get().body().text();

            String usersOnline = body.substring(body.indexOf("_user\":\"") + 8, body.indexOf("\",\"world"));

            String wotdName = body.substring(body.indexOf("worlds") + 8, body.indexOf(".png")).toUpperCase();

            logger.debug("Fetching Growtopia Detail with online users: {} and wotdName: {}", usersOnline, wotdName);
            return Optional.of(new GrowtopiaDetail(usersOnline, wotdName));
        } catch (IOException e) {
            return Optional.empty();
        }
    }
}
