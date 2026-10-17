package com.lavanderia.erplite.config;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

@Component
public class OpenBrowserConfig {

    @EventListener(ApplicationReadyEvent.class)
    public void abrirNavegadorAlIniciar() {
        String url = "http://localhost:8080/ordenes";

        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
            try {
                Desktop.getDesktop().browse(new URI(url));
                return;
            } catch (Exception ignored) {
            }
        }
        String os = System.getProperty("os.name").toLowerCase();
        Runtime runtime = Runtime.getRuntime();
        try {
            if (os.contains("win")) {
                runtime.exec("rundll32 url.dll,FileProtocolHandler " + url);
            } else if (os.contains("mac")) {
                runtime.exec("open " + url);
            } else if (os.contains("nix") || os.contains("nux")) {
                runtime.exec("xdg-open " + url);
            }
        } catch (IOException e) {
            System.err.println("No se pudo abrir el navegador automáticamente: " + e.getMessage());
        }
    }
}