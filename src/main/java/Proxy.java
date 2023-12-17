import com.google.gson.*;

public class Proxy {
    private final ExclusionStrategy strategy = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes field) {return field.getAnnotation(Exclude.class) != null;}
        @Override
        public boolean shouldSkipClass(Class<?> clazz) {return false;}
    };

    private final Gson gson = new GsonBuilder().setExclusionStrategies(strategy).create();

    public String serializeToJSON(Object object){
        JsonObject toSerialize = gson.toJsonTree(object).getAsJsonObject();

        toSerialize.addProperty("className",object.getClass().getName());
        return gson.toJson(toSerialize);
    }

    public Object deserializeFromJSON(String inputString){
        JsonObject retrievedObject = gson.fromJson(inputString, JsonObject.class);
        Class<?> classOfRetrObj;
        Object newObject = null;

        try{
            classOfRetrObj = Class.forName(retrievedObject.get("className").getAsString());
            newObject = gson.fromJson(retrievedObject, classOfRetrObj);
        } catch (ClassNotFoundException e){
            System.out.println("Class not found!");
        }

        return newObject;
    }
}
