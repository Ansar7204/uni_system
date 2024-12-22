package university.communication;

public class Language {
    private static Language instance;
    private Languages currentLanguage;

    public Language() {
        currentLanguage = Languages.EN;
    }

    public Language(Languages currentLanguage) {
        this.currentLanguage = currentLanguage;
    }

    public static Language getInstance() {
        if (instance == null) {
            instance = new Language();
        }
        return instance;
    }
    public static Language getInstance(Languages lang) {
        if (instance == null) {
            instance = new Language(lang); // Create new instance if null
        } else if (instance.getCurrentLanguage() != lang) {
            instance.setCurrentLanguage(lang); // Only update if different
        }
        return instance;
    }



    public Languages getCurrentLanguage() {
        return currentLanguage;
    }

    public void setCurrentLanguage(Languages language) {
        this.currentLanguage = language;
        System.out.println("Language changed to: " + language);
    }

    public String getLocalizedMessage(String engMessage, String rusMessage, String kazMessage) {
        switch (currentLanguage) {
            case RU:
                return rusMessage;
            case KZ:
                return kazMessage;
            default:
                return engMessage;
        }
    }
}

