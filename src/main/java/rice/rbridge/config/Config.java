package rice.rbridge.config;

public class Config {
    // Mod Toggle
    public boolean enabled;
    public boolean whisper;
    public boolean webhook;
    public boolean logRequests;
    public String webhookURL;
    public String botURL;
    public Integer botPort;
    public Integer port;
    public String apiKey;


    public Config(
            boolean enabled,
            boolean whisper,
            boolean webhook,
            boolean logRequests,
            String webhookURL,
            String botURL,
            Integer botPort,
            Integer port,
            String apiKey
    ) {
        this.enabled = enabled;
        this.whisper = whisper;
        this.webhook = webhook;
        this.logRequests = logRequests;
        this.webhookURL = webhookURL;
        this.botURL = botURL;
        this.botPort = botPort;
        this.port = port;
        this.apiKey = apiKey;
    }

    @Override
    public String toString() {
        return "Config" +
                "{" +
                "enabled=" + this.enabled +
                ",whisper=" + this.whisper +
                ",webhook=" + this.webhook +
                ",logRequests=" + this.logRequests +
                ",webhookURL=" + this.webhookURL +
                ",botPort=" + this.botPort +
                ",botURL=" + this.botURL +
                ",apiKey=" + this.apiKey +
                "}";
    }
}
