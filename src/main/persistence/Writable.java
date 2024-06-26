package persistence;

import org.json.JSONObject;

// Represents an interface with writable to Json methods
public interface Writable {

    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
