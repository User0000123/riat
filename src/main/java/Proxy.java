import com.google.gson.*;

/**
 * The type Proxy. Contains methods for serializing/deserializing
 * messages when communicating between the client and the server.
 *
 * @author Aleksej
 */
public class Proxy {
    private final ExclusionStrategy strategy = new ExclusionStrategy() {
        @Override
        public boolean shouldSkipField(FieldAttributes field) {return field.getAnnotation(Exclude.class) != null;}
        @Override
        public boolean shouldSkipClass(Class<?> clazz) {return false;}
    };

    private final Gson gson = new GsonBuilder().setExclusionStrategies(strategy).create();

    /**
     * Serialize to json string.
     *
     * @param object the object
     * @return the string
     */
    public String serializeToJSON(Object object){
        JsonObject toSerialize = gson.toJsonTree(object).getAsJsonObject();

        toSerialize.addProperty("className",object.getClass().getName());
        return gson.toJson(toSerialize);
    }

    /**
     * Deserialize from json object.
     *
     * @param inputString the input string
     * @return the object
     */
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
