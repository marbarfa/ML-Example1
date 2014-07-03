package com.ml.android.melitraining.net.robospice;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.ml.android.melitraining.net.MeliAPI;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

public class MeliRetrofitSpiceService extends RetrofitJacksonSpiceService {
    private final static String BASE_URL = "https://api.mercadolibre.com/";

    @Override
    public void onCreate() {
        super.onCreate();
        addRetrofitInterface(MeliAPI.ItemsAPI.class);
        addRetrofitInterface(MeliAPI.SearchAPI.class);
    }

//    @Override
//    protected Converter createConverter() {
//        Gson gson = new GsonBuilder()
//                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
//                .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
//                .registerTypeAdapter(Date.class, new DateDeserializer())
//                .create();
//        return new GsonConverter(gson);
//    }

    @Override
    protected String getServerUrl() {
        return BASE_URL;
    }


    private static final String[] DATE_FORMATS = new String[]{
            "yyyy-MM-dd'T'HH:mm:ssZ",
            "yyyy-MM-dd"
    };

    private static class DateDeserializer implements JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonElement jsonElement, Type typeOF,
                                JsonDeserializationContext context) throws JsonParseException {
            for (String format : DATE_FORMATS) {
                try {
                    return new SimpleDateFormat(format, Locale.US).parse(jsonElement.getAsString());
                } catch (ParseException e) {
                }
            }
            throw new JsonParseException("Unparseable date: \"" + jsonElement.getAsString()
                    + "\". Supported formats: " + Arrays.toString(DATE_FORMATS));
        }
    }
}